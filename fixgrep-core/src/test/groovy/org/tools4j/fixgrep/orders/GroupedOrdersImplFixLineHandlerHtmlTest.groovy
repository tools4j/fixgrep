package org.tools4j.fixgrep.orders

import org.tools4j.fix.ExecType
import org.tools4j.fixgrep.main.FixGrepMain
import org.tools4j.util.CircularBufferedReaderWriter
import org.tools4j.utils.ArgsAsString
import spock.lang.Shared
import spock.lang.Specification

/**
 * User: benjw
 * Date: 9/28/2018
 * Time: 6:15 AM
 */
class GroupedOrdersImplFixLineHandlerHtmlTest extends Specification {
    private final static int msgType = 35
    private final static int clOrdId = 11
    private final static int origClOrdId = 41
    private final static int senderCompId = 49
    private final static int targetCompId = 56
    private final static int orderId = 37
    private final static int execType = 150
    private final static int ordStatus = 39
    private final static int cxlRejResponseTo = 434

    private final static String clientToServer = "49=CLIENT${a}56=SERVER"
    private final static String serverToClient = "49=SERVER${a}56=CLIENT"

    private final static String clientToServer2 = "49=CLIENT2${a}56=SERVER2"
    private final static String serverToClient2 = "49=SERVER2${a}56=CLIENT2"

    static final char a = '\u0001'

    @Shared private String testOverrides;

    private static File newAssertionsFile = new File("new-assertions.txt")
    private static File resultsFile = new File("results.txt")
    private static boolean logResultsToFile = false
    private static boolean logNewAssertionsToFile = false
    private static boolean launchResultInBrowser = false

    def setupSpec() {
        if(logNewAssertionsToFile) deleteAndCreateNewFile(newAssertionsFile)
        if(logResultsToFile) deleteAndCreateNewFile(resultsFile)
        testOverrides = ' --html -p -O'
    }
    
    def "no order messages found"() {
        when:
        final String fix =
                """${msgType}=blah1${a}${clientToServer}${a}${clOrdId}=ABC")\n
                   ${msgType}=blah2${a}${clientToServer}${a}${clOrdId}=ABC"""

        def lines = parseToLines('--output-format-horizontal-html "${senderToTargetCompIdDirection} [${msgTypeName}] ${msgFix}" --suppress-bold-tags-and-values true', fix)

        then:
        assert lines == "No order messages found"
    }

    def "simple non-formatted"() {
        when:
        final String fix = "${msgType}=D${a}${clientToServer}${a}${clOrdId}=ABC"

        def lines = parseToLines('--output-format-horizontal-html "${senderToTargetCompIdDirection} [${msgTypeName}] ${msgFix}" --suppress-bold-tags-and-values true', fix)

        then:
        assert lines == """ORPHAN MESSAGES:<br/>
<div class='msg orphan-message'>CLIENT->SERVER [NewOrderSingle] <div class='fields'><span class='field annotatedField'><span class='tag annotation'>[MsgType]</span><span class='tag raw'>35</span><span class='equals'>=</span><span class='value raw'>D</span><span class='value annotation'>[NEWORDERSINGLE]</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[SenderCompID]</span><span class='tag raw'>49</span><span class='equals'>=</span><span class='value raw'>CLIENT</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[TargetCompID]</span><span class='tag raw'>56</span><span class='equals'>=</span><span class='value raw'>SERVER</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[ClOrdID]</span><span class='tag raw'>11</span><span class='equals'>=</span><span class='value raw'>ABC</span></span></div></div>"""
    }

    def "simple formatted"() {
        when:
        final String fix = "35=D${a}49=CLIENT${a}56=SERVER${a}11=ABC"

        def lines = parseToLines('--output-format-horizontal-html "${senderToTargetCompIdDirection} ${msgColor}[${msgTypeName}]${colorReset} ${msgFix}" --suppress-bold-tags-and-values false', fix)

        then:
        assert lines == """ORPHAN MESSAGES:<br/>
<div class='msg orphan-message'>CLIENT->SERVER <span class='FgCyan'>[NewOrderSingle]</span> <div class='fields'><span class='field annotatedField'><span class='tag annotation'>[MsgType]</span><span class='tag raw bold'>35</span><span class='equals bold'>=</span><span class='value raw bold'>D</span><span class='value annotation'>[NEWORDERSINGLE]</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[SenderCompID]</span><span class='tag raw bold'>49</span><span class='equals bold'>=</span><span class='value raw bold'>CLIENT</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[TargetCompID]</span><span class='tag raw bold'>56</span><span class='equals bold'>=</span><span class='value raw bold'>SERVER</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[ClOrdID]</span><span class='tag raw bold'>11</span><span class='equals bold'>=</span><span class='value raw bold'>ABC</span></span></div></div>"""
    }

