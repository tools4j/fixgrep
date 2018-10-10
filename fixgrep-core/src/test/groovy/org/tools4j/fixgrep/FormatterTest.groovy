package org.tools4j.fixgrep

import spock.lang.Specification

/**
 * User: ben
 * Date: 26/03/2018
 * Time: 6:34 AM
 */
class FormatterTest extends Specification {
    def "Format"() {
        expect:
        final WrappedFormatter formatter = new WrappedFormatter(new FormatSpec())
        final String formattedStr = formatter.format("2018-03-19 10:12:34.001 MyThread[2] Fix:" + MsgStrings.NOS_STRING)
        println formattedStr
        assert formattedStr == "CL_COMP_ID->EXCHANGE_COMP_ID \u001B[36m[NewOrderSingle]\u001B[0m [MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1mD\u001B[22m[NEWORDERSINGLE]|[SenderCompID]\u001B[1m49\u001B[22m\u001B[1m=\u001B[22m\u001B[1mCL_COMP_ID\u001B[22m|[TargetCompID]\u001B[1m56\u001B[22m\u001B[1m=\u001B[22m\u001B[1mEXCHANGE_COMP_ID\u001B[22m|[ClOrdID]\u001B[1m11\u001B[22m\u001B[1m=\u001B[22m\u001B[1mC1000000\u001B[22m|[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[22m|[Side]\u001B[1m54\u001B[22m\u001B[1m=\u001B[22m\u001B[1m2\u001B[22m[SELL]|[TransactTime]\u001B[1m60\u001B[22m\u001B[1m=\u001B[22m\u001B[1m20170609-10:00:00.000\u001B[22m|[OrderQty]\u001B[1m38\u001B[22m\u001B[1m=\u001B[22m\u001B[1m9210\u001B[22m|[OrdType]\u001B[1m40\u001B[22m\u001B[1m=\u001B[22m\u001B[1m1\u001B[22m[MARKET]|\u001B[1m61080\u001B[22m\u001B[1m=\u001B[22m\u001B[1mqwerty\u001B[22m|\u001B[1m60900\u001B[22m\u001B[1m=\u001B[22m\u001B[1masdf\u001B[22m"
    }

    def "Format with delimiter at end of string"() {
        expect:
        final WrappedFormatter formatter = new WrappedFormatter(new FormatSpec())
        final String formattedStr = formatter.format("2018-03-19 10:12:34.001 MyThread[2] Fix:" + MsgStrings.NOS_STRING + "\u0001")
        println formattedStr
       assert formattedStr == "CL_COMP_ID->EXCHANGE_COMP_ID \u001B[36m[NewOrderSingle]\u001B[0m [MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1mD\u001B[22m[NEWORDERSINGLE]|[SenderCompID]\u001B[1m49\u001B[22m\u001B[1m=\u001B[22m\u001B[1mCL_COMP_ID\u001B[22m|[TargetCompID]\u001B[1m56\u001B[22m\u001B[1m=\u001B[22m\u001B[1mEXCHANGE_COMP_ID\u001B[22m|[ClOrdID]\u001B[1m11\u001B[22m\u001B[1m=\u001B[22m\u001B[1mC1000000\u001B[22m|[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[22m|[Side]\u001B[1m54\u001B[22m\u001B[1m=\u001B[22m\u001B[1m2\u001B[22m[SELL]|[TransactTime]\u001B[1m60\u001B[22m\u001B[1m=\u001B[22m\u001B[1m20170609-10:00:00.000\u001B[22m|[OrderQty]\u001B[1m38\u001B[22m\u001B[1m=\u001B[22m\u001B[1m9210\u001B[22m|[OrdType]\u001B[1m40\u001B[22m\u001B[1m=\u001B[22m\u001B[1m1\u001B[22m[MARKET]|\u001B[1m61080\u001B[22m\u001B[1m=\u001B[22m\u001B[1mqwerty\u001B[22m|\u001B[1m60900\u001B[22m\u001B[1m=\u001B[22m\u001B[1masdf\u001B[22m"
    }
}
