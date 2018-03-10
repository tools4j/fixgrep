package org.tools4j.exchangesim.model

import org.tools4j.model.LimitPrice
import org.tools4j.model.LimitPriceWithRoundedFloatingPointError
import spock.lang.Specification

/**
 * User: ben
 * Date: 11/01/2017
 * Time: 5:28 PM
 */
class LimitPriceWithRoundedFloatingPointErrorTest extends Specification {
    def "RoundFloatingPointErrorFromPrice"() {
        expect:
        LimitPriceWithRoundedFloatingPointError
        assert new LimitPriceWithRoundedFloatingPointError(new LimitPrice(0.05d)) == new LimitPrice(0.05d)
        assert new LimitPriceWithRoundedFloatingPointError(new LimitPrice(0.05000000001d)) == new LimitPrice(0.05d)
        assert new LimitPriceWithRoundedFloatingPointError(new LimitPrice(0.0d)) == new LimitPrice(0.0d)
    }
}
