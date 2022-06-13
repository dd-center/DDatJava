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

import java.util.*

/**
 *  命令分发器
 *
 * @author 一七年夏
 * @since 2021-10-06 19:39
 */
abstract class AbstractCommandAdapter : ICommandAdapter {

    private val children: HashMap<String, ICommandListener> = HashMap();
    override fun getChildren(): List<ICommandListener> {
        return children.values.toList()
    }

    fun getChildrenMap(): Map<String, ICommandListener> {
        return Collections.unmodifiableMap(children);
    }

    override fun addListener(listener: ICommandListener): ICommandAdapter {
        children[listener.getListenKey()] = listener;
        if (listener is AbstractCommandAdapter) listener.parent = this
        return this
    }

    override fun popListener(key: String) {
        if (children.contains(key)) {
            val c = children[key]
            if (c is AbstractCommandAdapter) c.parent = null;
            children.remove(key)
        }
    }

    override fun onCommand(command: ICommandArgs) {
        val arg = command.nextArg()
        if (!children.contains(arg)) preDealWithCommand(command);
        else {
            command.next();
            children[arg]?.onCommand(command);
        }
    }

    override var parent: ICommandAdapter? = null
        protected set;
}