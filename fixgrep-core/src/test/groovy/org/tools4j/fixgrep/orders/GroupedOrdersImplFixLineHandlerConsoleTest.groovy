package org.tools4j.fixgrep.orders

import org.tools4j.fix.ExecType
import org.tools4j.fixgrep.config.ConfigBuilder
import org.tools4j.fixgrep.config.ConfigKeyedWithOption
import org.tools4j.fixgrep.config.FixGrepConfig
import org.tools4j.fixgrep.formatting.FormatSpec
import org.tools4j.fixgrep.formatting.Formatter
import org.tools4j.fixgrep.linehandlers.DefaultTextLineHandler
import spock.lang.Specification

import java.util.function.Consumer

/**
 * User: benjw
 * Date: 9/28/2018
 * Time: 6:15 AM
 */
class GroupedOrdersImplFixLineHandlerConsoleTest extends Specification {
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

    def "no order messages found"() {
        given:
        final StringBuilder output = new StringBuilder()
        final ConfigKeyedWithOption config = new ConfigBuilder(['--output-format-horizontal-console', '\${senderToTargetCompIdDirection} [\${msgTypeName}] \${msgFix}', '--suppress-bold-tags-and-values', 'true']).configAndArguments.config
        final FormatSpec formatSpec = new FormatSpec(new FixGrepConfig(config))

        when:
        final OrderGroupingFixLineHandler fixLineHandler = new OrderGroupingFixLineHandler(new Formatter(formatSpec), new UniqueIdSpecs(), new Consumer<String>() {
            @Override
            void accept(final String str) {
                output.append(str)
            }
        })
        final DefaultTextLineHandler textLineHandler = new DefaultTextLineHandler(formatSpec, fixLineHandler)
        textLineHandler.handle("${msgType}=blah1${a}${clientToServer}${a}${clOrdId}=ABC")
        textLineHandler.handle("${msgType}=blah2${a}${clientToServer}${a}${clOrdId}=ABC")
        textLineHandler.finish()

        then:
        assert output.toString() == "No order messages found"
    }

    def "simple non-formatted"() {
        given:
        final StringBuilder output = new StringBuilder()
        final ConfigKeyedWithOption config = new ConfigBuilder(['--output-format-horizontal-console', '\${senderToTargetCompIdDirection} [\${msgTypeName}] \${msgFix}', '--suppress-bold-tags-and-values', 'true']).configAndArguments.config
        final FormatSpec formatSpec = new FormatSpec(new FixGrepConfig(config))

        when:
        final OrderGroupingFixLineHandler fixLineHandler = new OrderGroupingFixLineHandler(new Formatter(formatSpec), new UniqueIdSpecs(), new Consumer<String>() {
            @Override
            void accept(final String str) {
                output.append(str)
            }
        })
        final DefaultTextLineHandler textLineHandler = new DefaultTextLineHandler(formatSpec, fixLineHandler)
        textLineHandler.handle("${msgType}=D${a}${clientToServer}${a}${clOrdId}=ABC")
        textLineHandler.finish()

        then:
        assert output.toString() ==
                """ORPHAN MESSAGES:
   CLIENT->SERVER [NewOrderSingle] [MsgType]35=D[NEWORDERSINGLE]|[SenderCompID]49=CLIENT|[TargetCompID]56=SERVER|[ClOrdID]11=ABC\n"""
    }

    def "simple formatted"() {
        given:
        final StringBuilder output = new StringBuilder()
        final ConfigKeyedWithOption config = new ConfigBuilder(['--output-format-horizontal-console', '${senderToTargetCompIdDirection} ${msgColor}[${msgTypeName}]${colorReset} ${msgFix}']).configAndArguments.config
        final FormatSpec formatSpec = new FormatSpec(new FixGrepConfig(config))

        when:
        final OrderGroupingFixLineHandler fixLineHandler = new OrderGroupingFixLineHandler(new Formatter(formatSpec), new UniqueIdSpecs(), new Consumer<String>() {
            @Override
            void accept(final String str) {
                output.append(str)
            }
        })
        final DefaultTextLineHandler textLineHandler = new DefaultTextLineHandler(formatSpec, fixLineHandler)
        textLineHandler.handle("35=D${a}49=CLIENT${a}56=SERVER${a}11=ABC")
        textLineHandler.finish()

        then:
        println output.toString()
        assert output.toString() ==
                """ORPHAN MESSAGES:
   CLIENT->SERVER \u001B[36m[NewOrderSingle]\u001B[0m [MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1mD\u001B[22m[NEWORDERSINGLE]|[SenderCompID]\u001B[1m49\u001B[22m\u001B[1m=\u001B[22m\u001B[1mCLIENT\u001B[22m|[TargetCompID]\u001B[1m56\u001B[22m\u001B[1m=\u001B[22m\u001B[1mSERVER\u001B[22m|[ClOrdID]\u001B[1m11\u001B[22m\u001B[1m=\u001B[22m\u001B[1mABC\u001B[22m\n"""
    }

