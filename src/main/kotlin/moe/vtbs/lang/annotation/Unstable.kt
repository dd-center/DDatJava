package moe.vtbs.lang.annotation

import kotlin.annotation.AnnotationTarget.*

/**
 *  不稳定的
 *
 * @author 一七年夏
 * @since 2022-04-18 19:41
 */
@RequiresOptIn("不稳定的使用方法，可能存在问题", level = RequiresOptIn.Level.WARNING)
@Retention(AnnotationRetention.BINARY)
@Target(
    CLASS,
    ANNOTATION_CLASS,
    PROPERTY,
    FIELD,
    LOCAL_VARIABLE,
    VALUE_PARAMETER,
    CONSTRUCTOR,
    FUNCTION,
    PROPERTY_GETTER,
    PROPERTY_SETTER
)
annotation class Unstable
