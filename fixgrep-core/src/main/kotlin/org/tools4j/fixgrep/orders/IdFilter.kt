package org.tools4j.fixgrep.orders

class IdFilter(val expressions: Set<Expression>) {
    constructor(expressions: List<String>): this(expressions.map{ Expression(it) }.toSet())
    constructor(): this(emptySet())

    fun matchesFilter(messages: IdentifiableOrder): Boolean {
        return matchesFilter(listOf(messages))
    }

    fun matchesFilter(messages: Collection<IdentifiableOrder>): Boolean {
        if (expressions.isEmpty()) return true;
        for (msg in messages) {
            if (msg.orderId != null && matchesFilter((msg.orderId as UniqueOrderId).id)) return true
            if (msg.clOrdId != null && matchesFilter((msg.clOrdId as UniqueClientOrderId).id)) return true
            if (msg.origClOrdId != null && matchesFilter((msg.origClOrdId as UniqueClientOrderId).id)) return true
        }
        return false
    }

    fun matchesFilter(str: String): Boolean {
        if (expressions.isEmpty()) return true;
        return expressions.filter { it.matches(str) }.isNotEmpty()
    }
}

class Expression(val expressionStr: String){
    val regex: Regex by lazy {
        Regex(expressionStr)
    }

    fun matches(str: String): Boolean {
        return str.contains(expressionStr) || regex.containsMatchIn(str)
    }
}