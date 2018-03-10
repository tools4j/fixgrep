package org.tools4j.exhangesim.services;

import org.tools4j.exhangesim.model.AuctionResults;
import org.tools4j.exhangesim.model.OrderBook;

import java.util.Collection;

/**
 * User: ben
 * Date: 20/10/2016
 * Time: 6:31 AM
 */
public class Auctioneer {
    public AuctionResults auction(final OrderBook orderBook) {
        Double lowestCrossingPrice = null;
        Double highestCrossingPrice = null;
        Double highestVolumeCrossingPrice = null;
        Long highestCrossingVolume = null;

        final Collection<Double> prices = orderBook.getAllPrices();
        for (final Double price : prices) {
            long bidVolume = orderBook.getBidLeavesQtyEqualToOrMoreAggressiveThan(price);
            long askVolume = orderBook.getAskLeavesQtyEqualToOrMoreAggressiveThan(price);
            long matchingVolume = Long.min(bidVolume, askVolume);

            if(matchingVolume > 0 && (lowestCrossingPrice == null || price < lowestCrossingPrice)){
                lowestCrossingPrice = price;
            }
            if(matchingVolume > 0 && (highestCrossingPrice == null || price > highestCrossingPrice)){
                highestCrossingPrice = price;
            }
            if(matchingVolume > 0 && (highestCrossingVolume == null || matchingVolume > highestCrossingVolume)){
                highestVolumeCrossingPrice = price;
                highestCrossingVolume = matchingVolume;
            }
        }
        return new AuctionResults(lowestCrossingPrice, highestCrossingPrice, highestVolumeCrossingPrice, highestCrossingVolume);
    }
}
