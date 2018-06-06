package org.tools4j.fixgrep

import org.tools4j.fix.FieldsSource
import spock.lang.Specification

/**
 * User: ben
 * Date: 26/03/2018
 * Time: 6:34 AM
 */
class FormatterTest extends Specification {
    def "Format"() {
        expect:
        final Formatter formatter = new Formatter(new FormatSpec())
        final String formattedStr = formatter.format("2018-03-19 10:12:34.001 MyThread[2] Fix:" + MsgStrings.NOS_STRING)
        println formattedStr
        assert formattedStr == "CL_COMP_ID->EXCHANGE_COMP_ID \u001B[34;1m[NewOrderSingle]\u001b[0m [MsgType]\u001B[1m35\u001b[0m\u001B[1m=\u001b[0m\u001B[1mD\u001b[0m[NEWORDERSINGLE]|[SenderCompID]\u001B[1m49\u001b[0m\u001B[1m=\u001b[0m\u001B[1mCL_COMP_ID\u001b[0m|[TargetCompID]\u001B[1m56\u001b[0m\u001B[1m=\u001b[0m\u001B[1mEXCHANGE_COMP_ID\u001b[0m|[ClOrdID]\u001B[1m11\u001b[0m\u001B[1m=\u001b[0m\u001B[1mC1000000\u001b[0m|[Symbol]\u001B[1m55\u001b[0m\u001B[1m=\u001b[0m\u001B[1mAUD/USD\u001b[0m|[Side]\u001B[1m54\u001b[0m\u001B[1m=\u001b[0m\u001B[1m2\u001b[0m[SELL]|[TransactTime]\u001B[1m60\u001b[0m\u001B[1m=\u001b[0m\u001B[1m20170609-10:00:00.000\u001b[0m|[OrderQty]\u001B[1m38\u001b[0m\u001B[1m=\u001b[0m\u001B[1m9210\u001b[0m|[OrdType]\u001B[1m40\u001b[0m\u001B[1m=\u001b[0m\u001B[1m1\u001b[0m[MARKET]|\u001B[1m61080\u001b[0m\u001B[1m=\u001b[0m\u001B[1mqwerty\u001b[0m|\u001B[1m60900\u001b[0m\u001B[1m=\u001b[0m\u001B[1masdf\u001b[0m"
    }
}
