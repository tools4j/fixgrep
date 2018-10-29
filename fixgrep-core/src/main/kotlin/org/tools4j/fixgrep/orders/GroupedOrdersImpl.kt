package org.tools4j.fixgrep.orders

import com.google.common.collect.ArrayListMultimap
import mu.KLogging
import org.tools4j.fix.Fields
import org.tools4j.fixgrep.linehandlers.FixLine

/**
 * User: benjw
 * Date: 9/20/2018
 * Time: 5:13 PM
 */
class GroupedOrdersImpl(val uniqueIdSpecs: UniqueIdSpecs): GroupedOrders {

    companion object: KLogging()

    override val messagesByOrderId: ArrayListMultimap<UniqueOrderId, OrderMsg> = ArrayListMultimap.create()
    override val unlinkedMessages: List<OrderMsg>
        get(){
            val unlinkedMessages = ArrayList(orphanMessages)
            unlinkedMessages.addAll(childlessNewOrderSinglesByClOrderId.values)
            return unlinkedMessages
        }

    internal val childlessNewOrderSinglesByClOrderId = LinkedHashMap<UniqueClientOrderId, OrderMsg>()
    internal val orderIdByClOrderId = LinkedHashMap<UniqueClientOrderId, UniqueOrderId>()
    internal val orphanMessages = ArrayList<OrderMsg>()

    class Ids(
            val clOrdId: UniqueClientOrderId?,
            val origClOrdId: UniqueClientOrderId?,
            val orderId: UniqueOrderId?
    )

    fun handle(fixLine: FixLine) {
        if(isOrderMessage(fixLine.fields)) {
            val orderMsg = OrderMsg(fixLine, uniqueIdSpecs)
            val ids = resolveIds(orderMsg)

            if (ids.clOrdId == null) {
                logger.warn { "Could not find or link to a clOrdId" }
                orphanMessages.add(orderMsg)
                return
            }

            if (!orderMsg.isNos() && ids.orderId == null) {
                logger.warn { "Could not find or link to an orderId" }
                orphanMessages.add(orderMsg)
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

            if(ids.orderId != null) orderIdByClOrderId.putIfAbsent(ids.clOrdId, ids.orderId)
            if(ids.origClOrdId != null && ids.orderId != null) orderIdByClOrderId.putIfAbsent(ids.origClOrdId, ids.orderId)
            messagesByOrderId.put(ids.orderId, orderMsg)
        }
    }

    fun resolveIds(orderMsg: OrderMsg): GroupedOrdersImpl.Ids {
        var clOrdId = orderMsg.clOrdId
        var orderId = orderMsg.orderId
        var origClOrdId = orderMsg.origClOrdId

        if(clOrdId == null && orderId != null) {
            clOrdId = findLastClOrdIdForOrderId(orderId) ?: clOrdId
        }

        if(orderId == null && clOrdId != null){
            orderId = orderIdByClOrderId.get(clOrdId) ?: orderId
        }

        if(orderId == null && origClOrdId != null){
            orderId = orderIdByClOrderId.get(origClOrdId) ?: orderId
        }

        if(origClOrdId == null && orderId != null) {
            origClOrdId = findLastOrigClOrdIdForOrderId(orderId) ?: origClOrdId
        }

        return Ids(clOrdId, origClOrdId, orderId)
    }

    private fun findLastClOrdIdForOrderId(orderId: UniqueOrderId): UniqueClientOrderId?{
        val messages = messagesByOrderId.get(orderId)
        return messages?.asReversed()?.filter { it.clOrdId != null }?.map { it.clOrdId }?.firstOrNull();
    }

    private fun findLastOrigClOrdIdForOrderId(orderId: UniqueOrderId): UniqueClientOrderId?{
        val messages = messagesByOrderId.get(orderId)
        return messages?.asReversed()?.filter { it.origClOrdId != null }?.map { it.origClOrdId }?.firstOrNull();
    }

    private fun isOrderMessage(fields: Fields): Boolean {
        val msgType = fields.getField(35)
        if(msgType == null){
            logger.error { "Fix line must contain tag 35." }
            return false
        }
        return OrderMsg.orderMessageTypes.contains(msgType.value.valueRaw)
    }
}