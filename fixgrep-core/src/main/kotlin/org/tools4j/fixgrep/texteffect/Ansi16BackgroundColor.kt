package org.tools4j.fixgrep.texteffect
/**
 * User: ben
 * Date: 13/03/2018
 * Time: 5:23 PM
 */
enum class Ansi16BackgroundColor(override val consoleTextBefore: String): TextEffect {
    Black("\u001B[40m"),
    Red("\u001B[41m"),
    Green("\u001B[42m"),
    Yellow("\u001B[43m"),
    Blue("\u001B[44m"),
    Purple("\u001B[45m"),
    Cyan("\u001B[46m"),
    White("\u001B[47m"),
    Gray("\u001b[40;1m"),
    BrightRed("\u001b[41;1m"),
    BrightGreen("\u001b[42;1m"),
    BrightYellow("\u001b[43;1m"),
    BrightBlue("\u001b[44;1m"),
    BrightMagenta("\u001b[45;1m"),
    BrightCyan("\u001b[46;1m"),
    BrightWhite("\u001b[47;1m");

    override val consoleTextAfter: String = Ansi.Reset

    override val htmlClass: String by lazy {
        "Bg" + name
    }

    companion object {
        @JvmStatic
        fun contains(str: String): Boolean{
            val cleanedString: String
            if(str.startsWith("Bg")){
                cleanedString = str.replaceFirst("Bg", "")
            } else {
                cleanedString = str
            }
            Ansi16BackgroundColor.values().forEach {
                if(it.name == cleanedString){
                    return true
                }
            }
            return false;
        }
        @JvmStatic
        fun parse(str: String): Ansi16BackgroundColor {
            if(str.startsWith("Bg")){
                return Ansi16BackgroundColor.valueOf(str.replaceFirst("Bg", ""))
            } else {
                return Ansi16BackgroundColor.valueOf(str)
            }
        }

        @JvmStatic
        fun listAsHtml(): String {
            val sb = StringBuilder()
            Ansi16BackgroundColor.values().forEach {
                sb.append("<span class=\"").append("Bg").append(it.name).append("\">").append(it.name).append("</span>")
            }
            return sb.toString()
        }

        @JvmStatic
        fun listForConsole(): String {
            val sb = StringBuilder()
            Ansi16BackgroundColor.values().forEach {
                if(sb.length > 0) sb.append(", ")
                sb.append(it.consoleTextBefore).append(it.name).append(it.consoleTextAfter)
            }
            return sb.toString()
        }
    }
}

