package org.tools4j.model

import org.tools4j.fix.Id
import org.tools4j.fix.Price
import org.tools4j.model.fix.messages.MessageHandler
import org.tools4j.model.fix.messages.NewOrderSingle
import org.tools4j.model.state.market.StateContext
import org.tools4j.model.state.market.MarketState
import org.tools4j.model.state.market.PendingNew

/**
 * User: ben
 * Date: 6/03/2018
 * Time: 7:08 AM
 */
class MarketOrder(
        nos: NewOrderSingle,
        override val orderId: Id,
        dateTimeService: DateTimeService,
        messageHandler: MessageHandler) : VersionedOrder<MarketState>(nos, dateTimeService, messageHandler) {

    protected val execIDGenerator: IdGenerator by lazy { IdGenerator(orderId.id + "_") }

    init {
        stateHistory.add(PendingNew(StateContext(
                this,
                this,
                messageHandler,
                dateTimeService,
                listeners,
                nos.fixSpec,
                execIDGenerator)))

        currentState.handleNewOrderSingle(nos)
    }

    fun fill(qty: Long, price: Price) {
        currentState.fill(qty, price)
    }
}