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
import moe.vtbs.shell.ascii.*

/**
 *  日志文本处理器
 *
 * @author 一七年夏
 * @since 2022-05-17 12:17
 */
open class LogStringMaker {

    companion object {
        private val b = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F')
        val instance = LogStringMaker()
    }

    open fun makeString(objs: List<Any?>): String {
        val sb = StringBuilder()
        for (obj in objs) {
            if (obj == null) {
                sb.append("null")
                continue
            }
            synchronized(obj) {
                val sb1 = StringBuilder()
                try {
                    when (obj) {
                        is AsciiString -> sb.append(obj.content)
                        is AsciiStringBuilder -> sb.append(obj.joinToString { string -> string.content })
                        is AsciiStringGroup -> sb.append(obj.joinToString { string -> string.content })
                        is Background -> sb.append("")
                        is Foreground -> sb.append("")
                        is Format -> sb.append("")
                        is Throwable -> {
                            if (sb1.isNotEmpty()) sb1.append("\n")
                            sb1.append(obj.stackTraceToString())
                        }
                        is String -> {
                            sb1.append(obj)
                        }
                        is ByteArray -> {
                            sb1.append("[")
                            for (n in obj) {
                                sb1.append(b[n.toInt() shr 4 and 0xf])
                                sb1.append(b[n.toInt() and 0xf]).append(" ")
                            }
                            sb1.append("]")
                        }
                        is IntArray -> sb.append(obj.contentToString())
                        is LongArray -> sb.append(obj.contentToString())
                        is ShortArray -> sb.append(obj.contentToString())
                        is BooleanArray -> sb.append(obj.contentToString())
                        is Map<*, *> -> {
                            val sb2 = StringBuilder()
                            sb2.append("{")
                            for (o in obj) {
                                sb2.append(makeString(listOf("\"", o.key, "\" -> \"", o.value, "\"")))
                                sb2.append(", ")
                            }
                            sb1.append(sb2.substring(0, sb2.length - 2)).append("]")
                        }
                        is Iterator<*>,
                        is Array<*> -> {
                            val sb2 = StringBuilder()
                            sb2.append("[")
                            if (obj is Iterator<*>) for (o in obj) sb2.append(makeString(listOf(o))).append(", ")
                            else if (obj is Array<*>) for (o in obj) sb2.append(makeString(listOf(o))).append(", ")
                            sb1.append(sb2.substring(0, sb2.length - 2)).append("]")
                        }
                        is Byte -> {
                            sb1.append(b[obj.toInt() shr 4 and 0xf])
                            sb1.append(b[obj.toInt() and 0xf])
                        }
                        else -> {
                            sb1.append(obj)
                        }
                    }
                    sb.append(sb1)
                } catch (e: Throwable) {
                    sb.append("<序列化异常 ${e::class.java.name}: ${e.message}\t -> ${obj::class.java.name}>")
                }
                return@synchronized
            }
        }
        return sb.toString()
    }
}