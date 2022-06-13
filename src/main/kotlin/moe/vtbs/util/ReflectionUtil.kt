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
package moe.vtbs.util

import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.isAccessible

/**
 *  反射
 *
 * @author 一七年夏
 * @since 2022-05-27 13:40
 */
object ReflectionUtil {
    /**
     * 查找一个类
     * @param clazz 类名
     * @return 类，如果未找到，则为null
     */
    fun findClass(clazz: String): Class<*>? {
        return try {
            Class.forName(clazz)
        } catch (e: ClassNotFoundException) {
            null
        }
    }

    @Suppress("UNCHECKED_CAST")
    inline fun <reified T : Any, R : Any?> T.method(name: String, vararg params: Pair<KClass<*>, Any?>): R {
        return method(T::class.java, name, *params)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any, R : Any?> T.method(clazz: Class<T>, name: String, vararg params: Pair<KClass<*>, Any?>): R {
        return clazz.getMethod(name, *params.map { it.first.java }.toTypedArray()).let { method ->
            method.isAccessible = true
            method.invoke(this, *params.map { it.second }.toTypedArray()) as R
        }
    }

    @Suppress("UNCHECKED_CAST")
    inline fun <reified T : Any, R : Any?> T.field(name: String): R {
        return field(T::class.java, name)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any, R : Any?> T.field(clazz: Class<T>, name: String): R {
        return clazz.getDeclaredField(name).let {
            it.isAccessible = true
            it.get(this) as R
        }
    }

    @Suppress("UNCHECKED_CAST")
    inline fun <reified T : Any, R : Any?> T.member(name: String): R {
        return member(T::class.java, name)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any, R : Any?> T.member(clazz: Class<T>, name: String): R {
        clazz.kotlin.declaredMemberProperties.find { it.name == name }?.let {
            it.isAccessible = true
            return (it as KProperty1<Any, R>).get(this)
        } ?: throw NoSuchMethodException("找不到属性${name}")
    }
}