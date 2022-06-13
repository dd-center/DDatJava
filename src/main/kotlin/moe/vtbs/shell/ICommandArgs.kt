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
 *  命令参数
 *
 * @author 一七年夏
 * @since 2021-10-06 18:40
 */
interface ICommandArgs {
    /**
     * 获取原始参数字符串
     * @return 原始参数字符串
     */
    fun getRawCommand(): String

    /**
     * 获取下一个匹配参数值
     * @return 参数值。
     */
    fun nextArg(): String {
        val r = getResidueArgs().trim();
        return if (r.contains(' ')) {
            val args = r.split(' ', limit = 2)
            args[0]
        } else r;
    }

    fun next(): Boolean {
        val r = getResidueArgs().trim();
        if (r.isEmpty()) return false;
        if (r.contains(' ')) {
            val args = r.split(' ', limit = 2)
            setResidueArgs(args[1]);
        } else setResidueArgs("")
        return true;
    }

    /**
     * 获取上一个匹配参数值
     * @return 参数值。null：没有上一个参数值了
     */
    fun beforeArg(): String? {
        if (getRawCommand().length == getResidueArgs().length) return null;
        val before = getBeforeArgs().trim();
        val arg: String = if (before.contains(' ')) {
            before.substring(before.lastIndexOf(' ') + 1)
        } else before
        return arg;
    }

    fun before(): Boolean {
        if (getBeforeArgs().isEmpty()) return false;
        val before = getBeforeArgs().trim();
        val arg: String = if (before.contains(' ')) {
            before.substring(before.lastIndexOf(' ') + 1)
        } else before
        setResidueArgs(arg + " " + getResidueArgs())
        return true;
    }

    /**
     * 获取剩余的参数字符串
     * @return 参数字符串
     */
    fun getResidueArgs(): String

    /**
     * 设置剩余的参数字符串
     */
    fun setResidueArgs(residueArgs: String)

    fun getBeforeArgs(): String {
        val raw = getRawCommand()
        return raw.substring(0, raw.length - getResidueArgs().length)
    }
}