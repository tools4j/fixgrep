package org.tools4j.fixgrep.orders

import org.tools4j.fix.Fields

/**
 * User: benjw
 * Date: 9/25/2018
 * Time: 6:21 AM
 */
class UniqueOriginalClientOrderIdSpec(val idTag: Int, val otherUniqueTags: List<Int>) {
    constructor(): this(41, listOf(49, 56)) //default is origClOrdId, and SenderCompId & TargetCompId

    fun getId(orderMsg: OrderMsg): UniqueClientOrderId{
        return getId(orderMsg.fields)
    }

    fun getId(fields: Fields): UniqueClientOrderId{
        return UniqueClientOrderId(fields.getField(idTag), fields.filterFields{otherUniqueTags.contains(it.tag.number)})
    }
}