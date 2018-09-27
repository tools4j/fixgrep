package org.tools4j.fixgrep.orders

import org.tools4j.fix.Field
import org.tools4j.fix.Fields

/**
 * User: benjw
 * Date: 9/25/2018
 * Time: 6:21 AM
 */
data class UniqueClientOrderId(val id: Field?, val otherFieldsToEnsureUniqueness: Fields) {
    fun isNull(): Boolean {
        return id == null
    }
}