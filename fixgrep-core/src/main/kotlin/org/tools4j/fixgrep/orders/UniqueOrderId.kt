package org.tools4j.fixgrep.orders

import org.tools4j.fix.Fields
import org.tools4j.fix.Field

/**
 * User: benjw
 * Date: 9/25/2018
 * Time: 6:21 AM
 */
data class UniqueOrderId(val id: Field?, val otherFieldsToEnsureUniqueness: Fields) {
    fun isNull(): Boolean {
        return id == null
    }
}