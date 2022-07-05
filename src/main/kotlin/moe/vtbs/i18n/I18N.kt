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

import moe.vtbs.Bootstrap
import moe.vtbs.lang.config.PConfig
import moe.vtbs.lang.service.ServiceManager
import moe.vtbs.shell.CommandSystem
import moe.vtbs.util.Image
import moe.vtbs.util.ReflectionUtil

/**
 *  I18N
 *
 * @author 一七年夏
 * @since 2022-07-05 14:33
 */
class I18N(parent: PConfig?) : PConfig(parent) {
    val languageID by notnull("zh-cn")
    val bootstrap = Bootstrap.I18N(this)
    val service = ServiceManager.I18N(this)
    val shell = CommandSystem.I18N(this)
    val util = UtilI18N(this)

    class UtilI18N(parent: PConfig?) : PConfig(parent) {
        val image = Image.I18N(this)
        val reflect = ReflectionUtil.I18N(this)
    }
}