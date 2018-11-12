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
class ReplacedAnnotationHtmlTest extends Specification {
    @Shared private final static String a = new Ascii1Char().toString()
    @Shared private TestFixGrep fixGrep;

    def setupSpec() {
        fixGrep = new TestFixGrep('-V --html -a rr')
    }

    def 'test vertical aligned format'(){
        when:
        final String fix = "35=D${a}11=ABC${a}55=AUD/USD\n35=8${a}150=F${a}55=AUD/USD"
        def lines = fixGrep.go('-A', fix)

        then:
        assert lines == """<div class='msg-header'>
================================================================================</br>
<span class='FgCyan'>NewOrderSingle</span><br/>
================================================================================
</div>
<table class='fields'>
<tr class='field annotatedField'><td class='tag annotation' colspan='2'>MsgType</td><td class='equals'>=</td><td class='value annotation' colspan='2'>NEWORDERSINGLE</td></tr>
<tr class='field annotatedField'><td class='tag annotation' colspan='2'>ClOrdID</td><td class='equals'>=</td><td class='value raw' colspan='2'>ABC</td></tr>
<tr class='field annotatedField'><td class='tag annotation' colspan='2'>Symbol</td><td class='equals'>=</td><td class='value raw' colspan='2'>AUD/USD</td></tr>
</table>

<br/>
<div class='msg-header'>
================================================================================</br>
<span class='FgGreen'>Exec.Trade</span><br/>
================================================================================
</div>
<table class='fields'>
<tr class='field annotatedField'><td class='tag annotation' colspan='2'>MsgType</td><td class='equals'>=</td><td class='value annotation' colspan='2'>EXECUTIONREPORT</td></tr>
<tr class='field annotatedField'><td class='tag annotation' colspan='2'>ExecType</td><td class='equals'>=</td><td class='value annotation' colspan='2'>TRADE_PARTIAL_FILL_OR_FILL</td></tr>
<tr class='field annotatedField'><td class='tag annotation' colspan='2'>Symbol</td><td class='equals'>=</td><td class='value raw' colspan='2'>AUD/USD</td></tr>
</table>

<br/>"""
    }


    @Unroll
    def 'test vertical non-aligned format'(){
        when:
        final String fix = "35=D${a}11=ABC${a}55=AUD/USD\n35=8${a}150=F${a}55=AUD/USD"
        def lines = fixGrep.go('', fix)

        then:
        assert lines == """<div class='msg-header'>
================================================================================</br>
<span class='FgCyan'>NewOrderSingle</span><br/>
================================================================================
</div>
<div class='fields'>
<div class='field annotatedField'><!--uid:0--><span class='tag annotation'>MsgType</span><span class='equals'>=</span><span class='value annotation'>NEWORDERSINGLE</span></div><!--uid:1-->
<div class='field annotatedField'><!--uid:2--><span class='tag annotation'>ClOrdID</span><span class='equals'>=</span><span class='value raw'>ABC</span></div><!--uid:3-->
<div class='field annotatedField'><!--uid:4--><span class='tag annotation'>Symbol</span><span class='equals'>=</span><span class='value raw'>AUD/USD</span></div><!--uid:5-->
</div>

<br/>
<div class='msg-header'>
================================================================================</br>
<span class='FgGreen'>Exec.Trade</span><br/>
================================================================================
</div>
<div class='fields'>
<div class='field annotatedField'><!--uid:0--><span class='tag annotation'>MsgType</span><span class='equals'>=</span><span class='value annotation'>EXECUTIONREPORT</span></div><!--uid:1-->
<div class='field annotatedField'><!--uid:2--><span class='tag annotation'>ExecType</span><span class='equals'>=</span><span class='value annotation'>TRADE_PARTIAL_FILL_OR_FILL</span></div><!--uid:3-->
<div class='field annotatedField'><!--uid:4--><span class='tag annotation'>Symbol</span><span class='equals'>=</span><span class='value raw'>AUD/USD</span></div><!--uid:5-->
</div>

<br/>"""
    }

