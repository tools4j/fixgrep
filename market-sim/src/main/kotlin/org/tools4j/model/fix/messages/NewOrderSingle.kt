package org.tools4j.model.fix.messages

import org.tools4j.fix.*
import org.tools4j.fix.spec.FixSpecDefinition
import org.tools4j.model.FieldsBuilder
import org.tools4j.utils.FormatUtils

/**
 * User: ben
 * Date: 6/06/2017
 * Time: 5:20 PM
 */
class NewOrderSingle(
        override val senderCompId: String,
        override val targetCompId: String,
        override val clOrdId: Id,
        override val origClOrderId: Id,
        override val instrument: String,
        override val transactTime: Long,
        val orderQty: Long,
        val price: Price,
        override val side: Side,
        val orderType: OrderType,
        fixSpec: FixSpecDefinition) : OrderRequestMessage, FixMessage(fixSpec) {


    constructor(
            senderCompId: String,
            targetCompId: String,
            clOrdId: Id,
            origClOrderId: Id,
            instrument: String,
            transactTime: Long,
            orderQty: Long,
            price: Price,
            side: Side,
            fixSpec: FixSpecDefinition
    ) : this(senderCompId, targetCompId, clOrdId, origClOrderId, instrument, transactTime, orderQty, price, side, price.orderType, fixSpec)

    override val orderId: Id?
        get() = null

    override val fields: Fields by lazy {
        FieldsBuilder()
                .with(FixFieldTypes.MsgType, "D")
                .with(FixFieldTypes.SenderCompID, senderCompId)
                .with(FixFieldTypes.TargetCompID, targetCompId)
                .with(FixFieldTypes.ClOrdID, origClOrderId)
                .with(FixFieldTypes.Symbol, instrument)
                .with(FixFieldTypes.Side, side.code.toLong())
                .with(FixFieldTypes.TransactTime, FormatUtils.toUTCTimestamp(transactTime))
                .with(FixFieldTypes.OrderQty, orderQty)
                .with(FixFieldTypes.OrdType, orderType.code.toLong())
                .with(FixFieldTypes.Price, price)
                .fields
    }

    override fun dispatchBackTo(messageHandler: DoubleDispatchingMessageHandler) {
        messageHandler.handleNewOrderSingle(this)
    }

    class Decoder(val fixSpec: FixSpecDefinition) : FixMessageDecoder<NewOrderSingle> {
        override val msgType: String
            get() = MSG_TYPE

        override fun createMessage(str: String, delimiter: String): NewOrderSingle {
            val fields = FieldsFromDelimitedString(str, delimiter).fields
            return createMessage(fields)

        }

        override fun createMessage(fields: Fields): NewOrderSingle {
            try {
                return NewOrderSingle(
                        fields.getField(FixFieldTypes.SenderCompID)!!.stringValue(),
                        fields.getField(FixFieldTypes.TargetCompID)!!.stringValue(),
                        fields.getField(FixFieldTypes.ClOrdID)!!.idValue(),
                        fields.getField(FixFieldTypes.OrigClOrdID)?.idValue() ?: fields.getField(FixFieldTypes.ClOrdID)!!.idValue(),
                        fields.getField(FixFieldTypes.Symbol)!!.stringValue(),
                        fields.getField(FixFieldTypes.TransactTime)!!.longValueFromUTCTimestamp(),
                        fields.getField(FixFieldTypes.OrderQty)!!.longValue(),
                        fields.getField(FixFieldTypes.Price)!!.priceValue(),
                        fields.getField(FixFieldTypes.Side)!!.sideValue(),
                        fields.getField(FixFieldTypes.OrdType)!!.orderTypeValue(),
                        fixSpec = fixSpec)
            } catch (e: Exception) {
                println("Could not create NewOrderSingle from fields: " + fields.toString().replace(Ascii1Char().toString(), "|"))
                e.printStackTrace()
                throw e
            }

        }

        companion object {
            private val MSG_TYPE = FixMessageType.NewOrderSingle.code
        }
    }

    fun createExecutionReportNew(orderId: Id, execId: Id): ExecutionReport {
        val builder = ExecutionReport.Builder()
        //Swap the target/sender compId around, as we are sending this puppy back
        builder.withSenderCompId(targetCompId)
                .withTargetCompId(senderCompId)
                .withClOrderId(clOrdId)
                .withOrderId(orderId)
                .withExecId(execId)
                .withExecType(ExecType.New)
                .withOrdStatus(OrdStatus.PendingNew)
                .withInstrument(instrument)
                .withSide(side)
                .withTransactTime(transactTime)
                .withOrigClOrderId(origClOrderId)
                .withPrice(price)
                .withFixSpec(fixSpec)
                .withLeavesQty(orderQty)
                .withCumQty(0)
        return builder.build()
    }
}
