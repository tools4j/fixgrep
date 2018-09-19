package org.tools4j.fixgrep

import org.tools4j.fix.Ascii1Char
import spock.lang.Shared

/**
 * User: benjw
 * Date: 9/19/2018
 * Time: 6:37 AM
 */
class VerticalTestUtil {
    companion object {
        val a = Ascii1Char().toString()
        val PRICES_FIX  =
        "35=X${a}" +
        "262=ABCD${a}" +
        "268=4${a}" +
        "279=0${a}" +
        "269=0${a}" +
        "9999=unknownField${a}" +
        "55=AUD/USD${a}" +
        "270=1.12345${a}" +
        "453=2${a}" +
        "448=Ben${a}" +
        "9999=unknownField${a}" +
        "447=A${a}" +
        "448=Andy${a}" +
        "279=0${a}" +
        "269=1${a}" +
        "453=2${a}" +
        "448=Amy${a}" +
        "447=A${a}" +
        "448=Milly${a}" +
        "55=AUD/USD${a}" +
        "270=1.12355${a}" +
        "279=0${a}" +
        "269=1${a}" +
        "453=2${a}" +
        "448=Amy${a}" +
        "447=A${a}" +
        "448=Milly${a}" +
        "9999=unknownField${a}" +
        "55=AUD/USD${a}" +
        "270=1.12355${a}" +
        "279=0${a}" +
        "269=0${a}" +
        "55=AUD/USD${a}" +
        "270=1.12335${a}" +
        "453=2${a}" +
        "448=Amy${a}" +
        "447=A${a}" +
        "448=Milly${a}" +
        "215=2${a}" +
        "216=3${a}" +
        "217=routingId1${a}" +
        "216=2${a}" +
        "217=routingId2${a}" +
        "1022=asdf${a}"
    }
}