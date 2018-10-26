package org.tools4j.fixgrep.orders

import org.tools4j.fixgrep.Formatter
import java.util.function.Consumer

/**
 * User: benjw
 * Date: 22/10/2018
 * Time: 17:05
 */
class OrderGroupingPrinter(
        val groupedOrders: GroupedOrders,
        val idFilter: IdFilter,
        val formatter: Formatter,
        val output: Consumer<String>){

    fun print(){
        if(groupedOrders.messagesByOrderId.isEmpty
                && groupedOrders.unlinkedMessages.isEmpty()){
            output.accept("No order messages found")
        }

        var atLeastOneOrderPrinted = false
        for (orderId in groupedOrders.messagesByOrderId.keySet()
                .filter { idFilter.matchesFilter(groupedOrders.messagesByOrderId.get(it)) }
                .sortedBy { groupedOrders.messagesByOrderId.get(it).first().seqNumber }
        ){
            printMessagesForOrder(groupedOrders.messagesByOrderId.get(orderId).toList(), orderId.id)
            output.accept(formatter.spec.getCarriageReturn())
            atLeastOneOrderPrinted = true
        }
        var orphanMessagesHeaderPrinted = false
        for (orphanMsg in groupedOrders.unlinkedMessages
                .filter { idFilter.matchesFilter(it) }
                .sortedBy { it.seqNumber }
        ){
            if(!orphanMessagesHeaderPrinted) {
                if(atLeastOneOrderPrinted) output.accept(formatter.spec.getCarriageReturn())
                output.accept("ORPHAN MESSAGES:")
                output.accept(formatter.spec.getCarriageReturn())
                orphanMessagesHeaderPrinted = true
            }
            val formattedMessage = formatter.format(orphanMsg.fixLine)
            output.accept(getOrphanMsgLinePrefix() + formattedMessage + getMsgLinePostfix())
        }
    }

    private fun printMessagesForOrder(messages: List<OrderMsg>, orderIdStr: String) {
        var orderHeader = formatter.spec.getOutputFormatGroupedOrderHeader()
        if (orderHeader.contains("\${origClOrdId}")) {
            val firstClOrdId = messages.first({it.clOrdId != null}).clOrdId
            val clOrdIdToReplace = if (firstClOrdId != null) firstClOrdId.id else ""
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
}