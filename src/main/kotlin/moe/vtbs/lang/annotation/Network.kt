package moe.vtbs.lang.annotation

/**
 *  网络操作
 *
 *  此注解注解的方法等表明进行了网络操作，需要被特别注意
 *
 * @author 一七年夏
 * @since 2022-04-28 21:33
 */

@RequiresOptIn("网络操作，最好是进行异常处理", level = RequiresOptIn.Level.WARNING)
@Retention(AnnotationRetention.BINARY)
@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.ANNOTATION_CLASS,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.FIELD,
    AnnotationTarget.LOCAL_VARIABLE,
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.CONSTRUCTOR,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
annotation class Network
