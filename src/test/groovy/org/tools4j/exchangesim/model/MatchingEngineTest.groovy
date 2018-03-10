package org.tools4j.exchangesim.model

import org.codehaus.groovy.runtime.NullObject
import org.tools4j.exhangesim.model.Match
import org.tools4j.exhangesim.model.MatchResults
import org.tools4j.exhangesim.model.OrderBook
import org.tools4j.fix.Fix50SP2FixSpecFromClassPath
import org.tools4j.fix.FixSpec
import org.tools4j.model.*
import org.tools4j.model.fix.messages.LoggingMessageHandler
import spock.lang.Specification

import static org.tools4j.groovytables.GroovyTables.withTable

/**
 * User: ben
 * Date: 4/11/2016
 * Time: 5:26 PM
 */
class MatchingEngineTest extends Specification {
    private OrderBook standardBook
    private OrderBook crossingBookWithMarketAskPrice
    private OrderBook emptyBook
    private OrderBook bookWithOnlyBids
    private OrderBook bookWithOnlyAsks
    private DateTimeService dateTimeService;
    private IdGenerator ordIdGenerator = new IdGenerator("O")
    private IdGenerator clOrdIdGenerator = new IdGenerator("C")
    private Dispatching

    def setup() {
        dateTimeService = new DateTimeService();

        standardBook = new OrderBook("ANZ")
        withTable {
            price  | qty       | symbol| side
            100.01 | 1_000_000 | "ANZ" | Side.BID
            100.02 | 1_100_000 | "ANZ" | Side.BID
            100.03 | 2_100_000 | "ANZ" | Side.BID
            100.04 | 3_100_000 | "ANZ" | Side.BID

            100.05 | 1_500_000 | "ANZ" | Side.ASK
            100.06 | 3_000_000 | "ANZ" | Side.ASK
            100.09 | 1_200_000 | "ANZ" | Side.ASK
            100.10 | 2_100_000 | "ANZ" | Side.ASK
        }.execute {
            standardBook.add(createOrder(new LimitPrice(price), qty, symbol, side))
        }

        crossingBookWithMarketAskPrice = new OrderBook("ANZ")
        withTable {
            price  | qty       | symbol| side
            100.06 | 3_100_000 | "ANZ" | Side.BID
            100.05 |   100_000 | "ANZ" | Side.BID
            100.04 | 2_100_000 | "ANZ" | Side.BID
            100.02 | 1_100_000 | "ANZ" | Side.BID
            100.01 | 1_000_000 | "ANZ" | Side.BID

            null   |   500_000 | "ANZ" | Side.ASK
            100.04 | 1_500_000 | "ANZ" | Side.ASK
            100.05 | 3_000_000 | "ANZ" | Side.ASK
            100.06 |   100_000 | "ANZ" | Side.ASK
            100.07 | 1_200_000 | "ANZ" | Side.ASK
            100.10 | 2_100_000 | "ANZ" | Side.ASK
        }.execute {
            final Price priceObj = (price instanceof NullObject) ? MarketPrice.INSTANCE : new LimitPrice(price)
            crossingBookWithMarketAskPrice.add(createOrder(priceObj, qty, symbol, side))
        }

        emptyBook = new OrderBook("ANZ")

        bookWithOnlyBids = new OrderBook("ANZ")
        withTable {
            price  | qty       | symbol| side
            100.06 | 3_100_000 | "ANZ" | Side.BID
            100.05 |   100_000 | "ANZ" | Side.BID
            100.04 | 2_100_000 | "ANZ" | Side.BID
            100.02 | 1_100_000 | "ANZ" | Side.BID
            100.01 | 1_000_000 | "ANZ" | Side.BID
        }.execute {
            bookWithOnlyBids.add(createOrder(new LimitPrice(price), qty, symbol, side))
        }

        bookWithOnlyAsks = new OrderBook("ANZ")
        withTable {
            price  | qty       | symbol| side
            100.06 | 3_100_000 | "ANZ" | Side.ASK
            100.05 |   100_000 | "ANZ" | Side.ASK
            100.04 | 2_100_000 | "ANZ" | Side.ASK
            100.02 | 1_100_000 | "ANZ" | Side.ASK
            100.01 | 1_000_000 | "ANZ" | Side.ASK
        }.execute {
            bookWithOnlyAsks.add(createOrder(new LimitPrice(price), qty, symbol, side))
        }
    }