    def "three NewOrderSingles"() {
        given:
        final StringBuilder output = new StringBuilder()
        final ConfigKeyedWithOption config = new ConfigBuilder(['--output-format-horizontal-console', '\${senderToTargetCompIdDirection} [\${msgTypeName}] \${msgFix}', '--suppress-bold-tags-and-values']).configAndArguments.config
        final FormatSpec formatSpec = new FormatSpec(new FixGrepConfig(config))

        when:
        final OrderGroupingFixLineHandler fixLineHandler = new OrderGroupingFixLineHandler(new Formatter(formatSpec), new UniqueIdSpecs(), new Consumer<String>() {
            @Override
            void accept(final String str) {
                output.append(str)
            }
        })
        final DefaultTextLineHandler textLineHandler = new DefaultTextLineHandler(formatSpec, fixLineHandler)
        textLineHandler.handle("${msgType}=D${a}${clientToServer}${a}${clOrdId}=ABC")
        textLineHandler.handle("${msgType}=D${a}${clientToServer}${a}${clOrdId}=DEF")
        textLineHandler.handle("${msgType}=D${a}${clientToServer}${a}${clOrdId}=GHI")
        textLineHandler.finish()

        then:
        println output
        assert output.toString() ==
                """ORPHAN MESSAGES:
   CLIENT->SERVER [NewOrderSingle] [MsgType]35=D[NEWORDERSINGLE]|[SenderCompID]49=CLIENT|[TargetCompID]56=SERVER|[ClOrdID]11=ABC
   CLIENT->SERVER [NewOrderSingle] [MsgType]35=D[NEWORDERSINGLE]|[SenderCompID]49=CLIENT|[TargetCompID]56=SERVER|[ClOrdID]11=DEF
   CLIENT->SERVER [NewOrderSingle] [MsgType]35=D[NEWORDERSINGLE]|[SenderCompID]49=CLIENT|[TargetCompID]56=SERVER|[ClOrdID]11=GHI\n"""
    }

    def "single order"() {
        given:
        final StringBuilder output = new StringBuilder()
        final ConfigKeyedWithOption config = new ConfigBuilder(['--output-format-horizontal-console', '\${senderToTargetCompIdDirection} [\${msgTypeName}] \${msgFix}', '--suppress-bold-tags-and-values', 'true', '-e', '35,49,56,150', '-a', 'aa']).configAndArguments.config
        final FormatSpec formatSpec = new FormatSpec(new FixGrepConfig(config))

        when:
        final OrderGroupingFixLineHandler fixLineHandler = new OrderGroupingFixLineHandler(new Formatter(formatSpec), new UniqueIdSpecs(), new Consumer<String>() {
            @Override
            void accept(final String str) {
                output.append(str)
            }
        })
        final DefaultTextLineHandler textLineHandler = new DefaultTextLineHandler(formatSpec, fixLineHandler)
        textLineHandler.handle("${msgType}=D${a}${clientToServer}${a}${clOrdId}=ABC")
        textLineHandler.handle("${msgType}=8${a}${serverToClient}${a}${execType}=${ExecType.New}${a}${clOrdId}=ABC${a}${orderId}=123")
        textLineHandler.handle("${msgType}=8${a}${serverToClient}${a}${execType}=${ExecType.PartialFill}${a}${clOrdId}=ABC${a}${orderId}=123")
        textLineHandler.handle("${msgType}=8${a}${serverToClient}${a}${execType}=${ExecType.PartialFill}${a}${orderId}=123")
        textLineHandler.handle("${msgType}=G${a}${clientToServer}${a}${origClOrdId}=ABC${a}${clOrdId}=DEF${a}${orderId}=123")
        textLineHandler.handle("${msgType}=8${a}${serverToClient}${a}${execType}=${ExecType.PartialFill}${a}${orderId}=123")
        textLineHandler.handle("${msgType}=G${a}${clientToServer}${a}${origClOrdId}=DEF${a}${clOrdId}=GHI${a}${ordStatus}=8${a}${cxlRejResponseTo}=2")
        textLineHandler.handle("${msgType}=9${a}${serverToClient}${a}${origClOrdId}=DEF${a}${clOrdId}=GHI")
        textLineHandler.handle("${msgType}=8${a}${serverToClient}${a}${execType}=${ExecType.PartialFill}${a}${orderId}=123")
        textLineHandler.finish()

        then:
        println output
        assert output.toString() ==
                """================================================================================
ORDER orderId:123 clOrdId:ABC
================================================================================
CLIENT->SERVER [NewOrderSingle] 11[ClOrdID]=ABC
   SERVER->CLIENT [Exec.New] 11[ClOrdID]=ABC|37[OrderID]=123
   SERVER->CLIENT [Exec.PartialFill] 11[ClOrdID]=ABC|37[OrderID]=123
   SERVER->CLIENT [Exec.PartialFill] 37[OrderID]=123
CLIENT->SERVER [OrderCancelReplaceRequest] 41[OrigClOrdID]=ABC|11[ClOrdID]=DEF|37[OrderID]=123
   SERVER->CLIENT [Exec.PartialFill] 37[OrderID]=123
CLIENT->SERVER [OrderCancelReplaceRequest] 41[OrigClOrdID]=DEF|11[ClOrdID]=GHI|39[OrdStatus]=8[REJECTED]|434[CxlRejResponseTo]=2[ORDER_CANCELREPLACE_REQUEST]
   SERVER->CLIENT [OrderCancelReject] 41[OrigClOrdID]=DEF|11[ClOrdID]=GHI
   SERVER->CLIENT [Exec.PartialFill] 37[OrderID]=123

"""
    }

