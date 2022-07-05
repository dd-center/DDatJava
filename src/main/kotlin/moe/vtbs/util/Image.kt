package moe.vtbs.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.him188.kotlin.jvm.blocking.bridge.JvmBlockingBridge
import moe.vtbs.i18n
import moe.vtbs.lang.annotation.Network
import moe.vtbs.lang.config.PConfig
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
    /**
     * 从URL获取一张图像
     * @return 图像，如果为null则为获取失败
     */
    @JvmStatic
    @JvmBlockingBridge
    @OptIn(Network::class)
    @Contract("null -> null")
    suspend fun getImage(url: String?): BufferedImage? {
        url ?: return null
        return try {
            withContext(Dispatchers.IO) {
                ImageIO.read(network.getInputStream(url))
            }
        } catch (e: Throwable) {
            logger.error(i18n.util.image.errCannotDownloadImage, e)
            null
        }
    }

    class I18N(parent: PConfig?) : PConfig(parent) {
        val errCannotDownloadImage by notnull("未能下载图像")
    }
}