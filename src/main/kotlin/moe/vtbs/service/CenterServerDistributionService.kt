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
package moe.vtbs.service

import kotlinx.coroutines.*
import me.him188.kotlin.jvm.blocking.bridge.JvmBlockingBridge
import moe.vtbs.lang.config.PConfig
import moe.vtbs.lang.config.config
import moe.vtbs.config.GlobalConfig
import moe.vtbs.i18n
import moe.vtbs.lang.papi
import moe.vtbs.logger
import moe.vtbs.lang.service.AbstractService
import moe.vtbs.service.distribution.DistributionProcessor
import moe.vtbs.service.distribution.DistributionWorker
import kotlin.time.Duration.Companion.seconds

/**
 *  vtbs.moe的分布式操作服务
 *
 * @author 一七年夏
 * @since 2022-06-13 12:40
 */
class CenterServerDistributionService : AbstractService() {
    override val name: String
        get() = i18n.service.distribution.title

    var distributionJob: Job? = null
    val worker = DistributionWorker(this)

    override fun start() {
        distributionJob = CoroutineScope(Dispatchers.Default).launch {
            distribute()
            val interval = config<GlobalConfig>().app.interval
            logger.info(i18n.service.distribution.start0.papi("time" to interval))
            delay(interval.seconds)
        }
    }

    override fun close() {
        distributionJob?.cancel()
        worker.webSocket?.close(1000, i18n.service.distribution.close0)
    }

    /**
     * 等待服务退出
     */
    @JvmBlockingBridge
    suspend fun waitFor() {
        distributionJob?.join()
    }

    /**
     * 检查分发任务
     */
    private suspend fun distribute() {
        worker.work()
    }

    class I18N(parent: PConfig?) : PConfig(parent) {
        val processor = DistributionProcessor.I18N(this)
        val worker = DistributionWorker.I18N(this)
        val title by notnull("VtbsMoe中央服务器分布式操作服务")
        val start0 by notnull("操作结束，下次操作将在%time%秒后执行")
        val close0 by notnull("客户端服务关闭")
    }
}