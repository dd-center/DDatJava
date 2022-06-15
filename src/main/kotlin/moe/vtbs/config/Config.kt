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
package moe.vtbs.config

import moe.vtbs.lang.context
import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.Yaml
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.nio.charset.StandardCharsets

/**
 *  配置文件
 *
 * @author 一七年夏
 * @since 2022-06-12 20:22
 */
interface Config {
    companion object {
        /**
         * 根路径
         */
        val root
            get() = context.get().baseDir.resolve("conf").apply {
                if (!exists()) mkdirs()
            }

        @JvmStatic
        @Suppress("UNCHECKED_CAST")
        fun <T : Config> getConfig(type: Class<T>): T {
            if (!configs.containsKey(type)) {
                return initConfig(type)
            }
            return configs[type] as T
        }

        @JvmStatic
        fun <T : Config> loadConfig(type: Class<T>): T = yaml.loadAs(loadConfig0(type), type).apply {
            configs[type] = this
        }

        @JvmStatic
        fun <T : Config> initConfig(type: Class<T>): T = initConfig0(type).apply {
            configs[type] = this
        }

        @JvmStatic
        fun saveConfig(config: Config) = saveConfig0(config)

        @JvmStatic
        fun getPath(type: Class<out Config>) = getConfigFile(type)
    }

    /**
     * 保存配置文件
     */
    fun save() = saveConfig(this)

    /**
     * 读取配置文件，这将会切换至新的配置文件对象
     */
    fun load() = loadConfig(this::class.java)

    fun path() = getConfigFile(this::class.java)
}

/**
 * 使用DSL操作配置文件
 */
inline fun <reified T : Config, R> config(dsl: T.() -> R): R {
    return Config.getConfig(T::class.java).let(dsl)
}

/**
 * 使用DSL操作配置文件
 */
inline fun <reified T : Config> config(dsl: T.() -> Unit) {
    return Config.getConfig(T::class.java).let(dsl)
}

/**
 * 获取当前配置文件的绝对地址
 *
 * @see ConfigPath
 * @see Configs.rootPath
 */
fun getConfigFile(type: Class<out Config>): File {
    val property = type.getAnnotation(ConfigPath::class.java)
        ?: throw IllegalArgumentException(
            "${type.canonicalName}中未找到${ConfigPath::class.java.canonicalName}注解"
        )
    return Config.root.resolve(property.path)
}

/**
 * 配置文件列表
 */
private val configs = mutableMapOf<Class<out Config>, Config>()

private val yaml = Yaml(ConfigRepresenter(), DumperOptions().apply {
    isProcessComments = true
})

private fun loadConfig0(type: Class<out Config>): String {
    val url = getConfigFile(type).absolutePath
    return FileInputStream(url).use {
        it.reader().readText()
    }
}

private fun saveConfig0(obj: Config) {
    try {
        val type: Class<out Config> = obj.javaClass
        val f = getConfigFile(type)
        val path = f.absolutePath
        val p = f.parentFile
        val n = File("$path.new")
        if (p != null && !p.exists()) if (!p.mkdirs()) throw IOException("未能创建文件夹$p")
        val yaml = yaml.dumpAsMap(obj)
        try {
            if (loadConfig0(obj.javaClass) == yaml) return
        } catch (_: Throwable) {
        }
        val data: ByteArray = yaml.toByteArray(StandardCharsets.UTF_8)
        if (n.exists()) if (!n.delete()) throw IOException("未能删除$n")
        val fo = FileOutputStream(n)
        fo.write(data)
        fo.flush()
        fo.close()
        if (f.exists()) if (!f.delete()) throw IOException("未能删除$f")
        if (!n.renameTo(f)) throw IOException("未能重命名文件$n")
    } catch (e: RuntimeException) {
        throw e
    } catch (e: Throwable) {
        throw RuntimeException(e)
    }
}

private fun <T : Config> initConfig0(type: Class<T>): T {

    val file = getConfigFile(type)
    return if (file.exists()) Config.loadConfig(type) else {
        try {
            val constructor = type.getDeclaredConstructor()
            constructor.isAccessible = true
            val instance = constructor.newInstance() as T
            Config.saveConfig(instance)
            instance
        } catch (e: RuntimeException) {
            throw e
        } catch (e: Throwable) {
            throw RuntimeException(e)
        }
    }
}