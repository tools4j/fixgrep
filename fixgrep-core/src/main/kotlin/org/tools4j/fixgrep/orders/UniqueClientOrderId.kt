package org.tools4j.fixgrep.orders

import org.tools4j.fix.Field
import org.tools4j.fix.Fields

/**
 * User: benjw
 * Date: 9/25/2018
 * Time: 6:21 AM
 */
class UniqueClientOrderId(id: Field?, senderField: Field?, targetField: Field?, otherFieldsToEnsureUniqueness: Fields): AbstractUniqueId(id, senderField, targetField, otherFieldsToEnsureUniqueness){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is UniqueClientOrderId) return false
        if (!super.equals(other)) return false
        return true
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}