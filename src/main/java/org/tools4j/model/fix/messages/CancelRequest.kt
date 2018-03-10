package org.tools4j.model.fix.messages

import org.tools4j.fix.Field
import org.tools4j.fix.FieldMap
import org.tools4j.fix.Fields
import org.tools4j.fix.FieldsFromDelimitedString
import org.tools4j.fix.FixFieldTypes
import org.tools4j.fix.FixMessageTypes
import org.tools4j.fix.FixSpec
import org.tools4j.model.FieldsBuilder
import org.tools4j.model.FormatUtils
import org.tools4j.model.Id
import org.tools4j.model.OrderType
import org.tools4j.model.Price
import org.tools4j.model.Side

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
        fixSpec: FixSpec) : OrderRequestMessage, FixMessage(fixSpec) {

    override fun dispatchBackTo(messageHandler: DoubleDispatchingMessageHandler) {
        messageHandler.handleCancelRequest(this);
    }

    override fun toString(): String {
        return "CancelRequest(senderCompId='$senderCompId', targetCompId='$targetCompId', origClOrdId=$origClOrderId, clOrdId=$clOrdId)"
    }

    override val fields: List<Field> by lazy {
        FieldsBuilder()
                .with(FixFieldTypes.MsgType, FixMessageTypes.OrderCancelRequest)
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

    class Decoder(val fixSpec: FixSpec) : FixMessageDecoder<CancelRequest> {
        override val msgType: String
            get() = MSG_TYPE

        override fun createMessage(str: String, delimiter: Char): CancelRequest {
            val fields = FieldsFromDelimitedString(str, delimiter)
            return createMessage(fields)

        }

        override fun createMessage(fields: Fields): CancelRequest {
            val fieldMap = FieldMap(fields).mapValue
            return CancelRequest(
                    senderCompId = fieldMap[FixFieldTypes.SenderCompID]!!.stringValue(),
                    targetCompId = fieldMap[FixFieldTypes.TargetCompID]!!.stringValue(),
                    orderId = fieldMap[FixFieldTypes.OrderID]?.idValue(),
                    origClOrderId = fieldMap[FixFieldTypes.OrigClOrdID]!!.idValue(),
                    clOrdId = fieldMap[FixFieldTypes.ClOrdID]!!.idValue(),
                    instrument = fieldMap[FixFieldTypes.Symbol]!!.stringValue(),
                    transactTime = fieldMap[FixFieldTypes.TransactTime]!!.longValueFromUTCTimestamp(),
                    side = fieldMap[FixFieldTypes.Side]!!.sideValue(),
                    orderQty = fieldMap[FixFieldTypes.OrderQty]!!.longValue(),
                    orderType = fieldMap[FixFieldTypes.OrdType]!!.orderTypeValue(),
                    price = fieldMap[FixFieldTypes.Price]!!.priceValue(),
                    fixSpec = fixSpec)
        }

        companion object {
            private val MSG_TYPE = FixMessageTypes.OrderCancelRequest
        }
    }


}
