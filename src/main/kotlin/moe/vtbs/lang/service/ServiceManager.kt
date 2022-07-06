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
package moe.vtbs.lang.service

import moe.vtbs.lang.config.PConfig
import moe.vtbs.i18n
import moe.vtbs.lang.papi
import moe.vtbs.logger
import moe.vtbs.service.CenterServerDistributionService
import moe.vtbs.service.ShellService

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
        require(Service::class.java.isAssignableFrom(clazz)) {
            // 0 不是 1
            i18n.service.setNotAs.papi(0 to clazz, 1 to Service::class.java)
        }
        require(!services.containsKey(clazz as Class<Service>)) {
            // 服务已被设置
            i18n.service.setHasBeenSet.papi(0 to clazz.name)
        }
        service.init(this)
        services[clazz] = service
        //服务设置成功
        logger.info(i18n.service.setSuccess.papi("name" to service.name, "class" to clazz.name))
    }

    /**
     * 注册一个服务
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : Service> set(service: T) {
        require(!services.containsKey(service::class.java as Class<Service>)) {
            i18n.service.setHasBeenSet.papi(0 to service::class.java)
        }
        service.init(this)
        services[service.javaClass] = service
        //服务设置成功
        logger.info(i18n.service.setSuccess.papi("name" to service.name, "class" to service.javaClass.name))
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
        require(!services.containsKey(clazz as Class<Service>)) {
            i18n.service.setHasBeenSet.papi(0 to clazz)
        }
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
        logger.info(i18n.service.disableRunning.papi(0 to service.name))
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

    class I18N(parent: PConfig?) : PConfig(parent) {
        val setNotAs by notnull("%0%不是%1%")
        val setHasBeenSet by notnull("服务%0%已经被注册了")
        val setSuccess by notnull("成功的注册了%name%(%class%)")
        val disableRunning by notnull("正在关闭服务%0%")

        val distribution = CenterServerDistributionService.I18N(this)
        val shell = ShellService.I18N(this)
    }
}