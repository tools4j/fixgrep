package org.tools4j.oms

import org.tools4j.messaging.PubSubMsgDispatcher
import org.tools4j.model.DateTimeService
import org.tools4j.model.IdGenerator
import org.tools4j.model.MarketOrder
import org.tools4j.model.fix.messages.Message
import org.tools4j.model.fix.messages.NewOrderSingle
import org.tools4j.strategy.EvaluationTrigger

/**
 * User: ben
 * Date: 26/02/2018
 * Time: 6:55 AM
 */
class MarketOms(compId: String,
                messageHandler: PubSubMsgDispatcher = PubSubMsgDispatcher(),
                dateTimeService: DateTimeService = DateTimeService(),
                val orderIdGenerator: IdGenerator = IdGenerator("O"),
                evaluationTrigger: EvaluationTrigger)
    : Oms<MarketOrder>(compId, messageHandler, dateTimeService, evaluationTrigger) {

    init {
        super.initialize()
    }

    override fun createNewOrder(newOrderSingle: NewOrderSingle): MarketOrder {
        return MarketOrder(newOrderSingle, orderIdGenerator.get(), dateTimeService, messageHandler)
    }

    override fun getOrderLookupId(message: Message): Any {
        return OrigClOrdIdAndSenderCompId(message.origClOrderId!!, message.senderCompId)
    }

    override fun getOrderLookupId(order: MarketOrder): Any {
        return order.origClOrdIdAndSenderCompId
    }
}