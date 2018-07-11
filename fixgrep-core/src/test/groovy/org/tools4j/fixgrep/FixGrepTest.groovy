package org.tools4j.fixgrep

import org.tools4j.fix.Ascii1Char
import org.tools4j.properties.Config
import org.tools4j.properties.ConfigAndArguments
import org.tools4j.properties.ConfigImpl
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
class FixGrepTest extends Specification {
    @Shared private final static String a = new Ascii1Char().toString()
    @Shared private Config testOverrides
    private static File actualOutputForFeedbackIntoTest = new File("test-output.txt")

    def setupSpec(){
        if(actualOutputForFeedbackIntoTest.exists()){
            actualOutputForFeedbackIntoTest.delete()
        }
        actualOutputForFeedbackIntoTest.createNewFile()
        testOverrides = new ConfigImpl(['output.line.format': '${msgFix}', 'piped.input': 'true'])
    }

    def cleanupSpec(){
        if(actualOutputForFeedbackIntoTest.exists()) actualOutputForFeedbackIntoTest.delete()
    }

    @Unroll
    def 'test consoleText #args'(final String args, final String expectedOutput){
        given:
        final String fix = "35=D${a}11=ABC${a}55=AUD/USD\n35=8${a}150=F${a}55=AUD/USD"

        when:
        def lines = parseToLines(args, fix)
        println 'expected:' + expectedOutput

        then:
        assert lines == expectedOutput

        where:
        args                               | expectedOutput
        ''                                 | '[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1mD\u001B[22m[NEWORDERSINGLE]|[ClOrdID]\u001B[1m11\u001B[22m\u001B[1m=\u001B[22m\u001B[1mABC\u001B[0m|[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[0m\n[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1m8\u001B[22m[EXECUTIONREPORT]|[ExecType]\u001B[1m150\u001B[22m\u001B[1m=\u001B[22m\u001B[1mF\u001B[22m[TRADE_PARTIAL_FILL_OR_FILL]|[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[0m'
        '--debug'                          | '[MsgType]\\u001b[1m35\\u001b[22m\\u001b[1m=\\u001b[22m\\u001b[1mD\\u001b[22m[NEWORDERSINGLE]|[ClOrdID]\\u001b[1m11\\u001b[22m\\u001b[1m=\\u001b[22m\\u001b[1mABC\\u001b[0m|[Symbol]\\u001b[1m55\\u001b[22m\\u001b[1m=\\u001b[22m\\u001b[1mAUD/USD\\u001b[0m\n[MsgType]\\u001b[1m35\\u001b[22m\\u001b[1m=\\u001b[22m\\u001b[1m8\\u001b[22m[EXECUTIONREPORT]|[ExecType]\\u001b[1m150\\u001b[22m\\u001b[1m=\\u001b[22m\\u001b[1mF\\u001b[22m[TRADE_PARTIAL_FILL_OR_FILL]|[Symbol]\\u001b[1m55\\u001b[22m\\u001b[1m=\\u001b[22m\\u001b[1mAUD/USD\\u001b[0m';
        '-x'                               | '[MsgType]\\u001b[1m35\\u001b[22m\\u001b[1m=\\u001b[22m\\u001b[1mD\\u001b[22m[NEWORDERSINGLE]|[ClOrdID]\\u001b[1m11\\u001b[22m\\u001b[1m=\\u001b[22m\\u001b[1mABC\\u001b[0m|[Symbol]\\u001b[1m55\\u001b[22m\\u001b[1m=\\u001b[22m\\u001b[1mAUD/USD\\u001b[0m\n[MsgType]\\u001b[1m35\\u001b[22m\\u001b[1m=\\u001b[22m\\u001b[1m8\\u001b[22m[EXECUTIONREPORT]|[ExecType]\\u001b[1m150\\u001b[22m\\u001b[1m=\\u001b[22m\\u001b[1mF\\u001b[22m[TRADE_PARTIAL_FILL_OR_FILL]|[Symbol]\\u001b[1m55\\u001b[22m\\u001b[1m=\\u001b[22m\\u001b[1mAUD/USD\\u001b[0m'
        '-q'                               | '[MsgType]35=D[NEWORDERSINGLE]|[ClOrdID]11=ABC|[Symbol]55=AUD/USD\n[MsgType]35=8[EXECUTIONREPORT]|[ExecType]150=F[TRADE_PARTIAL_FILL_OR_FILL]|[Symbol]55=AUD/USD'
        '-v 8'                             | '[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1mD\u001B[22m[NEWORDERSINGLE]|[ClOrdID]\u001B[1m11\u001B[22m\u001B[1m=\u001B[22m\u001B[1mABC\u001B[0m|[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[0m';
        '--exclude-messages-of-type D'     | '[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1m8\u001B[22m[EXECUTIONREPORT]|[ExecType]\u001B[1m150\u001B[22m\u001B[1m=\u001B[22m\u001B[1mF\u001B[22m[TRADE_PARTIAL_FILL_OR_FILL]|[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[0m'
        '--exclude-messages-of-type D,8'   | ''
        '-v D -v 8'                        | ''
        '-v D --tag-annotations __'        | '35=8|150=F|55=AUD/USD'
        '-v D --tag-annotations b_'        | '[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1m8\u001B[22m|[ExecType]\u001B[1m150\u001B[22m\u001B[1m=\u001B[22m\u001B[1mF\u001B[22m|[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[0m'
        '-m D'                             | '[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1mD\u001B[22m[NEWORDERSINGLE]|[ClOrdID]\u001B[1m11\u001B[22m\u001B[1m=\u001B[22m\u001B[1mABC\u001B[0m|[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[0m'
        '-m D -v D'                        | ''
        '--output-delimiter :'             | '[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1mD\u001B[22m[NEWORDERSINGLE]:[ClOrdID]\u001B[1m11\u001B[22m\u001B[1m=\u001B[22m\u001B[1mABC\u001B[0m:[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[0m\n[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1m8\u001B[22m[EXECUTIONREPORT]:[ExecType]\u001B[1m150\u001B[22m\u001B[1m=\u001B[22m\u001B[1mF\u001B[22m[TRADE_PARTIAL_FILL_OR_FILL]:[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[0m'
        '-o :'                             | '[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1mD\u001B[22m[NEWORDERSINGLE]:[ClOrdID]\u001B[1m11\u001B[22m\u001B[1m=\u001B[22m\u001B[1mABC\u001B[0m:[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[0m\n[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1m8\u001B[22m[EXECUTIONREPORT]:[ExecType]\u001B[1m150\u001B[22m\u001B[1m=\u001B[22m\u001B[1mF\u001B[22m[TRADE_PARTIAL_FILL_OR_FILL]:[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[0m'
        '--sort-by-tags 55,35'             | '[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[0m|[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1mD\u001B[22m[NEWORDERSINGLE]|[ClOrdID]\u001B[1m11\u001B[22m\u001B[1m=\u001B[22m\u001B[1mABC\u001B[0m\n[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[0m|[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1m8\u001B[22m[EXECUTIONREPORT]|[ExecType]\u001B[1m150\u001B[22m\u001B[1m=\u001B[22m\u001B[1mF\u001B[22m[TRADE_PARTIAL_FILL_OR_FILL]'
        '-s 55,35'                         | '[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[0m|[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1mD\u001B[22m[NEWORDERSINGLE]|[ClOrdID]\u001B[1m11\u001B[22m\u001B[1m=\u001B[22m\u001B[1mABC\u001B[0m\n[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[0m|[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1m8\u001B[22m[EXECUTIONREPORT]|[ExecType]\u001B[1m150\u001B[22m\u001B[1m=\u001B[22m\u001B[1mF\u001B[22m[TRADE_PARTIAL_FILL_OR_FILL]'
        '-s 55,11,35'                      | '[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[0m|[ClOrdID]\u001B[1m11\u001B[22m\u001B[1m=\u001B[22m\u001B[1mABC\u001B[0m|[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1mD\u001B[22m[NEWORDERSINGLE]\n[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[0m|[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1m8\u001B[22m[EXECUTIONREPORT]|[ExecType]\u001B[1m150\u001B[22m\u001B[1m=\u001B[22m\u001B[1mF\u001B[22m[TRADE_PARTIAL_FILL_OR_FILL]'
        '--only-include-tags 35'           | '[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1mD\u001B[22m[NEWORDERSINGLE]\n[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1m8\u001B[22m[EXECUTIONREPORT]'
        '--only-include-tags 35,55'        | '[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1mD\u001B[22m[NEWORDERSINGLE]|[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[0m\n[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1m8\u001B[22m[EXECUTIONREPORT]|[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[0m'
        '-t 35,55'                         | '[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1mD\u001B[22m[NEWORDERSINGLE]|[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[0m\n[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1m8\u001B[22m[EXECUTIONREPORT]|[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[0m'
        '-t 35,11'                         | '[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1mD\u001B[22m[NEWORDERSINGLE]|[ClOrdID]\u001B[1m11\u001B[22m\u001B[1m=\u001B[22m\u001B[1mABC\u001B[0m\n[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1m8\u001B[22m[EXECUTIONREPORT]'
        '-t 666'                           | ''
        '--exclude-tags 11'                | '[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1mD\u001B[22m[NEWORDERSINGLE]|[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[0m\n[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1m8\u001B[22m[EXECUTIONREPORT]|[ExecType]\u001B[1m150\u001B[22m\u001B[1m=\u001B[22m\u001B[1mF\u001B[22m[TRADE_PARTIAL_FILL_OR_FILL]|[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[0m'
        '-e 11'                            | '[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1mD\u001B[22m[NEWORDERSINGLE]|[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[0m\n[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1m8\u001B[22m[EXECUTIONREPORT]|[ExecType]\u001B[1m150\u001B[22m\u001B[1m=\u001B[22m\u001B[1mF\u001B[22m[TRADE_PARTIAL_FILL_OR_FILL]|[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[0m'
        '-t 11 -e 11'                      | ''
        '--highlights 35'                  | '\u001B[31m[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1mD\u001B[22m[NEWORDERSINGLE]\u001B[0m|[ClOrdID]\u001B[1m11\u001B[22m\u001B[1m=\u001B[22m\u001B[1mABC\u001B[0m|[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[0m\n\u001B[31m[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1m8\u001B[22m[EXECUTIONREPORT]\u001B[0m|[ExecType]\u001B[1m150\u001B[22m\u001B[1m=\u001B[22m\u001B[1mF\u001B[22m[TRADE_PARTIAL_FILL_OR_FILL]|[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[0m'
        '--highlights 35 -p'               | '\u001B[31m[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1mD\u001B[22m[NEWORDERSINGLE]\u001B[0m|[ClOrdID]\u001B[1m11\u001B[22m\u001B[1m=\u001B[22m\u001B[1mABC\u001B[0m|[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[0m\n\u001B[31m[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1m8\u001B[22m[EXECUTIONREPORT]\u001B[0m|[ExecType]\u001B[1m150\u001B[22m\u001B[1m=\u001B[22m\u001B[1mF\u001B[22m[TRADE_PARTIAL_FILL_OR_FILL]|[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[0m'
        '-h 35'                            | '\u001B[31m[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1mD\u001B[22m[NEWORDERSINGLE]\u001B[0m|[ClOrdID]\u001B[1m11\u001B[22m\u001B[1m=\u001B[22m\u001B[1mABC\u001B[0m|[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[0m\n\u001B[31m[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1m8\u001B[22m[EXECUTIONREPORT]\u001B[0m|[ExecType]\u001B[1m150\u001B[22m\u001B[1m=\u001B[22m\u001B[1mF\u001B[22m[TRADE_PARTIAL_FILL_OR_FILL]|[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[0m'
        '-h 35,55'                         | '\u001B[32m[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1mD\u001B[22m[NEWORDERSINGLE]\u001B[0m|[ClOrdID]\u001B[1m11\u001B[22m\u001B[1m=\u001B[22m\u001B[1mABC\u001B[0m|\u001B[31m[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[0m\n\u001B[32m[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1m8\u001B[22m[EXECUTIONREPORT]\u001B[0m|[ExecType]\u001B[1m150\u001B[22m\u001B[1m=\u001B[22m\u001B[1mF\u001B[22m[TRADE_PARTIAL_FILL_OR_FILL]|\u001B[31m[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[0m'
        '-h 35:Bg8,55:Bg9:Field'           | '\u001B[48;5;8m[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1mD\u001B[22m[NEWORDERSINGLE]\u001B[0m|[ClOrdID]\u001B[1m11\u001B[22m\u001B[1m=\u001B[22m\u001B[1mABC\u001B[0m|\u001B[48;5;9m[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[0m\n\u001B[48;5;8m[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1m8\u001B[22m[EXECUTIONREPORT]\u001B[0m|[ExecType]\u001B[1m150\u001B[22m\u001B[1m=\u001B[22m\u001B[1mF\u001B[22m[TRADE_PARTIAL_FILL_OR_FILL]|\u001B[48;5;9m[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[0m'
        '-h 35=D:Line,55'                  | '\u001B[32m[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1mD\u001B[22m[NEWORDERSINGLE]\u001B[0m\u001B[32m|\u001B[0m\u001B[32m[ClOrdID]\u001B[1m11\u001B[22m\u001B[1m=\u001B[22m\u001B[1mABC\u001B[0m\u001B[32m|\u001B[0m\u001B[31m\u001B[32m\u001B[31m[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[0m\n' + '[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1m8\u001B[22m[EXECUTIONREPORT]|[ExecType]\u001B[1m150\u001B[22m\u001B[1m=\u001B[22m\u001B[1mF\u001B[22m[TRADE_PARTIAL_FILL_OR_FILL]|\u001B[31m[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[0m'
        '-h 35=D:Fg8:Line,55=AUD:BgBrightGreen' | '\u001B[38;5;8m[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1mD\u001B[22m[NEWORDERSINGLE]\u001B[0m\u001B[38;5;8m|\u001B[0m\u001B[38;5;8m[ClOrdID]\u001B[1m11\u001B[22m\u001B[1m=\u001B[22m\u001B[1mABC\u001B[0m\u001B[38;5;8m|\u001B[0m\u001B[42;1m\u001B[38;5;8m\u001B[42;1m[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[0m\n' + '[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1m8\u001B[22m[EXECUTIONREPORT]|[ExecType]\u001B[1m150\u001B[22m\u001B[1m=\u001B[22m\u001B[1mF\u001B[22m[TRADE_PARTIAL_FILL_OR_FILL]|\u001B[42;1m[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[0m'
        '-F "${msgColor}[${msgTypeName}]${colorReset} ${msgFix}"'| '\u001B[36m[NewOrderSingle]\u001B[0m [MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1mD\u001B[22m[NEWORDERSINGLE]|[ClOrdID]\u001B[1m11\u001B[22m\u001B[1m=\u001B[22m\u001B[1mABC\u001B[0m|[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[0m\n' + '\u001B[32m[Exec.Trade]\u001B[0m [MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1m8\u001B[22m[EXECUTIONREPORT]|[ExecType]\u001B[1m150\u001B[22m\u001B[1m=\u001B[22m\u001B[1mF\u001B[22m[TRADE_PARTIAL_FILL_OR_FILL]|[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[0m'
        '-h 35 -F "${msgColor}[${msgTypeName}]${colorReset} ${msgFix}"'| '\u001B[36m[NewOrderSingle]\u001B[0m \u001B[31m[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1mD\u001B[22m[NEWORDERSINGLE]\u001B[0m|[ClOrdID]\u001B[1m11\u001B[22m\u001B[1m=\u001B[22m\u001B[1mABC\u001B[0m|[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[0m\n' + '\u001B[32m[Exec.Trade]\u001B[0m \u001B[31m[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1m8\u001B[22m[EXECUTIONREPORT]\u001B[0m|[ExecType]\u001B[1m150\u001B[22m\u001B[1m=\u001B[22m\u001B[1mF\u001B[22m[TRADE_PARTIAL_FILL_OR_FILL]|[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[0m'
        '-h 35 -n -F "${msgColor}[${msgTypeName}]${colorReset} ${msgFix}"'| '[NewOrderSingle] [MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1mD\u001B[22m[NEWORDERSINGLE]|[ClOrdID]\u001B[1m11\u001B[22m\u001B[1m=\u001B[22m\u001B[1mABC\u001B[0m|[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[0m\n[Exec.Trade] [MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1m8\u001B[22m[EXECUTIONREPORT]|[ExecType]\u001B[1m150\u001B[22m\u001B[1m=\u001B[22m\u001B[1mF\u001B[22m[TRADE_PARTIAL_FILL_OR_FILL]|[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[0m'
    }

