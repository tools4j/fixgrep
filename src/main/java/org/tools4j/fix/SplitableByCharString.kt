package org.tools4j.fix

import java.util.ArrayList

/**
 * User: ben
 * Date: 4/7/17
 * Time: 7:04 PM
 */
class SplitableByCharString(private val str: String?, private val delimiter: Char) : SplitableString {

    override fun split(): SplitString {
        if (str == null) {
            throw NullPointerException("Cannot split a null string.")
        }
        var sb = StringBuilder()
        val items = ArrayList<String>()
        val chars = str.toCharArray()
        for (i in chars.indices) {
            if (chars[i] == delimiter) {
                items.add(sb.toString())
                sb = StringBuilder()
            } else {
                sb.append(chars[i])
            }
        }
        if (sb.length > 0) {
            items.add(sb.toString())
        }
        val splitValues = items.toTypedArray()
        return SplitString(splitValues)
    }

    override fun splitAtFirst(): SplitString {
        if (str == null) {
            throw NullPointerException("Cannot split a null string.")
        }
        val delimPosition = str.indexOf(delimiter)
        return if (delimPosition < 0) {
            SplitString(arrayOf(str))
        } else SplitString(arrayOf(str.substring(0, delimPosition), str.substring(delimPosition + 1)))
    }

    override fun toString(): String {
        return "SplitableByCharString(str=$str, delimiter=$delimiter)"
    }
}