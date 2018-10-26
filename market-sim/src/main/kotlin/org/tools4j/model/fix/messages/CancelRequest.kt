package org.tools4j.model.fix.messages

import org.tools4j.fix.*
import org.tools4j.fix.spec.FixSpecDefinition
import org.tools4j.model.FieldsBuilder
import org.tools4j.utils.FormatUtils

/**
 * User: ben
 * Date: 6/06/2017
 * Time: 5:13 PM
 */
class CancelRequest(
        override val senderCompId: String,
        override val targetCompId: String,
        override val orderId: Id?,
        override val origClOrderId: Id,
        override val clOrdId: Id,
        override val instrument: String,
        override val transactTime: Long,
        override val side: Side,
        private val orderQty: Long,
        private val orderType: OrderType,
        private val price: Price,
        fixSpec: FixSpecDefinition) : OrderRequestMessage, FixMessage(fixSpec) {

    override fun dispatchBackTo(messageHandler: DoubleDispatchingMessageHandler) {
        messageHandler.handleCancelRequest(this);
    }

    override fun toString(): String {
        return "CancelRequest(senderCompId='$senderCompId', targetCompId='$targetCompId', origClOrdId=$origClOrderId, clOrdId=$clOrdId)"
    }

    override val fields: Fields by lazy {
        FieldsBuilder()
                .with(FixFieldTypes.MsgType, FixMessageType.OrderCancelRequest.code)
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

    class Decoder(val fixSpec: FixSpecDefinition) : FixMessageDecoder<CancelRequest> {
        override val msgType: String
            get() = MSG_TYPE

        override fun createMessage(str: String, delimiter: String): CancelRequest {
            val fields = FieldsFromDelimitedString(str, delimiter).fields
            return createMessage(fields)

        }

        override fun createMessage(fields: Fields): CancelRequest {
            return CancelRequest(
                    senderCompId = fields.getField(FixFieldTypes.SenderCompID)!!.stringValue(),
                    targetCompId = fields.getField(FixFieldTypes.TargetCompID)!!.stringValue(),
                    orderId = fields.getField(FixFieldTypes.OrderID)?.idValue(),
                    origClOrderId = fields.getField(FixFieldTypes.OrigClOrdID)!!.idValue(),
                    clOrdId = fields.getField(FixFieldTypes.ClOrdID)!!.idValue(),
                    instrument = fields.getField(FixFieldTypes.Symbol)!!.stringValue(),
                    transactTime = fields.getField(FixFieldTypes.TransactTime)!!.longValueFromUTCTimestamp(),
                    side = fields.getField(FixFieldTypes.Side)!!.sideValue(),
                    orderQty = fields.getField(FixFieldTypes.OrderQty)!!.longValue(),
                    orderType = fields.getField(FixFieldTypes.OrdType)!!.orderTypeValue(),
                    price = fields.getField(FixFieldTypes.Price)!!.priceValue(),
                    fixSpec = fixSpec)
        }

        companion object {
            private val MSG_TYPE = FixMessageType.OrderCancelRequest.code
        }
    }


}
