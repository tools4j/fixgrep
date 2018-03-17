package org.tools4j.fix

/**
 * User: ben
 * Date: 31/10/2014
 * Time: 7:49 PM
 */
class StrippedFixString(private val rawMessage: String, private val fixDelim: String) {

    override fun toString(): String {
        val ascii1 = Ascii1Char()

        //Strip space from start of string
        var str = rawMessage.replace("(?m)^\\s+".toRegex(), "")

        //Strip space from start of field after delimiter
        str = str.replace("(?m)$fixDelim\\s+".toRegex(), ascii1.toString())

        //Strip space from end of string (not including carriage returns)
        str = str.replace("(?m)[\\t ]+$".toRegex(), "")

        //Strip space from end of field up to next delimiter
        str = str.replace("(?m)[\\t ]+$fixDelim".toRegex(), ascii1.toString())

        //Strip group repeat prefixes (lines starting with digits then a dot
        str = str.replace("(?m)^\\d+\\.\\s*".toRegex(), "")

        //Strip group repeat prefixes (fields starting with digits then a dot
        str = str.replace("(?m)$fixDelim\\d+\\.\\s*".toRegex(), ascii1.toString())

        //Replace any remaining fix delims
        str = str.replace(("(?m)" + fixDelim).toRegex(), ascii1.toString())

        //Strip line feeds
        str = str.replace("\\r".toRegex(), "")

        //Strip lines containing just whitespace
        str = str.replace("(?m)^\\s+$\\n".toRegex(), "")

        //String empty lines
        str = str.replace("\\n\\1+".toRegex(), "\\n")

        //Replace all carriage returns with a delimiter
        str = str.replace("\\n".toRegex(), ascii1.toString())

        //Strip off delimiter at end of string if there is one
        str = str.replace(("(?m)" + ascii1.toString() + "$").toRegex(), "")

        return str
    }
}
