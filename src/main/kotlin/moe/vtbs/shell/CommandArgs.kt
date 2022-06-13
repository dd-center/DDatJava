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
 *  命令
 *
 * @author 一七年夏
 * @since 2021-10-06 20:02
 */
class CommandArgs : ICommandArgs {
    constructor(rawCommand: String) {
        this.rawCommand = rawCommand;
        this.residueArgs = rawCommand;
    }

    private val rawCommand: String
    override fun getRawCommand(): String {
        return rawCommand
    }

    private var residueArgs = "";
    override fun getResidueArgs(): String {
        return residueArgs;
    }

    override fun setResidueArgs(residueArgs: String) {
        this.residueArgs = residueArgs;
    }
}