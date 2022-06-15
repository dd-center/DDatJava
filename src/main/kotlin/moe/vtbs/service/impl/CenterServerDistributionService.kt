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
package moe.vtbs.service.impl

import kotlinx.coroutines.*
import moe.vtbs.config.config
import moe.vtbs.config.impl.GlobalConfig
import moe.vtbs.lang.annotation.Blocked
import moe.vtbs.logger
import moe.vtbs.service.AbstractService
import moe.vtbs.service.impl.distribution.DistributionWorker

/**
 *  vtbs.moe的分布式操作服务
 *
 * @author 一七年夏
 * @since 2022-06-13 12:40
 */
class CenterServerDistributionService : AbstractService() {
    override val name: String
        get() = "VtbsMoe中央服务器分布式操作服务"

    var distributionJob: Job? = null
    val worker = DistributionWorker(this)

    override fun start() {
        distributionJob = CoroutineScope(Dispatchers.Default).launch {
            distribute()
            config<GlobalConfig> {
                logger.info("操作结束，下次操作将在${app.interval}秒后执行")
                delay(app.interval * 1000)
            }
        }
    }

    override fun close() {
        distributionJob?.cancel()
        worker.webSocket?.close(1000, "客户端服务关闭")
    }

    /**
     * 等待服务退出
     */
    @Blocked
    fun waitFor() = runBlocking { waitForAsync() }

    /**
     * 等待服务退出
     */
    suspend fun waitForAsync() {
        distributionJob?.join()
    }

    /**
     * 检查分发任务
     */
    private suspend fun distribute() {
        worker.workAsync()
    }
}