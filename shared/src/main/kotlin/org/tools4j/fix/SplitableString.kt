package org.tools4j.fix

/**
 * User: ben
 * Date: 4/7/17
 * Time: 7:03 PM
 */
interface SplitableString {
    fun split(): SplitString

    fun splitAtFirst(): SplitString
}
