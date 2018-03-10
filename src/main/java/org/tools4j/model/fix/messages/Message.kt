package org.tools4j.model.fix.messages

import org.tools4j.model.Id
import org.tools4j.oms.OrigClOrdIdAndSenderCompId

/**
 * User: ben
 * Date: 6/06/2017
 * Time: 5:17 PM
 */
interface  Message {
    val transactTime: Long
    val senderCompId: String
    val targetCompId: String
    val orderId: Id?
    val clOrdId: Id?
    val origClOrderId: Id?
    fun dispatchBackTo(messageHandler: DoubleDispatchingMessageHandler)
}
