package org.tools4j.fixgrep.help

import org.tools4j.fixgrep.Option

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
        for(help in options.optionsHelp){
            if(Option.optionsThatCanBeConfigurableInPropertiesFile.map{it.key}.contains(help.option.key)){
                sb.append("# ").append(help.tagline).append("\n")
                sb.append("# ").append(help.option.key)
                if(help.exampleValue != null) sb.append("=").append(help.exampleValue)
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