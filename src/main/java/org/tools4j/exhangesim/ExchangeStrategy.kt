package org.tools4j.exhangesim;

import org.tools4j.model.MarketOrder
import org.tools4j.model.VersionedOrder
import org.tools4j.strategy.Strategy


/**
 * User: ben
 * Date: 20/7/17
 * Time: 7:25 PM
 *
 * A MarketFacingStrategy has the option of creating one to many
 * new market orders.
 *
 */
class ExchangeStrategy(val order: MarketOrder, val exchange: Exchange) : Strategy {
    init {
        exchange.add(order)
    }

    override fun evaluate() {
        //no-op
    }
}
