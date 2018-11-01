package org.tools4j.fixgrep.help

/**
 * User: ben
 * Date: 9/05/2018
 * Time: 6:43 AM
 */
class Color256ConsoleDemo {
    val consoleIntro = "Below (should) be a table of numbers with colored backgrounds.  If you see this nice looking colorful table, then your console probably supports 256 colors.  In that case you can use the Fg or Bg prefixes to specify highlights.  e.g. --highlights=35:Bg91:line would format a fix line a bit like this \u001B[48;5;91m35=D|11=ABC|55=AUD/USD\u001B[m\n\n"

    val demo: String by lazy {
        val sb = StringBuilder()
        for(i in 0..15){
            sb.append("\u001B[48;5;${i}m" + getText(i) + "\u001B[m ")
        }
        sb.append("\n")

        for(i in 0..6){
            val colorPart1 = i*36 +16
            for(j in 0..35){
                val color = colorPart1+j
                if(color > 255) break
                sb.append("\u001B[48;5;${color}m" + getText(color, (j>10)) + "\u001B[m ")
            }
            sb.append("\n")
        }
        sb.toString()
    }

    val demoForConsole: String by lazy { consoleIntro + demo }

    private fun getText(color: Int): String {
        return getText(color, (color == 3 || color in 6..15))
    }

    private fun getText(color: Int, darkOnLight: Boolean): String {
        val text = ("" + color).padStart(3)
        val textAndForegroundColor: String
        if(darkOnLight){
            textAndForegroundColor = "\u001B[38;5;0m" + text
        } else {
            textAndForegroundColor = "\u001B[38;5;15m" + text
        }
        return textAndForegroundColor
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val demo = Color256ConsoleDemo()
            println(demo.consoleIntro + demo.demo)
        }
    }
}