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
        final Formatter formatter = new Formatter()
        final String formattedStr = formatter.format("2018-03-19 10:12:34.001 MyThread[2] Fix:" + MsgStrings.NOS_STRING)
        println formattedStr
        assert formattedStr == "2018-03-19 10:12:34.001 CL_COMP_ID->EXCHANGE_COMP_ID \u001B[34m[NewOrderSingle]\u001B[0m [MsgType]35=D[NEWORDERSINGLE]|[SenderCompID]49=CL_COMP_ID|[TargetCompID]56=EXCHANGE_COMP_ID|[ClOrdID]11=C1000000|[Symbol]55=AUD/USD|[Side]54=2[SELL]|[TransactTime]60=20170609-10:00:00.000|[OrderQty]38=9210|[OrdType]40=1[MARKET]|61080=qwerty|60900=asdf"

    }
}