    def 'test vertical non-aligned format - indentGroupRepeats - prices'(){
        when:
        def lines = fixGrep.go('', VerticalTestUtil.PRICES_FIX)

        then:
        assert lines == """<div class='msg-header'>
================================================================================</br>
<span class='FgYellow'>MarketDataIncrementalRefresh</span><br/>
================================================================================
</div>
<div class='fields'>
<div class='field annotatedField'><!--uid:0--><span class='tag annotation'>MsgType</span><span class='equals'>=</span><span class='value annotation'>MARKETDATAINCREMENTALREFRESH</span></div><!--uid:1-->
<div class='field annotatedField'><!--uid:2--><span class='tag annotation'>MDReqID</span><span class='equals'>=</span><span class='value raw'>ABCD</span></div><!--uid:3-->
<div class='group'><!--uid:4-->
<div class='field annotatedField'><!--uid:5--><span class='tag annotation'>NoMDEntries</span><span class='equals'>=</span><span class='value raw'>4</span></div><!--uid:6-->
<div class='group-repeats'><!--uid:7-->
<div class='group-repeat'><!--uid:8-->
<div class='group-repeat-number'><!--uid:9-->1.</div>
<div class='field annotatedField'><!--uid:10--><span class='tag annotation'>MDUpdateAction</span><span class='equals'>=</span><span class='value annotation'>NEW</span></div><!--uid:11-->
<div class='field annotatedField'><!--uid:12--><span class='tag annotation'>MDEntryType</span><span class='equals'>=</span><span class='value annotation'>BID</span></div><!--uid:13-->
<div class='field annotatedField'><!--uid:14--><span class='tag raw'>9999</span><span class='equals'>=</span><span class='value raw'>unknownField</span></div><!--uid:15-->
<div class='field annotatedField'><!--uid:16--><span class='tag annotation'>Symbol</span><span class='equals'>=</span><span class='value raw'>AUD/USD</span></div><!--uid:17-->
<div class='field annotatedField'><!--uid:18--><span class='tag annotation'>MDEntryPx</span><span class='equals'>=</span><span class='value raw'>1.12345</span></div><!--uid:19-->
<div class='group'><!--uid:20-->
<div class='field annotatedField'><!--uid:21--><span class='tag annotation'>NoPartyIDs</span><span class='equals'>=</span><span class='value raw'>2</span></div><!--uid:22-->
<div class='group-repeats'><!--uid:23-->
<div class='group-repeat'><!--uid:24-->
<div class='group-repeat-number'><!--uid:25-->1.</div>
<div class='field annotatedField'><!--uid:26--><span class='tag annotation'>PartyID</span><span class='equals'>=</span><span class='value raw'>Ben</span></div><!--uid:27-->
<div class='field annotatedField'><!--uid:28--><span class='tag raw'>9999</span><span class='equals'>=</span><span class='value raw'>unknownField</span></div><!--uid:29-->
<div class='field annotatedField'><!--uid:30--><span class='tag annotation'>PartyIDSource</span><span class='equals'>=</span><span class='value annotation'>AUSTRALIAN_TAX_FILE_NUMBER</span></div><!--uid:31-->
</div><!--group repeat exit uid:32-->
<div class='group-repeat'><!--uid:33-->
<div class='group-repeat-number'><!--uid:34-->2.</div>
<div class='field annotatedField'><!--uid:35--><span class='tag annotation'>PartyID</span><span class='equals'>=</span><span class='value raw'>Andy</span></div><!--uid:36-->
</div><!--group repeat exit uid:37-->
</div><!--group repeats exit uid::38-->
</div><!--group exit uid:39-->
</div><!--group repeat exit uid:40-->
<div class='group-repeat'><!--uid:41-->
<div class='group-repeat-number'><!--uid:42-->2.</div>
<div class='field annotatedField'><!--uid:43--><span class='tag annotation'>MDUpdateAction</span><span class='equals'>=</span><span class='value annotation'>NEW</span></div><!--uid:44-->
<div class='field annotatedField'><!--uid:45--><span class='tag annotation'>MDEntryType</span><span class='equals'>=</span><span class='value annotation'>OFFER</span></div><!--uid:46-->
<div class='group'><!--uid:47-->
<div class='field annotatedField'><!--uid:48--><span class='tag annotation'>NoPartyIDs</span><span class='equals'>=</span><span class='value raw'>2</span></div><!--uid:49-->
<div class='group-repeats'><!--uid:50-->
<div class='group-repeat'><!--uid:51-->
<div class='group-repeat-number'><!--uid:52-->1.</div>
<div class='field annotatedField'><!--uid:53--><span class='tag annotation'>PartyID</span><span class='equals'>=</span><span class='value raw'>Amy</span></div><!--uid:54-->
<div class='field annotatedField'><!--uid:55--><span class='tag annotation'>PartyIDSource</span><span class='equals'>=</span><span class='value annotation'>AUSTRALIAN_TAX_FILE_NUMBER</span></div><!--uid:56-->
</div><!--group repeat exit uid:57-->
<div class='group-repeat'><!--uid:58-->
<div class='group-repeat-number'><!--uid:59-->2.</div>
<div class='field annotatedField'><!--uid:60--><span class='tag annotation'>PartyID</span><span class='equals'>=</span><span class='value raw'>Milly</span></div><!--uid:61-->
</div><!--group repeat exit uid:62-->
</div><!--group repeats exit uid::63-->
</div><!--group exit uid:64-->
<div class='field annotatedField'><!--uid:65--><span class='tag annotation'>Symbol</span><span class='equals'>=</span><span class='value raw'>AUD/USD</span></div><!--uid:66-->
<div class='field annotatedField'><!--uid:67--><span class='tag annotation'>MDEntryPx</span><span class='equals'>=</span><span class='value raw'>1.12355</span></div><!--uid:68-->
</div><!--group repeat exit uid:69-->
<div class='group-repeat'><!--uid:70-->
<div class='group-repeat-number'><!--uid:71-->3.</div>
<div class='field annotatedField'><!--uid:72--><span class='tag annotation'>MDUpdateAction</span><span class='equals'>=</span><span class='value annotation'>NEW</span></div><!--uid:73-->
<div class='field annotatedField'><!--uid:74--><span class='tag annotation'>MDEntryType</span><span class='equals'>=</span><span class='value annotation'>OFFER</span></div><!--uid:75-->
<div class='group'><!--uid:76-->
<div class='field annotatedField'><!--uid:77--><span class='tag annotation'>NoPartyIDs</span><span class='equals'>=</span><span class='value raw'>2</span></div><!--uid:78-->
<div class='group-repeats'><!--uid:79-->
<div class='group-repeat'><!--uid:80-->
<div class='group-repeat-number'><!--uid:81-->1.</div>
<div class='field annotatedField'><!--uid:82--><span class='tag annotation'>PartyID</span><span class='equals'>=</span><span class='value raw'>Amy</span></div><!--uid:83-->
<div class='field annotatedField'><!--uid:84--><span class='tag annotation'>PartyIDSource</span><span class='equals'>=</span><span class='value annotation'>AUSTRALIAN_TAX_FILE_NUMBER</span></div><!--uid:85-->
</div><!--group repeat exit uid:86-->
<div class='group-repeat'><!--uid:87-->
<div class='group-repeat-number'><!--uid:88-->2.</div>
<div class='field annotatedField'><!--uid:89--><span class='tag annotation'>PartyID</span><span class='equals'>=</span><span class='value raw'>Milly</span></div><!--uid:90-->
<div class='field annotatedField'><!--uid:91--><span class='tag raw'>9999</span><span class='equals'>=</span><span class='value raw'>unknownField</span></div><!--uid:92-->
</div><!--group repeat exit uid:93-->
</div><!--group repeats exit uid::94-->
</div><!--group exit uid:95-->
<div class='field annotatedField'><!--uid:96--><span class='tag annotation'>Symbol</span><span class='equals'>=</span><span class='value raw'>AUD/USD</span></div><!--uid:97-->
<div class='field annotatedField'><!--uid:98--><span class='tag annotation'>MDEntryPx</span><span class='equals'>=</span><span class='value raw'>1.12355</span></div><!--uid:99-->
</div><!--group repeat exit uid:100-->
<div class='group-repeat'><!--uid:101-->
<div class='group-repeat-number'><!--uid:102-->4.</div>
<div class='field annotatedField'><!--uid:103--><span class='tag annotation'>MDUpdateAction</span><span class='equals'>=</span><span class='value annotation'>NEW</span></div><!--uid:104-->
<div class='field annotatedField'><!--uid:105--><span class='tag annotation'>MDEntryType</span><span class='equals'>=</span><span class='value annotation'>BID</span></div><!--uid:106-->
<div class='field annotatedField'><!--uid:107--><span class='tag annotation'>Symbol</span><span class='equals'>=</span><span class='value raw'>AUD/USD</span></div><!--uid:108-->
<div class='field annotatedField'><!--uid:109--><span class='tag annotation'>MDEntryPx</span><span class='equals'>=</span><span class='value raw'>1.12335</span></div><!--uid:110-->
<div class='group'><!--uid:111-->
<div class='field annotatedField'><!--uid:112--><span class='tag annotation'>NoPartyIDs</span><span class='equals'>=</span><span class='value raw'>2</span></div><!--uid:113-->
<div class='group-repeats'><!--uid:114-->
<div class='group-repeat'><!--uid:115-->
<div class='group-repeat-number'><!--uid:116-->1.</div>
<div class='field annotatedField'><!--uid:117--><span class='tag annotation'>PartyID</span><span class='equals'>=</span><span class='value raw'>Amy</span></div><!--uid:118-->
<div class='field annotatedField'><!--uid:119--><span class='tag annotation'>PartyIDSource</span><span class='equals'>=</span><span class='value annotation'>AUSTRALIAN_TAX_FILE_NUMBER</span></div><!--uid:120-->
</div><!--group repeat exit uid:121-->
<div class='group-repeat'><!--uid:122-->
<div class='group-repeat-number'><!--uid:123-->2.</div>
<div class='field annotatedField'><!--uid:124--><span class='tag annotation'>PartyID</span><span class='equals'>=</span><span class='value raw'>Milly</span></div><!--uid:125-->
</div><!--group repeat exit uid:126-->
</div><!--group repeats exit uid::127-->
</div><!--group exit uid:128-->
</div><!--group repeat exit uid:129-->
</div><!--group repeats exit uid::130-->
</div><!--group exit uid:131-->
<div class='group'><!--uid:132-->
<div class='field annotatedField'><!--uid:133--><span class='tag annotation'>NoRoutingIDs</span><span class='equals'>=</span><span class='value raw'>2</span></div><!--uid:134-->
<div class='group-repeats'><!--uid:135-->
<div class='group-repeat'><!--uid:136-->
<div class='group-repeat-number'><!--uid:137-->1.</div>
<div class='field annotatedField'><!--uid:138--><span class='tag annotation'>RoutingType</span><span class='equals'>=</span><span class='value annotation'>BLOCK_FIRM</span></div><!--uid:139-->
<div class='field annotatedField'><!--uid:140--><span class='tag annotation'>RoutingID</span><span class='equals'>=</span><span class='value raw'>routingId1</span></div><!--uid:141-->
</div><!--group repeat exit uid:142-->
<div class='group-repeat'><!--uid:143-->
<div class='group-repeat-number'><!--uid:144-->2.</div>
<div class='field annotatedField'><!--uid:145--><span class='tag annotation'>RoutingType</span><span class='equals'>=</span><span class='value annotation'>TARGET_LIST</span></div><!--uid:146-->
<div class='field annotatedField'><!--uid:147--><span class='tag annotation'>RoutingID</span><span class='equals'>=</span><span class='value raw'>routingId2</span></div><!--uid:148-->
</div><!--group repeat exit uid:149-->
</div><!--group repeats exit uid::150-->
</div><!--group exit uid:151-->
<div class='field annotatedField'><!--uid:152--><span class='tag annotation'>MDFeedType</span><span class='equals'>=</span><span class='value raw'>asdf</span></div><!--uid:153-->
</div>

<br/>"""
    }


