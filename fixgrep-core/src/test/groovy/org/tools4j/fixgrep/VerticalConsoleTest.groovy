package org.tools4j.fixgrep

import org.tools4j.fix.Ascii1Char
import org.tools4j.util.CircularBufferedReaderWriter
import org.tools4j.utils.ArgsAsString
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

/**
 * User: ben
 * Date: 12/03/2018
 * Time: 6:55 AM
 */
class VerticalConsoleTest extends Specification {
    @Shared private final static String a = new Ascii1Char().toString()
    @Shared private String testOverrides;

    private static File newAssertionsFile = new File("new-assertions.txt")
    private static File resultsFile = new File("results.txt")
    private static boolean logResultsToFile = true;
    private static boolean logNewAssertionsToFile = true;

    def setupSpec() {
        if(logNewAssertionsToFile) deleteAndCreateNewFile(newAssertionsFile)
        if(logResultsToFile) deleteAndCreateNewFile(resultsFile)
        testOverrides = ' -V -p'
    }

    def 'test vertical aligned format'(){
        when:
        final String fix = "35=D${a}11=ABC${a}55=AUD/USD\n35=8${a}150=F${a}55=AUD/USD"
        def lines = parseToLines('-A', fix)

        then:
        assert lines == """================================================================================
\u001B[36mNewOrderSingle\u001B[0m
================================================================================
 [MsgType]\u001B[1m35\u001B[22m \u001B[1m=\u001B[22m \u001B[1mD\u001B[22m[NEWORDERSINGLE] 
 [ClOrdID]\u001B[1m11\u001B[22m \u001B[1m=\u001B[22m \u001B[1mABC\u001B[22m               
  [Symbol]\u001B[1m55\u001B[22m \u001B[1m=\u001B[22m \u001B[1mAUD/USD\u001B[22m           


================================================================================
\u001B[32mExec.Trade\u001B[0m
================================================================================
   [MsgType]\u001B[1m35\u001B[22m \u001B[1m=\u001B[22m \u001B[1m8\u001B[22m[EXECUTIONREPORT]            
 [ExecType]\u001B[1m150\u001B[22m \u001B[1m=\u001B[22m \u001B[1mF\u001B[22m[TRADE_PARTIAL_FILL_OR_FILL] 
    [Symbol]\u001B[1m55\u001B[22m \u001B[1m=\u001B[22m \u001B[1mAUD/USD\u001B[22m                       

"""
    }


    def 'test vertical aligned format - highlighted field'(){
        when:
        final String fix = "35=D${a}11=ABC${a}55=AUD/USD\n35=8${a}150=F${a}55=AUD/USD"
        def lines = parseToLines('-A -h 35', fix)

        then:
        assert lines == """================================================================================
\u001B[36mNewOrderSingle\u001B[0m
================================================================================
\u001B[31m [MsgType]\u001B[1m35\u001B[22m \u001B[1m=\u001B[22m \u001B[1mD\u001B[22m[NEWORDERSINGLE] \u001B[0m
 [ClOrdID]\u001B[1m11\u001B[22m \u001B[1m=\u001B[22m \u001B[1mABC\u001B[22m               
  [Symbol]\u001B[1m55\u001B[22m \u001B[1m=\u001B[22m \u001B[1mAUD/USD\u001B[22m           


================================================================================
\u001B[32mExec.Trade\u001B[0m
================================================================================
\u001B[31m   [MsgType]\u001B[1m35\u001B[22m \u001B[1m=\u001B[22m \u001B[1m8\u001B[22m[EXECUTIONREPORT]            \u001B[0m
 [ExecType]\u001B[1m150\u001B[22m \u001B[1m=\u001B[22m \u001B[1mF\u001B[22m[TRADE_PARTIAL_FILL_OR_FILL] 
    [Symbol]\u001B[1m55\u001B[22m \u001B[1m=\u001B[22m \u001B[1mAUD/USD\u001B[22m                       

"""
    }

