package org.tools4j.oms

import org.tools4j.model.Id

/**
 * User: ben
 * Date: 9/03/2018
 * Time: 7:11 AM
 */
class OrigClOrdIdAndSenderCompId(val origClOrd: Id, val senderCompId: String){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is OrigClOrdIdAndSenderCompId) return false

        if (origClOrd != other.origClOrd) return false
        if (senderCompId != other.senderCompId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = origClOrd.hashCode()
        result = 31 * result + senderCompId.hashCode()
        return result
    }

    override fun toString(): String {
        return "OrigClOrdIdAndSenderCompId(origClOrd=$origClOrd, senderCompId='$senderCompId')"
    }
}