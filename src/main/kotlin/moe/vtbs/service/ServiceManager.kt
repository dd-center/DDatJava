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
package moe.vtbs.service

import moe.vtbs.logger

/**
 *  服务管理器
 *
 * @author 一七年夏
 * @since 2022-05-15 19:39
 */
class ServiceManager {
    val services = mutableMapOf<Class<out Service>, Service>()

    /**
     * 获取一个服务
     */
    @Suppress("UNCHECKED_CAST")
    operator fun <T : Service> get(type: Class<T>): T? {
        return services[type as Class<Service>] as T?
    }

    /**
     * 获取一个服务
     */
    @Suppress("UNCHECKED_CAST")
    inline fun <reified T : Service> get(): T? {
        return services[T::class.java as Class<Service>] as T?
    }

    /**
     * 注册一个服务
     */
    @Suppress("UNCHECKED_CAST")
    operator fun <T : Service> set(clazz: Class<T>, service: T) {
        require(Service::class.java.isAssignableFrom(clazz)) { "${clazz}不是${Service::class.java}" }
        require(!services.containsKey(clazz as Class<Service>)) { "服务已经被注册了" }
        service.init(this)
        services[clazz] = service
        logger.info("成功的注册了${service.name}(${clazz})")
    }

    /**
     * 注册一个服务
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : Service> set(service: T) {
        require(!services.containsKey(service::class.java as Class<Service>)) { "服务已经被注册了" }
        service.init(this)
        services[service.javaClass] = service
        logger.info("成功的注册了${service.name}(${service::class.java})")
    }

    /**
     * 注册一个服务
     */
    inline fun <reified T : Service> set() = set(T::class.java)

    /**
     * 注册一个服务
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : Service> set(clazz: Class<T>): T {
        require(!services.containsKey(clazz as Class<Service>)) { "服务已经被注册了" }
        val instance = clazz.getConstructor().newInstance()
        set(clazz, instance)
        return instance as T
    }

    /**
     * 获取或注册一个服务，服务将会自动启动
     */
    fun <T : Service> getOrCreate(clazz: Class<T>): T {
        return get(clazz) ?: set(clazz)
    }

    /**
     * 获取或注册一个服务，服务将会自动启动
     */
    inline fun <reified T : Service> getOrCreate(): T {
        return get() ?: set<T>().apply {
            start()
        }
    }

    /**
     * 关闭一个服务
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : Service> disable(type: Class<T>): Boolean {
        val service = services[type as Class<Service>] ?: return false
        logger.info("正在关闭服务${service.name}")
        try {
            service.close()
        } catch (e: Throwable) {
            logger.error("关闭服务时发生异常", e)
        }
        services.remove(type)
        return true
    }

    /**
     * 关闭一个服务
     */
    inline fun <reified T : Service> disable(): Boolean {
        return disable(T::class.java)
    }
}