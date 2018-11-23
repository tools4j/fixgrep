package org.tools4j.fixgrep

import org.tools4j.fix.Ascii1Char
import org.tools4j.fixgrep.utils.WrappedFixGrep
import spock.lang.Shared
import spock.lang.Specification

/**
 * User: ben
 * Date: 12/03/2018
 * Time: 6:55 AM
 */
class VerticalConsoleTest extends Specification {
    @Shared private final static String a = new Ascii1Char().toString()
    @Shared private WrappedFixGrep fixGrep;

    def setupSpec() {
        fixGrep = new WrappedFixGrep(' -V')
    }

    def 'test vertical aligned format'(){
        when:
        final String fix = "35=D${a}11=ABC${a}55=AUD/USD\n35=8${a}150=F${a}55=AUD/USD"
        def lines = fixGrep.go('-A', fix)

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
        def lines = fixGrep.go('-A -h 35', fix)

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
        def lines = fixGrep.go('-A -h 35:Msg', fix)

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
        def lines = fixGrep.go('', fix)

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
        def lines = fixGrep.go('', VerticalTestUtil.PRICES_FIX)

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
"""
    }


    def 'test vertical non-aligned format - indentGroupRepeats - prices - excluding some fields'(){
        when:
        def lines = fixGrep.go('-e 268,448,55,215,217,270 -q', VerticalTestUtil.PRICES_FIX)

        then:
        assert lines == """================================================================================
\u001B[33mMarketDataIncrementalRefresh\u001B[0m
================================================================================
[MsgType]35=X[MARKETDATAINCREMENTALREFRESH]
[MDReqID]262=ABCD
    1.  [MDUpdateAction]279=0[NEW]
        [MDEntryType]269=0[BID]
        9999=unknownField
        [NoPartyIDs]453=2
            1.  9999=unknownField
                [PartyIDSource]447=A[AUSTRALIAN_TAX_FILE_NUMBER]
    2.  [MDUpdateAction]279=0[NEW]
        [MDEntryType]269=1[OFFER]
        [NoPartyIDs]453=2
            1.  [PartyIDSource]447=A[AUSTRALIAN_TAX_FILE_NUMBER]
    3.  [MDUpdateAction]279=0[NEW]
        [MDEntryType]269=1[OFFER]
        [NoPartyIDs]453=2
            1.  [PartyIDSource]447=A[AUSTRALIAN_TAX_FILE_NUMBER]
            2.  9999=unknownField
    4.  [MDUpdateAction]279=0[NEW]
        [MDEntryType]269=0[BID]
        [NoPartyIDs]453=2
            1.  [PartyIDSource]447=A[AUSTRALIAN_TAX_FILE_NUMBER]
    1.  [RoutingType]216=3[BLOCK_FIRM]
    2.  [RoutingType]216=2[TARGET_LIST]
[MDFeedType]1022=asdf
"""
    }


    def 'test vertical non-aligned format - indentGroupRepeats - prices - excluding a lot of fields'(){
        when:
        def lines = fixGrep.go('-e 279 -q',
                    "35=X${a}" +
                    "268=2${a}" +
                    "279=0${a}" +
                    "279=0" )

        then:
        assert lines == """================================================================================
\u001B[33mMarketDataIncrementalRefresh\u001B[0m
================================================================================
[MsgType]35=X[MARKETDATAINCREMENTALREFRESH]
[NoMDEntries]268=2
"""
    }



    def 'test vertical non-aligned format - indentGroupRepeats - simple prices'(){
        when:
        final String fix =
                "35=X${a}" +
                "262=ABCD${a}" +
                "268=3${a}" +
                "279=0${a}" +
                "269=0${a}" +
                "55=AUD/USD${a}" +
                "270=1.12345${a}" +
                "279=0${a}" +
                "269=1${a}" +
                "55=AUD/USD${a}" +
                "270=1.12355${a}" +
                "279=0${a}" +
                "269=1${a}" +
                "55=AUD/USD${a}" +
                "270=1.12355${a}" +
                "1022=FeedA${a}"

        def lines = fixGrep.go('', fix)

        then:
        assert lines == """================================================================================
\u001B[33mMarketDataIncrementalRefresh\u001B[0m
================================================================================
[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1mX\u001B[22m[MARKETDATAINCREMENTALREFRESH]
[MDReqID]\u001B[1m262\u001B[22m\u001B[1m=\u001B[22m\u001B[1mABCD\u001B[22m
[NoMDEntries]\u001B[1m268\u001B[22m\u001B[1m=\u001B[22m\u001B[1m3\u001B[22m
    1.  [MDUpdateAction]\u001B[1m279\u001B[22m\u001B[1m=\u001B[22m\u001B[1m0\u001B[22m[NEW]
        [MDEntryType]\u001B[1m269\u001B[22m\u001B[1m=\u001B[22m\u001B[1m0\u001B[22m[BID]
        [Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[22m
        [MDEntryPx]\u001B[1m270\u001B[22m\u001B[1m=\u001B[22m\u001B[1m1.12345\u001B[22m
    2.  [MDUpdateAction]\u001B[1m279\u001B[22m\u001B[1m=\u001B[22m\u001B[1m0\u001B[22m[NEW]
        [MDEntryType]\u001B[1m269\u001B[22m\u001B[1m=\u001B[22m\u001B[1m1\u001B[22m[OFFER]
        [Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[22m
        [MDEntryPx]\u001B[1m270\u001B[22m\u001B[1m=\u001B[22m\u001B[1m1.12355\u001B[22m
    3.  [MDUpdateAction]\u001B[1m279\u001B[22m\u001B[1m=\u001B[22m\u001B[1m0\u001B[22m[NEW]
        [MDEntryType]\u001B[1m269\u001B[22m\u001B[1m=\u001B[22m\u001B[1m1\u001B[22m[OFFER]
        [Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[22m
        [MDEntryPx]\u001B[1m270\u001B[22m\u001B[1m=\u001B[22m\u001B[1m1.12355\u001B[22m
[MDFeedType]\u001B[1m1022\u001B[22m\u001B[1m=\u001B[22m\u001B[1mFeedA\u001B[22m
"""
    }

    def 'test vertical non-aligned format - DO NOT indentGroupRepeats - prices'(){
        when:
        def lines = fixGrep.go('--suppress-indent-group-repeats', VerticalTestUtil.PRICES_FIX)

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
}
