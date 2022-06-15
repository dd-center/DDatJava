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
package moe.vtbs.shell.ascii

/**
 * 背景色
 *
 * @author 一七年夏
 * @since 2022-05-17 10:46
 */
class Background private constructor(
    val value: String = ""
) {
    companion object {
        @JvmField
        val Black = Background("40")

        @JvmField
        val DarkRed = Background("41")

        @JvmField
        val DarkGreen = Background("42")

        @JvmField
        val Gold = Background("43")

        @JvmField
        val DarkBlue = Background("44")

        @JvmField
        val Purple = Background("45")

        @JvmField
        val LightBlue = Background("46")

        @JvmField
        val LightGray = Background("47")

        @JvmStatic
        fun create(value: String): Background {
            return Background(value)
        }
    }

    override fun toString(): String {
        return "\u001B[${value}m"
    }
}