    def "three NewOrderSingles"() {
        when:
        final String fix = """${msgType}=D${a}${clientToServer}${a}${clOrdId}=ABC\n
                              ${msgType}=D${a}${clientToServer}${a}${clOrdId}=DEF\n
                              ${msgType}=D${a}${clientToServer}${a}${clOrdId}=GHI"""

        def lines = parseToLines('--output-format-horizontal-html "${senderToTargetCompIdDirection} [${msgTypeName}] ${msgFix}" --suppress-bold-tags-and-values true', fix)

        then:
        assert lines == """ORPHAN MESSAGES:<br/>
<div class='msg orphan-message'>CLIENT->SERVER [NewOrderSingle] <div class='fields'><span class='field annotatedField'><span class='tag annotation'>[MsgType]</span><span class='tag raw'>35</span><span class='equals'>=</span><span class='value raw'>D</span><span class='value annotation'>[NEWORDERSINGLE]</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[SenderCompID]</span><span class='tag raw'>49</span><span class='equals'>=</span><span class='value raw'>CLIENT</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[TargetCompID]</span><span class='tag raw'>56</span><span class='equals'>=</span><span class='value raw'>SERVER</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[ClOrdID]</span><span class='tag raw'>11</span><span class='equals'>=</span><span class='value raw'>ABC</span></span></div></div>
<div class='msg orphan-message'>CLIENT->SERVER [NewOrderSingle] <div class='fields'><span class='field annotatedField'><span class='tag annotation'>[MsgType]</span><span class='tag raw'>35</span><span class='equals'>=</span><span class='value raw'>D</span><span class='value annotation'>[NEWORDERSINGLE]</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[SenderCompID]</span><span class='tag raw'>49</span><span class='equals'>=</span><span class='value raw'>CLIENT</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[TargetCompID]</span><span class='tag raw'>56</span><span class='equals'>=</span><span class='value raw'>SERVER</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[ClOrdID]</span><span class='tag raw'>11</span><span class='equals'>=</span><span class='value raw'>DEF</span></span></div></div>
<div class='msg orphan-message'>CLIENT->SERVER [NewOrderSingle] <div class='fields'><span class='field annotatedField'><span class='tag annotation'>[MsgType]</span><span class='tag raw'>35</span><span class='equals'>=</span><span class='value raw'>D</span><span class='value annotation'>[NEWORDERSINGLE]</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[SenderCompID]</span><span class='tag raw'>49</span><span class='equals'>=</span><span class='value raw'>CLIENT</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[TargetCompID]</span><span class='tag raw'>56</span><span class='equals'>=</span><span class='value raw'>SERVER</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[ClOrdID]</span><span class='tag raw'>11</span><span class='equals'>=</span><span class='value raw'>GHI</span></span></div></div>"""
    }

