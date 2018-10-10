package org.tools4j.fixgrep.orders

import com.google.common.collect.ArrayListMultimap
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
        val uniqueIdSpecs: UniqueIdSpecs,
        val output: Consumer<String>) : FixLineHandler {

    companion object: KLogging()

    val childlessNewOrderSinglesByClOrderId = LinkedHashMap<UniqueClientOrderId, OrderMsg>()
    val messagesByOrderId: ArrayListMultimap<UniqueOrderId, OrderMsg> = ArrayListMultimap.create()
    val orderIdByClOrderId = LinkedHashMap<UniqueClientOrderId, UniqueOrderId>()
    val orphanMessages = ArrayList<FixLine>()

    class Ids(
            val clOrdId: UniqueClientOrderId,
            val origClOrdId: UniqueClientOrderId,
            val orderId: UniqueOrderId
    )

    override fun handle(fixLine: FixLine) {
        if(isOrderMessage(fixLine.fields)) {
            val orderMsg = OrderMsg(fixLine, uniqueIdSpecs)
            val ids = resolveIds(orderMsg)

            if (ids.clOrdId.isNull()) {
                logger.warn { "Could not find or link to a clOrdId" }
                orphanMessages.add(fixLine)
                return
            }

            if (!orderMsg.isNos() && ids.orderId.isNull()) {
                logger.warn { "Could not find or link to an orderId" }
                orphanMessages.add(fixLine)
                return
            }

            if(orderMsg.isNos()){
                logger.info { "Adding nos to orphan list" }
                childlessNewOrderSinglesByClOrderId.put(ids.clOrdId, orderMsg)
                return;

            } else if(childlessNewOrderSinglesByClOrderId.containsKey(ids.clOrdId)){
                logger.info { "Moving orphaned Nos with clOrdId [$ids.clOrdId] to known message bucket for orderId [${ids.orderId}]"}
                messagesByOrderId.put(ids.orderId, childlessNewOrderSinglesByClOrderId.remove(ids.clOrdId))
            }

            orderIdByClOrderId.putIfAbsent(ids.clOrdId, ids.orderId)
            if(!ids.origClOrdId.isNull()) orderIdByClOrderId.putIfAbsent(ids.origClOrdId, ids.orderId)
            messagesByOrderId.put(ids.orderId, orderMsg)
        }
    }

    fun getMsgLinePrefix(orderMsgType: OrderMsg.OrderMsgType): String{
        return if(formatter.spec.formatInHtml){
            if(orderMsgType == OrderMsg.OrderMsgType.ORDER_RESPONSE){
                "<div class='msg order-response'>"
            } else {
                "<div class='msg order-request'>"
            }
        } else {
            if(orderMsgType == OrderMsg.OrderMsgType.ORDER_RESPONSE){
                "   "
            } else {
                ""
            }
        }
    }

    fun getMsgLinePostfix(): String{
        return if(formatter.spec.formatInHtml){
            "</div>\n"
        } else {
            "\n"
        }
    }

    fun getOrphanMsgLinePrefix(): String{
        return if(formatter.spec.formatInHtml){
            "<div class='msg orphan-message'>"
        } else {
            "   "
        }
    }

    fun resolveIds(orderMsg: OrderMsg): OrderGroupingFixLineHandler.Ids {
        var clOrdId = orderMsg.clOrdId
        var orderId = orderMsg.orderId
        var origClOrdId = orderMsg.origClOrdId

        if(clOrdId.isNull() && !orderId.isNull()) {
            clOrdId = findLastClOrdIdForOrderId(orderId) ?: clOrdId
        }

        if(orderId.isNull() && !clOrdId.isNull()){
            orderId = orderIdByClOrderId.get(clOrdId) ?: orderId
        }

        if(orderId.isNull() && !origClOrdId.isNull()){
            orderId = orderIdByClOrderId.get(origClOrdId) ?: orderId
        }

        if(origClOrdId.isNull() && !orderId.isNull()) {
            origClOrdId = findLastOrigClOrdIdForOrderId(orderId) ?: origClOrdId
        }

        return OrderGroupingFixLineHandler.Ids(clOrdId, origClOrdId, orderId)
    }

    private fun findLastClOrdIdForOrderId(orderId: UniqueOrderId): UniqueClientOrderId?{
        val messages = messagesByOrderId.get(orderId)
        return messages?.asReversed()?.filter { !it.clOrdId.isNull() }?.map { it.clOrdId }?.firstOrNull();
    }

    private fun findLastOrigClOrdIdForOrderId(orderId: UniqueOrderId): UniqueClientOrderId?{
        val messages = messagesByOrderId.get(orderId)
        return messages?.asReversed()?.filter { !it.origClOrdId.isNull() }?.map { it.origClOrdId }?.firstOrNull();
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
        if(messagesByOrderId.isEmpty
            && childlessNewOrderSinglesByClOrderId.isEmpty()
            && orphanMessages.isEmpty()){
            output.accept("No order messages found")
        }

        var atLeastOneOrderPrinted = false
        for (orderId in messagesByOrderId.keySet()) {
            printMessagesForOrder(messagesByOrderId.get(orderId).toList(), orderId.id!!.value.valueRaw)
            output.accept(formatter.spec.getCarriageReturn())
            atLeastOneOrderPrinted = true
        }
        orphanMessages.addAll(childlessNewOrderSinglesByClOrderId.map { it.value.fixLine })
        if(!orphanMessages.isEmpty()){
            if(atLeastOneOrderPrinted) output.accept(formatter.spec.getCarriageReturn())
            output.accept("ORPHAN MESSAGES:")
            output.accept(formatter.spec.getCarriageReturn())
            for (orphanMessage in orphanMessages) {
                val formattedMessage = formatter.format(orphanMessage)
                output.accept(getOrphanMsgLinePrefix() + formattedMessage + getMsgLinePostfix())
            }
        }
    }

    private fun printMessagesForOrder(messages: List<OrderMsg>, orderIdStr: String) {
        var orderHeader = formatter.spec.getOutputFormatGroupedOrderHeader()
        if (orderHeader.contains("\${origClOrdId}")) {
            val firstClOrdId = uniqueIdSpecs.uniqueClientOrderIdSpec.getId(messages.first()).id
            val clOrdIdToReplace = if (firstClOrdId != null) firstClOrdId.value.valueRaw else ""
            orderHeader = orderHeader.replace("\${origClOrdId}", clOrdIdToReplace)
        }
        if (orderHeader.contains("\${orderId}")) {
            orderHeader = orderHeader.replace("\${orderId}", orderIdStr)
        }
        if (orderHeader.contains("\${carriageReturn}")) {
            orderHeader = orderHeader.replace("\${carriageReturn}", "\n")
        }

        output.accept(orderHeader)
        for (message in messages) {
            if (formatter.spec.shouldPrint(message.fixLine)) {
                val formattedMessage = formatter.format(message.fixLine)
                if (formattedMessage != null) {
                    output.accept(getMsgLinePrefix(message.getOrderMsgType()) + formattedMessage + getMsgLinePostfix())
                }
            }
        }
    }
}