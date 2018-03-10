package org.tools4j.model.fix.messages

import org.tools4j.fix.Ascii1Char
import org.tools4j.fix.Field
import org.tools4j.fix.FieldMap
import org.tools4j.fix.Fields
import org.tools4j.fix.FieldsFromDelimitedString
import org.tools4j.fix.FixFieldTypes
import org.tools4j.fix.FixMessageTypes
import org.tools4j.fix.FixSpec
import org.tools4j.model.ExecType
import org.tools4j.model.FieldsBuilder
import org.tools4j.model.FormatUtils
import org.tools4j.model.Id
import org.tools4j.model.OrdStatus
import org.tools4j.model.OrderType
import org.tools4j.model.Price
import org.tools4j.model.Side
import org.tools4j.model.VersionedOrder

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
        fixSpec: FixSpec) : OrderRequestMessage, FixMessage(fixSpec) {

    override val orderId: Id?
        get() = null

    override val fields: List<Field> by lazy {
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

    class Decoder(val fixSpec: FixSpec) : FixMessageDecoder<NewOrderSingle> {
        override val msgType: String
            get() = MSG_TYPE

        override fun createMessage(str: String, delimiter: Char): NewOrderSingle {
            val fields = FieldsFromDelimitedString(str, delimiter)
            return createMessage(fields)

        }

        override fun createMessage(fields: Fields): NewOrderSingle {
            try {
                val fieldMap: Map<Number, Field> = FieldMap(fields).mapValue
                return NewOrderSingle(
                        fieldMap[FixFieldTypes.SenderCompID]!!.stringValue(),
                        fieldMap[FixFieldTypes.TargetCompID]!!.stringValue(),
                        fieldMap[FixFieldTypes.ClOrdID]!!.idValue(),
                        fieldMap[FixFieldTypes.OrigClOrdID]?.idValue() ?: fieldMap[FixFieldTypes.ClOrdID]!!.idValue(),
                        fieldMap[FixFieldTypes.Symbol]!!.stringValue(),
                        fieldMap[FixFieldTypes.TransactTime]!!.longValueFromUTCTimestamp(),
                        fieldMap[FixFieldTypes.OrderQty]!!.longValue(),
                        fieldMap[FixFieldTypes.Price]!!.priceValue(),
                        fieldMap[FixFieldTypes.Side]!!.sideValue(),
                        fieldMap[FixFieldTypes.OrdType]!!.orderTypeValue(),
                        fixSpec = fixSpec)
            } catch (e: Exception) {
                println("Could not create NewOrderSingle from fields: " + fields.toString().replace(Ascii1Char().toChar(), '|'))
                e.printStackTrace()
                throw e
            }

        }

        companion object {
            private val MSG_TYPE = FixMessageTypes.NewOrderSingle
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