    def "single order"() {
        when:
        final String fix = """${msgType}=D${a}${clientToServer}${a}${clOrdId}=ABC\n
                                ${msgType}=8${a}${serverToClient}${a}${execType}=${ExecType.New}${a}${clOrdId}=ABC${a}${orderId}=123\n
                                ${msgType}=8${a}${serverToClient}${a}${execType}=${ExecType.PartialFill}${a}${clOrdId}=ABC${a}${orderId}=123\n
                                ${msgType}=8${a}${serverToClient}${a}${execType}=${ExecType.PartialFill}${a}${orderId}=123\n
                                ${msgType}=G${a}${clientToServer}${a}${origClOrdId}=ABC${a}${clOrdId}=DEF${a}${orderId}=123\n
                                ${msgType}=8${a}${serverToClient}${a}${execType}=${ExecType.PartialFill}${a}${orderId}=123\n
                                ${msgType}=G${a}${clientToServer}${a}${origClOrdId}=DEF${a}${clOrdId}=GHI${a}${ordStatus}=8${a}${cxlRejResponseTo}=2\n
                                ${msgType}=9${a}${serverToClient}${a}${origClOrdId}=DEF${a}${clOrdId}=GHI\n
                                ${msgType}=8${a}${serverToClient}${a}${execType}=${ExecType.PartialFill}${a}${orderId}=123\n"""

        def lines = parseToLines('--output-format-horizontal-html "${senderToTargetCompIdDirection} [${msgTypeName}] ${msgFix}" --suppress-bold-tags-and-values true', fix)

        then:
        assert lines == """<div class='order-header'>
=================================================================================<br/>
ORDER orderId:123 clOrdId:ABC<br/>
================================================================================</div>
<div class='msg order-request'>CLIENT->SERVER [NewOrderSingle] <div class='fields'><span class='field annotatedField'><span class='tag annotation'>[MsgType]</span><span class='tag raw'>35</span><span class='equals'>=</span><span class='value raw'>D</span><span class='value annotation'>[NEWORDERSINGLE]</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[SenderCompID]</span><span class='tag raw'>49</span><span class='equals'>=</span><span class='value raw'>CLIENT</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[TargetCompID]</span><span class='tag raw'>56</span><span class='equals'>=</span><span class='value raw'>SERVER</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[ClOrdID]</span><span class='tag raw'>11</span><span class='equals'>=</span><span class='value raw'>ABC</span></span></div></div>
<div class='msg order-response'>SERVER->CLIENT [Exec.New] <div class='fields'><span class='field annotatedField'><span class='tag annotation'>[MsgType]</span><span class='tag raw'>35</span><span class='equals'>=</span><span class='value raw'>8</span><span class='value annotation'>[EXECUTIONREPORT]</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[SenderCompID]</span><span class='tag raw'>49</span><span class='equals'>=</span><span class='value raw'>SERVER</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[TargetCompID]</span><span class='tag raw'>56</span><span class='equals'>=</span><span class='value raw'>CLIENT</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[ExecType]</span><span class='tag raw'>150</span><span class='equals'>=</span><span class='value raw'>New</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[ClOrdID]</span><span class='tag raw'>11</span><span class='equals'>=</span><span class='value raw'>ABC</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[OrderID]</span><span class='tag raw'>37</span><span class='equals'>=</span><span class='value raw'>123</span></span></div></div>
<div class='msg order-response'>SERVER->CLIENT [Exec.PartialFill] <div class='fields'><span class='field annotatedField'><span class='tag annotation'>[MsgType]</span><span class='tag raw'>35</span><span class='equals'>=</span><span class='value raw'>8</span><span class='value annotation'>[EXECUTIONREPORT]</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[SenderCompID]</span><span class='tag raw'>49</span><span class='equals'>=</span><span class='value raw'>SERVER</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[TargetCompID]</span><span class='tag raw'>56</span><span class='equals'>=</span><span class='value raw'>CLIENT</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[ExecType]</span><span class='tag raw'>150</span><span class='equals'>=</span><span class='value raw'>PartialFill</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[ClOrdID]</span><span class='tag raw'>11</span><span class='equals'>=</span><span class='value raw'>ABC</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[OrderID]</span><span class='tag raw'>37</span><span class='equals'>=</span><span class='value raw'>123</span></span></div></div>
<div class='msg order-response'>SERVER->CLIENT [Exec.PartialFill] <div class='fields'><span class='field annotatedField'><span class='tag annotation'>[MsgType]</span><span class='tag raw'>35</span><span class='equals'>=</span><span class='value raw'>8</span><span class='value annotation'>[EXECUTIONREPORT]</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[SenderCompID]</span><span class='tag raw'>49</span><span class='equals'>=</span><span class='value raw'>SERVER</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[TargetCompID]</span><span class='tag raw'>56</span><span class='equals'>=</span><span class='value raw'>CLIENT</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[ExecType]</span><span class='tag raw'>150</span><span class='equals'>=</span><span class='value raw'>PartialFill</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[OrderID]</span><span class='tag raw'>37</span><span class='equals'>=</span><span class='value raw'>123</span></span></div></div>
<div class='msg order-request'>CLIENT->SERVER [OrderCancelReplaceRequest] <div class='fields'><span class='field annotatedField'><span class='tag annotation'>[MsgType]</span><span class='tag raw'>35</span><span class='equals'>=</span><span class='value raw'>G</span><span class='value annotation'>[ORDERCANCELREPLACEREQUEST]</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[SenderCompID]</span><span class='tag raw'>49</span><span class='equals'>=</span><span class='value raw'>CLIENT</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[TargetCompID]</span><span class='tag raw'>56</span><span class='equals'>=</span><span class='value raw'>SERVER</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[OrigClOrdID]</span><span class='tag raw'>41</span><span class='equals'>=</span><span class='value raw'>ABC</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[ClOrdID]</span><span class='tag raw'>11</span><span class='equals'>=</span><span class='value raw'>DEF</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[OrderID]</span><span class='tag raw'>37</span><span class='equals'>=</span><span class='value raw'>123</span></span></div></div>
<div class='msg order-response'>SERVER->CLIENT [Exec.PartialFill] <div class='fields'><span class='field annotatedField'><span class='tag annotation'>[MsgType]</span><span class='tag raw'>35</span><span class='equals'>=</span><span class='value raw'>8</span><span class='value annotation'>[EXECUTIONREPORT]</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[SenderCompID]</span><span class='tag raw'>49</span><span class='equals'>=</span><span class='value raw'>SERVER</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[TargetCompID]</span><span class='tag raw'>56</span><span class='equals'>=</span><span class='value raw'>CLIENT</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[ExecType]</span><span class='tag raw'>150</span><span class='equals'>=</span><span class='value raw'>PartialFill</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[OrderID]</span><span class='tag raw'>37</span><span class='equals'>=</span><span class='value raw'>123</span></span></div></div>
<div class='msg order-request'>CLIENT->SERVER [OrderCancelReplaceRequest] <div class='fields'><span class='field annotatedField'><span class='tag annotation'>[MsgType]</span><span class='tag raw'>35</span><span class='equals'>=</span><span class='value raw'>G</span><span class='value annotation'>[ORDERCANCELREPLACEREQUEST]</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[SenderCompID]</span><span class='tag raw'>49</span><span class='equals'>=</span><span class='value raw'>CLIENT</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[TargetCompID]</span><span class='tag raw'>56</span><span class='equals'>=</span><span class='value raw'>SERVER</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[OrigClOrdID]</span><span class='tag raw'>41</span><span class='equals'>=</span><span class='value raw'>DEF</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[ClOrdID]</span><span class='tag raw'>11</span><span class='equals'>=</span><span class='value raw'>GHI</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[OrdStatus]</span><span class='tag raw'>39</span><span class='equals'>=</span><span class='value raw'>8</span><span class='value annotation'>[REJECTED]</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[CxlRejResponseTo]</span><span class='tag raw'>434</span><span class='equals'>=</span><span class='value raw'>2</span><span class='value annotation'>[ORDER_CANCELREPLACE_REQUEST]</span></span></div></div>
<div class='msg order-response'>SERVER->CLIENT [OrderCancelReject] <div class='fields'><span class='field annotatedField'><span class='tag annotation'>[MsgType]</span><span class='tag raw'>35</span><span class='equals'>=</span><span class='value raw'>9</span><span class='value annotation'>[ORDERCANCELREJECT]</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[SenderCompID]</span><span class='tag raw'>49</span><span class='equals'>=</span><span class='value raw'>SERVER</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[TargetCompID]</span><span class='tag raw'>56</span><span class='equals'>=</span><span class='value raw'>CLIENT</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[OrigClOrdID]</span><span class='tag raw'>41</span><span class='equals'>=</span><span class='value raw'>DEF</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[ClOrdID]</span><span class='tag raw'>11</span><span class='equals'>=</span><span class='value raw'>GHI</span></span></div></div>
<div class='msg order-response'>SERVER->CLIENT [Exec.PartialFill] <div class='fields'><span class='field annotatedField'><span class='tag annotation'>[MsgType]</span><span class='tag raw'>35</span><span class='equals'>=</span><span class='value raw'>8</span><span class='value annotation'>[EXECUTIONREPORT]</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[SenderCompID]</span><span class='tag raw'>49</span><span class='equals'>=</span><span class='value raw'>SERVER</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[TargetCompID]</span><span class='tag raw'>56</span><span class='equals'>=</span><span class='value raw'>CLIENT</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[ExecType]</span><span class='tag raw'>150</span><span class='equals'>=</span><span class='value raw'>PartialFill</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[OrderID]</span><span class='tag raw'>37</span><span class='equals'>=</span><span class='value raw'>123</span></span></div></div>
<br/>"""
    }

