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
package moe.vtbs

import org.junit.jupiter.api.Test

/**
 *  启动测试
 *
 * @author 一七年夏
 * @since 2022-06-13 16:57
 */
class BootstrapTest {

    @Test
    @Deprecated("有没有一种可能……Scanner不能与JUnit一同使用？")
    fun main() {
        Bootstrap.main(emptyArray())
    }
}