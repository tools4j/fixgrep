package org.tools4j.extensions

import com.sun.tools.internal.jxc.ap.Const
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
    val underscores = Pattern.compile("(?:_)?([A-Z])([A-Z]+)")
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
