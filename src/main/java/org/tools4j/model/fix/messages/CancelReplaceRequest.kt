package org.tools4j.model.fix.messages

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

/**
 * User: ben
 * Date: 6/06/2017
 * Time: 5:13 PM
 */
class CancelReplaceRequest(
        override val senderCompId: String,
        override val targetCompId: String,
        override val origClOrderId: Id,
        override val clOrdId: Id,
        override val instrument: String,
        override val transactTime: Long,
        override val side: Side,
        val orderQty: Long,
        val orderType: OrderType,
        val price: Price,
        override val orderId: Id?,
        fixSpec: FixSpec) : OrderRequestMessage, FixMessage(fixSpec) {

    override val fields: List<Field> by lazy {
        FieldsBuilder()
            .with(FixFieldTypes.MsgType, FixMessageTypes.OrderCancelReplaceRequest)
            .with(FixFieldTypes.SenderCompID, senderCompId)
            .with(FixFieldTypes.TargetCompID, targetCompId)
            .with(FixFieldTypes.OrigClOrdID, origClOrderId)
            .with(FixFieldTypes.ClOrdID, clOrdId)
            .with(FixFieldTypes.Symbol, instrument)
            .with(FixFieldTypes.Side, side.code.toLong())
            .with(FixFieldTypes.TransactTime, FormatUtils.toUTCTimestamp(transactTime))
            .with(FixFieldTypes.OrderQty, orderQty)
            .with(FixFieldTypes.OrdType, orderType.code.toLong())
            .with(FixFieldTypes.Price, price)
            .with(FixFieldTypes.OrderID, orderId)
            .fields
    }

    override fun dispatchBackTo(messageHandler: DoubleDispatchingMessageHandler) {
        messageHandler.handleCancelReplaceRequest(this)
    }

    class Decoder(val fixSpec: FixSpec) : FixMessageDecoder<CancelReplaceRequest> {
        override val msgType: String
            get() = MSG_TYPE

        override fun createMessage(str: String, delimiter: Char): CancelReplaceRequest {
            val fields = FieldsFromDelimitedString(str, delimiter)
            return createMessage(fields)

        }

        override fun createMessage(fields: Fields): CancelReplaceRequest {
            val fieldMap = FieldMap(fields).mapValue
            return CancelReplaceRequest(
                    senderCompId = fieldMap[FixFieldTypes.SenderCompID]!!.stringValue(),
                    targetCompId = fieldMap[FixFieldTypes.TargetCompID]!!.stringValue(),
                    origClOrderId = fieldMap[FixFieldTypes.OrigClOrdID]!!.idValue(),
                    clOrdId = fieldMap[FixFieldTypes.ClOrdID]!!.idValue(),
                    instrument = fieldMap[FixFieldTypes.Symbol]!!.stringValue(),
                    transactTime = fieldMap[FixFieldTypes.TransactTime]!!.longValueFromUTCTimestamp(),
                    side = fieldMap[FixFieldTypes.Side]!!.sideValue(),
                    orderQty = fieldMap[FixFieldTypes.OrderQty]!!.longValue(),
                    orderType = fieldMap[FixFieldTypes.OrdType]!!.orderTypeValue(),
                    price = fieldMap[FixFieldTypes.Price]!!.priceValue(),
                    orderId = fieldMap[FixFieldTypes.OrderID]!!.idValue(),
                    fixSpec = fixSpec)
        }

        companion object {
            private val MSG_TYPE = FixMessageTypes.OrderCancelReplaceRequest
        }
    }

    fun createRejectedMessage(execId: Id, ordStatus: OrdStatus, leavesQty: Long, cumQty: Long, reason: String): ExecutionReport {
        return createExecutionReport(execId, ExecType.Rejected, leavesQty, cumQty, ordStatus, reason)
    }

    fun createAcceptedMessage(execId: Id, ordStatus: OrdStatus, leavesQty: Long, cumQty: Long): ExecutionReport {
        return createExecutionReport(execId, ExecType.Replaced, leavesQty, cumQty, ordStatus, null)
    }

    private fun createExecutionReport(execId: Id, execType: ExecType, leavesQty: Long, cumQty: Long, ordStatus: OrdStatus, reason: String?): ExecutionReport {
        val builder = ExecutionReport.Builder()
        //Swap the target/sender compId around, as we are sending this puppy back
        builder.withSenderCompId(targetCompId)
                .withTargetCompId(senderCompId)
                .withClOrderId(clOrdId)
                .withExecId(execId)
                .withExecType(execType)
                .withOrdStatus(ordStatus)
                .withInstrument(instrument)
                .withSide(side)
                .withTransactTime(transactTime)
                .withOrigClOrderId(origClOrderId)
                .withPrice(price)
                .withText(reason)
                .withFixSpec(fixSpec)
                .withLeavesQty(leavesQty)
                .withCumQty(cumQty)
        return builder.build()
    }

    override fun toString(): String {
        return "CancelReplaceRequest(clOrdId=$clOrdId, orderQty=$orderQty, price=$price)"
    }
}