    def 'test vertical non-aligned format - indentGroupRepeats - prices - excluding some fields'(){
        when:
        def lines = fixGrep.go('-e 268,448,55,215,217,270', VerticalTestUtil.PRICES_FIX)

        then:
        assert lines == """<div class='msg-header'>
================================================================================</br>
<span class='FgYellow'>MarketDataIncrementalRefresh</span><br/>
================================================================================
</div>
<div class='fields'>
<div class='field annotatedField'><!--uid:0--><span class='tag annotation'>MsgType</span><span class='equals'>=</span><span class='value annotation'>MARKETDATAINCREMENTALREFRESH</span></div><!--uid:1-->
<div class='field annotatedField'><!--uid:2--><span class='tag annotation'>MDReqID</span><span class='equals'>=</span><span class='value raw'>ABCD</span></div><!--uid:3-->
<div class='group'><!--uid:4-->
<div class='group-repeats'><!--uid:5-->
<div class='group-repeat'><!--uid:6-->
<div class='group-repeat-number'><!--uid:7-->1.</div>
<div class='field annotatedField'><!--uid:8--><span class='tag annotation'>MDUpdateAction</span><span class='equals'>=</span><span class='value annotation'>NEW</span></div><!--uid:9-->
<div class='field annotatedField'><!--uid:10--><span class='tag annotation'>MDEntryType</span><span class='equals'>=</span><span class='value annotation'>BID</span></div><!--uid:11-->
<div class='field annotatedField'><!--uid:12--><span class='tag raw'>9999</span><span class='equals'>=</span><span class='value raw'>unknownField</span></div><!--uid:13-->
<div class='group'><!--uid:14-->
<div class='field annotatedField'><!--uid:15--><span class='tag annotation'>NoPartyIDs</span><span class='equals'>=</span><span class='value raw'>2</span></div><!--uid:16-->
<div class='group-repeats'><!--uid:17-->
<div class='group-repeat'><!--uid:18-->
<div class='group-repeat-number'><!--uid:19-->1.</div>
<div class='field annotatedField'><!--uid:20--><span class='tag raw'>9999</span><span class='equals'>=</span><span class='value raw'>unknownField</span></div><!--uid:21-->
<div class='field annotatedField'><!--uid:22--><span class='tag annotation'>PartyIDSource</span><span class='equals'>=</span><span class='value annotation'>AUSTRALIAN_TAX_FILE_NUMBER</span></div><!--uid:23-->
</div><!--group repeat exit uid:24-->
<div class='group-repeat'><!--uid:25-->
<div class='group-repeat-number'><!--uid:26-->2.</div>
</div><!--group repeat exit uid:27-->
</div><!--group repeats exit uid::28-->
</div><!--group exit uid:29-->
</div><!--group repeat exit uid:30-->
<div class='group-repeat'><!--uid:31-->
<div class='group-repeat-number'><!--uid:32-->2.</div>
<div class='field annotatedField'><!--uid:33--><span class='tag annotation'>MDUpdateAction</span><span class='equals'>=</span><span class='value annotation'>NEW</span></div><!--uid:34-->
<div class='field annotatedField'><!--uid:35--><span class='tag annotation'>MDEntryType</span><span class='equals'>=</span><span class='value annotation'>OFFER</span></div><!--uid:36-->
<div class='group'><!--uid:37-->
<div class='field annotatedField'><!--uid:38--><span class='tag annotation'>NoPartyIDs</span><span class='equals'>=</span><span class='value raw'>2</span></div><!--uid:39-->
<div class='group-repeats'><!--uid:40-->
<div class='group-repeat'><!--uid:41-->
<div class='group-repeat-number'><!--uid:42-->1.</div>
<div class='field annotatedField'><!--uid:43--><span class='tag annotation'>PartyIDSource</span><span class='equals'>=</span><span class='value annotation'>AUSTRALIAN_TAX_FILE_NUMBER</span></div><!--uid:44-->
</div><!--group repeat exit uid:45-->
<div class='group-repeat'><!--uid:46-->
<div class='group-repeat-number'><!--uid:47-->2.</div>
</div><!--group repeat exit uid:48-->
</div><!--group repeats exit uid::49-->
</div><!--group exit uid:50-->
</div><!--group repeat exit uid:51-->
<div class='group-repeat'><!--uid:52-->
<div class='group-repeat-number'><!--uid:53-->3.</div>
<div class='field annotatedField'><!--uid:54--><span class='tag annotation'>MDUpdateAction</span><span class='equals'>=</span><span class='value annotation'>NEW</span></div><!--uid:55-->
<div class='field annotatedField'><!--uid:56--><span class='tag annotation'>MDEntryType</span><span class='equals'>=</span><span class='value annotation'>OFFER</span></div><!--uid:57-->
<div class='group'><!--uid:58-->
<div class='field annotatedField'><!--uid:59--><span class='tag annotation'>NoPartyIDs</span><span class='equals'>=</span><span class='value raw'>2</span></div><!--uid:60-->
<div class='group-repeats'><!--uid:61-->
<div class='group-repeat'><!--uid:62-->
<div class='group-repeat-number'><!--uid:63-->1.</div>
<div class='field annotatedField'><!--uid:64--><span class='tag annotation'>PartyIDSource</span><span class='equals'>=</span><span class='value annotation'>AUSTRALIAN_TAX_FILE_NUMBER</span></div><!--uid:65-->
</div><!--group repeat exit uid:66-->
<div class='group-repeat'><!--uid:67-->
<div class='group-repeat-number'><!--uid:68-->2.</div>
<div class='field annotatedField'><!--uid:69--><span class='tag raw'>9999</span><span class='equals'>=</span><span class='value raw'>unknownField</span></div><!--uid:70-->
</div><!--group repeat exit uid:71-->
</div><!--group repeats exit uid::72-->
</div><!--group exit uid:73-->
</div><!--group repeat exit uid:74-->
<div class='group-repeat'><!--uid:75-->
<div class='group-repeat-number'><!--uid:76-->4.</div>
<div class='field annotatedField'><!--uid:77--><span class='tag annotation'>MDUpdateAction</span><span class='equals'>=</span><span class='value annotation'>NEW</span></div><!--uid:78-->
<div class='field annotatedField'><!--uid:79--><span class='tag annotation'>MDEntryType</span><span class='equals'>=</span><span class='value annotation'>BID</span></div><!--uid:80-->
<div class='group'><!--uid:81-->
<div class='field annotatedField'><!--uid:82--><span class='tag annotation'>NoPartyIDs</span><span class='equals'>=</span><span class='value raw'>2</span></div><!--uid:83-->
<div class='group-repeats'><!--uid:84-->
<div class='group-repeat'><!--uid:85-->
<div class='group-repeat-number'><!--uid:86-->1.</div>
<div class='field annotatedField'><!--uid:87--><span class='tag annotation'>PartyIDSource</span><span class='equals'>=</span><span class='value annotation'>AUSTRALIAN_TAX_FILE_NUMBER</span></div><!--uid:88-->
</div><!--group repeat exit uid:89-->
<div class='group-repeat'><!--uid:90-->
<div class='group-repeat-number'><!--uid:91-->2.</div>
</div><!--group repeat exit uid:92-->
</div><!--group repeats exit uid::93-->
</div><!--group exit uid:94-->
</div><!--group repeat exit uid:95-->
</div><!--group repeats exit uid::96-->
</div><!--group exit uid:97-->
<div class='group'><!--uid:98-->
<div class='group-repeats'><!--uid:99-->
<div class='group-repeat'><!--uid:100-->
<div class='group-repeat-number'><!--uid:101-->1.</div>
<div class='field annotatedField'><!--uid:102--><span class='tag annotation'>RoutingType</span><span class='equals'>=</span><span class='value annotation'>BLOCK_FIRM</span></div><!--uid:103-->
</div><!--group repeat exit uid:104-->
<div class='group-repeat'><!--uid:105-->
<div class='group-repeat-number'><!--uid:106-->2.</div>
<div class='field annotatedField'><!--uid:107--><span class='tag annotation'>RoutingType</span><span class='equals'>=</span><span class='value annotation'>TARGET_LIST</span></div><!--uid:108-->
</div><!--group repeat exit uid:109-->
</div><!--group repeats exit uid::110-->
</div><!--group exit uid:111-->
<div class='field annotatedField'><!--uid:112--><span class='tag annotation'>MDFeedType</span><span class='equals'>=</span><span class='value raw'>asdf</span></div><!--uid:113-->
</div>

<br/>"""
    }

