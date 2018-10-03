package org.tools4j.fixgrep.orders

import com.google.common.collect.LinkedHashMultimap
import mu.KLogging
import org.tools4j.fix.Fields
import org.tools4j.fixgrep.FixLine
import org.tools4j.fixgrep.FixLineHandler
import org.tools4j.fixgrep.Formatter
import java.util.function.Consumer

/**
 * User: benjw
 * Date: 9/20/2018
 * Time: 5:13 PM
 */
class OrderGroupingFixLineHandler(
        val formatter: Formatter,
        val uniqueClientOrderIdSpec: UniqueClientOrderIdSpec,
        val uniqueOriginalClientOrderIdSpec: UniqueOriginalClientOrderIdSpec,
        val uniqueOrderIdSpec: UniqueOrderIdSpec,
        val output: Consumer<String>) : FixLineHandler {

    val ORDER_RESPONSE_INDENT = "   "
    companion object: KLogging()

    val childlessNewOrderSinglesByClOrderId = LinkedHashMap<UniqueClientOrderId, OrderMsg>()
    val messagesByOrderId: LinkedHashMultimap<UniqueOrderId, OrderMsg> = LinkedHashMultimap.create()
    val orderIdByClOrderId = LinkedHashMap<UniqueClientOrderId, UniqueOrderId>()

    override fun handle(fixLine: FixLine) {
        if(isOrderMessage(fixLine.fields)){
            val orderMsg = OrderMsg(fixLine)
            val clOrdId = uniqueClientOrderIdSpec.getId(orderMsg)
            val orderId = uniqueOrderIdSpec.getId(orderMsg)
            val origClOrdId = uniqueOriginalClientOrderIdSpec.getId(orderMsg)
            val resolvedOrderId: UniqueOrderId

            if(orderMsg.isOrderRequestMessage()){
                logger.info { "Received order REQUEST message: " + fixLine }

                if(clOrdId.isNull()){
                    logger.warn { "Could not find clOrdId.  Could not find at least one of these fields: $uniqueClientOrderIdSpec from message" }
                    return

                } else if(orderMsg.isNos()){
                    logger.info { "Adding nos to orphan list" }
                    childlessNewOrderSinglesByClOrderId.put(clOrdId, orderMsg)
                    return

                } else if(orderIdByClOrderId.containsKey(clOrdId)) {
                    val linkedOrderId = orderIdByClOrderId.get(clOrdId)!!
                    logger.info { "Found existing clOrdId linked to order: $linkedOrderId" }
                    if(!orderId.isNull() && linkedOrderId != orderId) {
                        logger.warn { "orderId on message [$orderId] does not match with other orderId linked to clOrderId [$linkedOrderId], this implies that there have been duplicate clOrdIds received." }
                        return
                    } else {
                        resolvedOrderId = linkedOrderId
                    }

                } else if (orderIdByClOrderId.containsKey(origClOrdId)) {
                    val linkedOrderId = orderIdByClOrderId.get(origClOrdId)!!
                    logger.info { "Found orderId [$orderId] linked to origClOrdId [$origClOrdId], linking to new clOrdId [$clOrdId]" }
                    if (!orderId.isNull() && linkedOrderId != orderId) {
                        logger.warn { "orderId on message [$orderId] does not match with other orderId linked to origClOrdId [$linkedOrderId], this implies that there have been duplicate clOrdIds received." }
                        return
                    } else {
                        orderIdByClOrderId.put(clOrdId, linkedOrderId)
                        resolvedOrderId = linkedOrderId
                    }
                } else {
                    logger.warn { "Could not find any orderIds linked to clOrdId [$clOrdId] or origClOrdId [$origClOrdId]" }
                    return
                }
            } else {
                logger.info { "Received order RESPONSE message"}
                if(!orderId.isNull()){
                    resolvedOrderId = orderId
                    if(childlessNewOrderSinglesByClOrderId.containsKey(clOrdId)){
                        logger.info { "Moving orphaned Nos with clOrdId [$clOrdId] to known message bucket for orderId [$resolvedOrderId]"}
                        messagesByOrderId.put(resolvedOrderId, childlessNewOrderSinglesByClOrderId.remove(clOrdId))
                    }
                    orderIdByClOrderId.putIfAbsent(clOrdId, orderId)
                } else {
                    logger.warn { "Could not find mandatory orderId in message." }
                    return
                }
            }
            messagesByOrderId.put(resolvedOrderId, orderMsg)
        }
    }

    private fun formatAndPrint(fixLine: FixLine) {
        val formattedLine = formatter.format(fixLine)
        if (formattedLine != null) output.accept(formattedLine)
    }

    private fun isOrderMessage(fields: Fields): Boolean {
        val msgType = fields.getField(35)
        if(msgType == null){
            logger.error { "Fix line must contain tag 35." }
            return false
        }
        return OrderMsg.orderMessageTypes.contains(msgType.value.valueRaw)
    }

    override fun finish() {
        for (orderId in messagesByOrderId.keySet()) {
            printMessagesForOrder(messagesByOrderId.get(orderId).toList(), orderId.id!!.value.valueRaw)
            output.accept(formatter.spec.getCarriageReturn())
        }
        for (orderMsg in childlessNewOrderSinglesByClOrderId.values) {
            printMessagesForOrder(listOf(orderMsg), "UNKNOWN")
            output.accept(formatter.spec.getCarriageReturn())
        }
    }

    private fun printMessagesForOrder(messages: List<OrderMsg>, orderIdStr: String) {
        var orderHeader = formatter.spec.getOutputFormatGroupedOrderHeader()
        if (orderHeader.contains("\${origClOrdId}")) {
            val firstClOrdId = uniqueClientOrderIdSpec.getId(messages.first()).id
            val clOrdIdToReplace = if (firstClOrdId != null) firstClOrdId.value.valueRaw else ""
            orderHeader = orderHeader.replace("\${origClOrdId}", clOrdIdToReplace)
        }
        if (orderHeader.contains("\${orderId}")) {
            orderHeader = orderHeader.replace("\${orderId}", orderIdStr)
        }
        if (orderHeader.contains("\${carriageReturn}")) {
            orderHeader = orderHeader.replace("\${carriageReturn}", formatter.spec.getCarriageReturn())
        }

        output.accept(orderHeader)
        for (message in messages) {
            if (formatter.spec.shouldPrint(message.fixLine)) {
                val formattedMessage = formatter.format(message.fixLine)
                if (formattedMessage != null) {
                    if (message.isOrderResponseMessage()) {
                        output.accept(ORDER_RESPONSE_INDENT)
                    }
                    output.accept(formattedMessage + formatter.spec.getCarriageReturn())
                }
            }
        }
    }
}