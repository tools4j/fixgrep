package org.tools4j.fixgrep.orders

import org.tools4j.fixgrep.linehandlers.FixLine
import java.util.concurrent.atomic.AtomicLong

/**
 * User: benjw
 * Date: 9/21/2018
 * Time: 9:56 AM
 */
class OrderMsg(internal val fixLine: FixLine, internal val uniqueIdSpecs: UniqueIdSpecs): IdentifiableOrder{
    enum class OrderMsgType {ORDER_REQUEST, ORDER_RESPONSE}
    internal val fields = fixLine.fields
    internal val msgType: String? = fields.getField(35)!!.value.valueRaw
    val seqNumber = seqNumberProvider.getAndIncrement()

    override val clOrdId: UniqueClientOrderId? by lazy {
        if(!uniqueIdSpecs.uniqueClientOrderIdSpec.isPresent(fields)) null
        else uniqueIdSpecs.uniqueClientOrderIdSpec.getId(fields)
    }

    override val origClOrdId: UniqueClientOrderId? by lazy {
        if(!uniqueIdSpecs.uniqueOriginalClientOrderIdSpec.isPresent(fields)) null
        else uniqueIdSpecs.uniqueOriginalClientOrderIdSpec.getId(fields)
    }

    override val orderId: UniqueOrderId? by lazy {
        if(!uniqueIdSpecs.uniqueOrderIdSpec.isPresent(fields)) null
        else uniqueIdSpecs.uniqueOrderIdSpec.getId(fields)
    }

    fun isOrderRequestMessage(): Boolean = orderRequestMessageTypes.contains(msgType)
    fun isOrderResponseMessage(): Boolean = orderResponseMessageTypes.contains(msgType)
    fun isNos(): Boolean = msgType == NEW_ORDER_SINGLE
    fun isSubsequentOrderRequest(): Boolean = subsequentOrderRequestMessageTypes.contains(msgType)
    fun getOrderMsgType(): OrderMsgType = if(isOrderRequestMessage()) OrderMsgType.ORDER_REQUEST else OrderMsgType.ORDER_RESPONSE

    companion object {
        val NEW_ORDER_SINGLE = "D"
        val ORDER_CANCEL_REJECT = "9"
        val EXECUTION_REPORT = "8"
        val ORDER_CANCEL_REPLACE_REQUEST = "G"
        val ORDER_CANCEL_REQUEST = "F"
        val ORDER_STATUS_REQUEST = "H"

        val orderRequestMessageTypes = listOf(
                NEW_ORDER_SINGLE,
                ORDER_CANCEL_REQUEST,
                ORDER_CANCEL_REPLACE_REQUEST,
                ORDER_STATUS_REQUEST
        )

        val orderResponseMessageTypes = listOf(
                ORDER_CANCEL_REJECT,
                EXECUTION_REPORT
        )

        val orderMessageTypes = orderRequestMessageTypes + orderResponseMessageTypes

        val subsequentOrderRequestMessageTypes = listOf(
                ORDER_CANCEL_REQUEST,
                ORDER_CANCEL_REPLACE_REQUEST
        )

        val seqNumberProvider = AtomicLong(0)
    }
}