    def "non crossing book"() {
        when:
        final MatchResults matchResults = standardBook.match()
        println  matchResults

        then:
        assert matchResults.isEmpty()
    }

    def "crossing book"() {
        given:

        final OrderBook crossingBook = new OrderBook("ANZ")
        withTable {
           id | price  | qty       | symbol| side
            1 | 100.06 | 3_100_000 | "ANZ" | Side.BID
            2 | 100.05 |   100_000 | "ANZ" | Side.BID
            3 | 100.04 | 2_100_000 | "ANZ" | Side.BID
            4 | 100.02 | 1_100_000 | "ANZ" | Side.BID
            5 | 100.01 | 1_000_000 | "ANZ" | Side.BID

            6 | 100.04 | 1_500_000 | "ANZ" | Side.ASK
            7 | 100.05 | 3_000_000 | "ANZ" | Side.ASK
            8 | 100.06 |   100_000 | "ANZ" | Side.ASK
            9 | 100.07 | 1_200_000 | "ANZ" | Side.ASK
           10 | 100.10 | 2_100_000 | "ANZ" | Side.ASK
        }.execute {
            crossingBook.add(createOrder(clOrdIdGenerator.get(), new SimpleId(""+id), new LimitPrice(price), qty, symbol, side))
        }

        //Run matching
        final MatchResults matchResults = crossingBook.match()
        final Iterator<Match> matches = matchResults.iterator()

        withTable {
            matchedPrice  | matchedQty | bidId | askId
                  100.05  | 1_500_000  |     1 |    6
                  100.055 | 1_600_000  |     1 |    7
                  100.05  |   100_000  |     2 |    7
        }.execute {
            final Match match = matches.next()
            assert match.price == new LimitPrice(matchedPrice)
            assert match.qty == matchedQty
            assert match.ask.orderId == new SimpleId(""+askId)
            assert match.bid.orderId == new SimpleId(""+bidId)
        }
        assert !matches.hasNext(): "Unasserted matches still exist: " + matches.collect {it.toString() + "\n"}

        //Assert remaining book
        final Iterator<OrderUnderExecution> bids = crossingBook.getBids()
        final Iterator<OrderUnderExecution> asks = crossingBook.getAsks()

        withTable {
            id | price  | qty       | symbol| side
            3 | 100.04 | 2_100_000 | "ANZ" | Side.BID
            4 | 100.02 | 1_100_000 | "ANZ" | Side.BID
            5 | 100.01 | 1_000_000 | "ANZ" | Side.BID

            7 | 100.05 | 1_300_000 | "ANZ" | Side.ASK
            8 | 100.06 |   100_000 | "ANZ" | Side.ASK
            9 | 100.07 | 1_200_000 | "ANZ" | Side.ASK
           10 | 100.10 | 2_100_000 | "ANZ" | Side.ASK
        }.execute {
            final OrderUnderExecution order;
            if(side == Side.BID){
                order = bids.next()
            } else {
                order = asks.next()
            }
            assert order.orderId == new SimpleId(id)
            assert order.price == new LimitPrice(price)
            assert order.leavesQty == qty
            assert order.instrument == symbol
            assert order.side == side
        }
        assert !bids.hasNext(): "Unasserted bids still exist " + bids.collect {it.toString() + "\n"}
        assert !asks.hasNext(): "Unasserted bids still exist " + asks.collect {it.toString() + "\n"}
    }

