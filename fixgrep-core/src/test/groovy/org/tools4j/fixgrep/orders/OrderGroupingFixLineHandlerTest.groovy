package org.tools4j.fixgrep.orders

import org.tools4j.fix.ExecType
import org.tools4j.fixgrep.*
import spock.lang.Specification

import java.util.function.Consumer

/**
 * User: benjw
 * Date: 9/28/2018
 * Time: 6:15 AM
 */
class OrderGroupingFixLineHandlerTest extends Specification {
    private final static int msgType = 35
    private final static int clOrdId = 11
    private final static int origClOrdId = 41
    private final static int senderCompId = 49
    private final static int targetCompId = 56
    private final static int orderId = 37
    private final static int execType = 150
    private final static String clientToServer = "49=CLIENT${a}56=SERVER"
    private final static String serverToClient = "49=SERVER${a}56=CLIENT"
    static final char a = '\u0001'

    def "simple non-formatted"() {
        given:
        final StringBuilder output = new StringBuilder()
        final ConfigKeyedWithOption config = new ConfigBuilder(['--output-format-horizontal-console', '\${senderToTargetCompIdDirection} [\${msgTypeName}] \${msgFix}', '--suppress-bold-tags-and-values', 'true']).configAndArguments.config
        final FormatSpec formatSpec = new FormatSpec(config)

        when:
        final OrderGroupingFixLineHandler fixLineHandler = new OrderGroupingFixLineHandler(new Formatter(formatSpec), new UniqueClientOrderIdSpec(), new UniqueOriginalClientOrderIdSpec(), new UniqueOrderIdSpec(), new Consumer<String>() {
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
                """================================================================================
ORDER orderId:UNKNOWN clOrdId:ABC
================================================================================
CLIENT->SERVER [NewOrderSingle] [MsgType]35=D[NEWORDERSINGLE]|[SenderCompID]49=CLIENT|[TargetCompID]56=SERVER|[ClOrdID]11=ABC

"""
    }

    def "simple formatted"() {
        given:
        final StringBuilder output = new StringBuilder()
        final ConfigKeyedWithOption config = new ConfigBuilder(['--output-format-horizontal-console', '${senderToTargetCompIdDirection} ${msgColor}[${msgTypeName}]${colorReset} ${msgFix}', '--suppress-bold-tags-and-values', 'false']).configAndArguments.config
        final FormatSpec formatSpec = new FormatSpec(config)

        when:
        final OrderGroupingFixLineHandler fixLineHandler = new OrderGroupingFixLineHandler(new Formatter(formatSpec), new UniqueClientOrderIdSpec(), new UniqueOriginalClientOrderIdSpec(), new UniqueOrderIdSpec(), new Consumer<String>() {
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
                """================================================================================
ORDER orderId:UNKNOWN clOrdId:ABC
================================================================================
CLIENT->SERVER \u001B[36m[NewOrderSingle]\u001B[0m [MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1mD\u001B[22m[NEWORDERSINGLE]|[SenderCompID]\u001B[1m49\u001B[22m\u001B[1m=\u001B[22m\u001B[1mCLIENT\u001B[22m|[TargetCompID]\u001B[1m56\u001B[22m\u001B[1m=\u001B[22m\u001B[1mSERVER\u001B[22m|[ClOrdID]\u001B[1m11\u001B[22m\u001B[1m=\u001B[22m\u001B[1mABC\u001B[22m

"""
    }

    def "three NewOrderSingles"() {
        given:
        final StringBuilder output = new StringBuilder()
        final ConfigKeyedWithOption config = new ConfigBuilder(['--output-format-horizontal-console', '\${senderToTargetCompIdDirection} [\${msgTypeName}] \${msgFix}', '--suppress-bold-tags-and-values', 'true',]).configAndArguments.config
        final FormatSpec formatSpec = new FormatSpec(config)

        when:
        final OrderGroupingFixLineHandler fixLineHandler = new OrderGroupingFixLineHandler(new Formatter(formatSpec), new UniqueClientOrderIdSpec(), new UniqueOriginalClientOrderIdSpec(), new UniqueOrderIdSpec(), new Consumer<String>() {
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
                """================================================================================
ORDER orderId:UNKNOWN clOrdId:ABC
================================================================================
CLIENT->SERVER [NewOrderSingle] [MsgType]35=D[NEWORDERSINGLE]|[SenderCompID]49=CLIENT|[TargetCompID]56=SERVER|[ClOrdID]11=ABC

================================================================================
ORDER orderId:UNKNOWN clOrdId:DEF
================================================================================
CLIENT->SERVER [NewOrderSingle] [MsgType]35=D[NEWORDERSINGLE]|[SenderCompID]49=CLIENT|[TargetCompID]56=SERVER|[ClOrdID]11=DEF

================================================================================
ORDER orderId:UNKNOWN clOrdId:GHI
================================================================================
CLIENT->SERVER [NewOrderSingle] [MsgType]35=D[NEWORDERSINGLE]|[SenderCompID]49=CLIENT|[TargetCompID]56=SERVER|[ClOrdID]11=GHI

"""
    }

    def "single order"() {
        given:
        final StringBuilder output = new StringBuilder()
        final ConfigKeyedWithOption config = new ConfigBuilder(['--output-format-horizontal-console', '\${senderToTargetCompIdDirection} [\${msgTypeName}] \${msgFix}', '--suppress-bold-tags-and-values', 'true', '-e', '35,49,56,150', '-a', 'rr']).configAndArguments.config
        final FormatSpec formatSpec = new FormatSpec(config)

        when:
        final OrderGroupingFixLineHandler fixLineHandler = new OrderGroupingFixLineHandler(new Formatter(formatSpec), new UniqueClientOrderIdSpec(), new UniqueOriginalClientOrderIdSpec(), new UniqueOrderIdSpec(), new Consumer<String>() {
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
        textLineHandler.handle("${msgType}=G${a}${serverToClient}${a}${origClOrdId}=ABC${a}${clOrdId}=DEF${a}${orderId}=123")
        textLineHandler.handle("${msgType}=8${a}${serverToClient}${a}${execType}=${ExecType.PartialFill}${a}${orderId}=123")
        textLineHandler.handle("${msgType}=G${a}${serverToClient}${a}${origClOrdId}=DEF${a}${clOrdId}=GHI")
        textLineHandler.handle("${msgType}=8${a}${serverToClient}${a}${execType}=${ExecType.PartialFill}${a}${orderId}=123")
        textLineHandler.finish()

        then:
        println output
        assert output.toString() ==
                """================================================================================
ORDER orderId:UNKNOWN clOrdId:ABC
================================================================================
CLIENT->SERVER [NewOrderSingle] [MsgType]35=D[NEWORDERSINGLE]|[SenderCompID]49=CLIENT|[TargetCompID]56=SERVER|[ClOrdID]11=ABC

"""
    }
}
