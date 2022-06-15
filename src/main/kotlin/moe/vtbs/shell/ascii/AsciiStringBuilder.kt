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

import moe.vtbs.ascii.Ascii
import moe.vtbs.ascii.AsciiString

/**
 *  彩色文字文本构建器
 *
 * @author 一七年夏
 * @since 2022-05-17 13:42
 */
class AsciiStringBuilder : MutableList<AsciiString> by mutableListOf(), Ascii {
    fun build(): AsciiStringGroup {
        return AsciiStringGroup(this)
    }

    override fun toAsciiString(): String {
        return build().toAsciiString()
    }

    override fun toString(): String {
        return build().toString()
    }
}