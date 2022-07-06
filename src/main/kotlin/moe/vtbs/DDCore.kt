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

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import me.him188.kotlin.jvm.blocking.bridge.JvmBlockingBridge
import moe.vtbs.lang.Context
import moe.vtbs.lang.annotation.Network
import moe.vtbs.lang.annotation.Warning
import moe.vtbs.lang.context
import moe.vtbs.obj.bilibili.SpaceInfo
import moe.vtbs.lang.service.ServiceManager
import moe.vtbs.obj.v1.*
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
    val version = "2.1.0"

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
     * 从 vtbs.moe 网站获取全部 Vtuber 的索引
     * @return Vtuber表。{first: MID, second: UUID}
     */
    @Network
    @JvmStatic
    @JvmBlockingBridge
    suspend fun getVtbList(): Map<Int, String> {
        val out = mutableMapOf<Int, String>()
        val data = network.getString("https://api.vtbs.moe/v1/vtbs")
        JsonParser.parseString(data).asJsonArray.forEach {
            it as JsonObject
            out[it["mid"].asInt] = it["uuid"].asString
        }
        return out
    }

    @Network
    @JvmStatic
    @JvmBlockingBridge
    suspend fun getVtbInfo(): Array<VtbInfo> {
        return network.getJObject("https://api.vtbs.moe/v1/info")
    }

    /**
     * 从 vtbs.moe 网站获取全部Vtuber的简短信息
     * @return Vtuber简短信息的数组
     */
    @Network
    @JvmStatic
    @JvmBlockingBridge
    suspend fun getVtbListShort(): List<VtbShort> {
        return network.getString("https://api.vtbs.moe/v1/short").let { str ->
            JsonParser.parseString(str).asJsonArray.map {
                VtbShort(it.asJsonObject)
            }
        }
    }

    /**
     * 从 vtbs.moe 网站获取全部Vtuber的完整信息
     * @return Vtuber简短信息的数组
     */
    @Network
    @JvmStatic
    @JvmBlockingBridge
    suspend fun getVtbFullInfo(): Array<VtbFullInfo> {
        return network.getJObject("https://api.vtbs.moe/v1/fullinfo")
    }

    /**
     * 从 vtbs.moe 网站获取 Vtuber 的详情信息
     * @param id Vtuber 的UID
     * @return VtbDetail 详情信息
     */
    @Network
    @JvmStatic
    @JvmBlockingBridge
    suspend fun getDetail(id: Int): VtbDetail {
        return network.getJObject("https://api.vtbs.moe/v1/detail/$id")
    }

    /**
     * 从 vtbs.moe 网站获取全部 Vtuber 的舰长信息
     */
    @Network
    @JvmStatic
    @JvmBlockingBridge
    suspend fun getVtbGrounds(): Map<Int, VtbGrounds> {
        val gson = Gson()
        val out = mutableMapOf<Int, VtbGrounds>()
        val data = network.getString("https://api.vtbs.moe/v1/guard/all")
        JsonParser.parseString(data).asJsonObject.let {
            for (key in it.keySet()) out[key.toInt()] = gson.fromJson(it[key], VtbGrounds::class.java)
        }
        return out
    }

    /**
     * 从 vtbs.moe 网站获取某位 Vtuber 的舰长信息
     * @param id Vtuber 的 MID
     */
    @Network
    @JvmStatic
    @JvmBlockingBridge
    suspend fun getVtbGroundByMid(id: Int): Array<VtbGround> {
        return network.getJObject("https://api.vtbs.moe/v1/ground/$id")
    }

    /**
     * 从 vtbs.moe 网站获取全部正在直播的 Vtuber 信息
     */
    @Network
    @JvmStatic
    @JvmBlockingBridge
    suspend fun getLivingVtbs(): IntArray {
        return network.getJObject("https://api.vtbs.moe/v1/living")
    }

    /**
     * 从 vtbs.moe 网站获取 Vtuber 的直播间信息
     * @param id Vtuber 的 MID
     */
    @Network
    @JvmStatic
    @JvmBlockingBridge
    suspend fun getVtbLivingRoomInfo(id: Int): VtbRoomInfo {
        return network.getJObject("https://api.vtbs.moe/v1/room/$id")
    }

    /**
     * 从 bilbili 网站获取用户的详情信息（请勿频繁使用）
     *
     * @param id 用户的UID
     * @return 用户的详情信息
     */
    @Warning
    @Network
    @JvmStatic
    @JvmBlockingBridge
    suspend fun getSpaceInfo(id: Int): SpaceInfo {
        return SpaceInfo(
            JsonParser.parseString(
                network.getString("https://api.bilibili.com/x/space/acc/info?mid=$id")
            ).asJsonObject
        )
    }
}
