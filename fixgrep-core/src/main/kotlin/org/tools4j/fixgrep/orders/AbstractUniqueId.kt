package org.tools4j.fixgrep.orders

import org.tools4j.fix.Field
import org.tools4j.fix.Fields

/**
 * User: benjw
 * Date: 9/25/2018
 * Time: 6:21 AM
 */
abstract class AbstractUniqueId(val id: Field?, val senderField: Field?, val targetField: Field?, val otherFieldsToEnsureUniqueness: Fields) {
    fun isNull(): Boolean {
        return id == null
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AbstractUniqueId) return false
        if (id?.value?.valueRaw != other.id?.value?.valueRaw) return false
        if (!isSenderAndTargetFieldEqualOrSwitched(other.senderField, other.targetField)) return false
        if (otherFieldsToEnsureUniqueness.fieldsAsSet != other.otherFieldsToEnsureUniqueness.fieldsAsSet) return false
        return true
    }

    fun isSenderAndTargetFieldEqualOrSwitched(otherSenderField: Field?, otherTargetField: Field?): Boolean{
        return (isFieldsEqual(this.senderField, otherSenderField) && isFieldsEqual(this.targetField, otherTargetField))
                || (isFieldsEqual(this.senderField, otherTargetField) && isFieldsEqual(this.targetField, otherSenderField))
    }

    fun isFieldsEqual(thisSenderOrTargetId: Field?, otherSenderOrTargetField: Field?): Boolean{
        if(thisSenderOrTargetId == otherSenderOrTargetField) return true;
        if(thisSenderOrTargetId == null && otherSenderOrTargetField == null) return true;
        if(thisSenderOrTargetId == null || otherSenderOrTargetField == null) return false;
        if(thisSenderOrTargetId.value.valueRaw == otherSenderOrTargetField.value.valueRaw) return true;
        return false;
    }

    override fun hashCode(): Int {
        var result = id?.value?.hashCode() ?: 0
        result = 31 * result + ((senderField?.value?.valueRaw?.hashCode() ?: 0) + (targetField?.value?.valueRaw?.hashCode() ?: 0))
        result = 31 * result + otherFieldsToEnsureUniqueness.fieldsAsSet.hashCode()
        return result
    }

    override fun toString(): String {
        return "UniqueId(id=$id, senderField=$senderField, targetField=$targetField, otherFieldsToEnsureUniqueness=$otherFieldsToEnsureUniqueness)"
    }
}