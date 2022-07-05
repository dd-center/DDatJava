package moe.vtbs.lang

import java.util.*


/**
 * 占位符处理器
 *
 * @Author zq
 * @Date 2022/4/19 15:01
 * @Description
 * @Version 1.0
 */
class Placeholder(
    /**
     * 返回处理后的文本
     * @return
     */
    var str: String
) {
    /**
     * 返回需要替换掉的变量
     * @return
     */
    val variables: List<String>
    private val buffer = mutableMapOf<String, String?>()

    init {
        val list: MutableList<String> = LinkedList()
        val str = str.toCharArray()
        var start: Int
        var end: Int
        var i = 0
        while (i != -1) {
            start = this.str.indexOf("%", i)
            i = start
            if (start == -1) break
            if (str[i] == '%') {
                if (i - 1 >= 0 && str[i - 1] == '\\') {
                    i = start + 1
                    continue
                }
            }
            i = start + 1
            end = this.str.indexOf("%", i + 1)
            list.add(this.str.substring(start + 1, end))
            i = end + 1
        }
        variables = list
    }

    /**
     * 进行文本中的变量替换
     * @param para 键值对
     */
    fun setPara(para: Map<String, String?>) {
        buffer.putAll(para)
    }

    /**
     * 获取输出值
     */
    fun get(): String {
        var out = str
        buffer.forEach { (k, v) ->
            if (v == null) return@forEach
            out = out.replace("%$k%", v)
        }
        return out
    }

    companion object {
        fun processingText(text: String, cb: (String) -> String?): String {
            val sb = StringBuilder(text)
            val value = StringBuilder()
            var start: Int
            var end: Int
            while (true) {
                start = sb.indexOf("%", 0)
                if (start == -1) {
                    break
                }
                if (sb[start] == '%') {
                    if (start - 1 >= 0 && sb[start - 1] == '\\') {
                        value.append(sb.substring(0, start + 1))
                        sb.delete(0, start + 1)
                        continue
                    }
                }
                value.append(sb.substring(0, start))
                sb.delete(0, start)
                end = sb.indexOf("%", 1)
                val key = sb.substring(1, end)
                value.append(cb(key) ?: "%${key}%")
                sb.delete(0, end + 1)
            }
            value.append(sb)
            return value.toString()
        }
    }
}