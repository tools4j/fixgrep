package org.tools4j.exhangesim.services

import org.tools4j.exhangesim.model.AuctionResults
import org.tools4j.exhangesim.model.OrderBook

/**
 * User: ben
 * Date: 20/10/2016
 * Time: 6:31 AM
 */
class Auctioneer {
    fun auction(orderBook: OrderBook): AuctionResults {
        var lowestCrossingPrice: Double? = null
        var highestCrossingPrice: Double? = null
        var highestVolumeCrossingPrice: Double? = null
        var highestCrossingVolume: Long? = null

        val prices = orderBook.allPrices
        for (price in prices) {
            val bidVolume = orderBook.getBidLeavesQtyEqualToOrMoreAggressiveThan(price)
            val askVolume = orderBook.getAskLeavesQtyEqualToOrMoreAggressiveThan(price)
            val matchingVolume = java.lang.Long.min(bidVolume, askVolume)

            if (matchingVolume > 0 && (lowestCrossingPrice == null || price < lowestCrossingPrice)) {
                lowestCrossingPrice = price
            }
            if (matchingVolume > 0 && (highestCrossingPrice == null || price > highestCrossingPrice)) {
                highestCrossingPrice = price
            }
            if (matchingVolume > 0 && (highestCrossingVolume == null || matchingVolume > highestCrossingVolume)) {
                highestVolumeCrossingPrice = price
                highestCrossingVolume = matchingVolume
            }
        }
        return AuctionResults(lowestCrossingPrice, highestCrossingPrice, highestVolumeCrossingPrice, highestCrossingVolume)
    }
}