    @Unroll
    def 'single line test'(final String args, final String expectedOutput){
        given:
        final String fix = "35=D${a}11=ABC${a}55=AUD/USD\n35=8${a}150=F${a}55=AUD/USD"

        when:
        def lines = parseToLines(args, fix)
        println 'expected:' + expectedOutput

        then:
        assert lines == expectedOutput

        where:
        args                               | expectedOutput
        '--output-delimiter :'             | '[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1mD\u001B[22m[NEWORDERSINGLE]:[ClOrdID]\u001B[1m11\u001B[22m\u001B[1m=\u001B[22m\u001B[1mABC\u001B[0m:[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[0m\n[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1m8\u001B[22m[EXECUTIONREPORT]:[ExecType]\u001B[1m150\u001B[22m\u001B[1m=\u001B[22m\u001B[1mF\u001B[22m[TRADE_PARTIAL_FILL_OR_FILL]:[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[0m'
    }

    @Unroll
    def 'test line regex #args'(final String args, final String expectedOutput){
        given:
        final String fix = "2018-04-23 [6] 35=D${a}11=ABC${a}55=AUD/USD\n2018-04-23 [6] 35=8${a}150=F${a}55=AUD/USD"

        when:
        println "\033[31mnormal\033[1mbold\033[22mnormal"
        def lines = parseToLines(args, fix)

        then:
        assert lines == expectedOutput

        where:
        args                                                                                        | expectedOutput
        '--input-line-format "\\d\\d\\d\\d-\\d\\d-\\d\\d \\[\\d\\] (\\d+=.*)" --line-regexgroup-for-fix 1 '| '[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1mD\u001B[22m[NEWORDERSINGLE]|[ClOrdID]\u001B[1m11\u001B[22m\u001B[1m=\u001B[22m\u001B[1mABC\u001B[0m|[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[0m\n[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1m8\u001B[22m[EXECUTIONREPORT]|[ExecType]\u001B[1m150\u001B[22m\u001B[1m=\u001B[22m\u001B[1mF\u001B[22m[TRADE_PARTIAL_FILL_OR_FILL]|[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[0m'
    }

