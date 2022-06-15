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
package moe.vtbs

import com.google.gson.JsonParser
import kotlinx.coroutines.runBlocking
import moe.vtbs.lang.Context
import moe.vtbs.lang.annotation.Blocked
import moe.vtbs.lang.annotation.Network
import moe.vtbs.lang.context
import moe.vtbs.obj.SpaceInfo
import moe.vtbs.obj.VtbDetail
import moe.vtbs.service.ServiceManager
import moe.vtbs.shell.CommandSystem
import org.slf4j.LoggerFactory

/**
 *  DD核心
 *
 * @author 一七年夏
 * @since 2022-06-12 20:12
 */
object DDCore {
    /** 程序版本号 */
    @JvmStatic
    val version = "2.0.0"

    /** 程序运行路径 */
    @JvmStatic
    val workDir get() = context.get().baseDir

    @JvmStatic
    val logger = LoggerFactory.getLogger(DDCore::class.java)
    @JvmStatic
    fun setContext(context: Context) = Context.setContext(context)
    @JvmStatic
    fun setContext(context: android.content.Context) = Context.setContext(context)

    @JvmStatic
    val service = ServiceManager()
    @JvmStatic
    val shell = CommandSystem()

    /**
     * 从 vtbs.moe 网站获取 Vtuber 的详情信息
     * @param id Vtuber 的UID
     * @return VtbDetail 详情信息
     */
    @Blocked
    @Network
    @JvmStatic
    fun getDetail(id: Int) = runBlocking { getDetailAsync(id) }

    /**
     * 从 vtbs.moe 网站获取 Vtuber 的详情信息
     * @param id Vtuber 的UID
     * @return VtbDetail 详情信息
     */
    @Network
    suspend fun getDetailAsync(id: Int): VtbDetail {
        return network.getJObject("https://api.vtbs.moe/v1/detail/$id")
    }

    /**
     * 从 bilbili 网站获取用户的详情信息
     *
     * @param id 用户的UID
     * @return 用户的详情信息
     */
    @Blocked
    @Network
    @JvmStatic
    fun getSpaceInfo(id: Int) = runBlocking { getSpaceInfoAsync(id) }

    /**
     * 从 bilbili 网站获取用户的详情信息
     *
     * @param id 用户的UID
     * @return 用户的详情信息
     */
    @Network
    suspend fun getSpaceInfoAsync(id: Int): SpaceInfo {
        return SpaceInfo(
            JsonParser.parseString(
                network.getString("https://api.bilibili.com/x/space/acc/info?mid=$id")
            ).asJsonObject
        )
    }
}
