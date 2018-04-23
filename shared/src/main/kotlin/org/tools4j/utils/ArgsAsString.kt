package org.tools4j.utils

import java.util.*


/**
 * User: ben
 * Date: 19/04/2018
 * Time: 6:40 AM
 */
class ArgsAsString(val str: String, val quote: Char) {
    constructor(str: String): this(str, '"')

    fun toArgs(): List<String>{
        val tokens = ArrayList<String>()
        val sb = StringBuilder()
        var insideQuote = false
        for (c in str.toCharArray()) {
            if (c == quote) {
                insideQuote = !insideQuote

            } else if (c == ' ' && !insideQuote) {//when space is not inside quote split..
                tokens.add(sb.toString()) //token is ready, lets add it to list
                sb.delete(0, sb.length) //and reset StringBuilder`s content
            } else
                sb.append(c)//else add character to token
        }
        tokens.add(sb.toString())
        return tokens
    }
}