    def "single order - no sender or target compIds"() {
        when:
        final String fix = """${msgType}=D${a}${clientToServer}${a}${clOrdId}=ABC\n
                                ${msgType}=8${a}${serverToClient}${a}${execType}=${ExecType.New}${a}${clOrdId}=ABC${a}${orderId}=123\n
                                ${msgType}=8${a}${serverToClient}${a}${execType}=${ExecType.PartialFill}${a}${clOrdId}=ABC${a}${orderId}=123\n
                                ${msgType}=8${a}${serverToClient}${a}${execType}=${ExecType.PartialFill}${a}${orderId}=123\n
                                ${msgType}=G${a}${clientToServer}${a}${origClOrdId}=ABC${a}${clOrdId}=DEF${a}${orderId}=123\n
                                ${msgType}=8${a}${serverToClient}${a}${execType}=${ExecType.PartialFill}${a}${orderId}=123\n
                                ${msgType}=G${a}${clientToServer}${a}${origClOrdId}=DEF${a}${clOrdId}=GHI${a}${ordStatus}=8${a}${cxlRejResponseTo}=2\n
                                ${msgType}=9${a}${serverToClient}${a}${origClOrdId}=DEF${a}${clOrdId}=GHI\n
                                ${msgType}=8${a}${serverToClient}${a}${execType}=${ExecType.PartialFill}${a}${orderId}=123\n"""

        def lines = parseToLines('--output-format-horizontal-html "${senderToTargetCompIdDirection} [${msgTypeName}] ${msgFix}" --suppress-bold-tags-and-values true -e 35,49,56,150', fix)

        then:
        assert lines == """<div class='order-header'>
=================================================================================<br/>
ORDER orderId:123 clOrdId:ABC<br/>
================================================================================</div>
<div class='msg order-request'>CLIENT->SERVER [NewOrderSingle] <div class='fields'><span class='field annotatedField'><span class='tag annotation'>[ClOrdID]</span><span class='tag raw'>11</span><span class='equals'>=</span><span class='value raw'>ABC</span></span></div></div>
<div class='msg order-response'>SERVER->CLIENT [Exec.New] <div class='fields'><span class='field annotatedField'><span class='tag annotation'>[ClOrdID]</span><span class='tag raw'>11</span><span class='equals'>=</span><span class='value raw'>ABC</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[OrderID]</span><span class='tag raw'>37</span><span class='equals'>=</span><span class='value raw'>123</span></span></div></div>
<div class='msg order-response'>SERVER->CLIENT [Exec.PartialFill] <div class='fields'><span class='field annotatedField'><span class='tag annotation'>[ClOrdID]</span><span class='tag raw'>11</span><span class='equals'>=</span><span class='value raw'>ABC</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[OrderID]</span><span class='tag raw'>37</span><span class='equals'>=</span><span class='value raw'>123</span></span></div></div>
<div class='msg order-response'>SERVER->CLIENT [Exec.PartialFill] <div class='fields'><span class='field annotatedField'><span class='tag annotation'>[OrderID]</span><span class='tag raw'>37</span><span class='equals'>=</span><span class='value raw'>123</span></span></div></div>
<div class='msg order-request'>CLIENT->SERVER [OrderCancelReplaceRequest] <div class='fields'><span class='field annotatedField'><span class='tag annotation'>[OrigClOrdID]</span><span class='tag raw'>41</span><span class='equals'>=</span><span class='value raw'>ABC</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[ClOrdID]</span><span class='tag raw'>11</span><span class='equals'>=</span><span class='value raw'>DEF</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[OrderID]</span><span class='tag raw'>37</span><span class='equals'>=</span><span class='value raw'>123</span></span></div></div>
<div class='msg order-response'>SERVER->CLIENT [Exec.PartialFill] <div class='fields'><span class='field annotatedField'><span class='tag annotation'>[OrderID]</span><span class='tag raw'>37</span><span class='equals'>=</span><span class='value raw'>123</span></span></div></div>
<div class='msg order-request'>CLIENT->SERVER [OrderCancelReplaceRequest] <div class='fields'><span class='field annotatedField'><span class='tag annotation'>[OrigClOrdID]</span><span class='tag raw'>41</span><span class='equals'>=</span><span class='value raw'>DEF</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[ClOrdID]</span><span class='tag raw'>11</span><span class='equals'>=</span><span class='value raw'>GHI</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[OrdStatus]</span><span class='tag raw'>39</span><span class='equals'>=</span><span class='value raw'>8</span><span class='value annotation'>[REJECTED]</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[CxlRejResponseTo]</span><span class='tag raw'>434</span><span class='equals'>=</span><span class='value raw'>2</span><span class='value annotation'>[ORDER_CANCELREPLACE_REQUEST]</span></span></div></div>
<div class='msg order-response'>SERVER->CLIENT [OrderCancelReject] <div class='fields'><span class='field annotatedField'><span class='tag annotation'>[OrigClOrdID]</span><span class='tag raw'>41</span><span class='equals'>=</span><span class='value raw'>DEF</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[ClOrdID]</span><span class='tag raw'>11</span><span class='equals'>=</span><span class='value raw'>GHI</span></span></div></div>
<div class='msg order-response'>SERVER->CLIENT [Exec.PartialFill] <div class='fields'><span class='field annotatedField'><span class='tag annotation'>[OrderID]</span><span class='tag raw'>37</span><span class='equals'>=</span><span class='value raw'>123</span></span></div></div>
<br/>"""
    }

