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
package moe.vtbs.lang.config

import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.createType
import kotlin.reflect.full.isSupertypeOf

/**
 *  代理Config根
 *
 * @author 一七年夏
 * @since 2022-06-23 20:04
 */
abstract class PConfigRoot : PConfig(null) {

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any?> get(key: String, type: KType): T? {
        return concert(get0(key, type), type) as T?
    }

    override fun <T : Any?> set(key: String, data: T?) {
        set0(key, data)
    }

    override fun getPath(bean: String): String {
        return bean
    }

    abstract fun <T : Any?> get0(key: String, type: KType): T?
    abstract fun <T : Any?> set0(key: String, data: T?)

    @Suppress("UNCHECKED_CAST")
    open fun concert(obj: Any?, type: KType): Any? {
        obj ?: return null
        if (type.isSupertypeOf(obj::class.createType(nullable = true))) return obj
        val clazz = type.classifier as? KClass<*> ?: return null
        if (obj::class == clazz) return obj
        if (clazz == String::class) return obj.toString()
        if (clazz == Boolean::class) {
            if (obj is String) return obj.toBoolean()
            if (obj is Number) return (obj != 0)
        }
        if (obj is Number) {
            when (clazz) {
                Int::class -> return obj.toInt()
                Long::class -> return obj.toLong()
                Short::class -> return obj.toShort()
                Double::class -> return obj.toDouble()
                Float::class -> return obj.toFloat()
                Char::class -> return obj.toChar()
                Byte::class -> return obj.toByte()
            }
        }
        if (obj is String) {
            when (clazz) {
                Int::class -> return obj.toInt()
                Long::class -> return obj.toLong()
                Short::class -> return obj.toShort()
                Double::class -> return obj.toDouble()
                Float::class -> return obj.toFloat()
                Char::class -> return obj[0]
                Byte::class -> return obj.toByte()
            }
        }
        return obj
    }
}