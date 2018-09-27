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

    val orphanNewOrderSinglesByClOrderId = LinkedHashMap<UniqueClientOrderId, OrderMsg>()
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
                logger.info { "Received order REQUEST message: " + formatAndPrint(fixLine) }

                if(clOrdId.isNull()){
                    logger.warn { "Could not find clOrdId.  Could not find at least one of these fields: $uniqueClientOrderIdSpec from message" }
                    return

                } else if(orderMsg.isNos()){
                    logger.info { "Adding nos to orphan list" }
                    orphanNewOrderSinglesByClOrderId.put(clOrdId, orderMsg)
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
                    if(!clOrdId.isNull() && orphanNewOrderSinglesByClOrderId.containsKey(clOrdId)){
                        logger.info { "Moving orphaned Nos with clOrdId [$clOrdId] to known message bucket for orderId [$resolvedOrderId]"}
                        messagesByOrderId.put(resolvedOrderId, orphanNewOrderSinglesByClOrderId.remove(clOrdId))
                    }
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
        val msgType = fields[35].value.valueRaw
        return OrderMsg.orderMessageTypes.contains(msgType)
    }

    override fun finish() {
        val orderHeaderTemplate = formatter.spec.getOutputFormatGroupedOrderHeader()

        for (orderId in messagesByOrderId.keySet()) {
            val messages: List<OrderMsg> = ArrayList(messagesByOrderId.get(orderId))
            var orderHeader = orderHeaderTemplate
            if (orderHeader.contains("\${clOrdId}")) {
                val firstClOrdId = uniqueClientOrderIdSpec.getId(messages.first()).id
                val clOrdIdToReplace = if (firstClOrdId != null) firstClOrdId.value.valueRaw else ""
                orderHeader = orderHeader.replace("\${clOrdId}", clOrdIdToReplace)
            }
            if (orderHeader.contains("\${orderId}")) {
                orderHeader = orderHeader.replace("\${orderId}", orderId.id!!.value.valueRaw)
            }
            output.accept(orderHeader)
            for (message in messages) {
                if(!shouldPrint(message.fixLine)){
                    val formattedMessage = formatter.format(message.fixLine)
                    if(formattedMessage != null){
                        if(message.isOrderResponseMessage()){
                            output.accept(ORDER_RESPONSE_INDENT)
                        }
                        output.accept(formattedMessage)
                    }
                }
            }
        }
    }

    private fun shouldPrint(fixLine: FixLine): Boolean{
        if(!formatter.spec.includeOnlyMessagesOfType.isEmpty()
                && !formatter.spec.includeOnlyMessagesOfType.contains(fixLine.fields.msgTypeCode)){
            return false
        } else if(!formatter.spec.excludeMessagesOfType.isEmpty()
                && formatter.spec.excludeMessagesOfType.contains(fixLine.fields.msgTypeCode)) {
            return false
        } else {
            return true
        }
    }
}