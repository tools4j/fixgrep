package org.tools4j.fixgrep.orders

import org.tools4j.fix.Fields

/**
 * User: benjw
 * Date: 9/25/2018
 * Time: 6:21 AM
 */
class UniqueOrderIdSpec(val idTag: Int, val otherUniqueTags: List<Int>) {
    constructor(): this(37, listOf(49, 56)) //default is OrderId, and SenderCompId & TargetCompId

    fun getId(orderMsg: OrderMsg): UniqueOrderId{
        return getId(orderMsg.fields)
    }

    fun getId(fields: Fields): UniqueOrderId{
        return UniqueOrderId(fields.getField(idTag), fields.filterFields{otherUniqueTags.contains(it.tag.number)})
    }
}