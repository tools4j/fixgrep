package org.tools4j.model

import org.tools4j.fix.FixSpec
import org.tools4j.model.fix.messages.MessageHandler
import org.tools4j.model.fix.messages.NewOrderSingle
import org.tools4j.model.state.client.ClientState
import org.tools4j.model.state.client.PendingNew
import org.tools4j.model.state.client.StateContext

/**
 * User: ben
 * Date: 6/03/2018
 * Time: 7:08 AM
 */
class ClientOrder(
        nos: NewOrderSingle,
        dateTimeService: DateTimeService,
        messageHandler: MessageHandler) : VersionedOrder<ClientState>(nos, dateTimeService, messageHandler) {

    override val orderId: Id?
        get() = if(executions.isEmpty()) null else executions.first().orderId

    init {
        stateHistory.add(PendingNew(StateContext(
                this,
                this,
                messageHandler,
                dateTimeService,
                listeners,
                nos.fixSpec)))
    }

    fun amend(newClOrderId: Id, newQty: Long, newPrice: Price) {
        currentState.amend(OrderVersion(newClOrderId, newQty, newPrice, dateTimeService.now()))
    }

    fun cancel(newClOrderId: Id) {
        currentState.cancel(newClOrderId)
    }
}