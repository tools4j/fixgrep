package org.tools4j.exchangesim.model

import org.tools4j.exhangesim.model.OrderBook
import org.tools4j.fix.Fix50SP2FixSpecFromClassPath
import org.tools4j.model.*
import org.tools4j.model.fix.messages.LoggingMessageHandler
import spock.lang.Specification

/**
 * User: ben
 * Date: 19/10/2016
 * Time: 6:06 AM
 */
class OrderImplBookTest extends Specification{
    private OrderBook standardBook
    private OrderBook crossingBook
    private DateTimeService dateTimeService;
    private IdGenerator ordIdGenerator = new IdGenerator("O")
    private IdGenerator clOrdIdGenerator = new IdGenerator("C")
    
    def setup(){
        dateTimeService = new DateTimeService();
        standardBook = new OrderBook("ANZ")
        this.standardBook.add(createOrder(new LimitPrice(100.01d), 1_000_000, "ANZ", Side.BID))
        this.standardBook.add(createOrder(new LimitPrice(100.02d), 1_100_000, "ANZ", Side.BID))
        this.standardBook.add(createOrder(new LimitPrice(100.03d), 2_100_000, "ANZ", Side.BID))
        this.standardBook.add(createOrder(new LimitPrice(100.03d), 100_000, "ANZ", Side.BID))
        this.standardBook.add(createOrder(new LimitPrice(100.04d), 3_100_000, "ANZ", Side.BID))

        this.standardBook.add(createOrder(MarketPrice.INSTANCE, 200_000, "ANZ", Side.ASK))
        this.standardBook.add(createOrder(new LimitPrice(100.05d), 1_500_000, "ANZ", Side.ASK))
        this.standardBook.add(createOrder(new LimitPrice(100.06d), 3_000_000, "ANZ", Side.ASK))
        this.standardBook.add(createOrder(new LimitPrice(100.09d), 1_200_000, "ANZ", Side.ASK))
        this.standardBook.add(createOrder(new LimitPrice(100.09d), 200_000, "ANZ", Side.ASK))
        this.standardBook.add(createOrder(new LimitPrice(100.10d), 2_100_000, "ANZ", Side.ASK))

        crossingBook = new OrderBook("ANZ")
        this.crossingBook.add(createOrder(new LimitPrice(100.06d), 3_100_000, "ANZ", Side.BID))
        this.crossingBook.add(createOrder(new LimitPrice(100.04d), 2_100_000, "ANZ", Side.BID))
        this.crossingBook.add(createOrder(new LimitPrice(100.02d), 1_100_000, "ANZ", Side.BID))
        this.crossingBook.add(createOrder(new LimitPrice(100.01d), 1_000_000, "ANZ", Side.BID))

        this.crossingBook.add(createOrder(new LimitPrice(100.04d), 1_500_000, "ANZ", Side.ASK))
        this.crossingBook.add(createOrder(new LimitPrice(100.05d), 3_000_000, "ANZ", Side.ASK))
        this.crossingBook.add(createOrder(new LimitPrice(100.07d), 1_200_000, "ANZ", Side.ASK))
        this.crossingBook.add(createOrder(new LimitPrice(100.10d), 2_100_000, "ANZ", Side.ASK))
    }

