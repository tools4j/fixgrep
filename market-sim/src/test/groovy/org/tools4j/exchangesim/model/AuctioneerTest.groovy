package org.tools4j.exchangesim.model

import org.tools4j.exhangesim.model.AuctionResults
import org.tools4j.exhangesim.model.OrderBook
import org.tools4j.exhangesim.services.Auctioneer
import org.tools4j.fix.Fix50SP2FixSpecFromClassPath
import org.tools4j.fix.Id
import org.tools4j.fix.LimitPrice
import org.tools4j.fix.MarketPrice
import org.tools4j.fix.Price
import org.tools4j.fix.Side
import org.tools4j.fix.spec.FixSpecParser
import org.tools4j.model.*
import org.tools4j.model.fix.messages.LoggingMessageHandler
import org.tools4j.model.fix.messages.NewOrderSingle
import spock.lang.Specification

/**
 * User: ben
 * Date: 26/10/2016
 * Time: 5:33 PM
 */
class AuctioneerTest extends Specification {
    private OrderBook standardBook
    private OrderBook crossingBook
    private OrderBook crossingBookWithMarketAskPrice
    private OrderBook emptyBook
    private OrderBook bookWithOnlyBids
    private OrderBook bookWithOnlyAsks
    private DateTimeService dateTimeService
    private IdGenerator ordIdGenerator = new IdGenerator("O")
    private IdGenerator clOrdIdGenerator = new IdGenerator("C")


    def setup(){
        dateTimeService = new DateTimeService();
        standardBook = new OrderBook("ANZ")
        this.standardBook.add(createOrder(new LimitPrice(100.01d), 1_000_000, "ANZ", Side.BID))
        this.standardBook.add(createOrder(new LimitPrice(100.02d), 1_100_000, "ANZ", Side.BID))
        this.standardBook.add(createOrder(new LimitPrice(100.03d), 2_100_000, "ANZ", Side.BID))
        this.standardBook.add(createOrder(new LimitPrice(100.04d), 3_100_000, "ANZ", Side.BID))

        this.standardBook.add(createOrder(new LimitPrice(100.05d), 1_500_000, "ANZ", Side.ASK))
        this.standardBook.add(createOrder(new LimitPrice(100.06d), 3_000_000, "ANZ", Side.ASK))
        this.standardBook.add(createOrder(new LimitPrice(100.09d), 1_200_000, "ANZ", Side.ASK))
        this.standardBook.add(createOrder(new LimitPrice(100.10d), 2_100_000, "ANZ", Side.ASK))

        crossingBook = new OrderBook("ANZ")
        this.crossingBook.add(createOrder(new LimitPrice(100.06d), 3_100_000, "ANZ", Side.BID))
        this.crossingBook.add(createOrder(new LimitPrice(100.05d), 100_000, "ANZ", Side.BID))
        this.crossingBook.add(createOrder(new LimitPrice(100.04d), 2_100_000, "ANZ", Side.BID))
        this.crossingBook.add(createOrder(new LimitPrice(100.02d), 1_100_000, "ANZ", Side.BID))
        this.crossingBook.add(createOrder(new LimitPrice(100.01d), 1_000_000, "ANZ", Side.BID))

        this.crossingBook.add(createOrder(new LimitPrice(100.04d), 1_500_000, "ANZ", Side.ASK))
        this.crossingBook.add(createOrder(new LimitPrice(100.05d), 3_000_000, "ANZ", Side.ASK))
        this.crossingBook.add(createOrder(new LimitPrice(100.06d), 100_000, "ANZ", Side.ASK))
        this.crossingBook.add(createOrder(new LimitPrice(100.07d), 1_200_000, "ANZ", Side.ASK))
        this.crossingBook.add(createOrder(new LimitPrice(100.10d), 2_100_000, "ANZ", Side.ASK))

        crossingBookWithMarketAskPrice = new OrderBook("ANZ")
        this.crossingBookWithMarketAskPrice.add(createOrder(new LimitPrice(100.06d), 3_100_000, "ANZ", Side.BID))
        this.crossingBookWithMarketAskPrice.add(createOrder(new LimitPrice(100.05d), 100_000, "ANZ", Side.BID))
        this.crossingBookWithMarketAskPrice.add(createOrder(new LimitPrice(100.04d), 2_100_000, "ANZ", Side.BID))
        this.crossingBookWithMarketAskPrice.add(createOrder(new LimitPrice(100.02d), 1_100_000, "ANZ", Side.BID))
        this.crossingBookWithMarketAskPrice.add(createOrder(new LimitPrice(100.01d), 1_000_000, "ANZ", Side.BID))

        this.crossingBookWithMarketAskPrice.add(createOrder(MarketPrice.INSTANCE, 500_000, "ANZ", Side.ASK))
        this.crossingBookWithMarketAskPrice.add(createOrder(new LimitPrice(100.04d), 1_500_000, "ANZ", Side.ASK))
        this.crossingBookWithMarketAskPrice.add(createOrder(new LimitPrice(100.05d), 3_000_000, "ANZ", Side.ASK))
        this.crossingBookWithMarketAskPrice.add(createOrder(new LimitPrice(100.06d), 100_000, "ANZ", Side.ASK))
        this.crossingBookWithMarketAskPrice.add(createOrder(new LimitPrice(100.07d), 1_200_000, "ANZ", Side.ASK))
        this.crossingBookWithMarketAskPrice.add(createOrder(new LimitPrice(100.10d), 2_100_000, "ANZ", Side.ASK))

//        fromData{
//            price                  |       qty | symbol |     side
//            MarketPrice.INSTANCE   |   500_000 |  "ANZ" | Side.ASK
//            new LimitPrice(100.04) | 1_500_000 |  "ANZ" | Side.ASK
//            new LimitPrice(100.05) | 3_000_000 |  "ANZ" | Side.ASK
//            new LimitPrice(100.06) |   100_000 |  "ANZ" | Side.ASK
//            new LimitPrice(100.07) | 1_200_000 |  "ANZ" | Side.ASK
//            new LimitPrice(100.10) | 2_100_000 |  "ANZ" | Side.ASK
//        }.usingFactoryMethod{
//            this.crossingBookWithMarketAskPrice.add(new Order(new NumericId(), price, qty, symbol, side))
//        }


        emptyBook = new OrderBook("ANZ")

        bookWithOnlyBids = new OrderBook("ANZ")
        this.bookWithOnlyBids.add(createOrder(new LimitPrice(100.06d), 3_100_000, "ANZ", Side.BID))
        this.bookWithOnlyBids.add(createOrder(new LimitPrice(100.05d), 100_000, "ANZ", Side.BID))
        this.bookWithOnlyBids.add(createOrder(new LimitPrice(100.04d), 2_100_000, "ANZ", Side.BID))
        this.bookWithOnlyBids.add(createOrder(new LimitPrice(100.02d), 1_100_000, "ANZ", Side.BID))
        this.bookWithOnlyBids.add(createOrder(new LimitPrice(100.01d), 1_000_000, "ANZ", Side.BID))

        bookWithOnlyAsks = new OrderBook("ANZ")
        this.bookWithOnlyAsks.add(createOrder(new LimitPrice(100.06d), 3_100_000, "ANZ", Side.ASK))
        this.bookWithOnlyAsks.add(createOrder(new LimitPrice(100.05d), 100_000, "ANZ", Side.ASK))
        this.bookWithOnlyAsks.add(createOrder(new LimitPrice(100.04d), 2_100_000, "ANZ", Side.ASK))
        this.bookWithOnlyAsks.add(createOrder(new LimitPrice(100.02d), 1_100_000, "ANZ", Side.ASK))
        this.bookWithOnlyAsks.add(createOrder(new LimitPrice(100.01d), 1_000_000, "ANZ", Side.ASK))
    }


