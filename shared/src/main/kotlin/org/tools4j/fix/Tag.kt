package org.tools4j.fix

/**
 * User: ben
 * Date: 14/06/2017
 * Time: 5:26 PM
 */
interface Tag {
    val number: Int
    fun accept(tagVisitor: TagVisitor)
}