    def "single order - no sender or target compIds"() {
        given:
        final StringBuilder output = new StringBuilder()
        final ConfigKeyedWithOption config = new ConfigBuilder(['--output-format-horizontal-console', '\${senderToTargetCompIdDirection} [\${msgTypeName}] \${msgFix}', '--suppress-bold-tags-and-values', 'true', '-e', '35,49,56,150', '-a', 'aa']).configAndArguments.config
        final FormatSpec formatSpec = new FormatSpec(new FixGrepConfig(config))

        when:
        final OrderGroupingFixLineHandler fixLineHandler = new OrderGroupingFixLineHandler(new Formatter(formatSpec), new UniqueIdSpecs(), new Consumer<String>() {
            @Override
            void accept(final String str) {
                output.append(str)
            }
        })
        final DefaultTextLineHandler textLineHandler = new DefaultTextLineHandler(formatSpec, fixLineHandler)
        textLineHandler.handle("${msgType}=D${a}${clOrdId}=ABC")
        textLineHandler.handle("${msgType}=8${a}${execType}=${ExecType.New}${a}${clOrdId}=ABC${a}${orderId}=123")
        textLineHandler.handle("${msgType}=8${a}${execType}=${ExecType.PartialFill}${a}${clOrdId}=ABC${a}${orderId}=123")
        textLineHandler.handle("${msgType}=8${a}${execType}=${ExecType.PartialFill}${a}${orderId}=123")
        textLineHandler.handle("${msgType}=G${a}${origClOrdId}=ABC${a}${clOrdId}=DEF${a}${orderId}=123")
        textLineHandler.handle("${msgType}=8${a}${execType}=${ExecType.PartialFill}${a}${orderId}=123")
        textLineHandler.handle("${msgType}=G${a}${origClOrdId}=DEF${a}${clOrdId}=GHI${a}${ordStatus}=8${a}${cxlRejResponseTo}=2")
        textLineHandler.handle("${msgType}=9${a}${origClOrdId}=DEF${a}${clOrdId}=GHI")
        textLineHandler.handle("${msgType}=8${a}${execType}=${ExecType.PartialFill}${a}${orderId}=123")
        textLineHandler.finish()

        then:
        println output
        assert output.toString() ==
                """================================================================================
ORDER orderId:123 clOrdId:ABC
================================================================================
 [NewOrderSingle] 11[ClOrdID]=ABC
    [Exec.New] 11[ClOrdID]=ABC|37[OrderID]=123
    [Exec.PartialFill] 11[ClOrdID]=ABC|37[OrderID]=123
    [Exec.PartialFill] 37[OrderID]=123
 [OrderCancelReplaceRequest] 41[OrigClOrdID]=ABC|11[ClOrdID]=DEF|37[OrderID]=123
    [Exec.PartialFill] 37[OrderID]=123
 [OrderCancelReplaceRequest] 41[OrigClOrdID]=DEF|11[ClOrdID]=GHI|39[OrdStatus]=8[REJECTED]|434[CxlRejResponseTo]=2[ORDER_CANCELREPLACE_REQUEST]
    [OrderCancelReject] 41[OrigClOrdID]=DEF|11[ClOrdID]=GHI
    [Exec.PartialFill] 37[OrderID]=123

"""
    }

