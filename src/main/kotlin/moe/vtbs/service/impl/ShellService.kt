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
package moe.vtbs.service.impl

import kotlinx.coroutines.*
import moe.vtbs.service.AbstractService
import moe.vtbs.shell.CommandSystem
import java.util.*

/**
 *  控制台服务
 *
 * @author 一七年夏
 * @since 2022-06-13 13:15
 */
class ShellService : AbstractService() {
    override val name: String = "控制台服务"

    val processor = CommandSystem()
    private var scannerJob: Job? = null;
    private var scanner: Scanner? = null;
    override fun start() {
        if (scannerJob?.isActive == true) return
        scannerJob = CoroutineScope(Dispatchers.IO).launch {
            Scanner(System.`in`).use {
                while (true) {
                    try {
                        it.nextLine().let {
                            launch(Dispatchers.Default) {
                                processor.onCommand(it)
                            }
                        }
                    } catch (e: NoSuchElementException) {
                        delay(250)
                    }
                }
            }
        }
        processor.start()
    }

    override fun close() {
        scanner?.close()
        scannerJob?.cancel()
    }
}