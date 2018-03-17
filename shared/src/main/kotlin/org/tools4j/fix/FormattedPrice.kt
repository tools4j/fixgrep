package org.tools4j.fix

import org.tools4j.fix.Price
import org.tools4j.fix.Side

/**
 * User: ben
 * Date: 18/8/17
 * Time: 6:32 AM
 */
class FormattedPrice(private val price: Price, private val side: Side) {
    override fun toString(): String {
        return if (price.hasPrice()) {
            side.roundDoubleOutwards(price.get()).toString()
        } else {
            "MARKET"
        }
    }
}