    def "another single order, with orphan messages"() {
        given:
        final StringBuilder output = new StringBuilder()
        final ConfigKeyedWithOption config = new ConfigBuilder(['--output-format-horizontal-console', '\${senderToTargetCompIdDirection} [\${msgTypeName}] \${msgFix}', '--suppress-bold-tags-and-values', 'true', '-e', '49,56,150', '-a', 'aa']).configAndArguments.config
        final FormatSpec formatSpec = new FormatSpec(new FixGrepConfig(config))

        when:
        final OrderGroupingFixLineHandler fixLineHandler = new OrderGroupingFixLineHandler(new Formatter(formatSpec), new UniqueIdSpecs(), new Consumer<String>() {
            @Override
            void accept(final String str) {
                output.append(str)
            }
        })
        final DefaultTextLineHandler textLineHandler = new DefaultTextLineHandler(formatSpec, fixLineHandler)
        textLineHandler.handle("${msgType}=D${a}${clientToServer2}${a}${clOrdId}=ABC")
        textLineHandler.handle("${msgType}=8${a}${serverToClient2}${a}${execType}=${ExecType.New}${a}${clOrdId}=ABC${a}${orderId}=123")
        textLineHandler.handle("${msgType}=8${a}${serverToClient2}${a}${execType}=${ExecType.PartialFill}${a}${clOrdId}=ABC${a}${orderId}=123")
        textLineHandler.handle("${msgType}=8${a}${serverToClient2}${a}${execType}=${ExecType.PartialFill}${a}${clOrdId}=ABC")
        textLineHandler.handle("${msgType}=8${a}${serverToClient2}${a}${execType}=${ExecType.PartialFill}${a}${orderId}=123")
        textLineHandler.handle("${msgType}=G${a}${clientToServer2}${a}${origClOrdId}=ABC${a}${clOrdId}=DEF${a}${orderId}=123")
        textLineHandler.handle("${msgType}=8${a}${serverToClient2}${a}${execType}=${ExecType.PartialFill}${a}${orderId}=123")
        textLineHandler.handle("${msgType}=G${a}${clientToServer2}${a}${origClOrdId}=DEF${a}${clOrdId}=GHI${a}${ordStatus}=8${a}${cxlRejResponseTo}=2")
        textLineHandler.handle("${msgType}=9${a}${serverToClient2}${a}${origClOrdId}=DEF${a}${clOrdId}=GHI")
        textLineHandler.handle("${msgType}=9${a}${serverToClient2}${a}${origClOrdId}=ORPHAN1${a}${clOrdId}=ORPHAN1")
        textLineHandler.handle("${msgType}=8${a}${serverToClient2}${a}${execType}=${ExecType.PartialFill}${a}${orderId}=123")
        textLineHandler.handle("${msgType}=H${a}${clientToServer2}${a}${clOrdId}=GHI")
        textLineHandler.handle("${msgType}=9${a}${serverToClient2}${a}${origClOrdId}=ORPHAN2${a}${clOrdId}=ORPHAN2")
        textLineHandler.handle("${msgType}=8${a}${serverToClient2}${a}${execType}=${ExecType.OrderStatus}${a}${orderId}=123")
        textLineHandler.handle("${msgType}=H${a}${clientToServer2}${a}${orderId}=123")
        textLineHandler.handle("${msgType}=8${a}${serverToClient2}${a}${execType}=${ExecType.OrderStatus}${a}${orderId}=123")
        textLineHandler.handle("${msgType}=F${a}${clientToServer2}${a}${clOrdId}=GHI")
        textLineHandler.handle("${msgType}=8${a}${serverToClient2}${a}${execType}=${ExecType.Canceled}${a}${orderId}=123")
        textLineHandler.finish()

        then:
        println output
        assert output.toString() ==
                """================================================================================
ORDER orderId:123 clOrdId:ABC
================================================================================
CLIENT2->SERVER2 [NewOrderSingle] 35[MsgType]=D[NEWORDERSINGLE]|11[ClOrdID]=ABC
   SERVER2->CLIENT2 [Exec.New] 35[MsgType]=8[EXECUTIONREPORT]|11[ClOrdID]=ABC|37[OrderID]=123
   SERVER2->CLIENT2 [Exec.PartialFill] 35[MsgType]=8[EXECUTIONREPORT]|11[ClOrdID]=ABC|37[OrderID]=123
   SERVER2->CLIENT2 [Exec.PartialFill] 35[MsgType]=8[EXECUTIONREPORT]|11[ClOrdID]=ABC
   SERVER2->CLIENT2 [Exec.PartialFill] 35[MsgType]=8[EXECUTIONREPORT]|37[OrderID]=123
CLIENT2->SERVER2 [OrderCancelReplaceRequest] 35[MsgType]=G[ORDERCANCELREPLACEREQUEST]|41[OrigClOrdID]=ABC|11[ClOrdID]=DEF|37[OrderID]=123
   SERVER2->CLIENT2 [Exec.PartialFill] 35[MsgType]=8[EXECUTIONREPORT]|37[OrderID]=123
CLIENT2->SERVER2 [OrderCancelReplaceRequest] 35[MsgType]=G[ORDERCANCELREPLACEREQUEST]|41[OrigClOrdID]=DEF|11[ClOrdID]=GHI|39[OrdStatus]=8[REJECTED]|434[CxlRejResponseTo]=2[ORDER_CANCELREPLACE_REQUEST]
   SERVER2->CLIENT2 [OrderCancelReject] 35[MsgType]=9[ORDERCANCELREJECT]|41[OrigClOrdID]=DEF|11[ClOrdID]=GHI
   SERVER2->CLIENT2 [Exec.PartialFill] 35[MsgType]=8[EXECUTIONREPORT]|37[OrderID]=123
CLIENT2->SERVER2 [OrderStatusRequest] 35[MsgType]=H[ORDERSTATUSREQUEST]|11[ClOrdID]=GHI
   SERVER2->CLIENT2 [Exec.OrderStatus] 35[MsgType]=8[EXECUTIONREPORT]|37[OrderID]=123
CLIENT2->SERVER2 [OrderStatusRequest] 35[MsgType]=H[ORDERSTATUSREQUEST]|37[OrderID]=123
   SERVER2->CLIENT2 [Exec.OrderStatus] 35[MsgType]=8[EXECUTIONREPORT]|37[OrderID]=123
CLIENT2->SERVER2 [OrderCancelRequest] 35[MsgType]=F[ORDERCANCELREQUEST]|11[ClOrdID]=GHI
   SERVER2->CLIENT2 [Exec.Canceled] 35[MsgType]=8[EXECUTIONREPORT]|37[OrderID]=123


ORPHAN MESSAGES:
   SERVER2->CLIENT2 [OrderCancelReject] 35[MsgType]=9[ORDERCANCELREJECT]|41[OrigClOrdID]=ORPHAN1|11[ClOrdID]=ORPHAN1
   SERVER2->CLIENT2 [OrderCancelReject] 35[MsgType]=9[ORDERCANCELREJECT]|41[OrigClOrdID]=ORPHAN2|11[ClOrdID]=ORPHAN2
"""
    }


