package org.tools4j.fixgrep.help

import org.tools4j.fixgrep.OptionParserFactory

/**
 * User: ben
 * Date: 8/05/2018
 * Time: 6:41 AM
 */
class ExampleAppPropertiesGenerator {
    val content: String by lazy {
        val options = OptionsHelp(DocWriterFactory.ConsoleText)
        val sb = StringBuilder()
        sb.append("################################################\n")
        sb.append("# Add properties overrides into this file\n")
        sb.append("################################################\n\n")
        for(entry in options.helpByPropertyValues){
            if(!OptionParserFactory.propertiesThatShouldNotBeConfigurable.contains(entry.key)){
                sb.append("# ").append(entry.value.tagline).append("\n")
                sb.append("# ").append(entry.key)
                if(entry.value.exampleValue != null) sb.append("=").append(entry.value.exampleValue)
                sb.append("\n\n")
            }
        }
        sb.toString()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            println(ExampleAppPropertiesGenerator().content)
        }
    }
}