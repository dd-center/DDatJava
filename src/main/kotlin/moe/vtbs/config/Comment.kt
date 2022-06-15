package moe.vtbs.config

/**
 *  注释
 *
 * @author 一七年夏
 * @since 2022-06-12 20:35
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.PROPERTY)
annotation class Comment(val comment: String)
