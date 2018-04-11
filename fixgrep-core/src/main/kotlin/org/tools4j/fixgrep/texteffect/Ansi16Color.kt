package org.tools4j.fixgrep.texteffect

/**
 * User: ben
 * Date: 9/04/2018
 * Time: 5:22 PM
 */
object Ansi16Color {
    @JvmStatic
    fun contains(str: String): Boolean{
        if(str.startsWith("Fg")){
            return Ansi16ForegroundColor.contains(str.replaceFirst("Fg", ""))
        } else if(str.startsWith("Bg")){
            return Ansi16BackgroundColor.contains(str.replaceFirst("Bg", ""))
        } else {
            return Ansi16ForegroundColor.contains(str);
        }
    }

    @JvmStatic
    fun parse(str: String): TextEffect{
        if(str.startsWith("Fg")){
            return Ansi16ForegroundColor.valueOf(str.replaceFirst("Fg", ""))
        } else if(str.startsWith("Bg")){
            return Ansi16BackgroundColor.valueOf(str.replaceFirst("Bg", ""))
        } else {
            return Ansi16ForegroundColor.valueOf(str)
        }
    }
}