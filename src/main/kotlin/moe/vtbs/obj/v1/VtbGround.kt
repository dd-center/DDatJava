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
 *  Vtuber的舰长信息
 *
 * @author 一七年夏
 * @since 2022-07-05 19:37
 */
class VtbGround {
    var mid = 0

    @SerializedName("uname")
    var userName = ""
    var face = ""

    @SerializedName("level")
    private var level0 = 0

    val level
        get() = when (level0) {
            0 -> Level.Governor
            1 -> Level.Admiral
            2 -> Level.Captain
            else -> throw IllegalArgumentException("未知的舰长类型${level0}")
        }

    enum class Level {
        Governor, //总督
        Admiral, //提督
        Captain //舰长
    }
}