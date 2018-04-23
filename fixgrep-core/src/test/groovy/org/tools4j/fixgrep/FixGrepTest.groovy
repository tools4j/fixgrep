package org.tools4j.fixgrep

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import org.tools4j.fix.Ascii1Char
import org.tools4j.util.CircularBufferedReaderWriter
import org.tools4j.util.CircularLineWriter
import org.tools4j.util.CircularReaderWriter
import org.tools4j.utils.ArgsAsString
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

/**
 * User: ben
 * Date: 12/03/2018
 * Time: 6:55 AM
 */
class FixGrepTest extends Specification {
    @Shared private final static char a = new Ascii1Char().toChar()
    @Shared private Config overrides

    def setup(){
        overrides = ConfigFactory.parseMap(['line.format': '${msgFix}'])
    }

    @Unroll
    def 'test #args'(final String args, final String expectedOutput){
        given:
        final String fix = "35=D${a}11=ABC${a}55=AUD/USD\n35=8${a}150=F${a}55=AUD/USD"

        when:
        def lines = parseToLines(args, fix)

        then:
        assert lines == expectedOutput

        where:
        args                              | expectedOutput
        ''                                | '[MsgType]35=D[NEWORDERSINGLE]|[ClOrdID]11=ABC|[Symbol]55=AUD/USD\n[MsgType]35=8[EXECUTIONREPORT]|[ExecType]150=F[TRADE_PARTIAL_FILL_OR_FILL]|[Symbol]55=AUD/USD'
        '--exclude-messages-of-type 8'    | '[MsgType]35=D[NEWORDERSINGLE]|[ClOrdID]11=ABC|[Symbol]55=AUD/USD'
        '-z 8'                            | '[MsgType]35=D[NEWORDERSINGLE]|[ClOrdID]11=ABC|[Symbol]55=AUD/USD'
        '--exclude-messages-of-type D'    | '[MsgType]35=8[EXECUTIONREPORT]|[ExecType]150=F[TRADE_PARTIAL_FILL_OR_FILL]|[Symbol]55=AUD/USD'
        '--exclude-messages-of-type D,8'  | ''
        '-z D --tag-annotations __'       | '35=8|150=F|55=AUD/USD'
        '-z D --tag-annotations b_'       | '[MsgType]35=8|[ExecType]150=F|[Symbol]55=AUD/USD'
        '-m D'                            | '[MsgType]35=D[NEWORDERSINGLE]|[ClOrdID]11=ABC|[Symbol]55=AUD/USD'
        '-m D -z D'                       | ''
        '--output-delimiter :'            | '[MsgType]35=D[NEWORDERSINGLE]:[ClOrdID]11=ABC:[Symbol]55=AUD/USD\n[MsgType]35=8[EXECUTIONREPORT]:[ExecType]150=F[TRADE_PARTIAL_FILL_OR_FILL]:[Symbol]55=AUD/USD'
        '-o :'                            | '[MsgType]35=D[NEWORDERSINGLE]:[ClOrdID]11=ABC:[Symbol]55=AUD/USD\n[MsgType]35=8[EXECUTIONREPORT]:[ExecType]150=F[TRADE_PARTIAL_FILL_OR_FILL]:[Symbol]55=AUD/USD'
        '--sort-by-tags 55,35'            | '[Symbol]55=AUD/USD|[MsgType]35=D[NEWORDERSINGLE]|[ClOrdID]11=ABC\n[Symbol]55=AUD/USD|[MsgType]35=8[EXECUTIONREPORT]|[ExecType]150=F[TRADE_PARTIAL_FILL_OR_FILL]'
        '-s 55,35'                        | '[Symbol]55=AUD/USD|[MsgType]35=D[NEWORDERSINGLE]|[ClOrdID]11=ABC\n[Symbol]55=AUD/USD|[MsgType]35=8[EXECUTIONREPORT]|[ExecType]150=F[TRADE_PARTIAL_FILL_OR_FILL]'
        '-s 55,11,35'                     | '[Symbol]55=AUD/USD|[ClOrdID]11=ABC|[MsgType]35=D[NEWORDERSINGLE]\n[Symbol]55=AUD/USD|[MsgType]35=8[EXECUTIONREPORT]|[ExecType]150=F[TRADE_PARTIAL_FILL_OR_FILL]'
        '--only-include-tags 35'          | '[MsgType]35=D[NEWORDERSINGLE]\n[MsgType]35=8[EXECUTIONREPORT]'
        '--only-include-tags 35,55'       | '[MsgType]35=D[NEWORDERSINGLE]|[Symbol]55=AUD/USD\n[MsgType]35=8[EXECUTIONREPORT]|[Symbol]55=AUD/USD'
        '-i 35,55'                        | '[MsgType]35=D[NEWORDERSINGLE]|[Symbol]55=AUD/USD\n[MsgType]35=8[EXECUTIONREPORT]|[Symbol]55=AUD/USD'
        '-i 35,11'                        | '[MsgType]35=D[NEWORDERSINGLE]|[ClOrdID]11=ABC\n[MsgType]35=8[EXECUTIONREPORT]'
        '-i 666'                          | ''
        '--exclude-tags 11'               | '[MsgType]35=D[NEWORDERSINGLE]|[Symbol]55=AUD/USD\n[MsgType]35=8[EXECUTIONREPORT]|[ExecType]150=F[TRADE_PARTIAL_FILL_OR_FILL]|[Symbol]55=AUD/USD'
        '-e 11'                           | '[MsgType]35=D[NEWORDERSINGLE]|[Symbol]55=AUD/USD\n[MsgType]35=8[EXECUTIONREPORT]|[ExecType]150=F[TRADE_PARTIAL_FILL_OR_FILL]|[Symbol]55=AUD/USD'
        '-i 11 -e 11'                     | ''
        '--highlights 35'                 | '\u001B[31;1m[MsgType]35=D[NEWORDERSINGLE]\u001B[0m|[ClOrdID]11=ABC|[Symbol]55=AUD/USD\n\u001B[31;1m[MsgType]35=8[EXECUTIONREPORT]\u001B[0m|[ExecType]150=F[TRADE_PARTIAL_FILL_OR_FILL]|[Symbol]55=AUD/USD'
        '-h 35'                           | '\u001B[31;1m[MsgType]35=D[NEWORDERSINGLE]\u001B[0m|[ClOrdID]11=ABC|[Symbol]55=AUD/USD\n\u001B[31;1m[MsgType]35=8[EXECUTIONREPORT]\u001B[0m|[ExecType]150=F[TRADE_PARTIAL_FILL_OR_FILL]|[Symbol]55=AUD/USD'
        '-h 35,55'                        | '\u001B[32;1m[MsgType]35=D[NEWORDERSINGLE]\u001B[0m|[ClOrdID]11=ABC|\u001B[31;1m[Symbol]55=AUD/USD\u001B[0m\n\u001B[32;1m[MsgType]35=8[EXECUTIONREPORT]\u001B[0m|[ExecType]150=F[TRADE_PARTIAL_FILL_OR_FILL]|\u001B[31;1m[Symbol]55=AUD/USD\u001B[0m'
        '-h 35:Bg8,55:Bg9:Field'          | '\u001B[48;5;8m[MsgType]35=D[NEWORDERSINGLE]\u001B[0m|[ClOrdID]11=ABC|\u001B[48;5;9m[Symbol]55=AUD/USD\u001B[0m\n\u001B[48;5;8m[MsgType]35=8[EXECUTIONREPORT]\u001B[0m|[ExecType]150=F[TRADE_PARTIAL_FILL_OR_FILL]|\u001B[48;5;9m[Symbol]55=AUD/USD\u001B[0m'
        '-h 35=D:Line,55'                 | '\u001B[32;1m[MsgType]35=D[NEWORDERSINGLE]\u001B[0m|\u001B[32;1m[ClOrdID]11=ABC\u001B[0m|\u001B[32;1m\u001B[31;1m[Symbol]55=AUD/USD\u001B[0m\n[MsgType]35=8[EXECUTIONREPORT]|[ExecType]150=F[TRADE_PARTIAL_FILL_OR_FILL]|\u001B[31;1m[Symbol]55=AUD/USD\u001B[0m'
        '-l "${msgColor}[${msgTypeName}]${colorReset} ${msgFix}"'           | '\u001B[34;1m[NewOrderSingle]\u001B[0m [MsgType]35=D[NEWORDERSINGLE]|[ClOrdID]11=ABC|[Symbol]55=AUD/USD\n\u001B[34m[Exec.Trade]\u001B[0m [MsgType]35=8[EXECUTIONREPORT]|[ExecType]150=F[TRADE_PARTIAL_FILL_OR_FILL]|[Symbol]55=AUD/USD'
        '-h 35 -l "${msgColor}[${msgTypeName}]${colorReset} ${msgFix}"'     | '\u001B[34;1m[NewOrderSingle]\u001B[0m \u001B[31;1m[MsgType]35=D[NEWORDERSINGLE]\u001B[0m|[ClOrdID]11=ABC|[Symbol]55=AUD/USD\n\u001B[34m[Exec.Trade]\u001B[0m \u001B[31;1m[MsgType]35=8[EXECUTIONREPORT]\u001B[0m|[ExecType]150=F[TRADE_PARTIAL_FILL_OR_FILL]|[Symbol]55=AUD/USD'
        '-h 35 -n -l "${msgColor}[${msgTypeName}]${colorReset} ${msgFix}"'  | '[NewOrderSingle] [MsgType]35=D[NEWORDERSINGLE]|[ClOrdID]11=ABC|[Symbol]55=AUD/USD\n[Exec.Trade] [MsgType]35=8[EXECUTIONREPORT]|[ExecType]150=F[TRADE_PARTIAL_FILL_OR_FILL]|[Symbol]55=AUD/USD'

    }

