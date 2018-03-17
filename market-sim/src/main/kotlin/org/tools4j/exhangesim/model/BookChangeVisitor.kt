package org.tools4j.exhangesim.model

/**
 * User: ben
 * Date: 19/10/2016
 * Time: 5:48 PM
 */
interface BookChangeVisitor {
    fun visit(orderBook: OrderBook)
}