    def "another single order, with orphan messages"() {
        when:
        final String fix = """${msgType}=D${a}${clientToServer2}${a}${clOrdId}=ABC
                                ${msgType}=8${a}${serverToClient2}${a}${execType}=${ExecType.New}${a}${clOrdId}=ABC${a}${orderId}=123\n
                                ${msgType}=8${a}${serverToClient2}${a}${execType}=${ExecType.PartialFill}${a}${clOrdId}=ABC${a}${orderId}=123\n
                                ${msgType}=8${a}${serverToClient2}${a}${execType}=${ExecType.PartialFill}${a}${clOrdId}=ABC\n
                                ${msgType}=8${a}${serverToClient2}${a}${execType}=${ExecType.PartialFill}${a}${orderId}=123\n
                                ${msgType}=G${a}${clientToServer2}${a}${origClOrdId}=ABC${a}${clOrdId}=DEF${a}${orderId}=123\n
                                ${msgType}=8${a}${serverToClient2}${a}${execType}=${ExecType.PartialFill}${a}${orderId}=123\n
                                ${msgType}=G${a}${clientToServer2}${a}${origClOrdId}=DEF${a}${clOrdId}=GHI${a}${ordStatus}=8${a}${cxlRejResponseTo}=2\n
                                ${msgType}=9${a}${serverToClient2}${a}${origClOrdId}=DEF${a}${clOrdId}=GHI\n
                                ${msgType}=9${a}${serverToClient2}${a}${origClOrdId}=ORPHAN1${a}${clOrdId}=ORPHAN1\n
                                ${msgType}=8${a}${serverToClient2}${a}${execType}=${ExecType.PartialFill}${a}${orderId}=123\n
                                ${msgType}=H${a}${clientToServer2}${a}${clOrdId}=GHI\n
                                ${msgType}=9${a}${serverToClient2}${a}${origClOrdId}=ORPHAN2${a}${clOrdId}=ORPHAN2\n
                                ${msgType}=8${a}${serverToClient2}${a}${execType}=${ExecType.OrderStatus}${a}${orderId}=123\n
                                ${msgType}=H${a}${clientToServer2}${a}${orderId}=123\n
                                ${msgType}=8${a}${serverToClient2}${a}${execType}=${ExecType.OrderStatus}${a}${orderId}=123\n
                                ${msgType}=F${a}${clientToServer2}${a}${clOrdId}=GHI\n
                                ${msgType}=8${a}${serverToClient2}${a}${execType}=${ExecType.Canceled}${a}${orderId}=123"""

        def lines = parseToLines('--output-format-horizontal-html "${senderToTargetCompIdDirection} [${msgTypeName}] ${msgFix}" --suppress-bold-tags-and-values true -e 35,49,56,150', fix)

        then:
        assert lines == """<div class='order-header'>
=================================================================================<br/>
ORDER orderId:123 clOrdId:ABC<br/>
================================================================================</div>
<div class='msg order-request'>CLIENT2->SERVER2 [NewOrderSingle] <div class='fields'><span class='field annotatedField'><span class='tag annotation'>[ClOrdID]</span><span class='tag raw'>11</span><span class='equals'>=</span><span class='value raw'>ABC</span></span></div></div>
<div class='msg order-response'>SERVER2->CLIENT2 [Exec.New] <div class='fields'><span class='field annotatedField'><span class='tag annotation'>[ClOrdID]</span><span class='tag raw'>11</span><span class='equals'>=</span><span class='value raw'>ABC</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[OrderID]</span><span class='tag raw'>37</span><span class='equals'>=</span><span class='value raw'>123</span></span></div></div>
<div class='msg order-response'>SERVER2->CLIENT2 [Exec.PartialFill] <div class='fields'><span class='field annotatedField'><span class='tag annotation'>[ClOrdID]</span><span class='tag raw'>11</span><span class='equals'>=</span><span class='value raw'>ABC</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[OrderID]</span><span class='tag raw'>37</span><span class='equals'>=</span><span class='value raw'>123</span></span></div></div>
<div class='msg order-response'>SERVER2->CLIENT2 [Exec.PartialFill] <div class='fields'><span class='field annotatedField'><span class='tag annotation'>[ClOrdID]</span><span class='tag raw'>11</span><span class='equals'>=</span><span class='value raw'>ABC</span></span></div></div>
<div class='msg order-response'>SERVER2->CLIENT2 [Exec.PartialFill] <div class='fields'><span class='field annotatedField'><span class='tag annotation'>[OrderID]</span><span class='tag raw'>37</span><span class='equals'>=</span><span class='value raw'>123</span></span></div></div>
<div class='msg order-request'>CLIENT2->SERVER2 [OrderCancelReplaceRequest] <div class='fields'><span class='field annotatedField'><span class='tag annotation'>[OrigClOrdID]</span><span class='tag raw'>41</span><span class='equals'>=</span><span class='value raw'>ABC</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[ClOrdID]</span><span class='tag raw'>11</span><span class='equals'>=</span><span class='value raw'>DEF</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[OrderID]</span><span class='tag raw'>37</span><span class='equals'>=</span><span class='value raw'>123</span></span></div></div>
<div class='msg order-response'>SERVER2->CLIENT2 [Exec.PartialFill] <div class='fields'><span class='field annotatedField'><span class='tag annotation'>[OrderID]</span><span class='tag raw'>37</span><span class='equals'>=</span><span class='value raw'>123</span></span></div></div>
<div class='msg order-request'>CLIENT2->SERVER2 [OrderCancelReplaceRequest] <div class='fields'><span class='field annotatedField'><span class='tag annotation'>[OrigClOrdID]</span><span class='tag raw'>41</span><span class='equals'>=</span><span class='value raw'>DEF</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[ClOrdID]</span><span class='tag raw'>11</span><span class='equals'>=</span><span class='value raw'>GHI</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[OrdStatus]</span><span class='tag raw'>39</span><span class='equals'>=</span><span class='value raw'>8</span><span class='value annotation'>[REJECTED]</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[CxlRejResponseTo]</span><span class='tag raw'>434</span><span class='equals'>=</span><span class='value raw'>2</span><span class='value annotation'>[ORDER_CANCELREPLACE_REQUEST]</span></span></div></div>
<div class='msg order-response'>SERVER2->CLIENT2 [OrderCancelReject] <div class='fields'><span class='field annotatedField'><span class='tag annotation'>[OrigClOrdID]</span><span class='tag raw'>41</span><span class='equals'>=</span><span class='value raw'>DEF</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[ClOrdID]</span><span class='tag raw'>11</span><span class='equals'>=</span><span class='value raw'>GHI</span></span></div></div>
<div class='msg order-response'>SERVER2->CLIENT2 [Exec.PartialFill] <div class='fields'><span class='field annotatedField'><span class='tag annotation'>[OrderID]</span><span class='tag raw'>37</span><span class='equals'>=</span><span class='value raw'>123</span></span></div></div>
<div class='msg order-request'>CLIENT2->SERVER2 [OrderStatusRequest] <div class='fields'><span class='field annotatedField'><span class='tag annotation'>[ClOrdID]</span><span class='tag raw'>11</span><span class='equals'>=</span><span class='value raw'>GHI</span></span></div></div>
<div class='msg order-response'>SERVER2->CLIENT2 [Exec.OrderStatus] <div class='fields'><span class='field annotatedField'><span class='tag annotation'>[OrderID]</span><span class='tag raw'>37</span><span class='equals'>=</span><span class='value raw'>123</span></span></div></div>
<div class='msg order-request'>CLIENT2->SERVER2 [OrderStatusRequest] <div class='fields'><span class='field annotatedField'><span class='tag annotation'>[OrderID]</span><span class='tag raw'>37</span><span class='equals'>=</span><span class='value raw'>123</span></span></div></div>
<div class='msg order-response'>SERVER2->CLIENT2 [Exec.OrderStatus] <div class='fields'><span class='field annotatedField'><span class='tag annotation'>[OrderID]</span><span class='tag raw'>37</span><span class='equals'>=</span><span class='value raw'>123</span></span></div></div>
<div class='msg order-request'>CLIENT2->SERVER2 [OrderCancelRequest] <div class='fields'><span class='field annotatedField'><span class='tag annotation'>[ClOrdID]</span><span class='tag raw'>11</span><span class='equals'>=</span><span class='value raw'>GHI</span></span></div></div>
<div class='msg order-response'>SERVER2->CLIENT2 [Exec.Canceled] <div class='fields'><span class='field annotatedField'><span class='tag annotation'>[OrderID]</span><span class='tag raw'>37</span><span class='equals'>=</span><span class='value raw'>123</span></span></div></div>
<br/>
<br/>
ORPHAN MESSAGES:<br/>
<div class='msg orphan-message'>SERVER2->CLIENT2 [OrderCancelReject] <div class='fields'><span class='field annotatedField'><span class='tag annotation'>[OrigClOrdID]</span><span class='tag raw'>41</span><span class='equals'>=</span><span class='value raw'>ORPHAN1</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[ClOrdID]</span><span class='tag raw'>11</span><span class='equals'>=</span><span class='value raw'>ORPHAN1</span></span></div></div>
<div class='msg orphan-message'>SERVER2->CLIENT2 [OrderCancelReject] <div class='fields'><span class='field annotatedField'><span class='tag annotation'>[OrigClOrdID]</span><span class='tag raw'>41</span><span class='equals'>=</span><span class='value raw'>ORPHAN2</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[ClOrdID]</span><span class='tag raw'>11</span><span class='equals'>=</span><span class='value raw'>ORPHAN2</span></span></div></div>"""
    }


