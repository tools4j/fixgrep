package org.tools4j.fixgrep.help

import joptsimple.HelpFormatter
import joptsimple.OptionDescriptor
import org.tools4j.fixgrep.OptionParserFactory
import java.io.OutputStream

/**
 * User: ben
 * Date: 23/04/2018
 * Time: 5:25 PM
 */

class HelpGenerator: HelpFormatter {

    override fun format(options: MutableMap<String, out OptionDescriptor>): String {
        return help
    }

    fun go(os: OutputStream){
        val optionParser = OptionParserFactory().optionParser
        optionParser.formatHelpWith(this)
        optionParser.printHelpOn(os)
    }

    val help: String by lazy {
        val sb = StringBuilder()
        sb.append("Options:\n")
        val optionsHelp = OptionsHelp(DocWriterFactory.ConsoleText)
        for (desc in optionsHelp.helpByOptions.values.distinct()) {
            val optionVariations = desc.optionVariations
            sb.append(optionVariations.toString().padEnd(40))
                    .append(" ")
                    .append(optionsHelp.helpByOptions.get(desc.optionVariations.get(0))!!.tagline)
                    .append("\n")
        }
        sb.append("\n(type 'fixgrep --man' for more detailed help)\n")
        sb.toString()
    }

    companion object {
        val fix = "35=D|11=ABC|55=AUD/USD"

        @JvmStatic
        fun main(args: Array<String>) {
            val help = HelpGenerator()
            val optionParser = OptionParserFactory().optionParser
            optionParser.formatHelpWith(help)
            optionParser.printHelpOn(System.out)
        }
    }
}