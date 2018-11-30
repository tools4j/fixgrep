package org.tools4j.fixgrep.orders

/**
 * User: benjw
 * Date: 22/10/2018
 * Time: 06:42
 */
interface IdentifiableOrder {
    val orderId: UniqueOrderId?
    val clOrdId: UniqueClientOrderId?
    val origClOrdId: UniqueClientOrderId?

    data class SimpleIdentifiableOrder(override val orderId: UniqueOrderId?, override val clOrdId: UniqueClientOrderId?, override val origClOrdId: UniqueClientOrderId?) : IdentifiableOrder
}