    def "two single orders, with messages interleaved"() {
        when:
        final String fix = """${msgType}=D${a}${clientToServer}${a}${clOrdId}=ABC
                                ${msgType}=D${a}${clientToServer2}${a}${clOrdId}=ABC\n
                                ${msgType}=8${a}${serverToClient2}${a}${execType}=${ExecType.New}${a}${clOrdId}=ABC${a}${orderId}=123\n
                                ${msgType}=8${a}${serverToClient2}${a}${execType}=${ExecType.PartialFill}${a}${clOrdId}=ABC${a}${orderId}=123\n
                                ${msgType}=8${a}${serverToClient2}${a}${execType}=${ExecType.PartialFill}${a}${clOrdId}=ABC\n
                                ${msgType}=8${a}${serverToClient}${a}${execType}=${ExecType.New}${a}${clOrdId}=ABC${a}${orderId}=123\n
                                ${msgType}=8${a}${serverToClient2}${a}${execType}=${ExecType.PartialFill}${a}${orderId}=123\n
                                ${msgType}=G${a}${clientToServer2}${a}${origClOrdId}=ABC${a}${clOrdId}=DEF${a}${orderId}=123\n
                                ${msgType}=8${a}${serverToClient}${a}${execType}=${ExecType.PartialFill}${a}${clOrdId}=ABC${a}${orderId}=123\n
                                ${msgType}=8${a}${serverToClient2}${a}${execType}=${ExecType.PartialFill}${a}${orderId}=123\n
                                ${msgType}=G${a}${clientToServer2}${a}${origClOrdId}=DEF${a}${clOrdId}=GHI${a}${ordStatus}=8${a}${cxlRejResponseTo}=2\n
                                ${msgType}=9${a}${serverToClient2}${a}${origClOrdId}=DEF${a}${clOrdId}=GHI\n
                                ${msgType}=9${a}${serverToClient2}${a}${origClOrdId}=ORPHAN1${a}${clOrdId}=ORPHAN1\n
                                ${msgType}=8${a}${serverToClient}${a}${execType}=${ExecType.PartialFill}${a}${orderId}=123\n
                                ${msgType}=G${a}${clientToServer}${a}${origClOrdId}=ABC${a}${clOrdId}=DEF${a}${orderId}=123\n
                                ${msgType}=8${a}${serverToClient2}${a}${execType}=${ExecType.PartialFill}${a}${orderId}=123\n
                                ${msgType}=H${a}${clientToServer2}${a}${clOrdId}=GHI\n
                                ${msgType}=9${a}${serverToClient2}${a}${origClOrdId}=ORPHAN2${a}${clOrdId}=ORPHAN2\n
                                ${msgType}=8${a}${serverToClient}${a}${execType}=${ExecType.PartialFill}${a}${orderId}=123\n
                                ${msgType}=8${a}${serverToClient2}${a}${execType}=${ExecType.OrderStatus}${a}${orderId}=123\n
                                ${msgType}=G${a}${clientToServer}${a}${origClOrdId}=DEF${a}${clOrdId}=GHI${a}${ordStatus}=8${a}${cxlRejResponseTo}=2\n
                                ${msgType}=H${a}${clientToServer2}${a}${orderId}=123\n
                                ${msgType}=8${a}${serverToClient2}${a}${execType}=${ExecType.OrderStatus}${a}${orderId}=123\n
                                ${msgType}=9${a}${serverToClient}${a}${origClOrdId}=DEF${a}${clOrdId}=GHI\n
                                ${msgType}=F${a}${clientToServer2}${a}${clOrdId}=GHI\n
                                ${msgType}=8${a}${serverToClient}${a}${execType}=${ExecType.PartialFill}${a}${orderId}=123\n
                                ${msgType}=8${a}${serverToClient2}${a}${execType}=${ExecType.Canceled}${a}${orderId}=123"""

        def lines = parseToLines('--output-format-horizontal-html "${senderToTargetCompIdDirection} [${msgTypeName}] ${msgFix}" --suppress-bold-tags-and-values true -e 35,49,56,150', fix)

        then:
        assert lines == """<div class='order-header'>
=================================================================================<br/>
ORDER orderId:123 clOrdId:ABC<br/>
================================================================================</div>
<div class='msg order-request'>CLIENT->SERVER [NewOrderSingle] <div class='fields'><span class='field annotatedField'><span class='tag annotation'>[ClOrdID]</span><span class='tag raw'>11</span><span class='equals'>=</span><span class='value raw'>ABC</span></span></div></div>
<div class='msg order-response'>SERVER->CLIENT [Exec.New] <div class='fields'><span class='field annotatedField'><span class='tag annotation'>[ClOrdID]</span><span class='tag raw'>11</span><span class='equals'>=</span><span class='value raw'>ABC</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[OrderID]</span><span class='tag raw'>37</span><span class='equals'>=</span><span class='value raw'>123</span></span></div></div>
<div class='msg order-response'>SERVER->CLIENT [Exec.PartialFill] <div class='fields'><span class='field annotatedField'><span class='tag annotation'>[ClOrdID]</span><span class='tag raw'>11</span><span class='equals'>=</span><span class='value raw'>ABC</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[OrderID]</span><span class='tag raw'>37</span><span class='equals'>=</span><span class='value raw'>123</span></span></div></div>
<div class='msg order-response'>SERVER->CLIENT [Exec.PartialFill] <div class='fields'><span class='field annotatedField'><span class='tag annotation'>[OrderID]</span><span class='tag raw'>37</span><span class='equals'>=</span><span class='value raw'>123</span></span></div></div>
<div class='msg order-request'>CLIENT->SERVER [OrderCancelReplaceRequest] <div class='fields'><span class='field annotatedField'><span class='tag annotation'>[OrigClOrdID]</span><span class='tag raw'>41</span><span class='equals'>=</span><span class='value raw'>ABC</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[ClOrdID]</span><span class='tag raw'>11</span><span class='equals'>=</span><span class='value raw'>DEF</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[OrderID]</span><span class='tag raw'>37</span><span class='equals'>=</span><span class='value raw'>123</span></span></div></div>
<div class='msg order-response'>SERVER->CLIENT [Exec.PartialFill] <div class='fields'><span class='field annotatedField'><span class='tag annotation'>[OrderID]</span><span class='tag raw'>37</span><span class='equals'>=</span><span class='value raw'>123</span></span></div></div>
<div class='msg order-request'>CLIENT->SERVER [OrderCancelReplaceRequest] <div class='fields'><span class='field annotatedField'><span class='tag annotation'>[OrigClOrdID]</span><span class='tag raw'>41</span><span class='equals'>=</span><span class='value raw'>DEF</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[ClOrdID]</span><span class='tag raw'>11</span><span class='equals'>=</span><span class='value raw'>GHI</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[OrdStatus]</span><span class='tag raw'>39</span><span class='equals'>=</span><span class='value raw'>8</span><span class='value annotation'>[REJECTED]</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[CxlRejResponseTo]</span><span class='tag raw'>434</span><span class='equals'>=</span><span class='value raw'>2</span><span class='value annotation'>[ORDER_CANCELREPLACE_REQUEST]</span></span></div></div>
<div class='msg order-response'>SERVER->CLIENT [OrderCancelReject] <div class='fields'><span class='field annotatedField'><span class='tag annotation'>[OrigClOrdID]</span><span class='tag raw'>41</span><span class='equals'>=</span><span class='value raw'>DEF</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[ClOrdID]</span><span class='tag raw'>11</span><span class='equals'>=</span><span class='value raw'>GHI</span></span></div></div>
<div class='msg order-response'>SERVER->CLIENT [Exec.PartialFill] <div class='fields'><span class='field annotatedField'><span class='tag annotation'>[OrderID]</span><span class='tag raw'>37</span><span class='equals'>=</span><span class='value raw'>123</span></span></div></div>
<br/>
<div class='order-header'>
=================================================================================<br/>
ORDER orderId:123 clOrdId:ABC<br/>
================================================================================</div>
<div class='msg order-request'>CLIENT2->SERVER2 [NewOrderSingle] <div class='fields'><span class='field annotatedField'><span class='tag annotation'>[ClOrdID]</span><span class='tag raw'>11</span><span class='equals'>=</span><span class='value raw'>ABC</span></span></div></div>
<div class='msg order-response'>SERVER2->CLIENT2 [Exec.New] <div class='fields'><span class='field annotatedField'><span class='tag annotation'>[ClOrdID]</span><span class='tag raw'>11</span><span class='equals'>=</span><span class='value raw'>ABC</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[OrderID]</span><span class='tag raw'>37</span><span class='equals'>=</span><span class='value raw'>123</span></span></div></div>
<div class='msg order-response'>SERVER2->CLIENT2 [Exec.PartialFill] <div class='fields'><span class='field annotatedField'><span class='tag annotation'>[ClOrdID]</span><span class='tag raw'>11</span><span class='equals'>=</span><span class='value raw'>ABC</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[OrderID]</span><span class='tag raw'>37</span><span class='equals'>=</span><span class='value raw'>123</span></span></div></div>
<div class='msg order-response'>SERVER2->CLIENT2 [Exec.PartialFill] <div class='fields'><span class='field annotatedField'><span class='tag annotation'>[ClOrdID]</span><span class='tag raw'>11</span><span class='equals'>=</span><span class='value raw'>ABC</span></span></div></div>
<div class='msg order-response'>SERVER2->CLIENT2 [Exec.PartialFill] <div class='fields'><span class='field annotatedField'><span class='tag annotation'>[OrderID]</span><span class='tag raw'>37</span><span class='equals'>=</span><span class='value raw'>123</span></span></div></div>
<div class='msg order-request'>CLIENT2->SERVER2 [OrderCancelReplaceRequest] <div class='fields'><span class='field annotatedField'><span class='tag annotation'>[OrigClOrdID]</span><span class='tag raw'>41</span><span class='equals'>=</span><span class='value raw'>ABC</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[ClOrdID]</span><span class='tag raw'>11</span><span class='equals'>=</span><span class='value raw'>DEF</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[OrderID]</span><span class='tag raw'>37</span><span class='equals'>=</span><span class='value raw'>123</span></span></div></div>
<div class='msg order-response'>SERVER2->CLIENT2 [Exec.PartialFill] <div class='fields'><span class='field annotatedField'><span class='tag annotation'>[OrderID]</span><span class='tag raw'>37</span><span class='equals'>=</span><span class='value raw'>123</span></span></div></div>
<div class='msg order-request'>CLIENT2->SERVER2 [OrderCancelReplaceRequest] <div class='fields'><span class='field annotatedField'><span class='tag annotation'>[OrigClOrdID]</span><span class='tag raw'>41</span><span class='equals'>=</span><span class='value raw'>DEF</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[ClOrdID]</span><span class='tag raw'>11</span><span class='equals'>=</span><span class='value raw'>GHI</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[OrdStatus]</span><span class='tag raw'>39</span><span class='equals'>=</span><span class='value raw'>8</span><span class='value annotation'>[REJECTED]</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[CxlRejResponseTo]</span><span class='tag raw'>434</span><span class='equals'>=</span><span class='value raw'>2</span><span class='value annotation'>[ORDER_CANCELREPLACE_REQUEST]</span></span></div></div>
<div class='msg order-response'>SERVER2->CLIENT2 [OrderCancelReject] <div class='fields'><span class='field annotatedField'><span class='tag annotation'>[OrigClOrdID]</span><span class='tag raw'>41</span><span class='equals'>=</span><span class='value raw'>DEF</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[ClOrdID]</span><span class='tag raw'>11</span><span class='equals'>=</span><span class='value raw'>GHI</span></span></div></div>
<div class='msg order-response'>SERVER2->CLIENT2 [Exec.PartialFill] <div class='fields'><span class='field annotatedField'><span class='tag annotation'>[OrderID]</span><span class='tag raw'>37</span><span class='equals'>=</span><span class='value raw'>123</span></span></div></div>
<div class='msg order-request'>CLIENT2->SERVER2 [OrderStatusRequest] <div class='fields'><span class='field annotatedField'><span class='tag annotation'>[ClOrdID]</span><span class='tag raw'>11</span><span class='equals'>=</span><span class='value raw'>GHI</span></span></div></div>
<div class='msg order-response'>SERVER2->CLIENT2 [Exec.OrderStatus] <div class='fields'><span class='field annotatedField'><span class='tag annotation'>[OrderID]</span><span class='tag raw'>37</span><span class='equals'>=</span><span class='value raw'>123</span></span></div></div>
<div class='msg order-request'>CLIENT2->SERVER2 [OrderStatusRequest] <div class='fields'><span class='field annotatedField'><span class='tag annotation'>[OrderID]</span><span class='tag raw'>37</span><span class='equals'>=</span><span class='value raw'>123</span></span></div></div>
<div class='msg order-response'>SERVER2->CLIENT2 [Exec.OrderStatus] <div class='fields'><span class='field annotatedField'><span class='tag annotation'>[OrderID]</span><span class='tag raw'>37</span><span class='equals'>=</span><span class='value raw'>123</span></span></div></div>
<div class='msg order-request'>CLIENT2->SERVER2 [OrderCancelRequest] <div class='fields'><span class='field annotatedField'><span class='tag annotation'>[ClOrdID]</span><span class='tag raw'>11</span><span class='equals'>=</span><span class='value raw'>GHI</span></span></div></div>
<div class='msg order-response'>SERVER2->CLIENT2 [Exec.Canceled] <div class='fields'><span class='field annotatedField'><span class='tag annotation'>[OrderID]</span><span class='tag raw'>37</span><span class='equals'>=</span><span class='value raw'>123</span></span></div></div>
<br/>
<br/>
ORPHAN MESSAGES:<br/>
<div class='msg orphan-message'>SERVER2->CLIENT2 [OrderCancelReject] <div class='fields'><span class='field annotatedField'><span class='tag annotation'>[OrigClOrdID]</span><span class='tag raw'>41</span><span class='equals'>=</span><span class='value raw'>ORPHAN1</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[ClOrdID]</span><span class='tag raw'>11</span><span class='equals'>=</span><span class='value raw'>ORPHAN1</span></span></div></div>
<div class='msg orphan-message'>SERVER2->CLIENT2 [OrderCancelReject] <div class='fields'><span class='field annotatedField'><span class='tag annotation'>[OrigClOrdID]</span><span class='tag raw'>41</span><span class='equals'>=</span><span class='value raw'>ORPHAN2</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[ClOrdID]</span><span class='tag raw'>11</span><span class='equals'>=</span><span class='value raw'>ORPHAN2</span></span></div></div>"""
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

