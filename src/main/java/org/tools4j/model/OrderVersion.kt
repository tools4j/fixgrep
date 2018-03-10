package org.tools4j.model

/**
 * User: ben
 * Date: 6/06/2017
 * Time: 5:07 PM
 */
class OrderVersion(val clOrdId: Id, val qty: Long, val price: Price, val arrivalTime: Long) {
    private val orderType: OrderType
    val origClOrdId: Id? = null

    fun formattedPrice(side: Side): String {
        return FormattedPrice(price, side).toString()
    }

    init {
        this.orderType = if (price == MarketPrice.INSTANCE) OrderType.MARKET else OrderType.LIMIT
    }

    override fun toString(): String {
        return "OrderVersion(clOrdId=$clOrdId, qty=$qty, price=$price, origClOrdId=$origClOrdId)"
    }
}
