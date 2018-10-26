package org.tools4j.fixgrep

import joptsimple.OptionParser

/**
 * User: ben
 * Date: 29/03/2018
 * Time: 5:55 PM
 */
class OptionParserFactory{
    val optionParser: OptionParser by lazy {
        val parser = object: OptionParser() {
            init {
                //this.allowsUnrecognizedOptions()
                Option.configureAllOptions(this)
            }
        }
        parser
    }
}