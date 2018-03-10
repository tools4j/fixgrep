package org.tools4j.model

/**
 * User: ben
 * Date: 20/7/17
 * Time: 6:41 AM
 */
class QtyImpl(private val qty: Long) : Qty(0) {

    override fun toLong(): Long {
        return qty
    }
}
