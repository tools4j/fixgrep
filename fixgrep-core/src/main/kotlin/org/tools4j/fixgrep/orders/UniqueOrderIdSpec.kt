package org.tools4j.fixgrep.orders

import org.tools4j.fix.Fields

/**
 * User: benjw
 * Date: 9/25/2018
 * Time: 6:21 AM
 */
class UniqueOrderIdSpec(val idTag: Int, val senderTagId: Int?, val targetTagId: Int?, val otherUniqueTags: List<Int>) {
    constructor(): this(37, 49, 56, emptyList()) //default is OrderId, and SenderCompId & TargetCompId

    fun isPresent(fields: Fields): Boolean {
        return fields.getField(idTag) != null
    }

    fun getId(fields: Fields): UniqueOrderId{
        val idField = fields.getField(idTag)!!
        val senderField = if(senderTagId != null) fields.getField(senderTagId) else null
        val targetField = if(targetTagId != null) fields.getField(targetTagId) else null
        return UniqueOrderId(idField, senderField, targetField, fields.filterFields{otherUniqueTags.contains(it.tag.number)})
    }

    override fun toString(): String {
        return "UniqueOrderIdSpec(idTag=$idTag, senderTagId=$senderTagId, targetTagId=$targetTagId, otherUniqueTags=$otherUniqueTags)"
    }
}