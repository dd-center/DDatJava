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
package moe.vtbs.util

import kotlin.reflect.KProperty

/**
 *  DSL构建器
 *
 * @author 一七年夏
 * @since 2022-06-11 19:53
 */


class DSLBuilder<T>(val get: () -> T) {
    inline val invoker: (T.() -> Unit) -> T
        get() = {
            get().apply(it)
        }

    operator fun invoke(dsl: T.() -> Unit): T {
        return invoker(dsl)
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>): (T.() -> Unit) -> T {
        return invoker
    }
}

class DSLBuilder1<I, T>(val get: (I) -> T) {
    inline val invoker: I.(T.() -> Unit) -> T
        get() = { it ->
            get(this).apply(it)
        }

    operator fun invoke(i: I, it: T.() -> Unit): T {
        return invoker(i, it)
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>): I.(T.() -> Unit) -> T {
        return invoker
    }
}