    def "crossing book - with market prices"() {
        given:

        final OrderBook crossingBook = new OrderBook("ANZ")
        final double market = 0.0
        withTable {
           id | price  | qty       | symbol| side
            1 | market |    90_000 | "ANZ" | Side.BID
            2 | market | 1_000_000 | "ANZ" | Side.BID
            3 | 100.05 |   100_000 | "ANZ" | Side.BID
            4 | 100.04 | 2_100_000 | "ANZ" | Side.BID
            5 | 100.02 | 1_100_000 | "ANZ" | Side.BID
            6 | 100.01 | 1_000_000 | "ANZ" | Side.BID

            7 | market |    60_000 | "ANZ" | Side.ASK
            8 | market |    80_000 | "ANZ" | Side.ASK
            9 | 100.04 | 1_500_000 | "ANZ" | Side.ASK
           10 | 100.05 | 3_000_000 | "ANZ" | Side.ASK
           11 | 100.06 |   100_000 | "ANZ" | Side.ASK
           12 | 100.07 | 1_200_000 | "ANZ" | Side.ASK
           13 | 100.10 | 2_100_000 | "ANZ" | Side.ASK
        }.execute {
            final Price priceObj = price == market ? MarketPrice.INSTANCE: new LimitPrice(price)
            crossingBook.add(createOrder(clOrdIdGenerator.get(), new SimpleId(""+id), priceObj, qty, symbol, side))
        }

        //Run matching
        final MatchResults matchResults = crossingBook.match()
        final Iterator<Match> matches = matchResults.iterator()

        withTable {
            matchedPrice  | matchedQty | bidId | askId
            100.045       |    60_000  |     1 |    7
            100.045       |    30_000  |     1 |    8
            100.045       |    50_000  |     2 |    8
            100.045       |   950_000  |     2 |    9
            100.045       |   100_000  |     3 |    9
            100.040       |   450_000  |     4 |    9
        }.execute {
            final Match match = matches.next()
            assert match.price == new LimitPrice(matchedPrice)
            assert match.qty == matchedQty
            assert match.ask.orderId == new SimpleId(""+askId)
            assert match.bid.orderId == new SimpleId(""+bidId)
        }
        assert !matches.hasNext(): "Unasserted matches still exist: " + matches.collect {it.toString() + "\n"}

        //Assert remaining book
        final Iterator<OrderUnderExecution> bids = crossingBook.getBids()
        final Iterator<OrderUnderExecution> asks = crossingBook.getAsks()

        withTable {
            id | price  | qty       | symbol| side
            4 | 100.04 | 1_650_000 | "ANZ" | Side.BID
            5 | 100.02 | 1_100_000 | "ANZ" | Side.BID
            6 | 100.01 | 1_000_000 | "ANZ" | Side.BID

            10 | 100.05 | 3_000_000 | "ANZ" | Side.ASK
            11 | 100.06 |   100_000 | "ANZ" | Side.ASK
            12 | 100.07 | 1_200_000 | "ANZ" | Side.ASK
            13 | 100.10 | 2_100_000 | "ANZ" | Side.ASK
        }.execute {
            final OrderUnderExecution order;
            if(side == Side.BID){
                order = bids.next()
            } else {
                order = asks.next()
            }
            assert order.orderId == new SimpleId("" + id)
            assert order.price == new LimitPrice(price)
            assert order.leavesQty == qty
            assert order.instrument == symbol
            assert order.side == side
        }
        assert !bids.hasNext(): "Unasserted bids still exist " + bids.collect {it.toString() + "\n"}
        assert !asks.hasNext(): "Unasserted bids still exist " + asks.collect {it.toString() + "\n"}
    }

    private VersionedOrder createOrder(final Price price, final long qty, final String symbol, final Side side) {
        final Id clOrdId = clOrdIdGenerator.get()
        final Id orderId = ordIdGenerator.get()
        return createOrder(clOrdId, orderId, price, qty, symbol, side)
    }

    private VersionedOrder createOrder(Id clOrdId, Id orderId, final Price price, final long qty, final String symbol, final Side side) {
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
