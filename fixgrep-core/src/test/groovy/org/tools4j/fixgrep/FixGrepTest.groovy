package org.tools4j.fixgrep

import org.tools4j.fix.Ascii1Char
import org.tools4j.fixgrep.html.CssFile
import org.tools4j.fixgrep.html.TemplatedHtmlFile
import org.tools4j.properties.Config
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
    @Shared private TemplatedHtmlFile htmlFile;
    private static File actualOutputForFeedbackIntoTest = new File("test-output.txt")

    static {
        if(actualOutputForFeedbackIntoTest.exists()){
            actualOutputForFeedbackIntoTest.delete()
        }
        actualOutputForFeedbackIntoTest.createNewFile()
    }

    def setupSpec(){
        testOverrides = new ConfigImpl(['output.line.format': '${msgFix}'])
//        new CssFile().copyToWorkingDirIfRequired()
//        htmlFile = new TemplatedHtmlFile();
//        htmlFile.open();
    }

    def cleanupSpec(){
//        htmlFile.close()
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
        ''                                 | '[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1mD\u001b[22m[NEWORDERSINGLE]|[ClOrdID]\u001b[1m11\u001b[22m\u001b[1m=\u001b[22m\u001b[1mABC\u001b[22m|[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m\n[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1m8\u001b[22m[EXECUTIONREPORT]|[ExecType]\u001b[1m150\u001b[22m\u001b[1m=\u001b[22m\u001b[1mF\u001b[22m[TRADE_PARTIAL_FILL_OR_FILL]|[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m'
        '-p'                               | '[MsgType]35=D[NEWORDERSINGLE]|[ClOrdID]11=ABC|[Symbol]55=AUD/USD\n[MsgType]35=8[EXECUTIONREPORT]|[ExecType]150=F[TRADE_PARTIAL_FILL_OR_FILL]|[Symbol]55=AUD/USD'
        '--suppress-bold-tags-and-values'  | '[MsgType]35=D[NEWORDERSINGLE]|[ClOrdID]11=ABC|[Symbol]55=AUD/USD\n[MsgType]35=8[EXECUTIONREPORT]|[ExecType]150=F[TRADE_PARTIAL_FILL_OR_FILL]|[Symbol]55=AUD/USD'
        '--exclude-messages-of-type 8'     | '[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1mD\u001b[22m[NEWORDERSINGLE]|[ClOrdID]\u001b[1m11\u001b[22m\u001b[1m=\u001b[22m\u001b[1mABC\u001b[22m|[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m'
        '-v 8'                             | '[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1mD\u001b[22m[NEWORDERSINGLE]|[ClOrdID]\u001b[1m11\u001b[22m\u001b[1m=\u001b[22m\u001b[1mABC\u001b[22m|[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m'
        '--exclude-messages-of-type D'     | '[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1m8\u001b[22m[EXECUTIONREPORT]|[ExecType]\u001b[1m150\u001b[22m\u001b[1m=\u001b[22m\u001b[1mF\u001b[22m[TRADE_PARTIAL_FILL_OR_FILL]|[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m'
        '--exclude-messages-of-type D,8'   | ''
        '-v D -v 8'                        | ''
        '-v D --tag-annotations __'        | '35=8|150=F|55=AUD/USD'
        '-v D --tag-annotations b_'        | '[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1m8\u001b[22m|[ExecType]\u001b[1m150\u001b[22m\u001b[1m=\u001b[22m\u001b[1mF\u001b[22m|[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m'
        '-m D'                             | '[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1mD\u001b[22m[NEWORDERSINGLE]|[ClOrdID]\u001b[1m11\u001b[22m\u001b[1m=\u001b[22m\u001b[1mABC\u001b[22m|[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m'
        '-m D -v D'                        | ''
        '--output-delimiter :'             | '[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1mD\u001b[22m[NEWORDERSINGLE]:[ClOrdID]\u001b[1m11\u001b[22m\u001b[1m=\u001b[22m\u001b[1mABC\u001b[22m:[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m\n[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1m8\u001b[22m[EXECUTIONREPORT]:[ExecType]\u001b[1m150\u001b[22m\u001b[1m=\u001b[22m\u001b[1mF\u001b[22m[TRADE_PARTIAL_FILL_OR_FILL]:[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m'
        '-o :'                             | '[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1mD\u001b[22m[NEWORDERSINGLE]:[ClOrdID]\u001b[1m11\u001b[22m\u001b[1m=\u001b[22m\u001b[1mABC\u001b[22m:[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m\n[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1m8\u001b[22m[EXECUTIONREPORT]:[ExecType]\u001b[1m150\u001b[22m\u001b[1m=\u001b[22m\u001b[1mF\u001b[22m[TRADE_PARTIAL_FILL_OR_FILL]:[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m'
        '--sort-by-tags 55,35'             | '[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m|[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1mD\u001b[22m[NEWORDERSINGLE]|[ClOrdID]\u001b[1m11\u001b[22m\u001b[1m=\u001b[22m\u001b[1mABC\u001b[22m\n[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m|[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1m8\u001b[22m[EXECUTIONREPORT]|[ExecType]\u001b[1m150\u001b[22m\u001b[1m=\u001b[22m\u001b[1mF\u001b[22m[TRADE_PARTIAL_FILL_OR_FILL]'
        '-s 55,35'                         | '[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m|[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1mD\u001b[22m[NEWORDERSINGLE]|[ClOrdID]\u001b[1m11\u001b[22m\u001b[1m=\u001b[22m\u001b[1mABC\u001b[22m\n[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m|[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1m8\u001b[22m[EXECUTIONREPORT]|[ExecType]\u001b[1m150\u001b[22m\u001b[1m=\u001b[22m\u001b[1mF\u001b[22m[TRADE_PARTIAL_FILL_OR_FILL]'
        '-s 55,11,35'                      | '[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m|[ClOrdID]\u001b[1m11\u001b[22m\u001b[1m=\u001b[22m\u001b[1mABC\u001b[22m|[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1mD\u001b[22m[NEWORDERSINGLE]\n[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m|[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1m8\u001b[22m[EXECUTIONREPORT]|[ExecType]\u001b[1m150\u001b[22m\u001b[1m=\u001b[22m\u001b[1mF\u001b[22m[TRADE_PARTIAL_FILL_OR_FILL]'
        '--only-include-tags 35'           | '[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1mD\u001b[22m[NEWORDERSINGLE]\n[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1m8\u001b[22m[EXECUTIONREPORT]'
        '--only-include-tags 35,55'        | '[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1mD\u001b[22m[NEWORDERSINGLE]|[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m\n[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1m8\u001b[22m[EXECUTIONREPORT]|[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m'
        '-t 35,55'                         | '[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1mD\u001b[22m[NEWORDERSINGLE]|[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m\n[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1m8\u001b[22m[EXECUTIONREPORT]|[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m'
        '-t 35,11'                         | '[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1mD\u001b[22m[NEWORDERSINGLE]|[ClOrdID]\u001b[1m11\u001b[22m\u001b[1m=\u001b[22m\u001b[1mABC\u001b[22m\n[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1m8\u001b[22m[EXECUTIONREPORT]'
        '-t 666'                           | ''
        '--exclude-tags 11'                | '[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1mD\u001b[22m[NEWORDERSINGLE]|[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m\n[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1m8\u001b[22m[EXECUTIONREPORT]|[ExecType]\u001b[1m150\u001b[22m\u001b[1m=\u001b[22m\u001b[1mF\u001b[22m[TRADE_PARTIAL_FILL_OR_FILL]|[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m'
        '-e 11'                            | '[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1mD\u001b[22m[NEWORDERSINGLE]|[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m\n[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1m8\u001b[22m[EXECUTIONREPORT]|[ExecType]\u001b[1m150\u001b[22m\u001b[1m=\u001b[22m\u001b[1mF\u001b[22m[TRADE_PARTIAL_FILL_OR_FILL]|[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m'
        '-t 11 -e 11'                      | ''
        '--highlights 35'                  | '\u001b[31;1m[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1mD\u001b[22m[NEWORDERSINGLE]\u001b[0m|[ClOrdID]\u001b[1m11\u001b[22m\u001b[1m=\u001b[22m\u001b[1mABC\u001b[22m|[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m\n\u001b[31;1m[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1m8\u001b[22m[EXECUTIONREPORT]\u001b[0m|[ExecType]\u001b[1m150\u001b[22m\u001b[1m=\u001b[22m\u001b[1mF\u001b[22m[TRADE_PARTIAL_FILL_OR_FILL]|[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m'
        '--highlights 35 -p'               | '\u001B[31;1m[MsgType]35=D[NEWORDERSINGLE]\u001B[0m|[ClOrdID]11=ABC|[Symbol]55=AUD/USD\n\u001B[31;1m[MsgType]35=8[EXECUTIONREPORT]\u001B[0m|[ExecType]150=F[TRADE_PARTIAL_FILL_OR_FILL]|[Symbol]55=AUD/USD'
        '-h 35'                            | '\u001b[31;1m[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1mD\u001b[22m[NEWORDERSINGLE]\u001b[0m|[ClOrdID]\u001b[1m11\u001b[22m\u001b[1m=\u001b[22m\u001b[1mABC\u001b[22m|[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m\n\u001b[31;1m[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1m8\u001b[22m[EXECUTIONREPORT]\u001b[0m|[ExecType]\u001b[1m150\u001b[22m\u001b[1m=\u001b[22m\u001b[1mF\u001b[22m[TRADE_PARTIAL_FILL_OR_FILL]|[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m'
        '-h 35,55'                         | '\u001b[32;1m[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1mD\u001b[22m[NEWORDERSINGLE]\u001b[0m|[ClOrdID]\u001b[1m11\u001b[22m\u001b[1m=\u001b[22m\u001b[1mABC\u001b[22m|\u001b[31;1m[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m\u001b[0m\n\u001b[32;1m[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1m8\u001b[22m[EXECUTIONREPORT]\u001b[0m|[ExecType]\u001b[1m150\u001b[22m\u001b[1m=\u001b[22m\u001b[1mF\u001b[22m[TRADE_PARTIAL_FILL_OR_FILL]|\u001b[31;1m[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m\u001b[0m'
        '-h 35:Bg8,55:Bg9:Field'           | '\u001b[48;5;8m[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1mD\u001b[22m[NEWORDERSINGLE]\u001b[0m|[ClOrdID]\u001b[1m11\u001b[22m\u001b[1m=\u001b[22m\u001b[1mABC\u001b[22m|\u001b[48;5;9m[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m\u001b[0m\n\u001b[48;5;8m[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1m8\u001b[22m[EXECUTIONREPORT]\u001b[0m|[ExecType]\u001b[1m150\u001b[22m\u001b[1m=\u001b[22m\u001b[1mF\u001b[22m[TRADE_PARTIAL_FILL_OR_FILL]|\u001b[48;5;9m[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m\u001b[0m'
        '-h 35=D:Line,55'                  | '\u001B[32;1m[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1mD\u001B[22m[NEWORDERSINGLE]\u001B[0m\u001B[32;1m|\u001B[22m\u001B[32;1m[ClOrdID]\u001B[1m11\u001B[22m\u001B[1m=\u001B[22m\u001B[1mABC\u001B[22m\u001B[0m\u001B[32;1m|\u001B[22m\u001B[32;1m\u001B[31;1m[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[22m\u001B[0m\n' +
                '[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1m8\u001B[22m[EXECUTIONREPORT]|[ExecType]\u001B[1m150\u001B[22m\u001B[1m=\u001B[22m\u001B[1mF\u001B[22m[TRADE_PARTIAL_FILL_OR_FILL]|\u001B[31;1m[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[22m\u001B[0m'
        '-F "${msgColor}[${msgTypeName}]${colorReset} ${msgFix}"'| '\u001b[34;1m[NewOrderSingle]\u001b[0m [MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1mD\u001b[22m[NEWORDERSINGLE]|[ClOrdID]\u001b[1m11\u001b[22m\u001b[1m=\u001b[22m\u001b[1mABC\u001b[22m|[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m\n\u001b[34m[Exec.Trade]\u001b[0m [MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1m8\u001b[22m[EXECUTIONREPORT]|[ExecType]\u001b[1m150\u001b[22m\u001b[1m=\u001b[22m\u001b[1mF\u001b[22m[TRADE_PARTIAL_FILL_OR_FILL]|[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m'
        '-h 35 -F "${msgColor}[${msgTypeName}]${colorReset} ${msgFix}"'| '\u001b[34;1m[NewOrderSingle]\u001b[0m \u001b[31;1m[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1mD\u001b[22m[NEWORDERSINGLE]\u001b[0m|[ClOrdID]\u001b[1m11\u001b[22m\u001b[1m=\u001b[22m\u001b[1mABC\u001b[22m|[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m\n\u001b[34m[Exec.Trade]\u001b[0m \u001b[31;1m[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1m8\u001b[22m[EXECUTIONREPORT]\u001b[0m|[ExecType]\u001b[1m150\u001b[22m\u001b[1m=\u001b[22m\u001b[1mF\u001b[22m[TRADE_PARTIAL_FILL_OR_FILL]|[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m'
        '-h 35 -n -F "${msgColor}[${msgTypeName}]${colorReset} ${msgFix}"'| '[NewOrderSingle] [MsgType]\u001B[1m35\u001B[22m\u001b[1m=\u001b[22m\u001B[1mD\u001B[22m[NEWORDERSINGLE]|[ClOrdID]\u001B[1m11\u001B[22m\u001b[1m=\u001b[22m\u001B[1mABC\u001B[22m|[Symbol]\u001B[1m55\u001B[22m\u001b[1m=\u001b[22m\u001B[1mAUD/USD\u001B[22m\n[Exec.Trade] [MsgType]\u001B[1m35\u001B[22m\u001b[1m=\u001b[22m\u001B[1m8\u001B[22m[EXECUTIONREPORT]|[ExecType]\u001B[1m150\u001B[22m\u001b[1m=\u001b[22m\u001B[1mF\u001B[22m[TRADE_PARTIAL_FILL_OR_FILL]|[Symbol]\u001B[1m55\u001B[22m\u001b[1m=\u001b[22m\u001B[1mAUD/USD\u001B[22m'
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
        '--output-delimiter :'             | '[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1mD\u001b[22m[NEWORDERSINGLE]:[ClOrdID]\u001b[1m11\u001b[22m\u001b[1m=\u001b[22m\u001b[1mABC\u001b[22m:[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m\n[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1m8\u001b[22m[EXECUTIONREPORT]:[ExecType]\u001b[1m150\u001b[22m\u001b[1m=\u001b[22m\u001b[1mF\u001b[22m[TRADE_PARTIAL_FILL_OR_FILL]:[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m'
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
        '--input-line-format "\\d\\d\\d\\d-\\d\\d-\\d\\d \\[\\d\\] (\\d+=.*)" --line-regexgroup-for-fix 1 '| '[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1mD\u001B[22m[NEWORDERSINGLE]|[ClOrdID]\u001B[1m11\u001B[22m\u001B[1m=\u001B[22m\u001B[1mABC\u001B[22m|[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[22m\n[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1m8\u001B[22m[EXECUTIONREPORT]|[ExecType]\u001B[1m150\u001B[22m\u001B[1m=\u001B[22m\u001B[1mF\u001B[22m[TRADE_PARTIAL_FILL_OR_FILL]|[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[22m'
    }

    def 'test line regex with different line format'(){
        given:
        final String fix = "2018-04-23 [6] 35=D${a}11=ABC${a}55=AUD/USD\n2018-04-23 [7] 35=8${a}150=F${a}55=AUD/USD"

        when:
        def args = '--input-line-format "\\d\\d\\d\\d-\\d\\d-\\d\\d \\[(\\d)\\] (\\d+=.*)"' +
                ' --line-regexgroup-for-fix 2' +
                ' --output-line-format "Thread:$1 ${msgFix}"'

        def expectedOutput = 'Thread:6 [MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1mD\u001B[22m[NEWORDERSINGLE]|[ClOrdID]\u001B[1m11\u001B[22m\u001B[1m=\u001B[22m\u001B[1mABC\u001B[22m|[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[22m\n' +
                'Thread:7 [MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1m8\u001B[22m[EXECUTIONREPORT]|[ExecType]\u001B[1m150\u001B[22m\u001B[1m=\u001B[22m\u001B[1mF\u001B[22m[TRADE_PARTIAL_FILL_OR_FILL]|[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[22m'


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

        def expectedOutput = 'Thread:6 [MsgType][1m35[22m[1m=[22m[1mD[22m[NEWORDERSINGLE]|[ClOrdID][1m11[22m[1m=[22m[1mABC[22m|[Symbol][1m55[22m[1m=[22m[1mAUD/USD[22m\nThread:7 [MsgType][1m35[22m[1m=[22m[1m8[22m[EXECUTIONREPORT]|[ExecType][1m150[22m[1m=[22m[1mF[22m[TRADE_PARTIAL_FILL_OR_FILL]|[Symbol][1m55[22m[1m=[22m[1mAUD/USD[22m'

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
        '--input-delimiter :'              | '[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1mD\u001B[22m[NEWORDERSINGLE]|[ClOrdID]\u001B[1m11\u001B[22m\u001B[1m=\u001B[22m\u001B[1mABC\u001B[22m|[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[22m\n[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1m8\u001B[22m[EXECUTIONREPORT]|[ExecType]\u001B[1m150\u001B[22m\u001B[1m=\u001B[22m\u001B[1mF\u001B[22m[TRADE_PARTIAL_FILL_OR_FILL]|[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[22m'
        '-d :'                             | '[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1mD\u001B[22m[NEWORDERSINGLE]|[ClOrdID]\u001B[1m11\u001B[22m\u001B[1m=\u001B[22m\u001B[1mABC\u001B[22m|[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[22m\n[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1m8\u001B[22m[EXECUTIONREPORT]|[ExecType]\u001B[1m150\u001B[22m\u001B[1m=\u001B[22m\u001B[1mF\u001B[22m[TRADE_PARTIAL_FILL_OR_FILL]|[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[22m'
    }

    private String parseToLines(final String args, final String fix){
        final List<String> argsList = new ArgsAsString(args).toArgs()

        final CircularBufferedReaderWriter input = new CircularBufferedReaderWriter();
        final CircularBufferedReaderWriter output = new CircularBufferedReaderWriter();
        final Config testConfig = new ConfigBuilder(argsList, this.testOverrides).config

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
