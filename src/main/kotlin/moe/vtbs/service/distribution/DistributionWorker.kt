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

import kotlinx.coroutines.*
import me.him188.kotlin.jvm.blocking.bridge.JvmBlockingBridge
import moe.vtbs.DDCore
import moe.vtbs.lang.config.config
import moe.vtbs.config.GlobalConfig
import moe.vtbs.i18n
import moe.vtbs.lang.config.PConfig
import moe.vtbs.logger
import moe.vtbs.network
import moe.vtbs.service.CenterServerDistributionService
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
            logger.info(i18n.service.distribution.worker.infoWsConnectSuccess)
            waitForOpenCallback?.resume(Unit)
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            CoroutineScope(Dispatchers.IO).launch {
                DistributionProcessor.process(text, webSocket)
            }
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            logger.info(i18n.service.distribution.worker.infoWsConnectClose)
            this@DistributionWorker.webSocket = null;
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            logger.error(i18n.service.distribution.worker.errWsConnectFailure, t)
            waitForOpenCallback?.resumeWithException(t)
        }
    }

    var webSocket: WebSocket? = null
    var waitForOpenCallback: (CancellableContinuation<Unit>)? = null

    /**
     * 进行一次检查操作
     */
    @JvmBlockingBridge
    suspend fun work() {
        if (waitForOpenCallback != null) return
        logger.debug(i18n.service.distribution.worker.infoRequestJob)
        if (webSocket == null) {
            val req = Request.Builder().url(createURL()).build()
            webSocket = network.okHttpClient.newWebSocket(req, listener)
            try {
                suspendCancellableCoroutine<Unit> {
                    waitForOpenCallback = it
                }
            } catch (e: Throwable) {
                logger.error(i18n.service.distribution.worker.errRequestFailure, e)
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

    class I18N(parent: PConfig?) : PConfig(parent) {
        val infoRequestJob by notnull("正在请求新的任务……")
        val errRequestFailure by notnull("请求任务失败，未能打开WebSocket，下个检查周期时将会继续")
        val infoWsConnectSuccess by notnull("WebSocket@DistributionWorker连接成功！")
        val infoWsConnectClose by notnull("WebSocket@DistributionWorker关闭。")
        val errWsConnectFailure by notnull("WebSocket@DistributionWorker连接失败。")
    }
}