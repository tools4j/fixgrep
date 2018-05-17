package org.tools4j.fixgrep.texteffect
/**
 * User: ben
 * Date: 13/03/2018
 * Time: 5:23 PM
 */
class Ansi256Color(val color: Int, val backgroundForeground: AnsiForegroundBackground): TextEffect {
    init {
        if(color > 255){
            throw IllegalArgumentException("Color cannot be greater than 255: [$color]")
        }
    }

    override val ansiCode: String by lazy {
        val backgroundForegroundCode= if(backgroundForeground == AnsiForegroundBackground.BACKGROUND) "48" else "38"
        "\u001b[" + backgroundForegroundCode + ";5;" + color + "m"
    }

    override val htmlClass: String by lazy {
        backgroundForeground.abbreviation + color
    }

    override val prettyName: String by lazy {
        ansiCode + backgroundForeground.abbreviation + color + Ansi.Reset.ansiCode
    }

    override val name: String by lazy {
        backgroundForeground.abbreviation + color
    }

    companion object {
        @JvmStatic
        fun contains(str: String): Boolean{
            val cleanedStr: String
            if(str.startsWith("Fg") || str.startsWith("Bg")) {
                cleanedStr = str.trim().replaceFirst("Fg", "").replaceFirst("Bg", "")
            } else {
                cleanedStr = str.trim()
            }

            val matchesNumber = cleanedStr.matches(Regex("\\d+"))
            if(!matchesNumber) {
                return false
            }

            val intValue = cleanedStr.toInt()
            return (intValue >= 0 && intValue < 256)
        }

        @JvmStatic
        fun parse(str: String): Ansi256Color{
            val foregroundBackground: AnsiForegroundBackground
            val cleanedStr: String
            if(str.startsWith("Fg")){
                cleanedStr = str.trim().replaceFirst("Fg", "")
                foregroundBackground = AnsiForegroundBackground.FOREGROUND
            } else if(str.startsWith("Bg")) {
                cleanedStr = str.trim().replaceFirst("Bg", "")
                foregroundBackground = AnsiForegroundBackground.BACKGROUND
            } else {
                cleanedStr = str.trim()
                foregroundBackground = AnsiForegroundBackground.FOREGROUND
            }
            val matchesNumber = cleanedStr.matches(Regex("\\d+"))
            if(!matchesNumber) {
                throw IllegalArgumentException("Cannot parse value for Ansi256Color from [" + str + "]")
            }
            val intValue = cleanedStr.toInt()
            return Ansi256Color(intValue, foregroundBackground)
        }
    }



    override fun toString(): String {
        return "Ansi256Color(color=$color, backgroundForeground=$backgroundForeground)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Ansi256Color) return false

        if (color != other.color) return false
        if (backgroundForeground != other.backgroundForeground) return false

        return true
    }

    override fun hashCode(): Int {
        var result = color
        result = 31 * result + backgroundForeground.hashCode()
        return result
    }
}

