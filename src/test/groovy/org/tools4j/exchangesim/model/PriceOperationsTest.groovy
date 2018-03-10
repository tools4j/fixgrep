package org.tools4j.exchangesim.model

import org.tools4j.model.LimitPrice
import org.tools4j.model.MarketPrice
import org.tools4j.model.Price
import org.tools4j.model.PriceOperations
import org.tools4j.model.Side
import spock.lang.Specification
import spock.lang.Unroll

import java.util.function.Function

/**
 * User: ben
 * Date: 25/10/2016
 * Time: 6:38 AM
 */
class PriceOperationsTest extends Specification {
    private static Function<BigDecimal, Double> toDouble = new Function<BigDecimal, Double>() {
        @Override
        Double apply(final BigDecimal aDouble) {
            return aDouble.doubleValue()
        }

        @Override
        public String toString(){
            return "Double"
        }
    }

    private static Function<BigDecimal, Price> toPrice = new Function<BigDecimal, Price>() {
        @Override
        Price apply(final BigDecimal aDouble) {
            return new LimitPrice(aDouble.doubleValue())
        }

        @Override
        public String toString(){
            return "String"
        }
    }

    @Unroll
    def "test market price on left #rightModifier"(final Side side, final Function<BigDecimal,?> rightModifier) {
        final PriceOperations po = side.priceOperations
        assert po.isPrice(MarketPrice.INSTANCE).equalToOrMoreAggressiveThan(rightModifier.apply(100.05))
        assert po.isPrice(MarketPrice.INSTANCE).moreAggressiveThan(rightModifier.apply(100.05))
        assert !po.isPrice(MarketPrice.INSTANCE).equalToOrMorePassiveThan(rightModifier.apply(100.05))
        assert !po.isPrice(MarketPrice.INSTANCE).morePassiveThan(rightModifier.apply(100.05))

        where:
        side | rightModifier
        Side.BID | toDouble
        Side.BID | toPrice
        Side.ASK | toDouble
        Side.ASK | toPrice
    }

    @Unroll
    def "test market price on right #leftModifier"(final Side side, final Function<BigDecimal,?> leftModifier) {
        final PriceOperations po = side.priceOperations
        assert !po.isPrice(leftModifier.apply(100.05)).equalToOrMoreAggressiveThan(MarketPrice.INSTANCE)
        assert !po.isPrice(leftModifier.apply(100.05)).moreAggressiveThan(MarketPrice.INSTANCE)
        assert po.isPrice(leftModifier.apply(100.05)).equalToOrMorePassiveThan(MarketPrice.INSTANCE)
        assert po.isPrice(leftModifier.apply(100.05)).morePassiveThan(MarketPrice.INSTANCE)

        where:
        side | leftModifier
        Side.BID | toDouble
        Side.BID | toPrice
        Side.ASK | toDouble
        Side.ASK | toPrice
    }

    @Unroll
    def "test market price on both sides #side"(final Side side) {
        final PriceOperations po = side.priceOperations
        assert po.isPrice(MarketPrice.INSTANCE).equalToOrMoreAggressiveThan(MarketPrice.INSTANCE)
        assert !po.isPrice(MarketPrice.INSTANCE).moreAggressiveThan(MarketPrice.INSTANCE)
        assert po.isPrice(MarketPrice.INSTANCE).equalToOrMorePassiveThan(MarketPrice.INSTANCE)
        assert !po.isPrice(MarketPrice.INSTANCE).morePassiveThan(MarketPrice.INSTANCE)

        where:
        side | _
        Side.BID | _
        Side.ASK | _
    }


