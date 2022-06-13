package moe.vtbs.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import moe.vtbs.lang.annotation.Blocked
import moe.vtbs.lang.annotation.Network
import moe.vtbs.logger
import moe.vtbs.network
import org.jetbrains.annotations.Contract
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

/**
 *  图像工具
 *
 * @author 一七年夏
 * @since 2022-06-12 22:45
 */
object Image {

    @Blocked
    @Contract("null -> null")
    fun getImage(url: String?): BufferedImage? = runBlocking { getImageAsync(url) }

    @OptIn(Network::class)
    @Contract("null -> null")
    suspend fun getImageAsync(url: String?): BufferedImage? {
        url ?: return null
        return try {
            withContext(Dispatchers.IO) {
                ImageIO.read(network.getInputStream(url))
            }
        } catch (e: Throwable) {
            logger.error("未能下载图像", e)
            null
        }
    }
}