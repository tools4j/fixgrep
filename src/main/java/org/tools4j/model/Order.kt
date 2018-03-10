package org.tools4j.model

import org.tools4j.fix.FixSpec
import org.tools4j.model.fix.messages.NewOrderSingle
import java.text.NumberFormat

/**
 * User: ben
 * Date: 28/10/2016
 * Time: 6:33 AM
 */
interface Order : Priceable {

    val formattedPrice: String

    override val price: Price

    val side: Side

    val qty: Long

    val leavesQty: Long

    val cumQty: Long

    val instrument: String

    val clOrdId: Id

    val origClOrdId: Id

    val orderId: Id?

    val senderCompId: String

    val targetCompId: String

    val status: OrdStatus

    val isPrice: PriceOperations.Operator
        get() = side.priceOperations.isPrice(price)

    val qtyAtPrice: String
        get() = NumberFormat.getIntegerInstance().format(qty) + "@" + price

    val filled: Boolean
            get() = cumQty >= qty

    val orderType: OrderType
            get() = if (price.hasPrice()) OrderType.LIMIT else OrderType.MARKET

    fun createNewOrderSingle(dateTimeService: DateTimeService, fixSpec: FixSpec): NewOrderSingle {
        return NewOrderSingle(
                senderCompId = senderCompId,
                targetCompId = targetCompId,
                clOrdId = clOrdId,
                origClOrderId = origClOrdId,
                instrument = instrument,
                transactTime = dateTimeService.now(),
                orderQty = qty,
                price = price,
                side = side,
                orderType = orderType,
                fixSpec = fixSpec
        )
    }

    open fun isEqualToOrMoreAggressiveThan(priceable: Priceable): Boolean {
        return isPrice.equalToOrMoreAggressiveThan(priceable.price)
    }

    open fun isMoreAggressiveThan(priceable: Priceable): Boolean {
        return isPrice.moreAggressiveThan(priceable.price)
    }

    open fun isEqualToOrMorePassiveThan(priceable: Priceable): Boolean {
        return isPrice.equalToOrMorePassiveThan(priceable.price)
    }

    open fun isMorePassiveThan(priceable: Priceable): Boolean {
        return isPrice.morePassiveThan(priceable.price)
    }

    open fun getMidWith(other: Order): Price {
        return other.getMidWith(price)
    }

    open fun getMidWith(price: Price): Price {
        return this.price.mid(price)
    }
}