    def "two single orders, different compIds, same orderIds, with messages interleaved"() {
        given:
        final StringBuilder output = new StringBuilder()
        final ConfigKeyedWithOption config = new ConfigBuilder(['--output-format-horizontal-console', '\${senderToTargetCompIdDirection} [\${msgTypeName}] \${msgFix}', '--suppress-bold-tags-and-values', 'true', '-e', '49,56,150', '-a', 'aa']).configAndArguments.config
        final FormatSpec formatSpec = new FormatSpec(new FixGrepConfig(config))

        when:
        final OrderGroupingFixLineHandler fixLineHandler = new OrderGroupingFixLineHandler(new Formatter(formatSpec), new UniqueIdSpecs(), new Consumer<String>() {
            @Override
            void accept(final String str) {
                output.append(str)
            }
        })
        final DefaultTextLineHandler handlerPointer1 = new DefaultTextLineHandler(formatSpec, fixLineHandler)
        final DefaultTextLineHandler handlerPointer2 = handlerPointer1
        populateTwoSingleOrdersWithMessagesInterleaved_differentCompIdsSameOrderIds(handlerPointer1, handlerPointer2)
        

        then:
        println output
        assert output.toString() ==
                """================================================================================
ORDER orderId:123 clOrdId:ABC
================================================================================
CLIENT->SERVER [NewOrderSingle] 35[MsgType]=D[NEWORDERSINGLE]|11[ClOrdID]=ABC
   SERVER->CLIENT [Exec.New] 35[MsgType]=8[EXECUTIONREPORT]|11[ClOrdID]=ABC|37[OrderID]=123
   SERVER->CLIENT [Exec.PartialFill] 35[MsgType]=8[EXECUTIONREPORT]|11[ClOrdID]=ABC|37[OrderID]=123
   SERVER->CLIENT [Exec.PartialFill] 35[MsgType]=8[EXECUTIONREPORT]|37[OrderID]=123
CLIENT->SERVER [OrderCancelReplaceRequest] 35[MsgType]=G[ORDERCANCELREPLACEREQUEST]|41[OrigClOrdID]=ABC|11[ClOrdID]=DEF|37[OrderID]=123
   SERVER->CLIENT [Exec.PartialFill] 35[MsgType]=8[EXECUTIONREPORT]|37[OrderID]=123
CLIENT->SERVER [OrderCancelReplaceRequest] 35[MsgType]=G[ORDERCANCELREPLACEREQUEST]|41[OrigClOrdID]=DEF|11[ClOrdID]=GHI|39[OrdStatus]=8[REJECTED]|434[CxlRejResponseTo]=2[ORDER_CANCELREPLACE_REQUEST]
   SERVER->CLIENT [OrderCancelReject] 35[MsgType]=9[ORDERCANCELREJECT]|41[OrigClOrdID]=DEF|11[ClOrdID]=GHI
   SERVER->CLIENT [Exec.PartialFill] 35[MsgType]=8[EXECUTIONREPORT]|37[OrderID]=123

================================================================================
ORDER orderId:123 clOrdId:ABC
================================================================================
CLIENT2->SERVER2 [NewOrderSingle] 35[MsgType]=D[NEWORDERSINGLE]|11[ClOrdID]=ABC
   SERVER2->CLIENT2 [Exec.New] 35[MsgType]=8[EXECUTIONREPORT]|11[ClOrdID]=ABC|37[OrderID]=123
   SERVER2->CLIENT2 [Exec.PartialFill] 35[MsgType]=8[EXECUTIONREPORT]|11[ClOrdID]=ABC|37[OrderID]=123
   SERVER2->CLIENT2 [Exec.PartialFill] 35[MsgType]=8[EXECUTIONREPORT]|11[ClOrdID]=ABC
   SERVER2->CLIENT2 [Exec.PartialFill] 35[MsgType]=8[EXECUTIONREPORT]|37[OrderID]=123
CLIENT2->SERVER2 [OrderCancelReplaceRequest] 35[MsgType]=G[ORDERCANCELREPLACEREQUEST]|41[OrigClOrdID]=ABC|11[ClOrdID]=DEF|37[OrderID]=123
   SERVER2->CLIENT2 [Exec.PartialFill] 35[MsgType]=8[EXECUTIONREPORT]|37[OrderID]=123
CLIENT2->SERVER2 [OrderCancelReplaceRequest] 35[MsgType]=G[ORDERCANCELREPLACEREQUEST]|41[OrigClOrdID]=DEF|11[ClOrdID]=GHI|39[OrdStatus]=8[REJECTED]|434[CxlRejResponseTo]=2[ORDER_CANCELREPLACE_REQUEST]
   SERVER2->CLIENT2 [OrderCancelReject] 35[MsgType]=9[ORDERCANCELREJECT]|41[OrigClOrdID]=DEF|11[ClOrdID]=GHI
   SERVER2->CLIENT2 [Exec.PartialFill] 35[MsgType]=8[EXECUTIONREPORT]|37[OrderID]=123
CLIENT2->SERVER2 [OrderStatusRequest] 35[MsgType]=H[ORDERSTATUSREQUEST]|11[ClOrdID]=GHI
   SERVER2->CLIENT2 [Exec.OrderStatus] 35[MsgType]=8[EXECUTIONREPORT]|37[OrderID]=123
CLIENT2->SERVER2 [OrderStatusRequest] 35[MsgType]=H[ORDERSTATUSREQUEST]|37[OrderID]=123
   SERVER2->CLIENT2 [Exec.OrderStatus] 35[MsgType]=8[EXECUTIONREPORT]|37[OrderID]=123
CLIENT2->SERVER2 [OrderCancelRequest] 35[MsgType]=F[ORDERCANCELREQUEST]|11[ClOrdID]=GHI
   SERVER2->CLIENT2 [Exec.Canceled] 35[MsgType]=8[EXECUTIONREPORT]|37[OrderID]=123


ORPHAN MESSAGES:
   SERVER2->CLIENT2 [OrderCancelReject] 35[MsgType]=9[ORDERCANCELREJECT]|41[OrigClOrdID]=ORPHAN1|11[ClOrdID]=ORPHAN1
   SERVER2->CLIENT2 [OrderCancelReject] 35[MsgType]=9[ORDERCANCELREJECT]|41[OrigClOrdID]=ORPHAN2|11[ClOrdID]=ORPHAN2
"""
    }

