package org.tools4j.fixgrep.help

import joptsimple.HelpFormatter
import joptsimple.OptionDescriptor
import org.tools4j.fixgrep.config.OptionParserFactory
import java.io.OutputStream

/**
 * User: ben
 * Date: 23/04/2018
 * Time: 5:25 PM
 */

class HelpGenerator(val outputStream: OutputStream): HelpFormatter {

    override fun format(options: MutableMap<String, out OptionDescriptor>): String {
        return help
    }

    fun go(){
        val optionParser = OptionParserFactory().optionParser
        optionParser.formatHelpWith(this)
        optionParser.printHelpOn(outputStream)
    }

    val help: String by lazy {
        val sb = StringBuilder()
        sb.append("Usage: fixgrep [options] [files ...]\n")
        sb.append("Options:\n")
        val optionsHelp = OptionsHelp(DocWriterFactory.ConsoleText)
        for (help in optionsHelp.optionsHelp) {
            val optionVariations = help.option.optionVariationsWithDashPrefixesAsCommaDelimitedString
            sb.append(optionVariations.toString().padEnd(40))
                    .append(" ")
                    .append(help.tagline)
                    .append("\n")
        }
        sb.append("\n(type 'fixgrep --man' for more detailed help)\n")
        sb.toString()
    }

    companion object {
        val fix = "35=D|11=ABC|55=AUD/USD"

        @JvmStatic
        fun main(args: Array<String>) {
            val help = HelpGenerator(System.out)
            val optionParser = OptionParserFactory().optionParser
            optionParser.formatHelpWith(help)
            optionParser.printHelpOn(System.out)
        }
    }
}