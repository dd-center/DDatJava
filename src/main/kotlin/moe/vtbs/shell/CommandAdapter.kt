/*
 * Copyright (C) 2021 一七年夏
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
package moe.vtbs.shell


/**
 *  命令分发器
 *
 * @author 一七年夏
 * @since 2021-10-06 19:56
 */
open class CommandAdapter(
    private val listingKey: String,
    private val description: String = "",
) : AbstractCommandAdapter() {
    override fun getListenKey(): String {
        return listingKey
    }

    override fun getDescription(): String {
        return description;
    }
}