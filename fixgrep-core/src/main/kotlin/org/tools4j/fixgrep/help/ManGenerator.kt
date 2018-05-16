package org.tools4j.fixgrep.help

import joptsimple.OptionSpec
import org.tools4j.fixgrep.OptionParserFactory
import java.io.File

/**
 * User: ben
 * Date: 23/04/2018
 * Time: 5:25 PM
 */

class ManGenerator {
    val man: String by lazy {
        val sb = StringBuilder()
        val options = OptionParserFactory().optionParser.recognizedOptions()
        sb.append(howToConfigure(options))
        sb.append(OptionsHelp().toHtml())
        sb.toString()
    }

    private fun howToConfigure(options: MutableMap<String, out OptionSpec<*>>): String {
        val sb = StringBuilder()
        sb.append("""
    <h1>Ways to configure fixgrep</h1>
    <div class='configuration'>
    <h2>Fixgrep can be configured via three methods.</h2>
    <ol>
    <li>Command options.  These are specified as POSIX style options after the fixgrep command.  e.g. fixgrep --highlights 35 will highlight any MsgType tag/values.  It is best to use command line options if your configuration is a once off, or is specific to the context you are running in.</li>
    <li>application.properties.  If you wish to configure fixgrep to behave in a certain way every time you run it, then it is better to add your configuration to ~/.fixgrep/application.properties. If using the provided .sh file, when running fixgrep for the first time, it should have created a ~/.fixgrep folder, and will add that to your path when running fixgrep.  If fixgrep fails to create a ~/.fixgrep folder, then as a fallback it will create a properties file in the same directory as the .sh script file.</li>
    <ol>
    <h2>Differences between command-line options, and properties configuration</h2>
    Command-line options (usually) provide at least two different options which configure the same parameter.  Usually a short single character option, and a more descriptive option.  e.g. -o, --output-delimiter both configure the output delimiter.  However when configuring via the properties file only one option is available to use.  That is the longest command-line option, with dashes replaced with dots.  e.g. to configure the output delimiter in the properties file, you would need to use output.delimiter option.  e.g. output.delimiter=|  Below is a table listing the command line options, and on the right the equivalent properties to use in properties file configuration.""".trimIndent()).append("\n")
        sb.append("<table>")
        val optionsNotSupportedByProperties = listOf("?", "[arguments]")
        sb.append("<tr><th>option variations</th><th>equivalient properties key</th></tr>\n")
        for (desc in options.values.distinct().sortedBy { it.options().first() }) {
            if (optionsNotSupportedByProperties.contains(desc.options().first())) continue
            val optionVariations = OptionsHelp.OptionVariations(desc.options())
            sb.append("<tr><td>$optionVariations</td><td>${optionVariations.longestAsPropertyKey}</td></tr>\n")
        }
        sb.append("</table></div>")
        return sb.toString()
    }

    companion object {
        val fix = "35=D|11=ABC|55=AUD/USD"

        @JvmStatic
        fun main(args: Array<String>) {
            println(ManGenerator().man)
            val htmlFile = File("man.html")
            htmlFile.writeText(ManGenerator().man)
        }
    }
}