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

//import androidx.annotation.CallSuper

/**
 *  抽象服务
 *
 * @author 一七年夏
 * @since 2022-05-15 19:55
 */
abstract class AbstractService : Service {
    lateinit var manager: ServiceManager

    //@CallSuper
    override fun init(manager: ServiceManager) {
        super.init(manager)
        this.manager = manager
    }
}