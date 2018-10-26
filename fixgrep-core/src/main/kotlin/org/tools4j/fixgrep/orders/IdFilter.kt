package org.tools4j.fixgrep.orders

class IdFilter(val expressions: Collection<String>?) {
    constructor(): this(emptySet())

    fun matchesFilter(messages: IdentifiableOrder): Boolean {
        return matchesFilter(listOf(messages))
    }

    fun matchesFilter(messages: Collection<IdentifiableOrder>): Boolean {
        if (expressions == null || expressions.isEmpty()) return true;
        for (msg in messages) {
            if (msg.orderId != null && matchesFilter((msg.orderId as UniqueOrderId).id)) return true
            if (msg.clOrdId != null && matchesFilter((msg.clOrdId as UniqueClientOrderId).id)) return true
            if (msg.origClOrdId != null && matchesFilter((msg.origClOrdId as UniqueClientOrderId).id)) return true
        }
        return false
    }

    fun matchesFilter(str: String): Boolean {
        if (expressions == null || expressions.isEmpty()) return true;
        return expressions.filter { str.contains(it) }.isNotEmpty()
    }
}