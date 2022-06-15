package moe.vtbs.ascii

import moe.vtbs.shell.ascii.AsciiStringGroup
import moe.vtbs.shell.ascii.Foreground
import moe.vtbs.shell.ascii.Foreground.Companion.Black
import moe.vtbs.shell.ascii.Foreground.Companion.Blue
import moe.vtbs.shell.ascii.Foreground.Companion.Cyan
import moe.vtbs.shell.ascii.Foreground.Companion.DarkBlue
import moe.vtbs.shell.ascii.Foreground.Companion.DarkGreen
import moe.vtbs.shell.ascii.Foreground.Companion.DarkRed
import moe.vtbs.shell.ascii.Foreground.Companion.Gold
import moe.vtbs.shell.ascii.Foreground.Companion.Gray
import moe.vtbs.shell.ascii.Foreground.Companion.Green
import moe.vtbs.shell.ascii.Foreground.Companion.LightGray
import moe.vtbs.shell.ascii.Foreground.Companion.Pink
import moe.vtbs.shell.ascii.Foreground.Companion.Purple
import moe.vtbs.shell.ascii.Foreground.Companion.Red
import moe.vtbs.shell.ascii.Foreground.Companion.Sky
import moe.vtbs.shell.ascii.Foreground.Companion.White
import moe.vtbs.shell.ascii.Foreground.Companion.Yellow
import moe.vtbs.shell.ascii.Format
import moe.vtbs.shell.ascii.Format.Companion.Flicker
import moe.vtbs.shell.ascii.Format.Companion.Highlight
import moe.vtbs.shell.ascii.Format.Companion.Italic
import moe.vtbs.shell.ascii.Format.Companion.Strikethrough
import moe.vtbs.shell.ascii.Format.Companion.Underline

/** 转为彩色文字，如未启用彩色文字，则返回正确的普通文字 */
fun String.asAsciiColor(): AsciiStringGroup {
    val out = mutableListOf<AsciiString>()
    val s = this.replace("&", "&\u007f").split("&").toTypedArray() //
    var last = AsciiString.Default

    for (str in s) {
        var buffer = str
        if (buffer.isNotEmpty() && buffer.length >= 2 && buffer.substring(0, 2)
                .matches(Regex("^\u007f[\\dA-Fa-fK-Ok-oRr]"))
        ) {
            val color = buffer.substring(0, 2).replace("\u007f", "").lowercase()
            buffer = buffer.substring(2)

            fun switchColor(color: Foreground): Foreground {
                if (last.content.isNotEmpty()) out.add(last)
                last = AsciiString("", color)
                return color;
            }

            fun switchFormat(format: Format) {
                if (last.content.isNotEmpty()) out.add(last)
                last = AsciiString(last, "");
                last.format.add(format)
            }

            when (color) {
                "0" -> switchColor(Black)
                "1" -> switchColor(DarkBlue)
                "2" -> switchColor(DarkGreen)
                "3" -> switchColor(Cyan)
                "4" -> switchColor(DarkRed)
                "5" -> switchColor(Purple)
                "6" -> switchColor(Gold)
                "7" -> switchColor(LightGray)
                "8" -> switchColor(Gray)
                "9" -> switchColor(Blue)
                "a" -> switchColor(Green)
                "b" -> switchColor(Sky)
                "c" -> switchColor(Red)
                "d" -> switchColor(Pink)
                "e" -> switchColor(Yellow)
                "f" -> switchColor(White)
                "k" -> switchFormat(Flicker)
                "l" -> switchFormat(Highlight)
                "m" -> switchFormat(Strikethrough)
                "n" -> switchFormat(Underline)
                "o" -> switchFormat(Italic)
                "r" -> {
                    if (last.content.isNotEmpty()) out.add(last)
                    last = AsciiString.Default
                }
                else -> throw IllegalArgumentException("异常字符")
            }
            last.content += buffer
        } else last.content += buffer.replace("\u007f", "&")
    }
    if (last.content.isNotEmpty()) out.add(last)
    return out.toGroup()
}

fun List<AsciiString>.toGroup() = AsciiStringGroup(this)