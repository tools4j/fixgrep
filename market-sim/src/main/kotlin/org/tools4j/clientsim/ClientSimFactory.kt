package org.tools4j.clientsim;

import org.tools4j.fix.FixSpec
import org.tools4j.fix.RegistryFixDecoder
import org.tools4j.fix.session.HardwiredFixSession
import org.tools4j.fix.spec.FixSpecDefinition
import org.tools4j.messaging.PubSubMsgDispatcher
import org.tools4j.model.ClientOrder
import org.tools4j.model.DateTimeService
import org.tools4j.model.IdGenerator
import org.tools4j.model.Order
import org.tools4j.model.VersionedOrder
import org.tools4j.model.fix.messages.CancelReplaceRequest
import org.tools4j.model.fix.messages.CancelRequest
import org.tools4j.model.fix.messages.DecoderRegistry
import org.tools4j.model.fix.messages.ExecutionReport
import org.tools4j.model.fix.messages.NewOrderSingle
import org.tools4j.strategy.Strategy
import org.tools4j.strategy.StrategyFactory
import org.tools4j.oms.ClientOms
import org.tools4j.strategy.EvaluationTriggerImpl
import java.io.Writer


/**
 * User: ben
 * Date: 13/9/17
 * Time: 5:51 PM
 */
class ClientSimFactory(
        private val compId: String,
        private val targetCompId: String,
        fixSpec: FixSpecDefinition,
        evaluationTrigger: EvaluationTriggerImpl) {

    companion object {
        private val MILLIS_PER_HOUR = (60 * 60 * 1000).toLong()
        private val ONCE_PER_MINUTE = 0.00001666666667
        private val ONCE_PER_HOUR = 0.00000027777778
    }

    val dateTimeService = DateTimeService()
    val pubSubMsgDispatcher = PubSubMsgDispatcher()
    val clientSimOrderCreator: ClientSimOrderCreator
    val fixSession: HardwiredFixSession

    init {
        val decoderRegistry = DecoderRegistry()
        decoderRegistry.register(ExecutionReport.Decoder(fixSpec))
        decoderRegistry.register(NewOrderSingle.Decoder(fixSpec))
        decoderRegistry.register(CancelReplaceRequest.Decoder(fixSpec))
        decoderRegistry.register(CancelRequest.Decoder(fixSpec))

        val config = Config(
                minSliceQty = 10,
                averageAmendPriceAsFractionOfTotalPrice = 0.05,
                averageFillQtyAsFractionOfTotalOrderQty = 0.008,
                averageTotalOrderSize = 1000000,
                possibleInstruments = listOf("AUD/USD"),//, "GBP/AUD", "GBP/USD"),
                possibilityOfUnsolCancelPerEvaluation = 0.0001,
                possibilityOfAmendPerEvaluation = 0.001,
                possibilityOfAmendFailing = 0.2,
                averageAmendQtyAsFractionOfTotalOrderQty = 0.1,
                possibilityOfPricedOrder = 0.7,
                newOrderPriceDeviation = 0.0003,
                newOrderAveragePrice = 100.0,
                possibilityOfNosFailing = 0.01,
                possibilityOfCancelPerEvaluation = 0.001,
                possibilityOfCancelFailing = 0.2,
                possibilityOrNewOrderCreatedPerEvaluation = 0.2)

        val orderIdGenerator = IdGenerator("C")
        val oms = ClientOms(compId, pubSubMsgDispatcher, dateTimeService, evaluationTrigger = evaluationTrigger)
        clientSimOrderCreator = ClientSimOrderCreator(compId, targetCompId, dateTimeService, config, pubSubMsgDispatcher, oms, fixSpec, evaluationTrigger, orderIdGenerator)
        val fixDecoder = RegistryFixDecoder(decoderRegistry, fixSpec)
        fixSession = HardwiredFixSession(compId, targetCompId, pubSubMsgDispatcher, fixDecoder)
        val clientSimStrategyFactory = ClientSimStrategyFactory(dateTimeService, config, orderIdGenerator)
        oms.registerStrategyFactory(clientSimStrategyFactory)
    }

    class ClientSimStrategyFactory(val dateTimeService: DateTimeService, val config: Config, val orderIdGenerator: IdGenerator) : StrategyFactory<ClientOrder> {
        override fun shouldHandleNos(order: Order): Boolean {
            return true;
        }

        override fun createStrategy(newOrderSingle: NewOrderSingle, order: ClientOrder): Strategy {
            return ClientSimStrategy(order, dateTimeService, config, orderIdGenerator)
        }
    }
}
