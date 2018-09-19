package org.tools4j.fixgrep

import org.tools4j.fix.Ascii1Char
import org.tools4j.util.CircularBufferedReaderWriter
import org.tools4j.utils.ArgsAsString
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

/**
 * User: ben
 * Date: 12/03/2018
 * Time: 6:55 AM
 */
class VerticalHtmlTest extends Specification {
    @Shared private final static String a = new Ascii1Char().toString()
    @Shared private String testOverrides;

    private static File newAssertionsFile = new File("new-assertions.txt")
    private static File resultsFile = new File("results.txt")
    private static boolean logResultsToFile = true;
    private static boolean logNewAssertionsToFile = true;
    private static boolean launchResultInBrowser = false

    def setupSpec() {
        if(logNewAssertionsToFile) deleteAndCreateNewFile(newAssertionsFile)
        if(logResultsToFile) deleteAndCreateNewFile(resultsFile)
        testOverrides = ' -V --html -p'
    }

    def 'test vertical aligned format'(){
        when:
        final String fix = "35=D${a}11=ABC${a}55=AUD/USD\n35=8${a}150=F${a}55=AUD/USD"
        def lines = parseToLines('-A', fix)

        then:
        assert lines == """<div class='msg-header'>
================================================================================</br>
<span class='FgCyan'>NewOrderSingle</span><br/>
================================================================================
</div>
<table class='fields'>
<tr class='field annotatedField'><td class='tag-annotation'>MsgType</td><td class='tag-raw bold'>35</td><td class='equals bold'>=</td><td class='value-raw bold'>D</td><td class='value-annotation'>NEWORDERSINGLE</td></tr>
<tr class='field annotatedField'><td class='tag-annotation'>ClOrdID</td><td class='tag-raw bold'>11</td><td class='equals bold'>=</td><td class='value-raw bold' colspan='2'>ABC</td></tr>
<tr class='field annotatedField'><td class='tag-annotation'>Symbol</td><td class='tag-raw bold'>55</td><td class='equals bold'>=</td><td class='value-raw bold' colspan='2'>AUD/USD</td></tr>
</table>

<br/>
<div class='msg-header'>
================================================================================</br>
<span class='FgGreen'>Exec.Trade</span><br/>
================================================================================
</div>
<table class='fields'>
<tr class='field annotatedField'><td class='tag-annotation'>MsgType</td><td class='tag-raw bold'>35</td><td class='equals bold'>=</td><td class='value-raw bold'>8</td><td class='value-annotation'>EXECUTIONREPORT</td></tr>
<tr class='field annotatedField'><td class='tag-annotation'>ExecType</td><td class='tag-raw bold'>150</td><td class='equals bold'>=</td><td class='value-raw bold'>F</td><td class='value-annotation'>TRADE_PARTIAL_FILL_OR_FILL</td></tr>
<tr class='field annotatedField'><td class='tag-annotation'>Symbol</td><td class='tag-raw bold'>55</td><td class='equals bold'>=</td><td class='value-raw bold' colspan='2'>AUD/USD</td></tr>
</table>

<br/>"""
    }


    @Unroll
    def 'test vertical non-aligned format'(){
        when:
        final String fix = "35=D${a}11=ABC${a}55=AUD/USD\n35=8${a}150=F${a}55=AUD/USD"
        def lines = parseToLines('', fix)

        then:
        assert lines == """<div class='msg-header'>
================================================================================</br>
<span class='FgCyan'>NewOrderSingle</span><br/>
================================================================================
</div>
<div class='fields'>
<div class='field annotatedField'><span class='tag-annotation'>[MsgType]</span><span class='tag-raw bold'>35</span><span class='equals bold'>=</span><span class='value-raw bold'>D</span><span class='value-annotation'>[NEWORDERSINGLE]</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[ClOrdID]</span><span class='tag-raw bold'>11</span><span class='equals bold'>=</span><span class='value-raw bold'>ABC</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[Symbol]</span><span class='tag-raw bold'>55</span><span class='equals bold'>=</span><span class='value-raw bold'>AUD/USD</span></div>
</div>

<br/>
<div class='msg-header'>
================================================================================</br>
<span class='FgGreen'>Exec.Trade</span><br/>
================================================================================
</div>
<div class='fields'>
<div class='field annotatedField'><span class='tag-annotation'>[MsgType]</span><span class='tag-raw bold'>35</span><span class='equals bold'>=</span><span class='value-raw bold'>8</span><span class='value-annotation'>[EXECUTIONREPORT]</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[ExecType]</span><span class='tag-raw bold'>150</span><span class='equals bold'>=</span><span class='value-raw bold'>F</span><span class='value-annotation'>[TRADE_PARTIAL_FILL_OR_FILL]</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[Symbol]</span><span class='tag-raw bold'>55</span><span class='equals bold'>=</span><span class='value-raw bold'>AUD/USD</span></div>
</div>

<br/>"""
    }

