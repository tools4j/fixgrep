package org.tools4j.fixgrep.orders

import org.tools4j.fix.Field
import org.tools4j.fix.Fields
import org.tools4j.fix.FieldsImpl

/**
 * User: benjw
 * Date: 9/25/2018
 * Time: 6:21 AM
 */
class UniqueOrderId(id: String, sender: String?, target: String?, otherFieldsToEnsureUniqueness: Fields): AbstractUniqueId(id, sender, target, otherFieldsToEnsureUniqueness) {
    constructor(idField: Field, senderField: Field?, targetField: Field?, otherFieldsToEnsureUniqueness: Fields): this(idField.value.valueRaw, senderField?.value?.valueRaw, targetField?.value?.valueRaw, otherFieldsToEnsureUniqueness)
    constructor(id: String): this(id, null, null, FieldsImpl(emptyList()))

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is UniqueOrderId) return false
        if (!super.equals(other)) return false
        return true
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}
