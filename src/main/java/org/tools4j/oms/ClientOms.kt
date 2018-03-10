package org.tools4j.oms

import org.tools4j.messaging.PubSubMsgDispatcher
import org.tools4j.model.ClientOrder
import org.tools4j.model.DateTimeService
import org.tools4j.model.Id
import org.tools4j.model.fix.messages.ExecutionReport
import org.tools4j.model.fix.messages.Message
import org.tools4j.model.fix.messages.NewOrderSingle
import org.tools4j.model.fix.messages.OrderRequestMessage
import org.tools4j.strategy.EvaluationTrigger

/**
 * User: ben
 * Date: 26/02/2018
 * Time: 6:55 AM
 */
class ClientOms(compId: String,
                messageHandler: PubSubMsgDispatcher = PubSubMsgDispatcher(),
                dateTimeService: DateTimeService = DateTimeService(),
                evaluationTrigger: EvaluationTrigger) : Oms<ClientOrder>(compId, messageHandler, dateTimeService, evaluationTrigger) {

    override fun createNewOrder(newOrderSingle: NewOrderSingle): ClientOrder {
        return ClientOrder(newOrderSingle, dateTimeService, messageHandler)
    }

    init {
        super.initialize()
    }

    override fun getOrderLookupId(order: ClientOrder): Id {
        return order.origClOrdId
    }

    override fun getOrderLookupId(message: Message): Id {
        return when (message) {
            is ExecutionReport -> message.origClOrderId
            is OrderRequestMessage -> message.origClOrderId!!
            else -> throw UnsupportedOperationException("Unknown message type: " + message.javaClass.simpleName)
        }
    }
}