    def 'test vertical aligned format - highlighted message'(){
        when:
        final String fix = "35=D${a}11=ABC${a}55=AUD/USD\n35=8${a}150=F${a}55=AUD/USD"
        def lines = parseToLines('-A -h 35:Msg', fix)

        then:
        assert lines == """================================================================================
\u001B[36mNewOrderSingle\u001B[0m
================================================================================
\u001B[31m [MsgType]\u001B[1m35\u001B[22m \u001B[1m=\u001B[22m \u001B[1mD\u001B[22m[NEWORDERSINGLE] \u001B[0m
\u001B[31m [ClOrdID]\u001B[1m11\u001B[22m \u001B[1m=\u001B[22m \u001B[1mABC\u001B[22m               \u001B[0m
\u001B[31m  [Symbol]\u001B[1m55\u001B[22m \u001B[1m=\u001B[22m \u001B[1mAUD/USD\u001B[22m           \u001B[0m


================================================================================
\u001B[32mExec.Trade\u001B[0m
================================================================================
\u001B[31m   [MsgType]\u001B[1m35\u001B[22m \u001B[1m=\u001B[22m \u001B[1m8\u001B[22m[EXECUTIONREPORT]            \u001B[0m
\u001B[31m [ExecType]\u001B[1m150\u001B[22m \u001B[1m=\u001B[22m \u001B[1mF\u001B[22m[TRADE_PARTIAL_FILL_OR_FILL] \u001B[0m
\u001B[31m    [Symbol]\u001B[1m55\u001B[22m \u001B[1m=\u001B[22m \u001B[1mAUD/USD\u001B[22m                       \u001B[0m

"""
    }

    def 'test vertical non-aligned format'(){
        when:
        final String fix = "35=D${a}11=ABC${a}55=AUD/USD\n35=8${a}150=F${a}55=AUD/USD"
        def lines = parseToLines('', fix)

        then:
        assert lines == """================================================================================
\u001B[36mNewOrderSingle\u001B[0m
================================================================================
[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1mD\u001B[22m[NEWORDERSINGLE]
[ClOrdID]\u001B[1m11\u001B[22m\u001B[1m=\u001B[22m\u001B[1mABC\u001B[22m
[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[22m

================================================================================
\u001B[32mExec.Trade\u001B[0m
================================================================================
[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1m8\u001B[22m[EXECUTIONREPORT]
[ExecType]\u001B[1m150\u001B[22m\u001B[1m=\u001B[22m\u001B[1mF\u001B[22m[TRADE_PARTIAL_FILL_OR_FILL]
[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[22m
"""
    }

