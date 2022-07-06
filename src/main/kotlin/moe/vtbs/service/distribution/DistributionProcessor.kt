/*
 * Copyright (C) 2022 一七年夏
 * 
 * The part of program is free: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published 
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see https://www.gnu.org/licenses/
 */
package moe.vtbs.service.distribution

import com.google.gson.Gson
import kotlinx.coroutines.*
import moe.vtbs.i18n
import moe.vtbs.lang.NetInterface
import moe.vtbs.lang.config.PConfig
import moe.vtbs.lang.papi
import moe.vtbs.logger
import moe.vtbs.network
import okhttp3.*
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 *  分发处理器
 *
 * @author 一七年夏
 * @since 2022-06-13 16:17
 */
object DistributionProcessor {
    val gson = Gson()
    const val userAgent =
        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_6) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.0.2 Safari/605.1.15"

    /**
     * 处理由中央服务器传来的操作
     */
    suspend fun process(text: String, webSocket: WebSocket) {
        val jobInfo = gson.fromJson(text, DistributionReq::class.java)
        if (jobInfo.isValidJob) {
            logger.info(i18n.service.distribution.processor.infoProcessComing.papi("key" to jobInfo.key))
            val bean = DistributionResp()
            bean.key = jobInfo.key
            bean.data = getString(jobInfo.url) ?: "ERROR!"
            sendTo(gson.toJson(bean), webSocket)
        }
    }

    /**
     * 从指定连接获取文本
     * 单独抽出来是因为单独有个User-Agent
     * @see NetInterface.getString
     */
    suspend fun getString(url: String): String? {
        logger.info("Access: $url")
        try {
            val request = Request.Builder().url(url).apply {
                header("User-Agent", userAgent)
            }.build()
            val call = network.okHttpClient.newCall(request)
            val resp = suspendCancellableCoroutine { sc ->
                call.enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        sc.resumeWithException(e)
                    }

                    override fun onResponse(call: Call, response: Response) {
                        NetInterface.checkResponse(response)
                            ?.let { sc.resumeWithException(it) }
                            ?: sc.resume(response)
                    }
                })
            }
            return resp.use {
                withContext(Dispatchers.IO) { it.body!!.string() }
            }
        } catch (e: Throwable) {
            logger.error(i18n.service.distribution.processor.errGetData, e)
            return null
        }
    }

    /**
     * 发送文本到WebSocket
     * @throws IllegalStateException WebSocket被关闭了
     */
    private suspend fun sendTo(str: String, webSocket: WebSocket) {
        withContext(Dispatchers.IO) {
            var size: Int? = null;
            while (true) {
                if (!this.isActive) {
                    throw IllegalStateException(i18n.service.distribution.processor.errWebSocketClosed)
                }
                if (size == null) size = str.toByteArray().size
                // OKHTTP 的WebSocket有缓冲区，超了等着。
                if (webSocket.queueSize() + size > 16777216) {
                    delay(10)
                    continue
                }
                webSocket.send(str)
                size = null
            }
        }
    }

    class I18N(parent: PConfig?) : PConfig(parent) {
        val errWebSocketClosed by notnull("WebSocket被关闭了！")
        val errGetData by notnull("未能获取数据")
        val infoProcessComing by notnull("收到任务，键值为：%key%")
    }
}