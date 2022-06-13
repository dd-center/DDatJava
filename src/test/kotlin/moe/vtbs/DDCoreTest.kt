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

import kotlinx.coroutines.*
import javax.swing.Icon
import javax.swing.ImageIcon
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JWindow
import kotlin.test.Test

/**
 *  DDCore测试
 *
 * @author 一七年夏
 * @since 2022-06-12 23:04
 */
class DDCoreTest {
    val swing = CoroutineScope(Dispatchers.Main)

    @Test
    fun test1() = runBlocking {
        //logger.info("开始测试")
        //val detail = DDCore.getDetailAsync(846180)
        //logger.info("获取详情成功")
        //val image = detail.getFaceImageAsync() ?: return@runBlocking
        //logger.info("找到图片成功")
        val info = DDCore.getSpaceInfoAsync(846180)
        val image =info.liveRoom.getCoverImageAsync()
        val window = swing.async {
            logger.info("显示窗口")
            val window = JFrame()
            val jLabel = JLabel(ImageIcon(image))
            window.add(jLabel)
            window.isVisible = true
            window.setBounds(300, 300, 500, 500)
            window.setLocationRelativeTo(null)
            return@async window
        }.await()

        while (window.isVisible) delay(500)
    }
}