    def 'test vertical non-aligned format - indentGroupRepeats - prices'(){
        when:
        final String fix =
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
            "215=2${a}" + //NoRoutingIDs
            "216=3${a}" + //RoutingType
            "217=routingId1${a}" + //RoutingID
            "216=2${a}" + //RoutingType
            "217=routingId2${a}" + //RoutingID
            "1022=asdf${a}\n" +

            "35=D${a}" +
            "11=ABC${a}" +
            "55=AUD/USD"

        def lines = parseToLines('', fix)

        then:
        assert lines == """================================================================================
\u001B[33mMarketDataIncrementalRefresh\u001B[0m
================================================================================
[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1mX\u001B[22m[MARKETDATAINCREMENTALREFRESH]
[MDReqID]\u001B[1m262\u001B[22m\u001B[1m=\u001B[22m\u001B[1mABCD\u001B[22m
[NoMDEntries]\u001B[1m268\u001B[22m\u001B[1m=\u001B[22m\u001B[1m4\u001B[22m
    1.  [MDUpdateAction]\u001B[1m279\u001B[22m\u001B[1m=\u001B[22m\u001B[1m0\u001B[22m[NEW]
        [MDEntryType]\u001B[1m269\u001B[22m\u001B[1m=\u001B[22m\u001B[1m0\u001B[22m[BID]
        \u001B[1m9999\u001B[22m\u001B[1m=\u001B[22m\u001B[1munknownField\u001B[22m
        [Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[22m
        [MDEntryPx]\u001B[1m270\u001B[22m\u001B[1m=\u001B[22m\u001B[1m1.12345\u001B[22m
        [NoPartyIDs]\u001B[1m453\u001B[22m\u001B[1m=\u001B[22m\u001B[1m2\u001B[22m
            1.  [PartyID]\u001B[1m448\u001B[22m\u001B[1m=\u001B[22m\u001B[1mBen\u001B[22m
                \u001B[1m9999\u001B[22m\u001B[1m=\u001B[22m\u001B[1munknownField\u001B[22m
                [PartyIDSource]\u001B[1m447\u001B[22m\u001B[1m=\u001B[22m\u001B[1mA\u001B[22m[AUSTRALIAN_TAX_FILE_NUMBER]
            2.  [PartyID]\u001B[1m448\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAndy\u001B[22m
    2.  [MDUpdateAction]\u001B[1m279\u001B[22m\u001B[1m=\u001B[22m\u001B[1m0\u001B[22m[NEW]
        [MDEntryType]\u001B[1m269\u001B[22m\u001B[1m=\u001B[22m\u001B[1m1\u001B[22m[OFFER]
        [NoPartyIDs]\u001B[1m453\u001B[22m\u001B[1m=\u001B[22m\u001B[1m2\u001B[22m
            1.  [PartyID]\u001B[1m448\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAmy\u001B[22m
                [PartyIDSource]\u001B[1m447\u001B[22m\u001B[1m=\u001B[22m\u001B[1mA\u001B[22m[AUSTRALIAN_TAX_FILE_NUMBER]
            2.  [PartyID]\u001B[1m448\u001B[22m\u001B[1m=\u001B[22m\u001B[1mMilly\u001B[22m
        [Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[22m
        [MDEntryPx]\u001B[1m270\u001B[22m\u001B[1m=\u001B[22m\u001B[1m1.12355\u001B[22m
    3.  [MDUpdateAction]\u001B[1m279\u001B[22m\u001B[1m=\u001B[22m\u001B[1m0\u001B[22m[NEW]
        [MDEntryType]\u001B[1m269\u001B[22m\u001B[1m=\u001B[22m\u001B[1m1\u001B[22m[OFFER]
        [NoPartyIDs]\u001B[1m453\u001B[22m\u001B[1m=\u001B[22m\u001B[1m2\u001B[22m
            1.  [PartyID]\u001B[1m448\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAmy\u001B[22m
                [PartyIDSource]\u001B[1m447\u001B[22m\u001B[1m=\u001B[22m\u001B[1mA\u001B[22m[AUSTRALIAN_TAX_FILE_NUMBER]
            2.  [PartyID]\u001B[1m448\u001B[22m\u001B[1m=\u001B[22m\u001B[1mMilly\u001B[22m
                \u001B[1m9999\u001B[22m\u001B[1m=\u001B[22m\u001B[1munknownField\u001B[22m
        [Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[22m
        [MDEntryPx]\u001B[1m270\u001B[22m\u001B[1m=\u001B[22m\u001B[1m1.12355\u001B[22m
    4.  [MDUpdateAction]\u001B[1m279\u001B[22m\u001B[1m=\u001B[22m\u001B[1m0\u001B[22m[NEW]
        [MDEntryType]\u001B[1m269\u001B[22m\u001B[1m=\u001B[22m\u001B[1m0\u001B[22m[BID]
        [Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[22m
        [MDEntryPx]\u001B[1m270\u001B[22m\u001B[1m=\u001B[22m\u001B[1m1.12335\u001B[22m
        [NoPartyIDs]\u001B[1m453\u001B[22m\u001B[1m=\u001B[22m\u001B[1m2\u001B[22m
            1.  [PartyID]\u001B[1m448\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAmy\u001B[22m
                [PartyIDSource]\u001B[1m447\u001B[22m\u001B[1m=\u001B[22m\u001B[1mA\u001B[22m[AUSTRALIAN_TAX_FILE_NUMBER]
            2.  [PartyID]\u001B[1m448\u001B[22m\u001B[1m=\u001B[22m\u001B[1mMilly\u001B[22m
[NoRoutingIDs]\u001B[1m215\u001B[22m\u001B[1m=\u001B[22m\u001B[1m2\u001B[22m
    1.  [RoutingType]\u001B[1m216\u001B[22m\u001B[1m=\u001B[22m\u001B[1m3\u001B[22m[BLOCK_FIRM]
        [RoutingID]\u001B[1m217\u001B[22m\u001B[1m=\u001B[22m\u001B[1mroutingId1\u001B[22m
    2.  [RoutingType]\u001B[1m216\u001B[22m\u001B[1m=\u001B[22m\u001B[1m2\u001B[22m[TARGET_LIST]
        [RoutingID]\u001B[1m217\u001B[22m\u001B[1m=\u001B[22m\u001B[1mroutingId2\u001B[22m
[MDFeedType]\u001B[1m1022\u001B[22m\u001B[1m=\u001B[22m\u001B[1masdf\u001B[22m

================================================================================
\u001B[36mNewOrderSingle\u001B[0m
================================================================================
[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1mD\u001B[22m[NEWORDERSINGLE]
[ClOrdID]\u001B[1m11\u001B[22m\u001B[1m=\u001B[22m\u001B[1mABC\u001B[22m
[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[22m
"""
    }

