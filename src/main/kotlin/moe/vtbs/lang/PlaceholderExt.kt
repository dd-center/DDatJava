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
package moe.vtbs.lang

/**
 *  占位符扩展
 *
 * @author 一七年夏
 * @since 2022-04-19 16:59
 */

/**
 * 处理文本，替换全部文本中的占位符为变量值
 * @param this 输入文本，包含变量占位符
 * @param callback 处理回调
 */
fun String.papi(callback: (String) -> Any?): String = Placeholder.processingText(this) {
    return@processingText callback(it)?.toString()
}

/**
 * 处理文本，替换全部文本中的占位符为变量值
 * @param this 输入文本，包含变量占位符
 * @param map 包含占位符替换的词典
 */
fun String.papi(map: Map<String, Any?>): String {
    return papi a@{
        if (map.containsKey(it)) return@a map[it].toString()
        else null
    }
}

/**
 * 处理文本，替换全部文本中的占位符为变量值
 * @param this 输入文本，包含变量占位符
 * @param params 包含占位符替换的键值对
 */
fun String.papi(vararg params: Pair<Any, Any?>): String {
    return papi(params.associate { (k, v) -> Pair(k.toString(), v) })
}