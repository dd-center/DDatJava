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
 *  允许接收命令的接口
 *
 * @author 一七年夏
 * @since 2021-10-07 0:27
 */
interface ICommandListener {

    /**
     * 获取子命令适配器
     */
    fun getListenKey(): String

    fun onCommand(command: ICommandArgs);

    fun getDescription(): String {
        return ""
    }

    fun getHelp(): String {
        return "当前指令暂无帮助"
    }
}