    def 'test vertical non-aligned format - DO NOT indentGroupRepeats - prices'(){
        when:
        final String fix =
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
                        "215=2${a}" + //NoRoutingIDs
                        "216=3${a}" + //RoutingType
                        "217=routingId1${a}" + //RoutingID
                        "216=2${a}" + //RoutingType
                        "217=routingId2${a}" + //RoutingID
                        "1022=asdf"; //MDFeedType

        def lines = parseToLines('--indent-group-repeats=false', fix)

        then:
        assert lines == """================================================================================
\u001B[33mMarketDataIncrementalRefresh\u001B[0m
================================================================================
[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1mX\u001B[22m[MARKETDATAINCREMENTALREFRESH]
[MDReqID]\u001B[1m262\u001B[22m\u001B[1m=\u001B[22m\u001B[1mABCD\u001B[22m
[NoMDEntries]\u001B[1m268\u001B[22m\u001B[1m=\u001B[22m\u001B[1m4\u001B[22m
[MDUpdateAction]\u001B[1m279\u001B[22m\u001B[1m=\u001B[22m\u001B[1m0\u001B[22m[NEW]
[MDEntryType]\u001B[1m269\u001B[22m\u001B[1m=\u001B[22m\u001B[1m0\u001B[22m[BID]
\u001B[1m9999\u001B[22m\u001B[1m=\u001B[22m\u001B[1munknownField\u001B[22m
[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[22m
[MDEntryPx]\u001B[1m270\u001B[22m\u001B[1m=\u001B[22m\u001B[1m1.12345\u001B[22m
[NoPartyIDs]\u001B[1m453\u001B[22m\u001B[1m=\u001B[22m\u001B[1m2\u001B[22m
[PartyID]\u001B[1m448\u001B[22m\u001B[1m=\u001B[22m\u001B[1mBen\u001B[22m
\u001B[1m9999\u001B[22m\u001B[1m=\u001B[22m\u001B[1munknownField\u001B[22m
[PartyIDSource]\u001B[1m447\u001B[22m\u001B[1m=\u001B[22m\u001B[1mA\u001B[22m[AUSTRALIAN_TAX_FILE_NUMBER]
[PartyID]\u001B[1m448\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAndy\u001B[22m
[MDUpdateAction]\u001B[1m279\u001B[22m\u001B[1m=\u001B[22m\u001B[1m0\u001B[22m[NEW]
[MDEntryType]\u001B[1m269\u001B[22m\u001B[1m=\u001B[22m\u001B[1m1\u001B[22m[OFFER]
[NoPartyIDs]\u001B[1m453\u001B[22m\u001B[1m=\u001B[22m\u001B[1m2\u001B[22m
[PartyID]\u001B[1m448\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAmy\u001B[22m
[PartyIDSource]\u001B[1m447\u001B[22m\u001B[1m=\u001B[22m\u001B[1mA\u001B[22m[AUSTRALIAN_TAX_FILE_NUMBER]
[PartyID]\u001B[1m448\u001B[22m\u001B[1m=\u001B[22m\u001B[1mMilly\u001B[22m
[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[22m
[MDEntryPx]\u001B[1m270\u001B[22m\u001B[1m=\u001B[22m\u001B[1m1.12355\u001B[22m
[MDUpdateAction]\u001B[1m279\u001B[22m\u001B[1m=\u001B[22m\u001B[1m0\u001B[22m[NEW]
[MDEntryType]\u001B[1m269\u001B[22m\u001B[1m=\u001B[22m\u001B[1m1\u001B[22m[OFFER]
[NoPartyIDs]\u001B[1m453\u001B[22m\u001B[1m=\u001B[22m\u001B[1m2\u001B[22m
[PartyID]\u001B[1m448\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAmy\u001B[22m
[PartyIDSource]\u001B[1m447\u001B[22m\u001B[1m=\u001B[22m\u001B[1mA\u001B[22m[AUSTRALIAN_TAX_FILE_NUMBER]
[PartyID]\u001B[1m448\u001B[22m\u001B[1m=\u001B[22m\u001B[1mMilly\u001B[22m
\u001B[1m9999\u001B[22m\u001B[1m=\u001B[22m\u001B[1munknownField\u001B[22m
[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[22m
[MDEntryPx]\u001B[1m270\u001B[22m\u001B[1m=\u001B[22m\u001B[1m1.12355\u001B[22m
[MDUpdateAction]\u001B[1m279\u001B[22m\u001B[1m=\u001B[22m\u001B[1m0\u001B[22m[NEW]
[MDEntryType]\u001B[1m269\u001B[22m\u001B[1m=\u001B[22m\u001B[1m0\u001B[22m[BID]
[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[22m
[MDEntryPx]\u001B[1m270\u001B[22m\u001B[1m=\u001B[22m\u001B[1m1.12335\u001B[22m
[NoPartyIDs]\u001B[1m453\u001B[22m\u001B[1m=\u001B[22m\u001B[1m2\u001B[22m
[PartyID]\u001B[1m448\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAmy\u001B[22m
[PartyIDSource]\u001B[1m447\u001B[22m\u001B[1m=\u001B[22m\u001B[1mA\u001B[22m[AUSTRALIAN_TAX_FILE_NUMBER]
[PartyID]\u001B[1m448\u001B[22m\u001B[1m=\u001B[22m\u001B[1mMilly\u001B[22m
[NoRoutingIDs]\u001B[1m215\u001B[22m\u001B[1m=\u001B[22m\u001B[1m2\u001B[22m
[RoutingType]\u001B[1m216\u001B[22m\u001B[1m=\u001B[22m\u001B[1m3\u001B[22m[BLOCK_FIRM]
[RoutingID]\u001B[1m217\u001B[22m\u001B[1m=\u001B[22m\u001B[1mroutingId1\u001B[22m
[RoutingType]\u001B[1m216\u001B[22m\u001B[1m=\u001B[22m\u001B[1m2\u001B[22m[TARGET_LIST]
[RoutingID]\u001B[1m217\u001B[22m\u001B[1m=\u001B[22m\u001B[1mroutingId2\u001B[22m
[MDFeedType]\u001B[1m1022\u001B[22m\u001B[1m=\u001B[22m\u001B[1masdf\u001B[22m
"""
    }

