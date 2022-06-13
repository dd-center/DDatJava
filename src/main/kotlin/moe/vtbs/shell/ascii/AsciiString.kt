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
package moe.vtbs.ascii

import moe.vtbs.shell.ascii.Background
import moe.vtbs.shell.ascii.Foreground
import moe.vtbs.shell.ascii.Format

/**
 *  控制台彩色文本
 *
 * @author 一七年夏
 * @since 2022-05-17 10:29
 */
class AsciiString(
    var content: String,
    var foreground: Foreground? = null,
    var background: Background? = null,
    var format: MutableList<Format> = mutableListOf()
) : Ascii{
    companion object {
        val Default get() = AsciiString("")
    }

    @JvmOverloads
    constructor(
        content: String,
        foreground: Foreground? = null,
        background: Background? = null,
    ) : this(content, foreground, background, mutableListOf())

    constructor(
        before: AsciiString,
        content: String
    ) : this(
        content,
        before.foreground,
        before.background,
        before.format.toMutableList()
    )

    override fun toString(): String {
        return content
    }

    override fun toAsciiString(): String {
        val f = get { foreground?.value } +
                get { background?.value } +
                format.joinToString { it.value }
        if (f.isEmpty()) return content
        return "\u001b[$f".removeSuffix(";") +
                "m" +
                content +
                "\u001b[0m"
    }

    private fun get(callback: () -> String?): String {
        callback().let {
            if (it == null) return ""
            else return "${it};"
        }
    }
}