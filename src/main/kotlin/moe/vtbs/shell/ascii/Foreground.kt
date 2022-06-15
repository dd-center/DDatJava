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
 *  前景色
 *
 * @author 一七年夏
 * @since 2022-05-17 10:46
 */
class Foreground(
    val value: String = ""
) {
    companion object {
        @JvmField
        val Black = Foreground("30")

        @JvmField
        val DarkRed = Foreground("31")

        @JvmField
        val DarkGreen = Foreground("32")

        @JvmField
        val Gold = Foreground("33")

        @JvmField
        val DarkBlue = Foreground("34")

        @JvmField
        val Purple = Foreground("35")

        @JvmField
        val Cyan = Foreground("36")

        @JvmField
        val LightGray = Foreground("37")

        @JvmField
        val Gray = Foreground("90")

        @JvmField
        val Blue = Foreground("94")

        @JvmField
        val Green = Foreground("92")

        @JvmField
        val Sky = Foreground("96")

        @JvmField
        val Red = Foreground("91")

        @JvmField
        val Pink = Foreground("95")

        @JvmField
        val Yellow = Foreground("93")

        @JvmField
        val White = Foreground("97")

        @JvmStatic
        fun create(value: String): Foreground {
            return Foreground(value)
        }
    }

    override fun toString(): String {
        return "\u001B[${value}m"
    }
}