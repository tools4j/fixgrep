package org.tools4j.clientsim;

import org.tools4j.fix.FixSpec
import org.tools4j.messaging.PubSubMsgDispatcher
import org.tools4j.model.ClientOrder
import org.tools4j.model.DateTimeService
import org.tools4j.model.IdGenerator
import org.tools4j.fix.LimitPrice
import org.tools4j.fix.MarketPrice
import org.tools4j.fix.OrderType
import org.tools4j.fix.Price
import org.tools4j.model.RandomQtyGivenAverage
import org.tools4j.model.RoundedUpQtyToNearestSliceQty
import org.tools4j.fix.Side
import org.tools4j.fix.spec.FixSpecDefinition
import org.tools4j.model.fix.messages.NewOrderSingle
import org.tools4j.oms.Oms
import org.tools4j.strategy.Evaluatable
import org.tools4j.strategy.EvaluationTriggerImpl
import org.tools4j.util.Randomize


/**
 * User: ben
 * Date: 20/7/17
 * Time: 7:25 PM
 *
 * A MarketFacingStrategy has the option of creating one to many
 * new market orders.
 *
 */
class ClientSimOrderCreator(
        val compId: String,
        private val targetCompId: String,
        private val dateTimeService: DateTimeService,
        private val config: Config,
        private val messageHandler: PubSubMsgDispatcher,
        private val oms: Oms<ClientOrder>,
        private val fixSpec: FixSpecDefinition,
        private val evaluationTrigger: EvaluationTriggerImpl,
        private val orderIdGenerator: IdGenerator): Evaluatable  {

    private val randomize: Randomize = Randomize()

    init {
        evaluationTrigger.subscribe(this)
    }

    private fun getNewOrderPrice(): Price {
        return if (randomize.shouldDoConsideringPossibility(config.possibilityOfPricedOrder)) {
            val price = ((1.0 - randomize.randomDouble() * 2.0) * config.newOrderPriceDeviation * config.newOrderAveragePrice) + config.newOrderAveragePrice
            LimitPrice(price)
        } else {
            MarketPrice.INSTANCE
        }
    }

    override fun evaluate() {
        if(randomize.shouldDoConsideringPossibility(config.possibilityOrNewOrderCreatedPerEvaluation)) {
            val order = createOrder(dateTimeService.now())
            val newOrderSingle = order.createNewOrderSingle(dateTimeService, fixSpec)
            oms.handleNewOrder(order, newOrderSingle)
            messageHandler.handle(newOrderSingle)
            evaluationTrigger.evaluate()
        }
    }

    private fun createOrder(currentTime: Long): ClientOrder {
        val clOrdId = orderIdGenerator.get()

        val price = getNewOrderPrice()
        val newOrderSingle = NewOrderSingle(
                compId,
                targetCompId,
                clOrdId,
                clOrdId,
                randomize.getRandomElement(config.possibleInstruments),
                currentTime,
                RoundedUpQtyToNearestSliceQty(RandomQtyGivenAverage(config.averageTotalOrderSize), config.minSliceQty).toLong(),
                price,
                randomize.getRandomElement(Side.values()),
                OrderType.forPrice(price),
                fixSpec)

        return ClientOrder(newOrderSingle, dateTimeService, messageHandler)
    }
}