    def "two single orders, same compIds, different orderIds, with messages interleaved"() {
        given:
        final StringBuilder output = new StringBuilder()
        final ConfigKeyedWithOption config = new ConfigBuilder(['--output-format-horizontal-console', '\${senderToTargetCompIdDirection} [\${msgTypeName}] \${msgFix}', '--suppress-bold-tags-and-values', 'true', '-e', '49,56,150', '-a', 'aa']).configAndArguments.config
        final FormatSpec formatSpec = new FormatSpec(new FixGrepConfig(config))

        when:
        final OrderGroupingFixLineHandler fixLineHandler = new OrderGroupingFixLineHandler(new Formatter(formatSpec), new UniqueIdSpecs(), new Consumer<String>() {
            @Override
            void accept(final String str) {
                output.append(str)
            }
        })
        final DefaultTextLineHandler handlerPointer1 = new DefaultTextLineHandler(formatSpec, fixLineHandler)
        final DefaultTextLineHandler handlerPointer2 = handlerPointer1
        populateTwoSingleOrdersWithMessagesInterleaved_sameCompIdsDifferentOrderIds(handlerPointer1, handlerPointer2)


        then:
        println output
        assert output.toString() ==
                """================================================================================
ORDER orderId:1231 clOrdId:ABC1
================================================================================
CLIENT->SERVER [NewOrderSingle] 35[MsgType]=D[NEWORDERSINGLE]|11[ClOrdID]=ABC1
   SERVER->CLIENT [Exec.New] 35[MsgType]=8[EXECUTIONREPORT]|11[ClOrdID]=ABC1|37[OrderID]=1231
   SERVER->CLIENT [Exec.PartialFill] 35[MsgType]=8[EXECUTIONREPORT]|11[ClOrdID]=ABC1|37[OrderID]=1231
   SERVER->CLIENT [Exec.PartialFill] 35[MsgType]=8[EXECUTIONREPORT]|37[OrderID]=1231
CLIENT->SERVER [OrderCancelReplaceRequest] 35[MsgType]=G[ORDERCANCELREPLACEREQUEST]|41[OrigClOrdID]=ABC1|11[ClOrdID]=DEF1|37[OrderID]=1231
   SERVER->CLIENT [Exec.PartialFill] 35[MsgType]=8[EXECUTIONREPORT]|37[OrderID]=1231
CLIENT->SERVER [OrderCancelReplaceRequest] 35[MsgType]=G[ORDERCANCELREPLACEREQUEST]|41[OrigClOrdID]=DEF1|11[ClOrdID]=GHI1|39[OrdStatus]=8[REJECTED]|434[CxlRejResponseTo]=2[ORDER_CANCELREPLACE_REQUEST]
   SERVER->CLIENT [OrderCancelReject] 35[MsgType]=9[ORDERCANCELREJECT]|41[OrigClOrdID]=DEF1|11[ClOrdID]=GHI1
   SERVER->CLIENT [Exec.PartialFill] 35[MsgType]=8[EXECUTIONREPORT]|37[OrderID]=1231

================================================================================
ORDER orderId:1232 clOrdId:ABC2
================================================================================
CLIENT->SERVER [NewOrderSingle] 35[MsgType]=D[NEWORDERSINGLE]|11[ClOrdID]=ABC2
   SERVER->CLIENT [Exec.New] 35[MsgType]=8[EXECUTIONREPORT]|11[ClOrdID]=ABC2|37[OrderID]=1232
   SERVER->CLIENT [Exec.PartialFill] 35[MsgType]=8[EXECUTIONREPORT]|11[ClOrdID]=ABC2|37[OrderID]=1232
   SERVER->CLIENT [Exec.PartialFill] 35[MsgType]=8[EXECUTIONREPORT]|11[ClOrdID]=ABC2
   SERVER->CLIENT [Exec.PartialFill] 35[MsgType]=8[EXECUTIONREPORT]|37[OrderID]=1232
CLIENT->SERVER [OrderCancelReplaceRequest] 35[MsgType]=G[ORDERCANCELREPLACEREQUEST]|41[OrigClOrdID]=ABC2|11[ClOrdID]=DEF2|37[OrderID]=1232
   SERVER->CLIENT [Exec.PartialFill] 35[MsgType]=8[EXECUTIONREPORT]|37[OrderID]=1232
CLIENT->SERVER [OrderCancelReplaceRequest] 35[MsgType]=G[ORDERCANCELREPLACEREQUEST]|41[OrigClOrdID]=DEF2|11[ClOrdID]=GHI2|39[OrdStatus]=8[REJECTED]|434[CxlRejResponseTo]=2[ORDER_CANCELREPLACE_REQUEST]
   SERVER->CLIENT [OrderCancelReject] 35[MsgType]=9[ORDERCANCELREJECT]|41[OrigClOrdID]=DEF2|11[ClOrdID]=GHI2
   SERVER->CLIENT [Exec.PartialFill] 35[MsgType]=8[EXECUTIONREPORT]|37[OrderID]=1232
CLIENT->SERVER [OrderStatusRequest] 35[MsgType]=H[ORDERSTATUSREQUEST]|11[ClOrdID]=GHI2
   SERVER->CLIENT [Exec.OrderStatus] 35[MsgType]=8[EXECUTIONREPORT]|37[OrderID]=1232
CLIENT->SERVER [OrderStatusRequest] 35[MsgType]=H[ORDERSTATUSREQUEST]|37[OrderID]=1232
   SERVER->CLIENT [Exec.OrderStatus] 35[MsgType]=8[EXECUTIONREPORT]|37[OrderID]=1232
CLIENT->SERVER [OrderCancelRequest] 35[MsgType]=F[ORDERCANCELREQUEST]|11[ClOrdID]=GHI2
   SERVER->CLIENT [Exec.Canceled] 35[MsgType]=8[EXECUTIONREPORT]|37[OrderID]=1232


ORPHAN MESSAGES:
   SERVER->CLIENT [OrderCancelReject] 35[MsgType]=9[ORDERCANCELREJECT]|41[OrigClOrdID]=ORPHAN12|11[ClOrdID]=ORPHAN12
   SERVER->CLIENT [OrderCancelReject] 35[MsgType]=9[ORDERCANCELREJECT]|41[OrigClOrdID]=ORPHAN22|11[ClOrdID]=ORPHAN22
"""
    }

