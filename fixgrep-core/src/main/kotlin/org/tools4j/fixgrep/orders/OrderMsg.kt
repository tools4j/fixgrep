package org.tools4j.fixgrep.orders

import org.tools4j.fixgrep.FixLine

/**
 * User: benjw
 * Date: 9/21/2018
 * Time: 9:56 AM
 */
class OrderMsg(val fixLine: FixLine){
    companion object {
        val NEW_ORDER_SINGLE = "D"
        val ORDER_CANCEL_REJECT = "9"
        val EXECUTION_REPORT = "8"
        val ORDER_CANCEL_REPLACE_REQUEST = "G"
        val ORDER_CANCEL_REQUEST = "F"

        val orderMessageTypes = listOf(
                NEW_ORDER_SINGLE,
                ORDER_CANCEL_REJECT,
                EXECUTION_REPORT,
                ORDER_CANCEL_REPLACE_REQUEST,
                ORDER_CANCEL_REQUEST)

        val orderRequestMessageTypes = listOf(
                NEW_ORDER_SINGLE,
                ORDER_CANCEL_REQUEST,
                ORDER_CANCEL_REPLACE_REQUEST
        )

        val orderResponseMessageTypes = listOf(
                ORDER_CANCEL_REJECT,
                EXECUTION_REPORT
        )

        val subsequentOrderRequestMessageTypes = listOf(
                ORDER_CANCEL_REQUEST,
                ORDER_CANCEL_REPLACE_REQUEST
        )
    }

    val fields = fixLine.fields
    val msgType: String? = fields.getField(35)!!.value.valueRaw
    val clOrdIdStr: String? = fields.getField(11)?.value?.valueRaw
    val orderIdStr: String? = fields.getField(37)?.value?.valueRaw
    val origClOrdID: String? = fields.getField(41)?.value?.valueRaw

    fun isOrderRequestMessage(): Boolean = orderRequestMessageTypes.contains(msgType)
    fun isOrderResponseMessage(): Boolean = orderResponseMessageTypes.contains(msgType)
    fun isNos(): Boolean = msgType == NEW_ORDER_SINGLE
    fun isSubsequentOrderRequest(): Boolean = subsequentOrderRequestMessageTypes.contains(msgType)
    fun hasOrderId() = orderIdStr != null
    fun hasClOrdId() = clOrdIdStr != null
}