    @Unroll
    def 'test line regex #args'(final String args, final String expectedOutput){
        given:
        final String fix = "2018-04-23 [6] 35=D${a}11=ABC${a}55=AUD/USD\n2018-04-23 [6] 35=8${a}150=F${a}55=AUD/USD"

        when:
        def lines = parseToLines(args, fix)

        then:
        assert lines == expectedOutput

        where:
        args                                                                                         | expectedOutput
        '--line-regex "\\d\\d\\d\\d-\\d\\d-\\d\\d \\[\\d\\] (\\d+=.*)" --line-regexgroup-for-fix 1 ' | '[MsgType]35=D[NEWORDERSINGLE]|[ClOrdID]11=ABC|[Symbol]55=AUD/USD\n[MsgType]35=8[EXECUTIONREPORT]|[ExecType]150=F[TRADE_PARTIAL_FILL_OR_FILL]|[Symbol]55=AUD/USD'
    }

    def 'test line regex with different line format'(){
        given:
        final String fix = "2018-04-23 [6] 35=D${a}11=ABC${a}55=AUD/USD\n2018-04-23 [7] 35=8${a}150=F${a}55=AUD/USD"

        when:
        def args = '--line-regex "\\d\\d\\d\\d-\\d\\d-\\d\\d \\[(\\d)\\] (\\d+=.*)"' +
                ' --line-regexgroup-for-fix 2' +
                ' --line-format "Thread:$1 ${msgFix}"'

        def expectedOutput = 'Thread:6 [MsgType]35=D[NEWORDERSINGLE]|[ClOrdID]11=ABC|[Symbol]55=AUD/USD\n' +
                             'Thread:7 [MsgType]35=8[EXECUTIONREPORT]|[ExecType]150=F[TRADE_PARTIAL_FILL_OR_FILL]|[Symbol]55=AUD/USD'

        def lines = parseToLines(args, fix)

        then:
        assert lines == expectedOutput
    }


