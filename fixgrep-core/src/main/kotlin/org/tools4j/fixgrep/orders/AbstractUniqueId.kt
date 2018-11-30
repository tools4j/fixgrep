package org.tools4j.fixgrep.orders

import org.tools4j.fix.Field
import org.tools4j.fix.Fields

/**
 * User: benjw
 * Date: 9/25/2018
 * Time: 6:21 AM
 */
abstract class AbstractUniqueId(val id: String, val sender: String?, val target: String?, val otherFieldsToEnsureUniqueness: Fields) {
    constructor(idField: Field, senderField: Field?, targetField: Field?, otherFieldsToEnsureUniqueness: Fields): this(idField.value.valueRaw, senderField?.value?.valueRaw, targetField?.value?.valueRaw, otherFieldsToEnsureUniqueness)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AbstractUniqueId) return false
        if (id != other.id) return false
        if (!isSenderAndTargetEqualOrSwitched(other.sender, other.target)) return false
        if (otherFieldsToEnsureUniqueness.fieldsAsSet != other.otherFieldsToEnsureUniqueness.fieldsAsSet) return false
        return true
    }

    fun isSenderAndTargetEqualOrSwitched(otherSender: String?, otherTarget: String?): Boolean{
        return ((this.sender == otherSender && this.target == otherTarget)
                || (this.sender == otherTarget && this.target == otherSender))
    }

    fun isFieldsEqual(thisSenderOrTargetId: Field?, otherSenderOrTarget: Field?): Boolean{
        if(thisSenderOrTargetId == otherSenderOrTarget) return true;
        if(thisSenderOrTargetId == null && otherSenderOrTarget == null) return true;
        if(thisSenderOrTargetId == null || otherSenderOrTarget == null) return false;
        if(thisSenderOrTargetId.value.valueRaw == otherSenderOrTarget.value.valueRaw) return true;
        return false;
    }

    override fun hashCode(): Int {
        var result = id.hashCode() ?: 0
        result = 31 * result + ((sender?.hashCode() ?: 0) + (target?.hashCode() ?: 0))
        result = 31 * result + otherFieldsToEnsureUniqueness.fieldsAsSet.hashCode()
        return result
    }

    override fun toString(): String {
        return "UniqueId(id=$id, sender=$sender, target=$target, otherFieldsToEnsureUniqueness=$otherFieldsToEnsureUniqueness)"
    }
}