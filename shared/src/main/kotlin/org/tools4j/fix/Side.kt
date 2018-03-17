package org.tools4j.fix

import java.math.BigDecimal
import java.math.RoundingMode

/**
 * User: ben
 * Date: 5/06/2014
 * Time: 5:54 AM
 */
enum class Side private constructor(
        private val roundingMode: RoundingMode,
        private val priceAsDoubleComparator: Price.PriceAsDoubleComparator,
        val code: Int,
        private val noun: String,
        private val verb: String,
        private val presentParticiple: String) {

    BID(RoundingMode.FLOOR, Price.PriceAsDoubleComparator.sortHighToLow(), 1, "Bid", "Buy", "Buying"),
    ASK(RoundingMode.CEILING, Price.PriceAsDoubleComparator.sortLowToHigh(), 2, "Ask", "Sell", "Selling");

    val priceOperations: PriceOperations

    val isBuy: Boolean
        get() = this == BID

    internal val isSell: Boolean
        get() = this == ASK

    init {
        this.priceOperations = PriceOperations(this, priceAsDoubleComparator)
    }

    fun round(value: Double): BigDecimal {
        return roundDoubleOutwards(value)
    }

    fun roundDoubleOutwards(doublePrice: Double): BigDecimal {
        return roundBigDecimalOutwards(roundOffNoise(BigDecimal.valueOf(doublePrice)))
    }

    internal fun roundBigDecimalOutwards(price: BigDecimal): BigDecimal {
        return price.setScale(Quote.PRICE_PRECISION, roundingMode)
    }

    companion object {
        internal fun roundOffNoise(price: BigDecimal): BigDecimal {
            return price.setScale(Quote.NOISE_ROUNDING_PRECISION, RoundingMode.HALF_UP)
        }

        val codeToSide: Map<Int, Side> by lazy {
            val map = HashMap<Int, Side>()
            Side.values().forEach { map.put(it.code, it) }
            map
        }

        fun forCode(code: Int): Side {
            return codeToSide[code]!!
        }
    }
}