    def 'test line regex with different line format'(){
        given:
        final String fix = "2018-04-23 [6] 35=D${a}11=ABC${a}55=AUD/USD\n2018-04-23 [7] 35=8${a}150=F${a}55=AUD/USD"

        when:
        def args = '--input-line-format "\\d\\d\\d\\d-\\d\\d-\\d\\d \\[(\\d)\\] (\\d+=.*)"' +
                ' --line-regexgroup-for-fix 2' +
                ' --output-line-format "Thread:$1 ${msgFix}"'

        def expectedOutput = 'Thread:6 [MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1mD\u001B[22m[NEWORDERSINGLE]|[ClOrdID]\u001B[1m11\u001B[22m\u001B[1m=\u001B[22m\u001B[1mABC\u001B[0m|[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[0m\n' +
                'Thread:7 [MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1m8\u001B[22m[EXECUTIONREPORT]|[ExecType]\u001B[1m150\u001B[22m\u001B[1m=\u001B[22m\u001B[1mF\u001B[22m[TRADE_PARTIAL_FILL_OR_FILL]|[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[0m'


        def lines = parseToLines(args, fix)

        then:
        assert lines == expectedOutput
    }

    def 'test line regex with different line format, abbreviated options'(){
        given:
        final String fix = "2018-04-23 [6] 35=D${a}11=ABC${a}55=AUD/USD\n2018-04-23 [7] 35=8${a}150=F${a}55=AUD/USD"

        when:
        def args = '-R "\\d\\d\\d\\d-\\d\\d-\\d\\d \\[(\\d)\\] (\\d+=.*)"' +
                ' -G 2' +
                ' -F "Thread:$1 ${msgFix}"'

        def expectedOutput = 'Thread:6 [MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1mD\u001B[22m[NEWORDERSINGLE]|[ClOrdID]\u001B[1m11\u001B[22m\u001B[1m=\u001B[22m\u001B[1mABC\u001B[0m|[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[0m\n' +
                'Thread:7 [MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1m8\u001B[22m[EXECUTIONREPORT]|[ExecType]\u001B[1m150\u001B[22m\u001B[1m=\u001B[22m\u001B[1mF\u001B[22m[TRADE_PARTIAL_FILL_OR_FILL]|[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[0m'

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
        '--input-delimiter :'              | '[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1mD\u001B[22m[NEWORDERSINGLE]|[ClOrdID]\u001B[1m11\u001B[22m\u001B[1m=\u001B[22m\u001B[1mABC\u001B[0m|[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[0m\n[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1m8\u001B[22m[EXECUTIONREPORT]|[ExecType]\u001B[1m150\u001B[22m\u001B[1m=\u001B[22m\u001B[1mF\u001B[22m[TRADE_PARTIAL_FILL_OR_FILL]|[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[0m'
        '-d :'                             | '[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1mD\u001B[22m[NEWORDERSINGLE]|[ClOrdID]\u001B[1m11\u001B[22m\u001B[1m=\u001B[22m\u001B[1mABC\u001B[0m|[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[0m\n[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1m8\u001B[22m[EXECUTIONREPORT]|[ExecType]\u001B[1m150\u001B[22m\u001B[1m=\u001B[22m\u001B[1mF\u001B[22m[TRADE_PARTIAL_FILL_OR_FILL]|[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[0m'
    }

    private String parseToLines(final String args, final String fix){
        final List<String> argsList = new ArgsAsString(args).toArgs()

        final CircularBufferedReaderWriter input = new CircularBufferedReaderWriter();
        final CircularBufferedReaderWriter output = new CircularBufferedReaderWriter();
        final ConfigAndArguments testConfig = new ConfigBuilder(argsList, this.testOverrides).getConfigAndArguments()

        input.writer.write(fix)
        input.writer.flush()
        input.writer.close()

        new FixGrep(input.inputStream, output.outputStream, testConfig).go()
        output.outputStream.flush()
        String lines = output.readLines('\n')
        def testCriteriaIfActualIsCorrect = ("'" + args + "'").padRight(35) + "| '" + lines.replace("\n", "\\" + "n").replace('\u001b', '\\' + 'u001b') + "'"
        actualOutputForFeedbackIntoTest.append(testCriteriaIfActualIsCorrect + '\n')
        println 'actual:  ' + lines
        return lines
    }
}