    def "two single orders, with messages interleaved, filtering on clOrdId"() {
        given:
        final StringBuilder output = new StringBuilder()
        final ConfigKeyedWithOption config = new ConfigBuilder(['-o BC1 --output-format-horizontal-console', '\${senderToTargetCompIdDirection} [\${msgTypeName}] \${msgFix}', '--suppress-bold-tags-and-values', 'true', '-e', '49,56,150', '-a', 'aa']).configAndArguments.config
        final FormatSpec formatSpec = new FormatSpec(new FixGrepConfig(config))

        when:
        final OrderGroupingFixLineHandler fixLineHandler = new OrderGroupingFixLineHandler(new Formatter(formatSpec), new UniqueIdSpecs(), new IdFilter(["BC1"]), new Consumer<String>() {
            @Override
            void accept(final String str) {
                output.append(str)
            }
        })
        final DefaultTextLineHandler handlerPointer1 = new DefaultTextLineHandler(formatSpec, fixLineHandler)
        final DefaultTextLineHandler handlerPointer2 = handlerPointer1
        populateTwoSingleOrdersWithMessagesInterleaved_sameCompIdsDifferentOrderIds(handlerPointer1, handlerPointer2)

        then:
        println output
        assert output.toString() ==
                """================================================================================
ORDER orderId:1231 clOrdId:ABC1
================================================================================
35[MsgType]=D[NEWORDERSINGLE]|11[ClOrdID]=ABC1
   35[MsgType]=8[EXECUTIONREPORT]|11[ClOrdID]=ABC1|37[OrderID]=1231
   35[MsgType]=8[EXECUTIONREPORT]|11[ClOrdID]=ABC1|37[OrderID]=1231
   35[MsgType]=8[EXECUTIONREPORT]|37[OrderID]=1231
35[MsgType]=G[ORDERCANCELREPLACEREQUEST]|41[OrigClOrdID]=ABC1|11[ClOrdID]=DEF1|37[OrderID]=1231
   35[MsgType]=8[EXECUTIONREPORT]|37[OrderID]=1231
35[MsgType]=G[ORDERCANCELREPLACEREQUEST]|41[OrigClOrdID]=DEF1|11[ClOrdID]=GHI1|39[OrdStatus]=8[REJECTED]|434[CxlRejResponseTo]=2[ORDER_CANCELREPLACE_REQUEST]
   35[MsgType]=9[ORDERCANCELREJECT]|41[OrigClOrdID]=DEF1|11[ClOrdID]=GHI1
   35[MsgType]=8[EXECUTIONREPORT]|37[OrderID]=1231

"""
    }

    protected void populateTwoSingleOrdersWithMessagesInterleaved_differentCompIdsSameOrderIds(DefaultTextLineHandler handlerPointer1, DefaultTextLineHandler handlerPointer2) {
        handlerPointer1.handle("${msgType}=D${a}${clientToServer}${a}${clOrdId}=ABC")
        handlerPointer2.handle("${msgType}=D${a}${clientToServer2}${a}${clOrdId}=ABC")
        handlerPointer2.handle("${msgType}=8${a}${serverToClient2}${a}${execType}=${ExecType.New}${a}${clOrdId}=ABC${a}${orderId}=123")
        handlerPointer2.handle("${msgType}=8${a}${serverToClient2}${a}${execType}=${ExecType.PartialFill}${a}${clOrdId}=ABC${a}${orderId}=123")
        handlerPointer2.handle("${msgType}=8${a}${serverToClient2}${a}${execType}=${ExecType.PartialFill}${a}${clOrdId}=ABC")
        handlerPointer1.handle("${msgType}=8${a}${serverToClient}${a}${execType}=${ExecType.New}${a}${clOrdId}=ABC${a}${orderId}=123")
        handlerPointer2.handle("${msgType}=8${a}${serverToClient2}${a}${execType}=${ExecType.PartialFill}${a}${orderId}=123")
        handlerPointer2.handle("${msgType}=G${a}${clientToServer2}${a}${origClOrdId}=ABC${a}${clOrdId}=DEF${a}${orderId}=123")
        handlerPointer1.handle("${msgType}=8${a}${serverToClient}${a}${execType}=${ExecType.PartialFill}${a}${clOrdId}=ABC${a}${orderId}=123")
        handlerPointer2.handle("${msgType}=8${a}${serverToClient2}${a}${execType}=${ExecType.PartialFill}${a}${orderId}=123")
        handlerPointer2.handle("${msgType}=G${a}${clientToServer2}${a}${origClOrdId}=DEF${a}${clOrdId}=GHI${a}${ordStatus}=8${a}${cxlRejResponseTo}=2")
        handlerPointer2.handle("${msgType}=9${a}${serverToClient2}${a}${origClOrdId}=DEF${a}${clOrdId}=GHI")
        handlerPointer2.handle("${msgType}=9${a}${serverToClient2}${a}${origClOrdId}=ORPHAN1${a}${clOrdId}=ORPHAN1")
        handlerPointer1.handle("${msgType}=8${a}${serverToClient}${a}${execType}=${ExecType.PartialFill}${a}${orderId}=123")
        handlerPointer1.handle("${msgType}=G${a}${clientToServer}${a}${origClOrdId}=ABC${a}${clOrdId}=DEF${a}${orderId}=123")
        handlerPointer2.handle("${msgType}=8${a}${serverToClient2}${a}${execType}=${ExecType.PartialFill}${a}${orderId}=123")
        handlerPointer2.handle("${msgType}=H${a}${clientToServer2}${a}${clOrdId}=GHI")
        handlerPointer2.handle("${msgType}=9${a}${serverToClient2}${a}${origClOrdId}=ORPHAN2${a}${clOrdId}=ORPHAN2")
        handlerPointer1.handle("${msgType}=8${a}${serverToClient}${a}${execType}=${ExecType.PartialFill}${a}${orderId}=123")
        handlerPointer2.handle("${msgType}=8${a}${serverToClient2}${a}${execType}=${ExecType.OrderStatus}${a}${orderId}=123")
        handlerPointer1.handle("${msgType}=G${a}${clientToServer}${a}${origClOrdId}=DEF${a}${clOrdId}=GHI${a}${ordStatus}=8${a}${cxlRejResponseTo}=2")
        handlerPointer2.handle("${msgType}=H${a}${clientToServer2}${a}${orderId}=123")
        handlerPointer2.handle("${msgType}=8${a}${serverToClient2}${a}${execType}=${ExecType.OrderStatus}${a}${orderId}=123")
        handlerPointer1.handle("${msgType}=9${a}${serverToClient}${a}${origClOrdId}=DEF${a}${clOrdId}=GHI")
        handlerPointer2.handle("${msgType}=F${a}${clientToServer2}${a}${clOrdId}=GHI")
        handlerPointer1.handle("${msgType}=8${a}${serverToClient}${a}${execType}=${ExecType.PartialFill}${a}${orderId}=123")
        handlerPointer2.handle("${msgType}=8${a}${serverToClient2}${a}${execType}=${ExecType.Canceled}${a}${orderId}=123")
        handlerPointer2.finish()
    }

