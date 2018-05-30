package org.tools4j.fix

/**
 * User: ben
 * Date: 21/05/2018
 * Time: 6:02 PM
 */

class DelimiterImpl(override val delimiter: String): Delimiter {
    override fun toConsoleText(): String{
        return delimiter
    }

    override fun toHtml(): String{
        return "<span class='delim'>$delimiter</span>"
    }
}