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
package moe.vtbs.config.impl

import moe.vtbs.config.Comment
import moe.vtbs.config.Config
import moe.vtbs.config.ConfigPath

/**
 *  全局配置
 *
 * @author 一七年夏
 * @since 2022-06-13 12:46
 */
@ConfigPath("config.yml")
class GlobalConfig : Config {
    @Comment("应用程序设置")
    var app = App()

    class App {
        @Comment(
            """
            操作间隔，单位为秒
            默认值为 120
            """
        )
        var interval = 120L

        @Comment("昵称")
        var nickname = "<unset>"
    }
}