    protected void populateTwoSingleOrdersWithMessagesInterleaved_sameCompIdsDifferentOrderIds(DefaultTextLineHandler handlerPointer1, DefaultTextLineHandler handlerPointer2) {
        handlerPointer1.handle("${msgType}=D${a}${clientToServer}${a}${clOrdId}=ABC1")
        handlerPointer2.handle("${msgType}=D${a}${clientToServer}${a}${clOrdId}=ABC2")
        handlerPointer2.handle("${msgType}=8${a}${serverToClient}${a}${execType}=${ExecType.New}${a}${clOrdId}=ABC2${a}${orderId}=1232")
        handlerPointer2.handle("${msgType}=8${a}${serverToClient}${a}${execType}=${ExecType.PartialFill}${a}${clOrdId}=ABC2${a}${orderId}=1232")
        handlerPointer2.handle("${msgType}=8${a}${serverToClient}${a}${execType}=${ExecType.PartialFill}${a}${clOrdId}=ABC2")
        handlerPointer1.handle("${msgType}=8${a}${serverToClient}${a}${execType}=${ExecType.New}${a}${clOrdId}=ABC1${a}${orderId}=1231")
        handlerPointer2.handle("${msgType}=8${a}${serverToClient}${a}${execType}=${ExecType.PartialFill}${a}${orderId}=1232")
        handlerPointer2.handle("${msgType}=G${a}${clientToServer}${a}${origClOrdId}=ABC2${a}${clOrdId}=DEF2${a}${orderId}=1232")
        handlerPointer1.handle("${msgType}=8${a}${serverToClient}${a}${execType}=${ExecType.PartialFill}${a}${clOrdId}=ABC1${a}${orderId}=1231")
        handlerPointer2.handle("${msgType}=8${a}${serverToClient}${a}${execType}=${ExecType.PartialFill}${a}${orderId}=1232")
        handlerPointer2.handle("${msgType}=G${a}${clientToServer}${a}${origClOrdId}=DEF2${a}${clOrdId}=GHI2${a}${ordStatus}=8${a}${cxlRejResponseTo}=2")
        handlerPointer2.handle("${msgType}=9${a}${serverToClient}${a}${origClOrdId}=DEF2${a}${clOrdId}=GHI2")
        handlerPointer2.handle("${msgType}=9${a}${serverToClient}${a}${origClOrdId}=ORPHAN12${a}${clOrdId}=ORPHAN12")
        handlerPointer1.handle("${msgType}=8${a}${serverToClient}${a}${execType}=${ExecType.PartialFill}${a}${orderId}=1231")
        handlerPointer1.handle("${msgType}=G${a}${clientToServer}${a}${origClOrdId}=ABC1${a}${clOrdId}=DEF1${a}${orderId}=1231")
        handlerPointer2.handle("${msgType}=8${a}${serverToClient}${a}${execType}=${ExecType.PartialFill}${a}${orderId}=1232")
        handlerPointer2.handle("${msgType}=H${a}${clientToServer}${a}${clOrdId}=GHI2")
        handlerPointer2.handle("${msgType}=9${a}${serverToClient}${a}${origClOrdId}=ORPHAN22${a}${clOrdId}=ORPHAN22")
        handlerPointer1.handle("${msgType}=8${a}${serverToClient}${a}${execType}=${ExecType.PartialFill}${a}${orderId}=1231")
        handlerPointer2.handle("${msgType}=8${a}${serverToClient}${a}${execType}=${ExecType.OrderStatus}${a}${orderId}=1232")
        handlerPointer1.handle("${msgType}=G${a}${clientToServer}${a}${origClOrdId}=DEF1${a}${clOrdId}=GHI1${a}${ordStatus}=8${a}${cxlRejResponseTo}=2")
        handlerPointer2.handle("${msgType}=H${a}${clientToServer}${a}${orderId}=1232")
        handlerPointer2.handle("${msgType}=8${a}${serverToClient}${a}${execType}=${ExecType.OrderStatus}${a}${orderId}=1232")
        handlerPointer1.handle("${msgType}=9${a}${serverToClient}${a}${origClOrdId}=DEF1${a}${clOrdId}=GHI1")
        handlerPointer2.handle("${msgType}=F${a}${clientToServer}${a}${clOrdId}=GHI2")
        handlerPointer1.handle("${msgType}=8${a}${serverToClient}${a}${execType}=${ExecType.PartialFill}${a}${orderId}=1231")
        handlerPointer2.handle("${msgType}=8${a}${serverToClient}${a}${execType}=${ExecType.Canceled}${a}${orderId}=1232")
        handlerPointer2.finish()
    }
}