    @Unroll
    def 'test input delimiter #args'(final String args, final String expectedOutput){
        given:
        final String fix = "35=D:11=ABC:55=AUD/USD\n35=8:150=F:55=AUD/USD"

        when:
        def lines = parseToLines(args, fix)

        then:
        assert lines == expectedOutput

        where:
        args                            | expectedOutput
        '--input-delimiter :'           | '[MsgType]35=D[NEWORDERSINGLE]|[ClOrdID]11=ABC|[Symbol]55=AUD/USD\n[MsgType]35=8[EXECUTIONREPORT]|[ExecType]150=F[TRADE_PARTIAL_FILL_OR_FILL]|[Symbol]55=AUD/USD'
        '-d :'                          | '[MsgType]35=D[NEWORDERSINGLE]|[ClOrdID]11=ABC|[Symbol]55=AUD/USD\n[MsgType]35=8[EXECUTIONREPORT]|[ExecType]150=F[TRADE_PARTIAL_FILL_OR_FILL]|[Symbol]55=AUD/USD'
    }

    private String parseToLines(final String args, final String fix){
        final List<String> argsList = new ArgsAsString(args).toArgs()

        final CircularBufferedReaderWriter input = new CircularBufferedReaderWriter();
        final CircularBufferedReaderWriter output = new CircularBufferedReaderWriter();
        final Config testConfig = new ConfigBuilder(argsList, this.overrides).config

        input.writer.write(fix)
        input.writer.flush()
        input.writer.close()

        new FixGrep(input.inputStream, output.outputStream, testConfig).go()
        output.outputStream.flush()
        String lines = output.readLines('\n')
        println lines
        return lines
    }
}
