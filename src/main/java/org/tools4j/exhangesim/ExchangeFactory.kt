package org.tools4j.exhangesim;

import org.tools4j.fix.Fix50SP2FixSpecFromClassPath
import org.tools4j.fix.FixSpec
import org.tools4j.fix.RegistryFixDecoder
import org.tools4j.fix.session.FixSession
import org.tools4j.fix.session.HardwiredFixSession
import org.tools4j.messaging.PubSubMsgDispatcher
import org.tools4j.model.MarketOrder
import org.tools4j.model.Order
import org.tools4j.model.VersionedOrder
import org.tools4j.model.fix.messages.CancelReplaceRequest
import org.tools4j.model.fix.messages.CancelRequest
import org.tools4j.model.fix.messages.DecoderRegistry
import org.tools4j.model.fix.messages.ExecutionReport
import org.tools4j.model.fix.messages.NewOrderSingle
import org.tools4j.strategy.Strategy
import org.tools4j.strategy.StrategyFactory
import org.tools4j.oms.MarketOms
import org.tools4j.strategy.EvaluationTrigger


/**
 * User: ben
 * Date: 13/9/17
 * Time: 5:51 PM
 */
class ExchangeFactory(
        val compId: String,
        val targetCompId: String,
        val counterparty: FixSession,
        fixSpec: FixSpec,
        val evaluationTrigger: EvaluationTrigger) {

        val exchange: Exchange
    val fixSession: FixSession

    init {
        val pubSubMsgDispatcher = PubSubMsgDispatcher()

        val decoderRegistry = DecoderRegistry()
        decoderRegistry.register(ExecutionReport.Decoder(fixSpec))
        decoderRegistry.register(NewOrderSingle.Decoder(fixSpec))
        decoderRegistry.register(CancelReplaceRequest.Decoder(fixSpec))
        decoderRegistry.register(CancelRequest.Decoder(fixSpec))

        val fixSpec = Fix50SP2FixSpecFromClassPath().load();
        exchange = Exchange()

        val oms = MarketOms(compId = "ACME_EXCHANGE", messageHandler = pubSubMsgDispatcher, evaluationTrigger = evaluationTrigger)
        oms.registerStrategyFactory(ExchangeStrategyFactory(exchange))
        pubSubMsgDispatcher.subscribe(compId, oms)

        val fixDecoder = RegistryFixDecoder(decoderRegistry, fixSpec)
        fixSession = HardwiredFixSession(compId, targetCompId, pubSubMsgDispatcher, fixDecoder)
        fixSession.counterparty = counterparty
    }

    class ExchangeStrategyFactory(val exchange: Exchange) : StrategyFactory<MarketOrder> {
        override fun shouldHandleNos(order: Order): Boolean {
            return true
        }

        override fun createStrategy(newOrderSingle: NewOrderSingle, order: MarketOrder): Strategy {
            return ExchangeStrategy(order, exchange)
        }
    }
}