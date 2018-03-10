package org.tools4j.exhangesim.model

import org.tools4j.model.Id
import org.tools4j.model.Price
import org.tools4j.model.VersionedOrder

import java.text.NumberFormat
import java.util.function.Supplier

/**
 * User: ben
 * Date: 27/10/2016
 * Time: 6:32 AM
 */
class Match(
        val bid: VersionedOrder<*>,
        val ask: VersionedOrder<*>,
        val qty: Long,
        val price: Price) {

    override fun toString(): String {
        return (NumberFormat.getIntegerInstance().format(qty) + "@$price"
                + ": bid:${bid.qtyAtPrice} ask:${ask.qtyAtPrice}")
    }
}
