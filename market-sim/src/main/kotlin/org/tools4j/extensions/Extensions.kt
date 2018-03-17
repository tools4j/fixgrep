package org.tools4j.extensions

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