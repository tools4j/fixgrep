package org.tools4j.oms;

import mu.KLogging
import org.tools4j.messaging.PubSubMsgDispatcher
import org.tools4j.model.DateTimeService
import org.tools4j.model.Id
import org.tools4j.model.IdGenerator
import org.tools4j.model.VersionedOrder
import org.tools4j.model.fix.messages.DelegatingMessageHandler
import org.tools4j.model.fix.messages.Message
import org.tools4j.model.fix.messages.MessageHandler
import org.tools4j.model.fix.messages.NewOrderSingle
import org.tools4j.strategy.Evaluatable
import org.tools4j.strategy.EvaluationTrigger
import org.tools4j.strategy.Strategy
import org.tools4j.strategy.StrategyFactory
import java.util.function.Supplier

/**
 * User: ben
 * Date: 20/7/17
 * Time: 7:25 PM
 *
 * A MarketFacingStrategy has the option of creating one to many
 * new market orders.
 *
 */
abstract class Oms<O: VersionedOrder<*>>
    (val compId: String,
     val messageHandler: PubSubMsgDispatcher = PubSubMsgDispatcher(),
     val dateTimeService: DateTimeService = DateTimeService(),
     val evaluationTrigger: EvaluationTrigger)
    : DelegatingMessageHandler, Evaluatable {

    companion object: KLogging()

    private val ordersByOrderLookupId: MutableMap<in Any, O> = HashMap()
    private val strategiesByOrderLookupId: MutableMap<in Any, Strategy> = HashMap()
    private val strategyRegistry: MutableList<StrategyFactory<O>> = ArrayList()

    fun initialize() {
        this.messageHandler.subscribe(this.compId, this)
        evaluationTrigger?.subscribe(this)
    }

    abstract fun getOrderLookupId(message: Message): Any

    abstract fun getOrderLookupId(order: O): Any

    abstract fun createNewOrder(newOrderSingle: NewOrderSingle): O

    override fun handle(msg: Message) {
        super.handle(msg)
    }

    override fun evaluate() {
        strategiesByOrderLookupId.values.forEach { it.evaluate() }
    }

    override fun handleNonDelegated(msg: Message){
        val nos = msg as NewOrderSingle
        val order = createNewOrder(nos)
        handleNewOrder(order, nos)
    }

    fun handleNewOrder(order: O, nos: NewOrderSingle) {
        val id = getOrderLookupId(order)
        logger.info { "OMS::${compId}::registering-order::${id}" }
        ordersByOrderLookupId[id] = order
        strategyRegistry.forEach {
            if (it.shouldHandleNos(order)) {
                strategiesByOrderLookupId.put(id, it.createStrategy(nos, order))
            }
        }
    }

    fun registerStrategyFactory(strategyFactory: StrategyFactory<O>){
        strategyRegistry.add(strategyFactory)
    }

    override fun delegateMessage(message: Message): Boolean {
        return !(message is NewOrderSingle)
    }

    override fun getDelegate(message: Message): MessageHandler? {
        val orderLookupId = getOrderLookupId(message)

        val order = ordersByOrderLookupId[orderLookupId]
        if(order != null){
            return order
        } else {
            throw IllegalStateException("Could not find order with orderId: $orderLookupId in " + this.javaClass.simpleName)
        }
    }
}
