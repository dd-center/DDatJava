package moe.vtbs.lang.annotation

/**
 *  阻塞方法
 *
 *  此注解注解的方法进行了阻塞操作，不建议使用
 *
 * @author 一七年夏
 * @since 2022-04-28 21:33
 */

@RequiresOptIn("阻塞方法", level = RequiresOptIn.Level.WARNING)
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.FUNCTION)
annotation class Blocked