    def "test getVolumeEqualToOrMoreAggressiveThan"(){
        given:
        assert standardBook.getLeavesQtyEqualToOrMoreAggressiveThan(Side.BID, 100.05d) == 0
        assert standardBook.getLeavesQtyEqualToOrMoreAggressiveThan(Side.BID, 100.04d) == 3_100_000
        assert standardBook.getLeavesQtyEqualToOrMoreAggressiveThan(Side.BID, 100.03d) == 5_300_000
        assert standardBook.getLeavesQtyEqualToOrMoreAggressiveThan(Side.BID, 100.02d) == 6_400_000
        assert standardBook.getLeavesQtyEqualToOrMoreAggressiveThan(Side.BID, 100.01d) == 7_400_000
        assert standardBook.getLeavesQtyEqualToOrMoreAggressiveThan(Side.BID, 100.00d) == 7_400_000

        assert standardBook.getLeavesQtyEqualToOrMoreAggressiveThan(Side.ASK, 100.04d) ==   200_000 //Because of the market latestOrderVersion
        assert standardBook.getLeavesQtyEqualToOrMoreAggressiveThan(Side.ASK, 100.05d) == 1_700_000
        assert standardBook.getLeavesQtyEqualToOrMoreAggressiveThan(Side.ASK, 100.06d) == 4_700_000
        assert standardBook.getLeavesQtyEqualToOrMoreAggressiveThan(Side.ASK, 100.07d) == 4_700_000
        assert standardBook.getLeavesQtyEqualToOrMoreAggressiveThan(Side.ASK, 100.08d) == 4_700_000
        assert standardBook.getLeavesQtyEqualToOrMoreAggressiveThan(Side.ASK, 100.09d) == 6_100_000
        assert standardBook.getLeavesQtyEqualToOrMoreAggressiveThan(Side.ASK, 100.10d) == 8_200_000
    }

    def "test getBids"(){
        given:
        final Iterator<VersionedOrder> bids = standardBook.getBids()

        assertOrderEquals(bids.next(), new LimitPrice(100.04d), 3_100_000, "ANZ", Side.BID)
        assertOrderEquals(bids.next(), new LimitPrice(100.03d), 2_100_000, "ANZ", Side.BID)
        assertOrderEquals(bids.next(), new LimitPrice(100.03d),   100_000, "ANZ", Side.BID)
        assertOrderEquals(bids.next(), new LimitPrice(100.02d), 1_100_000, "ANZ", Side.BID)
        assertOrderEquals(bids.next(), new LimitPrice(100.01d), 1_000_000, "ANZ", Side.BID)
    }

    def "test getAsks"(){
        given:
        final Iterator<VersionedOrder> asks = standardBook.getAsks()

        assertOrderEquals(asks.next(), MarketPrice.INSTANCE,     200_000, "ANZ", Side.ASK)
        assertOrderEquals(asks.next(), new LimitPrice(100.05d), 1_500_000, "ANZ", Side.ASK)
        assertOrderEquals(asks.next(), new LimitPrice(100.06d), 3_000_000, "ANZ", Side.ASK)
        assertOrderEquals(asks.next(), new LimitPrice(100.09d), 1_200_000, "ANZ", Side.ASK)
        assertOrderEquals(asks.next(), new LimitPrice(100.09d),   200_000, "ANZ", Side.ASK)
        assertOrderEquals(asks.next(), new LimitPrice(100.10d), 2_100_000, "ANZ", Side.ASK)
    }

    def "test getMid"(){
        given:
        assert standardBook.getMid() == new LimitPrice(100.045d)
    }

    def assertOrderEquals(final VersionedOrder order, final Price price, final int qty, final String symbol, final Side side) {
        assert order.price == price
        assert order.qty == qty
        assert order.instrument == symbol
        assert order.side == side
    }

    def "test order book"(){
        given:
        println  crossingBook.toString()
    }

    def "test getPrices standard book"(){
        when:
        Collection<Double> prices = standardBook.getAllPrices()

        then:
        assert prices == [100.01, 100.02, 100.03, 100.04, 100.05, 100.06, 100.09, 100.1] as Set<Double>
    }

    def "test getPrices crossed book"(){
        when:
        Collection<Double> prices = crossingBook.getAllPrices()

        then:
        assert prices == [100.01, 100.02, 100.04, 100.05, 100.06, 100.07, 100.10] as Set<Double>
    }

    private VersionedOrder createOrder(final Price price, final long qty, final String symbol, final Side side) {
        final Id clOrdId = clOrdIdGenerator.get()
        final Id orderId = ordIdGenerator.get()

        return new MarketOrder(
                "senderCompId",
                "targetCompId",
                clOrdId,
                orderId,
                symbol,
                qty,
                price,
                dateTimeService.now(),
                dateTimeService.now(),
                side,
                dateTimeService,
                new Fix50SP2FixSpecFromClassPath().load(),
                new LoggingMessageHandler());
    }
}
