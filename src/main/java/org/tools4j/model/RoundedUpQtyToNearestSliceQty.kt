package org.tools4j.model

/**
 * User: ben
 * Date: 19/7/17
 * Time: 5:41 PM
 */
class RoundedUpQtyToNearestSliceQty(private val source: Qty, private val minSliceQty: Long) : Qty(0) {

    constructor(source: Long, minSliceQty: Long) : this(QtyImpl(source), minSliceQty) {}

    override fun toLong(): Long {
        val qty = source.toInt()
        val returnVal = qty + minSliceQty - qty % minSliceQty
        return returnVal
    }
}
