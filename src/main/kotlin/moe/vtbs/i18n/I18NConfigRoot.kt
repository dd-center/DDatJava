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
package moe.vtbs.i18n

import moe.vtbs.lang.config.PConfigRoot
import moe.vtbs.lang.config.config
import moe.vtbs.config.GlobalConfig
import kotlin.reflect.KType

/**
 *  I18N配置文件根
 *
 * @author 一七年夏
 * @since 2022-07-05 14:10
 */
object I18NConfigRoot : PConfigRoot() {

    const val ENTER = "⮰"
    private val languageMap: Map<String, String> = try {
        val map = mutableMapOf<String, String>()
        val name = "/lang/${config<GlobalConfig>().language}.ini"
        I18NConfigRoot::class.java.getResourceAsStream(name)?.use {
            val texts = it.reader().readLines().filter { line ->
                !line.startsWith("#") && line.contains("=")
            }
            texts.forEach { line ->
                val pair = line.split("=", limit = 2)
                map[pair[0]] = pair[1].replace(ENTER, "\n")
            }
        } ?: throw RuntimeException("Failed to load language file, language file 'Classpath:/$name' does not exist!")
        map.toMap()
    } catch (e: Throwable) {
        e.printStackTrace()
        System.err.println("Failed to load language file, Simplified Chinese will be used as default language")
        emptyMap()
    }

    val i18n = I18N(this)

    @Suppress("UNCHECKED_CAST")
    override fun <T> get0(key: String, type: KType): T? {
        return languageMap[key] as? T
    }

    override fun <T> set0(key: String, data: T?) {
        //Not Support! It's read only.
    }
}