    def 'test vertical non-aligned format - indentGroupRepeats - prices'(){
        when:
        def lines = parseToLines('', VerticalTestUtil.PRICES_FIX)

        then:
        assert lines == """<div class='msg-header'>
================================================================================</br>
<span class='FgYellow'>MarketDataIncrementalRefresh</span><br/>
================================================================================
</div>
<div class='fields'>
<div class='field annotatedField'><span class='tag-annotation'>[MsgType]</span><span class='tag-raw bold'>35</span><span class='equals bold'>=</span><span class='value-raw bold'>X</span><span class='value-annotation'>[MARKETDATAINCREMENTALREFRESH]</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[MDReqID]</span><span class='tag-raw bold'>262</span><span class='equals bold'>=</span><span class='value-raw bold'>ABCD</span></div>
<div class='group'>
<div class='field annotatedField'><span class='tag-annotation'>[NoMDEntries]</span><span class='tag-raw bold'>268</span><span class='equals bold'>=</span><span class='value-raw bold'>4</span></div>
<div class='group-repeats'>
<div class='group-repeat'>
<div class='group-repeat-number'>1.</div>
<div class='field annotatedField'><span class='tag-annotation'>[MDUpdateAction]</span><span class='tag-raw bold'>279</span><span class='equals bold'>=</span><span class='value-raw bold'>0</span><span class='value-annotation'>[NEW]</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[MDEntryType]</span><span class='tag-raw bold'>269</span><span class='equals bold'>=</span><span class='value-raw bold'>0</span><span class='value-annotation'>[BID]</span></div>
<div class='field annotatedField'><span class='tag-raw bold'>9999</span><span class='equals bold'>=</span><span class='value-raw bold'>unknownField</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[Symbol]</span><span class='tag-raw bold'>55</span><span class='equals bold'>=</span><span class='value-raw bold'>AUD/USD</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[MDEntryPx]</span><span class='tag-raw bold'>270</span><span class='equals bold'>=</span><span class='value-raw bold'>1.12345</span></div>
<div class='group'>
<div class='field annotatedField'><span class='tag-annotation'>[NoPartyIDs]</span><span class='tag-raw bold'>453</span><span class='equals bold'>=</span><span class='value-raw bold'>2</span></div>
<div class='group-repeats'>
<div class='group-repeat'>
<div class='group-repeat-number'>1.</div>
<div class='field annotatedField'><span class='tag-annotation'>[PartyID]</span><span class='tag-raw bold'>448</span><span class='equals bold'>=</span><span class='value-raw bold'>Ben</span></div>
<div class='field annotatedField'><span class='tag-raw bold'>9999</span><span class='equals bold'>=</span><span class='value-raw bold'>unknownField</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[PartyIDSource]</span><span class='tag-raw bold'>447</span><span class='equals bold'>=</span><span class='value-raw bold'>A</span><span class='value-annotation'>[AUSTRALIAN_TAX_FILE_NUMBER]</span></div>
</div><!--group repeat exit-->
<div class='group-repeat'>
<div class='group-repeat-number'>2.</div>
<div class='field annotatedField'><span class='tag-annotation'>[PartyID]</span><span class='tag-raw bold'>448</span><span class='equals bold'>=</span><span class='value-raw bold'>Andy</span></div>
</div><!--group repeat exit-->
</div><!--group repeats exit-->
</div><!--group exit-->
</div><!--group repeat exit-->
<div class='group-repeat'>
<div class='group-repeat-number'>2.</div>
<div class='field annotatedField'><span class='tag-annotation'>[MDUpdateAction]</span><span class='tag-raw bold'>279</span><span class='equals bold'>=</span><span class='value-raw bold'>0</span><span class='value-annotation'>[NEW]</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[MDEntryType]</span><span class='tag-raw bold'>269</span><span class='equals bold'>=</span><span class='value-raw bold'>1</span><span class='value-annotation'>[OFFER]</span></div>
<div class='group'>
<div class='field annotatedField'><span class='tag-annotation'>[NoPartyIDs]</span><span class='tag-raw bold'>453</span><span class='equals bold'>=</span><span class='value-raw bold'>2</span></div>
<div class='group-repeats'>
<div class='group-repeat'>
<div class='group-repeat-number'>1.</div>
<div class='field annotatedField'><span class='tag-annotation'>[PartyID]</span><span class='tag-raw bold'>448</span><span class='equals bold'>=</span><span class='value-raw bold'>Amy</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[PartyIDSource]</span><span class='tag-raw bold'>447</span><span class='equals bold'>=</span><span class='value-raw bold'>A</span><span class='value-annotation'>[AUSTRALIAN_TAX_FILE_NUMBER]</span></div>
</div><!--group repeat exit-->
<div class='group-repeat'>
<div class='group-repeat-number'>2.</div>
<div class='field annotatedField'><span class='tag-annotation'>[PartyID]</span><span class='tag-raw bold'>448</span><span class='equals bold'>=</span><span class='value-raw bold'>Milly</span></div>
</div><!--group repeat exit-->
</div><!--group repeats exit-->
</div><!--group exit-->
<div class='field annotatedField'><span class='tag-annotation'>[Symbol]</span><span class='tag-raw bold'>55</span><span class='equals bold'>=</span><span class='value-raw bold'>AUD/USD</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[MDEntryPx]</span><span class='tag-raw bold'>270</span><span class='equals bold'>=</span><span class='value-raw bold'>1.12355</span></div>
</div><!--group repeat exit-->
<div class='group-repeat'>
<div class='group-repeat-number'>3.</div>
<div class='field annotatedField'><span class='tag-annotation'>[MDUpdateAction]</span><span class='tag-raw bold'>279</span><span class='equals bold'>=</span><span class='value-raw bold'>0</span><span class='value-annotation'>[NEW]</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[MDEntryType]</span><span class='tag-raw bold'>269</span><span class='equals bold'>=</span><span class='value-raw bold'>1</span><span class='value-annotation'>[OFFER]</span></div>
<div class='group'>
<div class='field annotatedField'><span class='tag-annotation'>[NoPartyIDs]</span><span class='tag-raw bold'>453</span><span class='equals bold'>=</span><span class='value-raw bold'>2</span></div>
<div class='group-repeats'>
<div class='group-repeat'>
<div class='group-repeat-number'>1.</div>
<div class='field annotatedField'><span class='tag-annotation'>[PartyID]</span><span class='tag-raw bold'>448</span><span class='equals bold'>=</span><span class='value-raw bold'>Amy</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[PartyIDSource]</span><span class='tag-raw bold'>447</span><span class='equals bold'>=</span><span class='value-raw bold'>A</span><span class='value-annotation'>[AUSTRALIAN_TAX_FILE_NUMBER]</span></div>
</div><!--group repeat exit-->
<div class='group-repeat'>
<div class='group-repeat-number'>2.</div>
<div class='field annotatedField'><span class='tag-annotation'>[PartyID]</span><span class='tag-raw bold'>448</span><span class='equals bold'>=</span><span class='value-raw bold'>Milly</span></div>
<div class='field annotatedField'><span class='tag-raw bold'>9999</span><span class='equals bold'>=</span><span class='value-raw bold'>unknownField</span></div>
</div><!--group repeat exit-->
</div><!--group repeats exit-->
</div><!--group exit-->
<div class='field annotatedField'><span class='tag-annotation'>[Symbol]</span><span class='tag-raw bold'>55</span><span class='equals bold'>=</span><span class='value-raw bold'>AUD/USD</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[MDEntryPx]</span><span class='tag-raw bold'>270</span><span class='equals bold'>=</span><span class='value-raw bold'>1.12355</span></div>
</div><!--group repeat exit-->
<div class='group-repeat'>
<div class='group-repeat-number'>4.</div>
<div class='field annotatedField'><span class='tag-annotation'>[MDUpdateAction]</span><span class='tag-raw bold'>279</span><span class='equals bold'>=</span><span class='value-raw bold'>0</span><span class='value-annotation'>[NEW]</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[MDEntryType]</span><span class='tag-raw bold'>269</span><span class='equals bold'>=</span><span class='value-raw bold'>0</span><span class='value-annotation'>[BID]</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[Symbol]</span><span class='tag-raw bold'>55</span><span class='equals bold'>=</span><span class='value-raw bold'>AUD/USD</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[MDEntryPx]</span><span class='tag-raw bold'>270</span><span class='equals bold'>=</span><span class='value-raw bold'>1.12335</span></div>
<div class='group'>
<div class='field annotatedField'><span class='tag-annotation'>[NoPartyIDs]</span><span class='tag-raw bold'>453</span><span class='equals bold'>=</span><span class='value-raw bold'>2</span></div>
<div class='group-repeats'>
<div class='group-repeat'>
<div class='group-repeat-number'>1.</div>
<div class='field annotatedField'><span class='tag-annotation'>[PartyID]</span><span class='tag-raw bold'>448</span><span class='equals bold'>=</span><span class='value-raw bold'>Amy</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[PartyIDSource]</span><span class='tag-raw bold'>447</span><span class='equals bold'>=</span><span class='value-raw bold'>A</span><span class='value-annotation'>[AUSTRALIAN_TAX_FILE_NUMBER]</span></div>
</div><!--group repeat exit-->
<div class='group-repeat'>
<div class='group-repeat-number'>2.</div>
<div class='field annotatedField'><span class='tag-annotation'>[PartyID]</span><span class='tag-raw bold'>448</span><span class='equals bold'>=</span><span class='value-raw bold'>Milly</span></div>
</div><!--group repeat exit-->
</div><!--group repeats exit-->
</div><!--group exit-->
</div><!--group repeat exit-->
</div><!--group repeats exit-->
</div><!--group exit-->
<div class='group'>
<div class='field annotatedField'><span class='tag-annotation'>[NoRoutingIDs]</span><span class='tag-raw bold'>215</span><span class='equals bold'>=</span><span class='value-raw bold'>2</span></div>
<div class='group-repeats'>
<div class='group-repeat'>
<div class='group-repeat-number'>1.</div>
<div class='field annotatedField'><span class='tag-annotation'>[RoutingType]</span><span class='tag-raw bold'>216</span><span class='equals bold'>=</span><span class='value-raw bold'>3</span><span class='value-annotation'>[BLOCK_FIRM]</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[RoutingID]</span><span class='tag-raw bold'>217</span><span class='equals bold'>=</span><span class='value-raw bold'>routingId1</span></div>
</div><!--group repeat exit-->
<div class='group-repeat'>
<div class='group-repeat-number'>2.</div>
<div class='field annotatedField'><span class='tag-annotation'>[RoutingType]</span><span class='tag-raw bold'>216</span><span class='equals bold'>=</span><span class='value-raw bold'>2</span><span class='value-annotation'>[TARGET_LIST]</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[RoutingID]</span><span class='tag-raw bold'>217</span><span class='equals bold'>=</span><span class='value-raw bold'>routingId2</span></div>
</div><!--group repeat exit-->
</div><!--group repeats exit-->
</div><!--group exit-->
<div class='field annotatedField'><span class='tag-annotation'>[MDFeedType]</span><span class='tag-raw bold'>1022</span><span class='equals bold'>=</span><span class='value-raw bold'>asdf</span></div>
</div>

<br/>"""
    }