    def 'test vertical non-aligned format - DO NOT indentGroupRepeats - prices'(){
        when:
        def lines = fixGrep.go('--indent-group-repeats=false', VerticalTestUtil.PRICES_FIX)

        then:
        assert lines == """<div class='msg-header'>
================================================================================</br>
<span class='FgYellow'>MarketDataIncrementalRefresh</span><br/>
================================================================================
</div>
<div class='fields'>
<div class='field annotatedField'><!--uid:0--><span class='tag annotation'>MsgType</span><span class='equals'>=</span><span class='value annotation'>MARKETDATAINCREMENTALREFRESH</span></div><!--uid:1-->
<div class='field annotatedField'><!--uid:2--><span class='tag annotation'>MDReqID</span><span class='equals'>=</span><span class='value raw'>ABCD</span></div><!--uid:3-->
<div class='field annotatedField'><!--uid:4--><span class='tag annotation'>NoMDEntries</span><span class='equals'>=</span><span class='value raw'>4</span></div><!--uid:5-->
<div class='field annotatedField'><!--uid:6--><span class='tag annotation'>MDUpdateAction</span><span class='equals'>=</span><span class='value annotation'>NEW</span></div><!--uid:7-->
<div class='field annotatedField'><!--uid:8--><span class='tag annotation'>MDEntryType</span><span class='equals'>=</span><span class='value annotation'>BID</span></div><!--uid:9-->
<div class='field annotatedField'><!--uid:10--><span class='tag raw'>9999</span><span class='equals'>=</span><span class='value raw'>unknownField</span></div><!--uid:11-->
<div class='field annotatedField'><!--uid:12--><span class='tag annotation'>Symbol</span><span class='equals'>=</span><span class='value raw'>AUD/USD</span></div><!--uid:13-->
<div class='field annotatedField'><!--uid:14--><span class='tag annotation'>MDEntryPx</span><span class='equals'>=</span><span class='value raw'>1.12345</span></div><!--uid:15-->
<div class='field annotatedField'><!--uid:16--><span class='tag annotation'>NoPartyIDs</span><span class='equals'>=</span><span class='value raw'>2</span></div><!--uid:17-->
<div class='field annotatedField'><!--uid:18--><span class='tag annotation'>PartyID</span><span class='equals'>=</span><span class='value raw'>Ben</span></div><!--uid:19-->
<div class='field annotatedField'><!--uid:20--><span class='tag raw'>9999</span><span class='equals'>=</span><span class='value raw'>unknownField</span></div><!--uid:21-->
<div class='field annotatedField'><!--uid:22--><span class='tag annotation'>PartyIDSource</span><span class='equals'>=</span><span class='value annotation'>AUSTRALIAN_TAX_FILE_NUMBER</span></div><!--uid:23-->
<div class='field annotatedField'><!--uid:24--><span class='tag annotation'>PartyID</span><span class='equals'>=</span><span class='value raw'>Andy</span></div><!--uid:25-->
<div class='field annotatedField'><!--uid:26--><span class='tag annotation'>MDUpdateAction</span><span class='equals'>=</span><span class='value annotation'>NEW</span></div><!--uid:27-->
<div class='field annotatedField'><!--uid:28--><span class='tag annotation'>MDEntryType</span><span class='equals'>=</span><span class='value annotation'>OFFER</span></div><!--uid:29-->
<div class='field annotatedField'><!--uid:30--><span class='tag annotation'>NoPartyIDs</span><span class='equals'>=</span><span class='value raw'>2</span></div><!--uid:31-->
<div class='field annotatedField'><!--uid:32--><span class='tag annotation'>PartyID</span><span class='equals'>=</span><span class='value raw'>Amy</span></div><!--uid:33-->
<div class='field annotatedField'><!--uid:34--><span class='tag annotation'>PartyIDSource</span><span class='equals'>=</span><span class='value annotation'>AUSTRALIAN_TAX_FILE_NUMBER</span></div><!--uid:35-->
<div class='field annotatedField'><!--uid:36--><span class='tag annotation'>PartyID</span><span class='equals'>=</span><span class='value raw'>Milly</span></div><!--uid:37-->
<div class='field annotatedField'><!--uid:38--><span class='tag annotation'>Symbol</span><span class='equals'>=</span><span class='value raw'>AUD/USD</span></div><!--uid:39-->
<div class='field annotatedField'><!--uid:40--><span class='tag annotation'>MDEntryPx</span><span class='equals'>=</span><span class='value raw'>1.12355</span></div><!--uid:41-->
<div class='field annotatedField'><!--uid:42--><span class='tag annotation'>MDUpdateAction</span><span class='equals'>=</span><span class='value annotation'>NEW</span></div><!--uid:43-->
<div class='field annotatedField'><!--uid:44--><span class='tag annotation'>MDEntryType</span><span class='equals'>=</span><span class='value annotation'>OFFER</span></div><!--uid:45-->
<div class='field annotatedField'><!--uid:46--><span class='tag annotation'>NoPartyIDs</span><span class='equals'>=</span><span class='value raw'>2</span></div><!--uid:47-->
<div class='field annotatedField'><!--uid:48--><span class='tag annotation'>PartyID</span><span class='equals'>=</span><span class='value raw'>Amy</span></div><!--uid:49-->
<div class='field annotatedField'><!--uid:50--><span class='tag annotation'>PartyIDSource</span><span class='equals'>=</span><span class='value annotation'>AUSTRALIAN_TAX_FILE_NUMBER</span></div><!--uid:51-->
<div class='field annotatedField'><!--uid:52--><span class='tag annotation'>PartyID</span><span class='equals'>=</span><span class='value raw'>Milly</span></div><!--uid:53-->
<div class='field annotatedField'><!--uid:54--><span class='tag raw'>9999</span><span class='equals'>=</span><span class='value raw'>unknownField</span></div><!--uid:55-->
<div class='field annotatedField'><!--uid:56--><span class='tag annotation'>Symbol</span><span class='equals'>=</span><span class='value raw'>AUD/USD</span></div><!--uid:57-->
<div class='field annotatedField'><!--uid:58--><span class='tag annotation'>MDEntryPx</span><span class='equals'>=</span><span class='value raw'>1.12355</span></div><!--uid:59-->
<div class='field annotatedField'><!--uid:60--><span class='tag annotation'>MDUpdateAction</span><span class='equals'>=</span><span class='value annotation'>NEW</span></div><!--uid:61-->
<div class='field annotatedField'><!--uid:62--><span class='tag annotation'>MDEntryType</span><span class='equals'>=</span><span class='value annotation'>BID</span></div><!--uid:63-->
<div class='field annotatedField'><!--uid:64--><span class='tag annotation'>Symbol</span><span class='equals'>=</span><span class='value raw'>AUD/USD</span></div><!--uid:65-->
<div class='field annotatedField'><!--uid:66--><span class='tag annotation'>MDEntryPx</span><span class='equals'>=</span><span class='value raw'>1.12335</span></div><!--uid:67-->
<div class='field annotatedField'><!--uid:68--><span class='tag annotation'>NoPartyIDs</span><span class='equals'>=</span><span class='value raw'>2</span></div><!--uid:69-->
<div class='field annotatedField'><!--uid:70--><span class='tag annotation'>PartyID</span><span class='equals'>=</span><span class='value raw'>Amy</span></div><!--uid:71-->
<div class='field annotatedField'><!--uid:72--><span class='tag annotation'>PartyIDSource</span><span class='equals'>=</span><span class='value annotation'>AUSTRALIAN_TAX_FILE_NUMBER</span></div><!--uid:73-->
<div class='field annotatedField'><!--uid:74--><span class='tag annotation'>PartyID</span><span class='equals'>=</span><span class='value raw'>Milly</span></div><!--uid:75-->
<div class='field annotatedField'><!--uid:76--><span class='tag annotation'>NoRoutingIDs</span><span class='equals'>=</span><span class='value raw'>2</span></div><!--uid:77-->
<div class='field annotatedField'><!--uid:78--><span class='tag annotation'>RoutingType</span><span class='equals'>=</span><span class='value annotation'>BLOCK_FIRM</span></div><!--uid:79-->
<div class='field annotatedField'><!--uid:80--><span class='tag annotation'>RoutingID</span><span class='equals'>=</span><span class='value raw'>routingId1</span></div><!--uid:81-->
<div class='field annotatedField'><!--uid:82--><span class='tag annotation'>RoutingType</span><span class='equals'>=</span><span class='value annotation'>TARGET_LIST</span></div><!--uid:83-->
<div class='field annotatedField'><!--uid:84--><span class='tag annotation'>RoutingID</span><span class='equals'>=</span><span class='value raw'>routingId2</span></div><!--uid:85-->
<div class='field annotatedField'><!--uid:86--><span class='tag annotation'>MDFeedType</span><span class='equals'>=</span><span class='value raw'>asdf</span></div><!--uid:87-->
</div>

<br/>"""
    }
}
