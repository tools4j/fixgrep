package org.tools4j.fixgrep.help

import joptsimple.HelpFormatter
import joptsimple.OptionDescriptor
import org.tools4j.fixgrep.OptionParserFactory
import org.tools4j.fixgrep.highlights.DefaultHighlightTextEffects
import org.tools4j.fixgrep.highlights.ExampleTable
import org.tools4j.fixgrep.texteffect.Ansi16BackgroundColor
import org.tools4j.fixgrep.texteffect.Ansi16ForegroundColor
import org.tools4j.fixgrep.texteffect.Ansi256Color
/**
 * User: ben
 * Date: 23/04/2018
 * Time: 5:25 PM
 */

class HelpGenerator: HelpFormatter {

    override fun format(options: MutableMap<String, out OptionDescriptor>): String {
        val buffer = StringBuilder()
        buffer.append("""
${bold}Ways to configure fixgrep${reset}
Fixgrep can be configured via three methods.
1. Command options.  These are specified as POSIX style options after the fixgrep command.  e.g. fixgrep --highlights 35.  It is best to use command line options if your configuration is a once off, or is specific to the context you are running in.
2. application.properties.  If you wish to configure fixgrep to behave in a certain way every time you run it, then it is better to add your configuration to ~/.fixgrep/application.properties. If using the provided .sh file, when running fixgrep for the first time, it should have created a ~/.fixgrep folder, and will add that to your path when running fixgrep.  If fixgrep fails to create a ~/.fixgrep folder, then as a fallback it will create a properties file in the same directory as the .sh script file.
${bold}Differences between command-line options, and properties configuration${reset}
Command-line options (usually) provide at least two different options which configure the same parameter.  Usually a short single character option, and a more descriptive option.  e.g. -o, --output-delimiter both configure the output delimiter.  However when configuring via the properties file only one option is available to use.  That is the longest command-line option, with dashes replaced with dots.  e.g. to configure the output delimiter in the properties file, you would need to use output.delimiter option.  e.g. output.delimiter=|  Below is a table listing the command line options, and on the right the equivalent properties to use in properties file configuration.""".trimIndent()).append("\n")

        val optionsNotSupportedByProperties = listOf("?", "[arguments]")
        buffer.append("| ${bold}option variations${reset} | ${bold}equivalient properties key${reset} |\n")
        for(desc in options.values.distinct().sortedBy { it.options().first() }){
            if(optionsNotSupportedByProperties.contains(desc.options().first())) continue
            val optionVariations = OptionsHelp.OptionVariations(desc.options())
            buffer.append("| $optionVariations | ${optionVariations.longest.replace('-','.')} |\n" )
        }
        return buffer.toString()
    }

    companion object {
        val bold = "\u001B[1m"
        val reset = "\u001B[0m"
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