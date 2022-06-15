package moe.vtbs

import moe.vtbs.config.config
import moe.vtbs.config.impl.GlobalConfig
import moe.vtbs.lang.annotation.Blocked
import moe.vtbs.service.impl.CenterServerDistributionService
import moe.vtbs.service.impl.ShellService
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
        config<GlobalConfig> {
            logger.info("配置文件路径：${path()}")
            if (app.nickname == "<unset>") waitForSetANickName()
            val svc = service.set<CenterServerDistributionService>()
            svc.start()
            service.set<ShellService>().start()
            @OptIn(Blocked::class)
            svc.waitFor()
            exitProcess(0)
        }
    }

    private fun waitForSetANickName() {
        logger.error("未设置账户昵称，请输入一个账户昵称并按下确定。")
        logger.error("或者按下Ctrl+C退出程序，打开配置文件手动编辑")
        Scanner(System.`in`).use {
            while (true) {
                val name = it.nextLine()
                if (name.trim().isNotEmpty()) {
                    config<GlobalConfig> {
                        app.nickname = name
                        save()
                    }
                    return
                }
            }
        }
    }
}