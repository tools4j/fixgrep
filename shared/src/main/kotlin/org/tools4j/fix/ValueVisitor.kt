package org.tools4j.fix

/**
 * User: benjw
 * Date: 7/16/2018
 * Time: 5:09 PM
 */
interface ValueVisitor {
    fun visit(value: Value)
}