    private String parseToLines(String args, final String fix){
        args = args + testOverrides
        final List<String> argsList = new ArgsAsString(args).toArgs()

        final CircularBufferedReaderWriter input = new CircularBufferedReaderWriter();
        final CircularBufferedReaderWriter output = new CircularBufferedReaderWriter();

        input.writer.write(fix)
        input.writer.flush()
        input.writer.close()

        new FixGrepMain(input.inputStream, output.outputStream, argsList).go()

        output.outputStream.flush()
        String lines = output.readLines('\n')

        if(logNewAssertionsToFile) {
            def testCriteriaIfActualIsCorrect = ("'" + args + "'").padRight(35) + "| '" + lines.replace("\n", "\\" + "n").replace('\u001b', '\\' + 'u001b') + "'"
            newAssertionsFile.append(testCriteriaIfActualIsCorrect + '\n')
        }
        if(logResultsToFile) {
            def testCriteriaIfActualIsCorrect = ("'" + args + "'").padRight(35) + "| '" + lines.replace("\n", "\\" + "n").replace('\u001b', '\\' + 'u001b') + "'"
            resultsFile.append(args)
            resultsFile.append('\n')
            resultsFile.append(lines)
            resultsFile.append('\n')
            resultsFile.append('\n')
        }
        println lines
        return lines
    }

    protected void deleteAndCreateNewFile(final File file) {
        if (file) {
            if (file.exists()) {
                file.delete()
            }
            file.createNewFile()
        }
    }
}
