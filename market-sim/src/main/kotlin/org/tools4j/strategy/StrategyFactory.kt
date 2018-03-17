package org.tools4j.strategy

import org.tools4j.model.Order
import org.tools4j.model.VersionedOrder
import org.tools4j.model.fix.messages.NewOrderSingle

/**
 * User: ben
 * Date: 23/8/17
 * Time: 6:55 AM
 */
interface StrategyFactory<O: VersionedOrder<*>> {
    fun shouldHandleNos(order: Order): Boolean
    fun createStrategy(newOrderSingle: NewOrderSingle, order: O): Strategy
}