    def 'test vertical non-aligned format - indentGroupRepeats - prices - excluding some fields'(){
        when:
        def lines = parseToLines('-e 268,448,55,215,217,270', VerticalTestUtil.PRICES_FIX)

        then:
        assert lines == """<div class='msg-header'>
================================================================================</br>
<span class='FgYellow'>MarketDataIncrementalRefresh</span><br/>
================================================================================
</div>
<div class='fields'>
<div class='field annotatedField'><span class='tag-annotation'>[MsgType]</span><span class='tag-raw bold'>35</span><span class='equals bold'>=</span><span class='value-raw bold'>X</span><span class='value-annotation'>[MARKETDATAINCREMENTALREFRESH]</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[MDReqID]</span><span class='tag-raw bold'>262</span><span class='equals bold'>=</span><span class='value-raw bold'>ABCD</span></div>
<div class='group'>
<div class='group-repeats'>
<div class='group-repeat'>
<div class='group-repeat-number'>1.</div>
<div class='field annotatedField'><span class='tag-annotation'>[MDUpdateAction]</span><span class='tag-raw bold'>279</span><span class='equals bold'>=</span><span class='value-raw bold'>0</span><span class='value-annotation'>[NEW]</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[MDEntryType]</span><span class='tag-raw bold'>269</span><span class='equals bold'>=</span><span class='value-raw bold'>0</span><span class='value-annotation'>[BID]</span></div>
<div class='field annotatedField'><span class='tag-raw bold'>9999</span><span class='equals bold'>=</span><span class='value-raw bold'>unknownField</span></div>
<div class='group'>
<div class='field annotatedField'><span class='tag-annotation'>[NoPartyIDs]</span><span class='tag-raw bold'>453</span><span class='equals bold'>=</span><span class='value-raw bold'>2</span></div>
<div class='group-repeats'>
<div class='group-repeat'>
<div class='group-repeat-number'>1.</div>
<div class='field annotatedField'><span class='tag-raw bold'>9999</span><span class='equals bold'>=</span><span class='value-raw bold'>unknownField</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[PartyIDSource]</span><span class='tag-raw bold'>447</span><span class='equals bold'>=</span><span class='value-raw bold'>A</span><span class='value-annotation'>[AUSTRALIAN_TAX_FILE_NUMBER]</span></div>
</div><!--group repeat exit-->
<div class='group-repeat'>
<div class='group-repeat-number'>2.</div>
</div><!--group repeat exit-->
</div><!--group repeats exit-->
</div><!--group exit-->
</div><!--group repeat exit-->
<div class='group-repeat'>
<div class='group-repeat-number'>2.</div>
<div class='field annotatedField'><span class='tag-annotation'>[MDUpdateAction]</span><span class='tag-raw bold'>279</span><span class='equals bold'>=</span><span class='value-raw bold'>0</span><span class='value-annotation'>[NEW]</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[MDEntryType]</span><span class='tag-raw bold'>269</span><span class='equals bold'>=</span><span class='value-raw bold'>1</span><span class='value-annotation'>[OFFER]</span></div>
<div class='group'>
<div class='field annotatedField'><span class='tag-annotation'>[NoPartyIDs]</span><span class='tag-raw bold'>453</span><span class='equals bold'>=</span><span class='value-raw bold'>2</span></div>
<div class='group-repeats'>
<div class='group-repeat'>
<div class='group-repeat-number'>1.</div>
<div class='field annotatedField'><span class='tag-annotation'>[PartyIDSource]</span><span class='tag-raw bold'>447</span><span class='equals bold'>=</span><span class='value-raw bold'>A</span><span class='value-annotation'>[AUSTRALIAN_TAX_FILE_NUMBER]</span></div>
</div><!--group repeat exit-->
<div class='group-repeat'>
<div class='group-repeat-number'>2.</div>
</div><!--group repeat exit-->
</div><!--group repeats exit-->
</div><!--group exit-->
</div><!--group repeat exit-->
<div class='group-repeat'>
<div class='group-repeat-number'>3.</div>
<div class='field annotatedField'><span class='tag-annotation'>[MDUpdateAction]</span><span class='tag-raw bold'>279</span><span class='equals bold'>=</span><span class='value-raw bold'>0</span><span class='value-annotation'>[NEW]</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[MDEntryType]</span><span class='tag-raw bold'>269</span><span class='equals bold'>=</span><span class='value-raw bold'>1</span><span class='value-annotation'>[OFFER]</span></div>
<div class='group'>
<div class='field annotatedField'><span class='tag-annotation'>[NoPartyIDs]</span><span class='tag-raw bold'>453</span><span class='equals bold'>=</span><span class='value-raw bold'>2</span></div>
<div class='group-repeats'>
<div class='group-repeat'>
<div class='group-repeat-number'>1.</div>
<div class='field annotatedField'><span class='tag-annotation'>[PartyIDSource]</span><span class='tag-raw bold'>447</span><span class='equals bold'>=</span><span class='value-raw bold'>A</span><span class='value-annotation'>[AUSTRALIAN_TAX_FILE_NUMBER]</span></div>
</div><!--group repeat exit-->
<div class='group-repeat'>
<div class='group-repeat-number'>2.</div>
<div class='field annotatedField'><span class='tag-raw bold'>9999</span><span class='equals bold'>=</span><span class='value-raw bold'>unknownField</span></div>
</div><!--group repeat exit-->
</div><!--group repeats exit-->
</div><!--group exit-->
</div><!--group repeat exit-->
<div class='group-repeat'>
<div class='group-repeat-number'>4.</div>
<div class='field annotatedField'><span class='tag-annotation'>[MDUpdateAction]</span><span class='tag-raw bold'>279</span><span class='equals bold'>=</span><span class='value-raw bold'>0</span><span class='value-annotation'>[NEW]</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[MDEntryType]</span><span class='tag-raw bold'>269</span><span class='equals bold'>=</span><span class='value-raw bold'>0</span><span class='value-annotation'>[BID]</span></div>
<div class='group'>
<div class='field annotatedField'><span class='tag-annotation'>[NoPartyIDs]</span><span class='tag-raw bold'>453</span><span class='equals bold'>=</span><span class='value-raw bold'>2</span></div>
<div class='group-repeats'>
<div class='group-repeat'>
<div class='group-repeat-number'>1.</div>
<div class='field annotatedField'><span class='tag-annotation'>[PartyIDSource]</span><span class='tag-raw bold'>447</span><span class='equals bold'>=</span><span class='value-raw bold'>A</span><span class='value-annotation'>[AUSTRALIAN_TAX_FILE_NUMBER]</span></div>
</div><!--group repeat exit-->
<div class='group-repeat'>
<div class='group-repeat-number'>2.</div>
</div><!--group repeat exit-->
</div><!--group repeats exit-->
</div><!--group exit-->
</div><!--group repeat exit-->
</div><!--group repeats exit-->
</div><!--group exit-->
<div class='group'>
<div class='group-repeats'>
<div class='group-repeat'>
<div class='group-repeat-number'>1.</div>
<div class='field annotatedField'><span class='tag-annotation'>[RoutingType]</span><span class='tag-raw bold'>216</span><span class='equals bold'>=</span><span class='value-raw bold'>3</span><span class='value-annotation'>[BLOCK_FIRM]</span></div>
</div><!--group repeat exit-->
<div class='group-repeat'>
<div class='group-repeat-number'>2.</div>
<div class='field annotatedField'><span class='tag-annotation'>[RoutingType]</span><span class='tag-raw bold'>216</span><span class='equals bold'>=</span><span class='value-raw bold'>2</span><span class='value-annotation'>[TARGET_LIST]</span></div>
</div><!--group repeat exit-->
</div><!--group repeats exit-->
</div><!--group exit-->
<div class='field annotatedField'><span class='tag-annotation'>[MDFeedType]</span><span class='tag-raw bold'>1022</span><span class='equals bold'>=</span><span class='value-raw bold'>asdf</span></div>
</div>

<br/>"""
    }

