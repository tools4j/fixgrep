package org.tools4j.fixgrep.texteffect
/**
 * User: ben
 * Date: 13/03/2018
 * Time: 5:23 PM
 */
enum class Ansi16ForegroundColor(override val consoleTextBefore: String): TextEffect {
    Black("\u001B[30m"),
    Red("\u001B[31m"),
    Green("\u001B[32m"),
    Yellow("\u001B[33m"),
    Blue("\u001B[34m"),
    Purple("\u001B[35m"),
    Cyan("\u001B[36m"),
    White("\u001B[37m"),
    Gray("\u001b[30;1m"),
    BrightRed("\u001b[31;1m"),
    BrightGreen("\u001b[32;1m"),
    BrightYellow("\u001b[33;1m"),
    BrightBlue("\u001b[34;1m"),
    BrightMagenta("\u001b[35;1m"),
    BrightCyan("\u001b[36;1m"),
    BrightWhite("\u001b[37;1m");

    override val consoleTextAfter: String = Ansi.Reset

    override val htmlClass: String by lazy {
        "Fg" + name
    }

    val backgroundColorForDemo: Ansi16BackgroundColor by lazy {
        if(this == Black || this == Blue || this == BrightBlue || this == Gray) Ansi16BackgroundColor.White
        else Ansi16BackgroundColor.Black
    }

    companion object {
        @JvmStatic
        fun contains(str: String): Boolean{
            val cleanedString: String
            if(str.startsWith("Fg")){
                cleanedString = str.replaceFirst("Fg", "")
            } else {
                cleanedString = str
            }
            Ansi16ForegroundColor.values().forEach {
                if(it.name == cleanedString){
                    return true
                }
            }
            return false;
        }

        @JvmStatic
        fun parse(str: String): Ansi16ForegroundColor {
            if(str.startsWith("Fg")){
                return Ansi16ForegroundColor.valueOf(str.replaceFirst("Fg", ""))
            } else {
                return Ansi16ForegroundColor.valueOf(str)
            }
        }

        @JvmStatic
        fun listAsHtml(): String {
            val sb = StringBuilder()
            sb.append("<span class='color-demo'>\n")
            Ansi16ForegroundColor.values().forEach {
                sb.append("<span class='").append("Fg").append(it.name).append(" Bg").append(it.backgroundColorForDemo.name).append("'>").append("Fg"+it.name).append("</span>")
            }
            sb.append("</span>")
            return sb.toString()
        }

        @JvmStatic
        fun listForConsole(): String {
            val sb = StringBuilder()
            Ansi16ForegroundColor.values().forEach {
                if(sb.length > 0) sb.append(", ")
                sb.append(it.consoleTextBefore).append(it.backgroundColorForDemo.consoleTextBefore).append("Fg"+it.name).append(it.consoleTextAfter)
            }
            return sb.toString()
        }
    }
}

