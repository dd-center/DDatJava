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
package moe.vtbs.service.impl.distribution

import kotlinx.coroutines.*
import moe.vtbs.DDCore
import moe.vtbs.config.config
import moe.vtbs.config.impl.GlobalConfig
import moe.vtbs.logger
import moe.vtbs.network
import moe.vtbs.service.impl.CenterServerDistributionService
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.net.URLEncoder
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 *  分发Worker
 *
 * @author 一七年夏
 * @since 2022-06-13 15:48
 */
class DistributionWorker(svc: CenterServerDistributionService) {
    val listener = object : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            logger.info("WebSocket@DistributionWorker连接成功")
            waitForOpenCallback?.resume(Unit)
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            CoroutineScope(Dispatchers.IO).launch {
                DistributionProcessor.process(text, webSocket)
            }
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            logger.info("WebSocket@DistributionWorker连接关闭")
            this@DistributionWorker.webSocket = null;
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            logger.error("WebSocket@DistributionWorker连接失败", t)
            waitForOpenCallback?.resumeWithException(t)
        }
    }

    var webSocket: WebSocket? = null
    var waitForOpenCallback: (CancellableContinuation<Unit>)? = null

    /**
     * 进行一次检查操作
     */
    suspend fun workAsync() {
        if (waitForOpenCallback != null) return
        logger.debug("Requesting for a job...")
        if (webSocket == null) {
            val req = Request.Builder().url(createURL()).build()
            webSocket = network.okHttpClient.newWebSocket(req, listener)
            try {
                suspendCancellableCoroutine<Unit> {
                    waitForOpenCallback = it
                }
            } catch (e: Throwable) {
                logger.error("Cannot open the distributing websocket. Waiting for next round", e)
                return
            } finally {
                waitForOpenCallback = null
            }
        }
        webSocket!!.send("DDhttp")
    }

    /**
     * 创建连接到 vtbs.moe 的 WebSocket 连接的地址
     * @return URL
     */
    private fun createURL(): String {
        val version = DDCore.version
        val javaVersion = System.getProperty("java.version")
        val osName = System.getProperty("os.name")
        val osVersion = System.getProperty("os.version")
        val osArch = System.getProperty("os.arch")
        val platform = URLEncoder.encode("$osName-$osVersion", "UTF-8") + "_$osArch"
        val name = config<GlobalConfig, String> { URLEncoder.encode(app.nickname, "UTF-8") }

        return """
            wss://cluster.vtbs.moe/
            ?runtime=java
            &version=${version}_$javaVersion
            &platform=$platform
            &name=$name
            """.trimIndent().replace("\n", "")
    }
}