    def 'test vertical non-aligned format - DO NOT indentGroupRepeats - prices'(){
        when:
        def lines = parseToLines('--indent-group-repeats=false', VerticalTestUtil.PRICES_FIX)

        then:
        assert lines == """<div class='msg-header'>
================================================================================</br>
<span class='FgYellow'>MarketDataIncrementalRefresh</span><br/>
================================================================================
</div>
<div class='fields'>
<div class='field annotatedField'><span class='tag-annotation'>[MsgType]</span><span class='tag-raw bold'>35</span><span class='equals bold'>=</span><span class='value-raw bold'>X</span><span class='value-annotation'>[MARKETDATAINCREMENTALREFRESH]</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[MDReqID]</span><span class='tag-raw bold'>262</span><span class='equals bold'>=</span><span class='value-raw bold'>ABCD</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[NoMDEntries]</span><span class='tag-raw bold'>268</span><span class='equals bold'>=</span><span class='value-raw bold'>4</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[MDUpdateAction]</span><span class='tag-raw bold'>279</span><span class='equals bold'>=</span><span class='value-raw bold'>0</span><span class='value-annotation'>[NEW]</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[MDEntryType]</span><span class='tag-raw bold'>269</span><span class='equals bold'>=</span><span class='value-raw bold'>0</span><span class='value-annotation'>[BID]</span></div>
<div class='field annotatedField'><span class='tag-raw bold'>9999</span><span class='equals bold'>=</span><span class='value-raw bold'>unknownField</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[Symbol]</span><span class='tag-raw bold'>55</span><span class='equals bold'>=</span><span class='value-raw bold'>AUD/USD</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[MDEntryPx]</span><span class='tag-raw bold'>270</span><span class='equals bold'>=</span><span class='value-raw bold'>1.12345</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[NoPartyIDs]</span><span class='tag-raw bold'>453</span><span class='equals bold'>=</span><span class='value-raw bold'>2</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[PartyID]</span><span class='tag-raw bold'>448</span><span class='equals bold'>=</span><span class='value-raw bold'>Ben</span></div>
<div class='field annotatedField'><span class='tag-raw bold'>9999</span><span class='equals bold'>=</span><span class='value-raw bold'>unknownField</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[PartyIDSource]</span><span class='tag-raw bold'>447</span><span class='equals bold'>=</span><span class='value-raw bold'>A</span><span class='value-annotation'>[AUSTRALIAN_TAX_FILE_NUMBER]</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[PartyID]</span><span class='tag-raw bold'>448</span><span class='equals bold'>=</span><span class='value-raw bold'>Andy</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[MDUpdateAction]</span><span class='tag-raw bold'>279</span><span class='equals bold'>=</span><span class='value-raw bold'>0</span><span class='value-annotation'>[NEW]</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[MDEntryType]</span><span class='tag-raw bold'>269</span><span class='equals bold'>=</span><span class='value-raw bold'>1</span><span class='value-annotation'>[OFFER]</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[NoPartyIDs]</span><span class='tag-raw bold'>453</span><span class='equals bold'>=</span><span class='value-raw bold'>2</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[PartyID]</span><span class='tag-raw bold'>448</span><span class='equals bold'>=</span><span class='value-raw bold'>Amy</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[PartyIDSource]</span><span class='tag-raw bold'>447</span><span class='equals bold'>=</span><span class='value-raw bold'>A</span><span class='value-annotation'>[AUSTRALIAN_TAX_FILE_NUMBER]</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[PartyID]</span><span class='tag-raw bold'>448</span><span class='equals bold'>=</span><span class='value-raw bold'>Milly</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[Symbol]</span><span class='tag-raw bold'>55</span><span class='equals bold'>=</span><span class='value-raw bold'>AUD/USD</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[MDEntryPx]</span><span class='tag-raw bold'>270</span><span class='equals bold'>=</span><span class='value-raw bold'>1.12355</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[MDUpdateAction]</span><span class='tag-raw bold'>279</span><span class='equals bold'>=</span><span class='value-raw bold'>0</span><span class='value-annotation'>[NEW]</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[MDEntryType]</span><span class='tag-raw bold'>269</span><span class='equals bold'>=</span><span class='value-raw bold'>1</span><span class='value-annotation'>[OFFER]</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[NoPartyIDs]</span><span class='tag-raw bold'>453</span><span class='equals bold'>=</span><span class='value-raw bold'>2</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[PartyID]</span><span class='tag-raw bold'>448</span><span class='equals bold'>=</span><span class='value-raw bold'>Amy</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[PartyIDSource]</span><span class='tag-raw bold'>447</span><span class='equals bold'>=</span><span class='value-raw bold'>A</span><span class='value-annotation'>[AUSTRALIAN_TAX_FILE_NUMBER]</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[PartyID]</span><span class='tag-raw bold'>448</span><span class='equals bold'>=</span><span class='value-raw bold'>Milly</span></div>
<div class='field annotatedField'><span class='tag-raw bold'>9999</span><span class='equals bold'>=</span><span class='value-raw bold'>unknownField</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[Symbol]</span><span class='tag-raw bold'>55</span><span class='equals bold'>=</span><span class='value-raw bold'>AUD/USD</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[MDEntryPx]</span><span class='tag-raw bold'>270</span><span class='equals bold'>=</span><span class='value-raw bold'>1.12355</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[MDUpdateAction]</span><span class='tag-raw bold'>279</span><span class='equals bold'>=</span><span class='value-raw bold'>0</span><span class='value-annotation'>[NEW]</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[MDEntryType]</span><span class='tag-raw bold'>269</span><span class='equals bold'>=</span><span class='value-raw bold'>0</span><span class='value-annotation'>[BID]</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[Symbol]</span><span class='tag-raw bold'>55</span><span class='equals bold'>=</span><span class='value-raw bold'>AUD/USD</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[MDEntryPx]</span><span class='tag-raw bold'>270</span><span class='equals bold'>=</span><span class='value-raw bold'>1.12335</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[NoPartyIDs]</span><span class='tag-raw bold'>453</span><span class='equals bold'>=</span><span class='value-raw bold'>2</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[PartyID]</span><span class='tag-raw bold'>448</span><span class='equals bold'>=</span><span class='value-raw bold'>Amy</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[PartyIDSource]</span><span class='tag-raw bold'>447</span><span class='equals bold'>=</span><span class='value-raw bold'>A</span><span class='value-annotation'>[AUSTRALIAN_TAX_FILE_NUMBER]</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[PartyID]</span><span class='tag-raw bold'>448</span><span class='equals bold'>=</span><span class='value-raw bold'>Milly</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[NoRoutingIDs]</span><span class='tag-raw bold'>215</span><span class='equals bold'>=</span><span class='value-raw bold'>2</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[RoutingType]</span><span class='tag-raw bold'>216</span><span class='equals bold'>=</span><span class='value-raw bold'>3</span><span class='value-annotation'>[BLOCK_FIRM]</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[RoutingID]</span><span class='tag-raw bold'>217</span><span class='equals bold'>=</span><span class='value-raw bold'>routingId1</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[RoutingType]</span><span class='tag-raw bold'>216</span><span class='equals bold'>=</span><span class='value-raw bold'>2</span><span class='value-annotation'>[TARGET_LIST]</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[RoutingID]</span><span class='tag-raw bold'>217</span><span class='equals bold'>=</span><span class='value-raw bold'>routingId2</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[MDFeedType]</span><span class='tag-raw bold'>1022</span><span class='equals bold'>=</span><span class='value-raw bold'>asdf</span></div>
</div>

<br/>"""
    }

    private String parseToLines(String args, final String fix){
        args = args + testOverrides
        final List<String> argsList = new ArgsAsString(args).toArgs()

        if(launchResultInBrowser){
            final CircularBufferedReaderWriter browserLaunchInput = new CircularBufferedReaderWriter();
            final CircularBufferedReaderWriter browserLaunchOutput = new CircularBufferedReaderWriter();

            browserLaunchInput.writer.write(fix)
            browserLaunchInput.writer.flush()
            browserLaunchInput.writer.close()

            final List<String> argsListWithLaunchBrowserFlag = new ArrayList<>(argsList)
            argsListWithLaunchBrowserFlag.add('-l')
            new FixGrepMain(browserLaunchInput.inputStream, browserLaunchOutput.outputStream, argsListWithLaunchBrowserFlag).go()
        }

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

        println 'actual:  ' + lines
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
