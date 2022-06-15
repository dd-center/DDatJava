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
 *  格式
 *
 * @author 一七年夏
 * @since 2022-05-17 10:46
 */
class Format(
    val value: String = ""
) {

    companion object {
        @JvmField
        val Reset = Format("0")

        @JvmField
        val Underline = Format("4")

        @JvmField
        val Strikethrough = Format("9")

        @JvmField
        val Italic = Format("3")

        @JvmField
        val Highlight = Format("7")

        @JvmField
        val Flicker = Format("5")

        @JvmField
        val Blanking = Format("8")

        @JvmField
        val HighBrightness = Format("1")
    }

    override fun toString(): String {
        return "\u001B[${value}m"
    }
}