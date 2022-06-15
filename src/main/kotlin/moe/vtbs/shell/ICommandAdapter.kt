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

import moe.vtbs.logger
import java.util.regex.Pattern

/**
 *  允许接收与分发命令的接口
 *
 * @author 一七年夏
 * @since 2021-10-06 18:32
 */
interface ICommandAdapter : ICommandListener {

    companion object {
        private val hasAscii: Boolean by lazy {
            try {
                Class.forName("moe.vtbs.ascii.AsciiString")
                return@lazy true
            } catch (e: Throwable) {
                return@lazy false
            }
        }
    }

    val parent: ICommandAdapter?

    /**
     * 获取子命令侦听器
     */
    fun getChildren(): List<ICommandListener>

    /**
     * 添加子命令侦听器
     */
    fun addListener(listener: ICommandListener): ICommandAdapter;

    /**
     * 简单的添加子命令侦听器
     */
    fun addListener(
        key: String,
        description: String? = null,
        help: String? = null,
        onCommand: (command: ICommandArgs) -> Unit
    ): ICommandAdapter {
        addListener(object : CommandListener(key, description, help) {
            override fun onCommand(command: ICommandArgs) = onCommand(command)
        })
        return this
    }

    fun popListener(key: String);

    /**
     * 处理由上游传来的命令
     */
    override fun onCommand(command: ICommandArgs) {
        val arg = command.nextArg()
        var adapter: ICommandListener? = null;
        for (a in getChildren()) {
            if (a.getListenKey() == arg) {
                adapter = a;
                break;
            }
        }
        if (adapter == null) preDealWithCommand(command);
        else {
            if (command.next()) adapter.onCommand(command);
        }
    }

    fun preDealWithCommand(command: ICommandArgs) {
        val residue = command.getResidueArgs()
        if (Pattern.matches("^\\?\\s.+|^help\\s.+|^\\?$|^help$|^$", residue)) {
            showHelp(command);
            return;
        }
        dealWithCommand(command);
    }

    /**
     * 处理传来的命令
     */
    fun dealWithCommand(command: ICommandArgs) {
        val residue = command.getResidueArgs()
        val before = command.getBeforeArgs()
        val strBefore = if (before.isNotEmpty() && !before.endsWith(' ')) "$before " else before
        val out = genAsciiDealWithString(residue, before, strBefore)
        logger.warn(LogStringMaker.instance.makeString(out))
    }


    fun genAsciiDealWithString(residue: String, before: String, strBefore: String): List<Any> {
        if (hasAscii) return AsciiUtil.instance.genAsciiDealWithString(residue, before, strBefore)
        val residue1 = if (residue.isEmpty()) " [~]"
        else "[$residue]"
        return listOf<Any>(
            """
            无效的命令，发生在 $before$residue1
            输入 "${strBefore}help" 或 "$strBefore?" 获取帮助
            """.trimIndent()
        )
    }

    fun showHelp(command: ICommandArgs) {
        if (Pattern.matches("^\\?$|^help$|^$", command.getResidueArgs())) {
            logger.info("指令“${command.getBeforeArgs()}”的帮助：\n${getHelp()}")
        } else {
            val before = command.getBeforeArgs();
            command.next()
            val key = command.nextArg();
            var listener: ICommandListener? = null;
            for (l in getChildren()) {
                if (l.getListenKey() == key) {
                    listener = l;
                    break;
                }
            }
            if (listener != null) logger.info("指令“$before$key”的帮助信息\n${listener.getHelp()}")
            else logger.warn("不存在“$before$key”这个指令");
        }
    }

    override fun getHelp(): String {
        return if (getChildren().isEmpty()) "当前指令暂无帮助"
        else getSubCommand();
    }

    fun getSubCommand(): String {
        val adapters = getChildren();
        val sb = StringBuilder();
        for (i in adapters.indices) {
            val c = adapters[i]
            sb.append("${i + 1}\t\t").append(c.getListenKey()).append("\t\t\t").append(c.getDescription()).append("\n")
        };
        return "共${adapters.size}条子命令：\n$sb";
    }

}