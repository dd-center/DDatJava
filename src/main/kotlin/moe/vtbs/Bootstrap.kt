package moe.vtbs

import kotlinx.coroutines.runBlocking
import moe.vtbs.lang.config.PConfig
import moe.vtbs.lang.config.config
import moe.vtbs.config.GlobalConfig
import moe.vtbs.service.CenterServerDistributionService
import moe.vtbs.service.ShellService
import java.util.*
import kotlin.system.exitProcess

/**
 *  启动器
 *
 * @author 一七年夏
 * @since 2022-06-13 12:44
 */
object Bootstrap {

    @JvmStatic
    fun main(args: Array<String>) {
        val lang = i18n.languageID
        val configLang = config<GlobalConfig>().language
        if (lang != configLang) {
            logger.warn("The user specified $configLang as the programming language, but actually used $lang")
        }
        // 配置文件路径
        val path = config<GlobalConfig>().path()
        logger.info("${i18n.bootstrap.configFilePath}${path}")
        val nickname = config<GlobalConfig>().distribution.nickname
        if (nickname == "<unset>") waitForSetANickName()
        val svc = service.set<CenterServerDistributionService>()
        svc.start()
        service.set<ShellService>().start()
        runBlocking { svc.waitFor() }
        exitProcess(0)
    }

    private fun waitForSetANickName() {
        logger.error(i18n.bootstrap.waitForSetANickName)
        Scanner(System.`in`).use {
            while (true) {
                val name = it.nextLine()
                if (name.trim().isNotEmpty()) {
                    config<GlobalConfig> {
                        distribution.nickname = name
                        save()
                    }
                    return
                }
            }
        }
    }

    class I18N(parent: PConfig?) : PConfig(parent) {
        val configFilePath by notnull("配置文件路径：")
        val waitForSetANickName by notnull(
            """
            未设置账户昵称，请输入一个账户昵称并按下回车键。
            或者按下Ctrl+C退出程序，打开配置文件手动编辑。
        """.trimIndent()
        )
    }
}