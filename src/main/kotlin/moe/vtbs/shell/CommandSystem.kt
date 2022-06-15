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
 *  命令系统
 *
 * @author 一七年夏
 * @since 2021-10-06 17:48
 */
open class CommandSystem : ICommandAdapter {
    private var running = false;
    val commandAdapter = RootCommandAdapter()

    fun start() {
        if (running) {
            logger.warn("命令系统已经启动过一次了")
            return
        };
        logger.info("命令系统成功启动，键入 “?” 或 “help” 以获取使用帮助，键入“{父指令} ? [子指令]”以查看子指令的帮助")
    }

    fun onCommand(command: String) {
        commandAdapter.onCommand(CommandArgs(command))
    }

    inner class RootCommandAdapter : AbstractCommandAdapter() {

        override fun getListenKey(): String {
            return ""
        }

        override fun showHelp(command: ICommandArgs) {
            if (Pattern.matches("^\\?$|^help$|^$", command.getResidueArgs())) {
                logger.info(getHelp())
            } else {
                val before = command.getBeforeArgs();
                command.next()
                val key = command.nextArg();
                val listener: ICommandListener? = getChildrenMap()[key];
                if (listener != null) logger.info("指令“$before$key”的帮助信息\n${listener.getHelp()}")
                else logger.warn("不存在“$before$key”这个指令");
            }
        }
    }

    override val parent: ICommandAdapter?
        get() = commandAdapter.parent

    override fun getChildren(): List<ICommandListener> {
        return commandAdapter.getChildren();
    }

    override fun addListener(listener: ICommandListener): ICommandAdapter {
        return commandAdapter.addListener(listener)
    }

    override fun popListener(key: String) {
        return commandAdapter.popListener(key);
    }

    override fun getListenKey(): String {
        return commandAdapter.getListenKey()
    }
}