    def "test auction - standard book"(){
        when:
        final Auctioneer auctioneer = new Auctioneer();
        final AuctionResults auctionResults = auctioneer.auction(standardBook)

        then:
        assert auctionResults.lowestCrossingPrice == null
        assert auctionResults.highestCrossingPrice == null
        assert auctionResults.highestVolumeCrossingPrice == null
        assert auctionResults.highestCrossingVolume == null
    }

    def "test auction - empty book"(){
        when:
        final Auctioneer auctioneer = new Auctioneer();
        final AuctionResults auctionResults = auctioneer.auction(emptyBook)

        then:
        assert auctionResults.lowestCrossingPrice == null
        assert auctionResults.highestCrossingPrice == null
        assert auctionResults.highestVolumeCrossingPrice == null
        assert auctionResults.highestCrossingVolume == null
    }

    def "test auction - book with only bids"(){
        when:
        final Auctioneer auctioneer = new Auctioneer();
        final AuctionResults auctionResults = auctioneer.auction(bookWithOnlyBids)

        then:
        assert auctionResults.lowestCrossingPrice == null
        assert auctionResults.highestCrossingPrice == null
        assert auctionResults.highestVolumeCrossingPrice == null
        assert auctionResults.highestCrossingVolume == null
    }

    def "test auction - book with only asks"(){
        when:
        final Auctioneer auctioneer = new Auctioneer();
        final AuctionResults auctionResults = auctioneer.auction(bookWithOnlyAsks)

        then:
        assert auctionResults.lowestCrossingPrice == null
        assert auctionResults.highestCrossingPrice == null
        assert auctionResults.highestVolumeCrossingPrice == null
        assert auctionResults.highestCrossingVolume == null
    }

    def "test auction - crossed book"(){
        when:
        final Auctioneer auctioneer = new Auctioneer();
        final AuctionResults auctionResults = auctioneer.auction(crossingBook)

        then:
        assert auctionResults.lowestCrossingPrice == 100.04d
        assert auctionResults.highestCrossingPrice == 100.06d
        assert auctionResults.highestVolumeCrossingPrice == 100.05d
        assert auctionResults.highestCrossingVolume == 3_200_000L
    }

    def "test auction - crossed book with market ask price"(){
        when:
        final Auctioneer auctioneer = new Auctioneer();
        final AuctionResults auctionResults = auctioneer.auction(crossingBookWithMarketAskPrice)

        then:
        assert auctionResults.lowestCrossingPrice == 100.01d
        assert auctionResults.highestCrossingPrice == 100.06d
        assert auctionResults.highestVolumeCrossingPrice == 100.05d
        assert auctionResults.highestCrossingVolume == 3_200_000L
    }

    private VersionedOrder createOrder(final Price price, final long qty, final String symbol, final Side side) {
        final Id clOrdId = clOrdIdGenerator.get()
        final Id orderId = ordIdGenerator.get()

        return new MarketOrder(
            new NewOrderSingle(
                    "senderCompId",
                    "targetCompId",
                    clOrdId,
                    clOrdId,
                    symbol,
                    dateTimeService.now(),
                    qty,
                    price,
                    side,
                    new FixSpecParser().parseSpec()
            ),
            orderId,
            dateTimeService,
            new LoggingMessageHandler());
    }
}
