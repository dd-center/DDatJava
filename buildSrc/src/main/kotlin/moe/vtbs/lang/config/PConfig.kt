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
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1
import kotlin.reflect.KType
import kotlin.reflect.full.createType
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.memberProperties

/**
 *  代理Config
 *
 * @author 一七年夏
 * @since 2022-06-23 20:00
 */
open class PConfig(val parent: PConfig?) {

    protected inner class NotNull {
        operator fun <T : PConfig, R> setValue(pConfig: T, property: KProperty<*>, s: R) {
            this@PConfig.setValue(pConfig, property, s)
        }

        @Suppress("UNCHECKED_CAST")
        operator fun <T : PConfig, R> getValue(pConfig: T, property: KProperty<*>): R {
            return this@PConfig.getValue<T, R>(pConfig, property) as R
        }
    }

    inner class DefaultValue<R>(val callback: () -> R?) {
        operator fun <T : PConfig> setValue(pConfig: T, property: KProperty<*>, s: R?) {
            return this@PConfig.setValue<T, R>(pConfig, property, s)
        }

        operator fun <T : PConfig> getValue(pConfig: T, property: KProperty<*>): R? {
            return this@PConfig.getValue(pConfig, property) ?: callback().apply {
                setValue(pConfig, property, this)
            }
        }
    }

    inner class NotNullValue<R>(val callback: () -> R) {
        operator fun <T : PConfig> setValue(pConfig: T, property: KProperty<*>, s: R) {
            return this@PConfig.setValue<T, R>(pConfig, property, s)
        }

        operator fun <T : PConfig> getValue(pConfig: T, property: KProperty<*>): R {
            return this@PConfig.getValue(pConfig, property) ?: callback().apply {
                setValue(pConfig, property, this)
            }
        }
    }

    //请使用 “by this”，因为 “by default” 暂时无法使用
    //protected val default get() = this
    protected fun <T> default(callback: () -> T?) = DefaultValue(callback)
    protected fun <T> default(value: T?) = DefaultValue { value }
    protected val notnull = NotNull()
    protected fun <T> notnull(callback: () -> T) = NotNullValue(callback)
    protected fun <T> notnull(value: T) = NotNullValue { value }

    protected open fun <T : Any?> get(key: String, type: KType): T? {
        return parent!!.get("${parentProperty.name}.${key}", type)
    }

    operator fun get(key: String): Any? {
        return get(key, Any::class.createType(nullable = true))
    }

    open operator fun <T : Any?> set(key: String, data: T?) {
        parent!!["${parentProperty.name}.${key}"] = data
    }

    protected operator fun <T : PConfig, R> setValue(pConfig: T, property: KProperty<*>, s: R?) {
        set(property.name, s)
    }

    @Suppress("UNCHECKED_CAST")
    protected operator fun <T : PConfig, R> getValue(pConfig: T, property: KProperty<*>): R? {
        return get(property.name, property.returnType)
    }

    open val KProperty<*>.path: String
        get() = getPath(this.name)

    open fun getPath(bean: String): String {
        return parent!!.getPath("${parentProperty.name}.${bean}")
    }

    @Suppress("UNCHECKED_CAST")
    protected val parentProperty: KProperty<*> by lazy {
        require(parent != null) { "When calling parentProperty, the parent configuration cannot be null!" }
        parent::class.memberProperties.firstOrNull {
            it as? KProperty1<PConfig, *> ?: return@firstOrNull false
            val kc = it.returnType.classifier as? KClass<*> ?: return@firstOrNull false
            if (kc.isSubclassOf(this::class)) {
                it.get(parent) == this
            } else return@firstOrNull false
        } ?: throw IllegalStateException("Failed to find current configuration in parent configuration!")
    }
}