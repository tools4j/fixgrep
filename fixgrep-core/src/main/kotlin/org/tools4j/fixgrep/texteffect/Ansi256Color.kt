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

    override val consoleTextAfter: String = Ansi.Reset

    override val consoleTextBefore: String by lazy {
        val backgroundForegroundCode= if(backgroundForeground == AnsiForegroundBackground.BACKGROUND) "48" else "38"
        "\u001b[" + backgroundForegroundCode + ";5;" + color + "m"
    }

    override fun contains(textEffect: TextEffect): Boolean {
        return this.equals(textEffect)
    }

    override val htmlClass: String by lazy {
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


}

