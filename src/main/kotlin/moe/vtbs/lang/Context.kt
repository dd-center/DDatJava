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

import moe.vtbs.util.DSLBuilder
import moe.vtbs.util.ReflectionUtil.findClass
import java.io.File

/**
 *  上下文
 *
 * @author 一七年夏
 * @since 2022-06-13 10:56
 */
interface Context {
    companion object {
        var instance: Context = DefaultContext()
            private set;

        fun setContext(context: Context) {
            instance = context
        }

        fun setContext(context: android.content.Context): Context {
            return AndroidContext(context).apply {
                instance = this
            }
        }
    }

    val baseDir: File
}

val context = DSLBuilder { Context.instance }

/**
 *  默认上下文
 *
 * @author 一七年夏
 * @since 2022-06-13 11:19
 */
class DefaultContext : Context {
    override val baseDir by lazy {
        if (findClass("android.content.Context") != null) {
            throw IllegalStateException(
                """
                Cannot get baseDir form Android. 
                Use "moe.vtbs.lang.Context.Companion.setContext(android.content.Context)" to initialize it.
                """.trimIndent()
            )
        }
        File("./").absoluteFile
    }
}


/**
 *  安卓上下文
 *
 * @author 一七年夏
 * @since 2022-06-13 11:16
 */
internal class AndroidContext(context: android.content.Context) : Context {
    val context = if (context is android.content.ContextWrapper) context.applicationContext else context
    override val baseDir: File
        get() = context.getExternalFilesDir("") ?: context.filesDir

}