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

import com.google.gson.JsonArray
import com.google.gson.JsonObject

/**
 *  Vtb的舰长信息
 *
 * @author 一七年夏
 * @since 2022-07-05 19:24
 */
class VtbGrounds(data: JsonObject) {
    val userName: String
    val face: String
    val mid: Int
    val member: Member

    init {
        userName = data["uname"].asString
        face = data["face"].asString
        mid = data["mid"].asInt
        member = Member(data["dd"].asJsonArray)
    }

    class Member(
        val governor: Array<Int>,
        val admiral: Array<Int>,
        val captain: Array<Int>
    ) {
        constructor(data: JsonArray) : this(
            data[0].asJsonArray.map { it.asInt }.toTypedArray(),
            data[1].asJsonArray.map { it.asInt }.toTypedArray(),
            data[2].asJsonArray.map { it.asInt }.toTypedArray()
        )
    }
}