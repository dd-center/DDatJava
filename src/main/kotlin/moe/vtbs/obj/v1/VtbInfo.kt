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
package moe.vtbs.obj.v1

import com.google.gson.annotations.SerializedName
import me.him188.kotlin.jvm.blocking.bridge.JvmBlockingBridge
import moe.vtbs.util.Image

/**
 *  虚拟主播信息
 *
 * @author 一七年夏
 * @since 2022-07-05 18:44
 */
class VtbInfo {
    var mid = 0
    var uname = ""
    var video = 0

    @SerializedName("roomid")
    var roomID = 0
    var sign = ""
    var notice = ""
    var face = ""

    @JvmBlockingBridge
    suspend fun getFaceImage() = Image.getImage(face)

    var rise = 0
    var topPhoto = ""

    @JvmBlockingBridge
    suspend fun getTopPhotoImage() = Image.getImage(topPhoto)

    var archiveView = 0
    var follower = 0

    @SerializedName("liveStatus")
    private var liveStatus0 = 0
    val liveStatus get() = liveStatus0 == 1
    var recordNum = 0
    var guardNum = 0
    var liveNum = 0
    var averageLive: Any? = null
    var weekLive = 0L
    var guardChange = 0
    var guardType = intArrayOf()
    var areaRank = 0

    @SerializedName("online")
    private var online0 = 0
    val online get() = online0 == 1

    var title = ""
    var time = 0L
    var lastLive = LastLive()

    class LastLive {
        var online = 0
        val time = 0L
    }
}