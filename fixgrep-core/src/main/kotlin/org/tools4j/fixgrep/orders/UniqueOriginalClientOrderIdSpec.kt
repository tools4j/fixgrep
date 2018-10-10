package org.tools4j.fixgrep.orders

import org.tools4j.fix.Fields

/**
 * User: benjw
 * Date: 9/25/2018
 * Time: 6:21 AM
 */
class UniqueOriginalClientOrderIdSpec(val idTag: Int, val senderTagId: Int?, val targetTagId: Int?, val otherUniqueTags: List<Int>) {
    constructor(): this(41, 49, 56, emptyList()) //default is origClOrdId, and SenderCompId & TargetCompId

    fun getId(orderMsg: OrderMsg): UniqueClientOrderId{
        return getId(orderMsg.fields)
    }

    fun getId(fields: Fields): UniqueClientOrderId{
        val idField = fields.getField(idTag)
        val senderField = if(senderTagId != null) fields.getField(senderTagId) else null
        val targetField = if(targetTagId != null) fields.getField(targetTagId) else null
        return UniqueClientOrderId(idField, senderField, targetField, fields.filterFields{otherUniqueTags.contains(it.tag.number)})
    }

    override fun toString(): String {
        return "UniqueOriginalClientOrderIdSpec(idTag=$idTag, senderTagId=$senderTagId, targetTagId=$targetTagId, otherUniqueTags=$otherUniqueTags)"
    }
}