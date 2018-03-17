package org.tools4j.model.fix.messages

import org.tools4j.fix.ExecType
import org.tools4j.fix.Fields
import org.tools4j.fix.FieldsFromDelimitedString
import org.tools4j.fix.FixFieldTypes
import org.tools4j.fix.FixMessageTypes
import org.tools4j.fix.FixSpec
import org.tools4j.fix.Id
import org.tools4j.fix.OrdStatus
import org.tools4j.fix.OrderType
import org.tools4j.fix.Price
import org.tools4j.fix.Side
import org.tools4j.model.FieldsBuilder
import org.tools4j.utils.FormatUtils

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

    override val fields: Fields by lazy {
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
            val fields = FieldsFromDelimitedString(str, delimiter).fields
            return createMessage(fields)

        }

        override fun createMessage(fields: Fields): CancelReplaceRequest {
            return CancelReplaceRequest(
                    senderCompId = fields.getField(FixFieldTypes.SenderCompID)!!.stringValue(),
                    targetCompId = fields.getField(FixFieldTypes.TargetCompID)!!.stringValue(),
                    origClOrderId = fields.getField(FixFieldTypes.OrigClOrdID)!!.idValue(),
                    clOrdId = fields.getField(FixFieldTypes.ClOrdID)!!.idValue(),
                    instrument = fields.getField(FixFieldTypes.Symbol)!!.stringValue(),
                    transactTime = fields.getField(FixFieldTypes.TransactTime)!!.longValueFromUTCTimestamp(),
                    side = fields.getField(FixFieldTypes.Side)!!.sideValue(),
                    orderQty = fields.getField(FixFieldTypes.OrderQty)!!.longValue(),
                    orderType = fields.getField(FixFieldTypes.OrdType)!!.orderTypeValue(),
                    price = fields.getField(FixFieldTypes.Price)!!.priceValue(),
                    orderId = fields.getField(FixFieldTypes.OrderID)?.idValue(),
                    fixSpec = fixSpec)
        }

        companion object {
            private val MSG_TYPE = FixMessageTypes.OrderCancelReplaceRequest
        }
    }

    fun createRejectedMessage(orderId: Id, execId: Id, ordStatus: OrdStatus, leavesQty: Long, cumQty: Long, reason: String): ExecutionReport {
        return createExecutionReport(orderId, execId, ExecType.Rejected, leavesQty, cumQty, ordStatus, reason)
    }

    fun createAcceptedMessage(orderId: Id, execId: Id, ordStatus: OrdStatus, leavesQty: Long, cumQty: Long): ExecutionReport {
        return createExecutionReport(orderId, execId, ExecType.Replaced, leavesQty, cumQty, ordStatus, null)
    }

    private fun createExecutionReport(orderId: Id, execId: Id, execType: ExecType, leavesQty: Long, cumQty: Long, ordStatus: OrdStatus, reason: String?): ExecutionReport {
        val builder = ExecutionReport.Builder()
        //Swap the target/sender compId around, as we are sending this puppy back
        builder.withSenderCompId(targetCompId)
                .withTargetCompId(senderCompId)
                .withClOrderId(clOrdId)
                .withOrderId(orderId)
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
