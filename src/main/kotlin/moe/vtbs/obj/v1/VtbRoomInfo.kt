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

/**
 *  Vtuber直播间信息
 *
 * @author 一七年夏
 * @since 2022-07-05 19:47
 */
class VtbRoomInfo {
    var uid = 0
    var roomId = ""
    var title = ""

    @SerializedName("live_tile")
    var liveTime = 0L
}