/*
 * Copyright (C) 2021 一七年夏
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
package moe.vtbs.lang.exception

import okhttp3.Response
import java.io.IOException

/**
 *  网络错误
 *
 * @author 一七年夏
 * @since 2021-12-28 17:13
 */
open class NetException(
    val code: Int,
    val response: Response,
    message: String?,
    cause: Throwable?
) : IOException(message, cause) {

    constructor(
        code: Int,
        response: Response,
        cause: Throwable?
    ) : this(code, response, null, cause)

    constructor(
        code: Int,
        response: Response
    ) : this(code, response, null, null)

    companion object {
        /**
         * 创建一个网络异常
         */
        fun makeException(
            code: Int,
            response: Response,
            message: String? = null,
            cause: Throwable? = null,
            runnable: (NetException.() -> Unit)? = null
        ): NetException {
            val s = if (message != null) "${message}: ${getMessage(code)}" else getMessage(code)
            val e = NetException(code, response, s, cause)
            runnable?.let { it(e) }
            return e
        }

        private fun getMessage(code: Int): String {
            return "$code " + when (code) {
                400 -> "错误请求"
                401 -> "未授权"
                403 -> "已禁止"
                404 -> "未找到"
                405 -> "方法禁用"
                406 -> "不接受"
                407 -> "需要代理授权"
                408 -> "请求超时"
                409 -> "冲突"
                410 -> "已删除"
                411 -> "需要有效长度"
                412 -> "未满足前提条件"
                413 -> "请求实体过大"
                414 -> "请求的 URI 过长"
                415 -> "不支持的媒体类型"
                416 -> "请求范围不符合要求"
                417 -> "未满足期望值"
                500 -> "服务器内部错误"
                501 -> "尚未实施"
                502 -> "错误网关"
                503 -> "服务不可用"
                504 -> "网关超时"
                505 -> "HTTP 版本不受支持"
                else -> "其他错误"
            }
        }
    }
}