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
 *  命令侦听器
 *
 * @author 一七年夏
 * @since 2021-10-07 0:34
 */
abstract class CommandListener(private val key: String, description: String? = null, help: String? = null) :
    ICommandListener {

    private var _description: String? = description;
    private var _help: String? = help;

    override fun getListenKey(): String {
        return key;
    }

    override fun getDescription(): String {
        return if (_description == null) super.getDescription() else _description!!;
    }

    override fun getHelp(): String {
        return if (_help == null) super.getHelp() else _help!!;
    }
}