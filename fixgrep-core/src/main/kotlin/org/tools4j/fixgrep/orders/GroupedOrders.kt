package org.tools4j.fixgrep.orders

import com.google.common.collect.ArrayListMultimap

/**
 * User: benjw
 * Date: 22/10/2018
 * Time: 17:07
 */
interface GroupedOrders {
    val messagesByOrderId: ArrayListMultimap<UniqueOrderId, OrderMsg>
    val unlinkedMessages: List<OrderMsg>
}