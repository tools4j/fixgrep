package org.tools4j.model

/**
 * User: ben
 * Date: 6/06/2017
 * Time: 5:39 PM
 */
enum class OrderType private constructor(val code: Int) {
    MARKET(1),
    LIMIT(2);

    companion object {
        val codeToOrderType: Map<Int, OrderType> by lazy {
            val map = HashMap<Int, OrderType>()
            OrderType.values().forEach { map.put(it.code, it) }
            map
        }

        fun forCode(code: Int): OrderType {
            return codeToOrderType[code]!!
        }

        fun forPrice(price: Price): OrderType {
            return if(price.hasPrice()) return OrderType.LIMIT else OrderType.MARKET
        }
    }
}
