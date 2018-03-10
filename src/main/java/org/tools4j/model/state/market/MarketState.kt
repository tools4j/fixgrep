package org.tools4j.model.state.market

import org.tools4j.model.Price
import org.tools4j.model.state.State

/**
 * User: ben
 * Date: 8/03/2018
 * Time: 5:31 PM
 */
interface MarketState: State {
    fun fill(qty: Long, price: Price) {
        throw UnsupportedOperationException("fill not supported in state: " + this.javaClass.simpleName)
    }
}