        @Unroll
    def "test BUY #leftModifier:#rightModifier"(Function<BigDecimal,?> leftModifier, Function<BigDecimal,?> rightModifier) {
        final PriceOperations po = Side.BID.priceOperations
        assert po.isPrice(leftModifier.apply(100.05)).equalToOrMoreAggressiveThan(rightModifier.apply(100.05))
        assert po.isPrice(leftModifier.apply(100.05)).equalToOrMoreAggressiveThan(rightModifier.apply(100.01))
        assert po.isPrice(leftModifier.apply(100.05)).equalToOrMoreAggressiveThan(rightModifier.apply(0))
        assert po.isPrice(leftModifier.apply(100.05)).equalToOrMoreAggressiveThan(rightModifier.apply(-100))
        assert !po.isPrice(leftModifier.apply(100.05)).equalToOrMoreAggressiveThan(rightModifier.apply(100.06))

        assert !po.isPrice(leftModifier.apply(100.05)).moreAggressiveThan(rightModifier.apply(100.05))
        assert po.isPrice(leftModifier.apply(100.05)).moreAggressiveThan(rightModifier.apply(100.01))
        assert po.isPrice(leftModifier.apply(100.05)).moreAggressiveThan(rightModifier.apply(0))
        assert po.isPrice(leftModifier.apply(100.05)).moreAggressiveThan(rightModifier.apply(-100))
        assert !po.isPrice(leftModifier.apply(100.05)).moreAggressiveThan(rightModifier.apply(100.06))

        assert po.isPrice(leftModifier.apply(100.05)).equalToOrMorePassiveThan(rightModifier.apply(100.05))
        assert po.isPrice(leftModifier.apply(100.05)).equalToOrMorePassiveThan(rightModifier.apply(100.06))
        assert !po.isPrice(leftModifier.apply(100.05)).equalToOrMorePassiveThan(rightModifier.apply(0))
        assert !po.isPrice(leftModifier.apply(100.05)).equalToOrMorePassiveThan(rightModifier.apply(-100))

        assert !po.isPrice(leftModifier.apply(100.05)).morePassiveThan(rightModifier.apply(100.05))
        assert po.isPrice(leftModifier.apply(100.05)).morePassiveThan(rightModifier.apply(100.06))
        assert !po.isPrice(leftModifier.apply(100.05)).morePassiveThan(rightModifier.apply(0))
        assert !po.isPrice(leftModifier.apply(100.05)).morePassiveThan(rightModifier.apply(-100))
        
        where:
        leftModifier  |  rightModifier
        toDouble      |  toDouble
        toPrice       |  toPrice
        toDouble      |  toPrice
        toPrice       |  toDouble
    }


    @Unroll
    def "test SELL #leftModifier:#rightModifier"(Function<BigDecimal,?> leftModifier, Function<BigDecimal,?> rightModifier) {
        final PriceOperations po = Side.ASK.priceOperations
        assert po.isPrice(leftModifier.apply(100.05)).equalToOrMorePassiveThan(rightModifier.apply(100.05))
        assert po.isPrice(leftModifier.apply(100.05)).equalToOrMorePassiveThan(rightModifier.apply(100.01))
        assert po.isPrice(leftModifier.apply(100.05)).equalToOrMorePassiveThan(rightModifier.apply(0))
        assert po.isPrice(leftModifier.apply(100.05)).equalToOrMorePassiveThan(rightModifier.apply(-100))
        assert !po.isPrice(leftModifier.apply(100.05)).equalToOrMorePassiveThan(rightModifier.apply(100.06))

        assert !po.isPrice(leftModifier.apply(100.05)).morePassiveThan(rightModifier.apply(100.05))
        assert po.isPrice(leftModifier.apply(100.05)).morePassiveThan(rightModifier.apply(100.01))
        assert po.isPrice(leftModifier.apply(100.05)).morePassiveThan(rightModifier.apply(0))
        assert po.isPrice(leftModifier.apply(100.05)).morePassiveThan(rightModifier.apply(-100))
        assert !po.isPrice(leftModifier.apply(100.05)).morePassiveThan(rightModifier.apply(100.06))

        assert po.isPrice(leftModifier.apply(100.05)).equalToOrMoreAggressiveThan(rightModifier.apply(100.05))
        assert po.isPrice(leftModifier.apply(100.05)).equalToOrMoreAggressiveThan(rightModifier.apply(100.06))
        assert !po.isPrice(leftModifier.apply(100.05)).equalToOrMoreAggressiveThan(rightModifier.apply(0))
        assert !po.isPrice(leftModifier.apply(100.05)).equalToOrMoreAggressiveThan(rightModifier.apply(-100))

        assert !po.isPrice(leftModifier.apply(100.05)).moreAggressiveThan(rightModifier.apply(100.05))
        assert po.isPrice(leftModifier.apply(100.05)).moreAggressiveThan(rightModifier.apply(100.06))
        assert !po.isPrice(leftModifier.apply(100.05)).moreAggressiveThan(rightModifier.apply(0))
        assert !po.isPrice(leftModifier.apply(100.05)).moreAggressiveThan(rightModifier.apply(-100))

        where:
        leftModifier  |  rightModifier
        toDouble      |  toDouble
        toPrice       |  toPrice
        toDouble      |  toPrice
        toPrice       |  toDouble
    }
}
