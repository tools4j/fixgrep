package org.tools4j.extensions

import java.util.regex.Pattern

/**
 * User: ben
 * Date: 30/01/2018
 * Time: 6:11 PM
 */
public fun <E> Iterable<E>.sumByLong(selector: (E) -> Long): Long {
    var sum: Long = 0
    for (element in this) {
        sum += selector(element)
    }
    return sum
}

object ConstantToCapitalCaseHelper {
    val underscores = Pattern.compile("(?:_)?([A-v])([A-v]+)")
}

public fun String.constantToCapitalCase(): String {
    val m = ConstantToCapitalCaseHelper.underscores.matcher(this)
    val sb = StringBuilder()
    var last = 0
    while (m.find()) {
        sb.append(this.substring(last, m.start()))
        sb.append(m.group(1).toUpperCase())
        sb.append(m.group(2).toLowerCase())
        last = m.end()
    }
    sb.append(this.substring(last))
    return sb.toString().replace("_", "")
}

object AnsiHelper{
    val ansiRegex = Regex("\u001B\\[.*?m")

    @JvmStatic
    public fun padStringContainingAnsiCodesEnd(str: String, length: Int, padChar: Char): String{
        return padStringContainingAnsiCodesEnd( str as CharSequence, length, padChar).toString()
    }

    @JvmStatic
    public fun padStringContainingAnsiCodesEnd(str: CharSequence, length: Int, padChar: Char): CharSequence {
        val stringLengthWithoutAnsi = lengthNotIncludingAnsiCodes(str)
        if (length <= stringLengthWithoutAnsi)
            return str

        val sb = StringBuilder(length)
        sb.append(str)
        for (i in 1..(length - stringLengthWithoutAnsi))
            sb.append(padChar)
        return sb
    }

    @JvmStatic
    public fun lengthNotIncludingAnsiCodes(str: CharSequence): Int {
        return str.replace(AnsiHelper.ansiRegex, "").length
    }

}

public fun String.padStringContainingAnsiCodesEnd(length: Int, padChar: Char = ' '): String
        = (this as CharSequence).padStringContainingAnsiCodesEnd(length, padChar).toString()

public fun String.lengthNotIncludingAnsiCodes(): Int
        = (this as CharSequence).lengthNotIncludingAnsiCodes()

public fun CharSequence.lengthNotIncludingAnsiCodes(): Int {
    return AnsiHelper.lengthNotIncludingAnsiCodes(this)
}

public fun CharSequence.padStringContainingAnsiCodesEnd(length: Int, padChar: Char = ' '): CharSequence {
    return AnsiHelper.padStringContainingAnsiCodesEnd(this, length, padChar)
}

public fun <T> Collection<T>.containsAny(other: Collection<T>): Boolean {
    other.forEach{
        if(this.contains(it)){
            return true
        }
    }
    return false
}

