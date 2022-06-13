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
package moe.vtbs.obj

import kotlinx.coroutines.runBlocking
import moe.vtbs.lang.annotation.Blocked
import moe.vtbs.util.Image

/**
 *  虚拟主播详情
 *
 * @author 一七年夏
 * @since 2022-06-12 21:55
 */
class VtbDetail {
    /**
     * VTB UID
     */
    var mid: String? = ""
    var uuid: String? = ""

    /**
     * VTB 用户名
     */
    var uname: String? = ""

    /**
     * 视频数
     */
    var video = 0

    /**
     * 直播间号
     */
    var roomid = 0

    /**
     * 签名
     */
    var sign: String? = ""

    /**
     * 公告
     */
    var notice: String? = ""

    /**
     * 头像
     */
    var face: String? = ""

    @Blocked
    fun getFaceImage() = runBlocking { getFaceImageAsync() }
    suspend fun getFaceImageAsync() = Image.getImageAsync(face)

    /**
     * 高级直播间号
     */
    var rise: Int? = 0

    /**
     * Banner 图像
     */
    var topPhoto: String? = ""

    @Blocked
    fun getTopImage() = runBlocking { getTopImageAsync() }
    suspend fun getTopImageAsync() = Image.getImageAsync(topPhoto)

    /**
     *
     */
    var archiveView = 0
    var follower = 0

    /**
     * 直播状态
     */
    var liveStatus = false
    var recordNum = 0
    var guardNum = 0
    var lastLive: LastLive = LastLive()
    var guardChange = 0
    var guardType = intArrayOf()
    var online = 0

    /**
     * 直播间名称
     */
    var title: String? = ""
    var time = 0L
    var liveStartTime = 0L

    class LastLive {
        var online = 0
        var time = 0L
    }
}