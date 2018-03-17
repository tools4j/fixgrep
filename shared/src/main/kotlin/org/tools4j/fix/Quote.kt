package org.tools4j.fix

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * User: ben
 * Date: 15/04/2016
 * Time: 5:16 PM
 */
class Quote(
        val venue: Venue,
        override val price: Price,
        val qty: Long) : Priceable {

    internal fun getFormattedPrice(side: Side): String {
        return side.roundDoubleOutwards(price.get()).toString() + ""
    }


    internal fun toString(side: Side): String {
        return "\$venue[\$qty@\${getFormattedPrice(side)}]"
    }

    internal fun toKdbInsertQuery(side: Side, symbol: String, localDateTime: LocalDateTime): String {
        return "`quotes insert (\${KDB_DATE_FORMATTER.format(localDateTime)};`\$instrument;`\$side;\$price;\$qty;`\$venue)"
    }

    companion object {
        internal val PRICE_PRECISION = 3
        internal val NOISE_ROUNDING_PRECISION = 10
        val KDB_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd'D'HH:mm:ss.n")
    }
}
