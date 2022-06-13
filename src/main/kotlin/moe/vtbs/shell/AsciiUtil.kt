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
package moe.vtbs.shell

import moe.vtbs.ascii.AsciiString
import moe.vtbs.shell.ascii.AsciiStringBuilder
import moe.vtbs.shell.ascii.Foreground
import moe.vtbs.shell.ascii.Format

/**
 *  Ascii工具
 *
 * @author 一七年夏
 * @since 2022-05-19 19:28
 */
internal class AsciiUtil {
    companion object{
        val instance = AsciiUtil()
    }
    fun genAsciiDealWithString(residue: String, before: String, strBefore: String): List<Any> {
        val pos = AsciiStringBuilder();
        if (residue.isEmpty()) pos.apply {
            add(AsciiString(" "))
            add(AsciiString("~${residue}", Foreground.Red, format = mutableListOf(Format.Underline)))
        }
        else pos.apply {
            add(AsciiString(residue, Foreground.Red, format = mutableListOf(Format.Underline)))
        }
        pos.add(0, AsciiString(before, Foreground.White))
        return listOf(
            "无效的命令，发生在 ", pos, "\n",
            "输入 \"${strBefore}help\" 或 \"$strBefore?\" 获取帮助\n"
        )
    }
}