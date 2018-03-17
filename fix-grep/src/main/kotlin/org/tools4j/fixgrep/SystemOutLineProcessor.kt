package org.tools4j.fixgrep

import java.util.function.Consumer

/**
 * User: ben
 * Date: 14/03/2018
 * Time: 6:25 AM
 */
class SystemOutLineProcessor: Consumer<String> {
    override fun accept(str: String) {
        println(str)
    }
}