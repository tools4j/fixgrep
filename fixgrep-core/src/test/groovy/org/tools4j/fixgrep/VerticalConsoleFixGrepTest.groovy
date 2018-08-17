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
class VerticalConsoleFixGrepTest extends Specification {
    @Shared private final static String a = new Ascii1Char().toString()
    @Shared private String testOverrides;

    private static File newAssertionsFile = new File("new-assertions.txt")
    private static File resultsFile = new File("results.txt")
    private static boolean logResultsToFile = true;
    private static boolean logNewAssertionsToFile = true;
    private static boolean launchResultInBrowser = true

    def setupSpec() {
        if(logNewAssertionsToFile) deleteAndCreateNewFile(newAssertionsFile)
        if(logResultsToFile) deleteAndCreateNewFile(resultsFile)
        testOverrides = ' -V --html -p'
    }

    def 'test various scenarios'(){
        given:
        testOverrides = ' -V --html -p --output-format-vertical-html "${msgFix}"'

        expect:
        final String fix = "35=D${a}11=ABC${a}55=AUD/USD\n35=8${a}150=F${a}55=AUD/USD"

//        assert parseToLines('-q', fix) == """<table class='fields'>
//<tr class='field annotatedField'><td class='tag-annotation'>MsgType</td><td class='tag-raw'>35</td><td class='equals'>=</td><td class='value-raw'>D</td><td class='value-annotation'>NEWORDERSINGLE</td></tr>
//<tr class='field annotatedField'><td class='tag-annotation'>ClOrdID</td><td class='tag-raw'>11</td><td class='equals'>=</td><td class='value-raw' colspan='2'>ABC</td></tr>
//<tr class='field annotatedField'><td class='tag-annotation'>Symbol</td><td class='tag-raw'>55</td><td class='equals'>=</td><td class='value-raw' colspan='2'>AUD/USD</td></tr>
//</table>
//
//<table class='fields'>
//<tr class='field annotatedField'><td class='tag-annotation'>MsgType</td><td class='tag-raw'>35</td><td class='equals'>=</td><td class='value-raw'>8</td><td class='value-annotation'>EXECUTIONREPORT</td></tr>
//<tr class='field annotatedField'><td class='tag-annotation'>ExecType</td><td class='tag-raw'>150</td><td class='equals'>=</td><td class='value-raw'>F</td><td class='value-annotation'>TRADE_PARTIAL_FILL_OR_FILL</td></tr>
//<tr class='field annotatedField'><td class='tag-annotation'>Symbol</td><td class='tag-raw'>55</td><td class='equals'>=</td><td class='value-raw' colspan='2'>AUD/USD</td></tr>
//</table>
//"""
//
//        assert parseToLines('-v 8', fix) == """<table class='fields'>
//<tr class='field annotatedField'><td class='tag-annotation'>MsgType</td><td class='tag-raw bold'>35</td><td class='equals bold'>=</td><td class='value-raw bold'>D</td><td class='value-annotation'>NEWORDERSINGLE</td></tr>
//<tr class='field annotatedField'><td class='tag-annotation'>ClOrdID</td><td class='tag-raw bold'>11</td><td class='equals bold'>=</td><td class='value-raw bold' colspan='2'>ABC</td></tr>
//<tr class='field annotatedField'><td class='tag-annotation'>Symbol</td><td class='tag-raw bold'>55</td><td class='equals bold'>=</td><td class='value-raw bold' colspan='2'>AUD/USD</td></tr>
//</table>
//"""
//
//        assert parseToLines('--exclude-messages-of-type D', fix) == """<table class='fields'>
//<tr class='field annotatedField'><td class='tag-annotation'>MsgType</td><td class='tag-raw bold'>35</td><td class='equals bold'>=</td><td class='value-raw bold'>8</td><td class='value-annotation'>EXECUTIONREPORT</td></tr>
//<tr class='field annotatedField'><td class='tag-annotation'>ExecType</td><td class='tag-raw bold'>150</td><td class='equals bold'>=</td><td class='value-raw bold'>F</td><td class='value-annotation'>TRADE_PARTIAL_FILL_OR_FILL</td></tr>
//<tr class='field annotatedField'><td class='tag-annotation'>Symbol</td><td class='tag-raw bold'>55</td><td class='equals bold'>=</td><td class='value-raw bold' colspan='2'>AUD/USD</td></tr>
//</table>
//"""
//
//
//        assert parseToLines('--exclude-messages-of-type D,8', fix) == ''
//        assert parseToLines('-v D -v 8', fix) == ''
//
//        assert parseToLines('-v D --tag-annotations __', fix) == """<table class='fields'>
//<tr class='field'><td class='tag-raw bold'>35</td><td class='equals bold'>=</td><td class='value-raw bold'>8</td></tr>
//<tr class='field'><td class='tag-raw bold'>150</td><td class='equals bold'>=</td><td class='value-raw bold'>F</td></tr>
//<tr class='field'><td class='tag-raw bold'>55</td><td class='equals bold'>=</td><td class='value-raw bold' colspan='2'>AUD/USD</td></tr>
//</table>
//"""
//
//        assert parseToLines('-v D --tag-annotations b_', fix) == """<table class='fields'>
//<tr class='field annotatedField'><td class='tag-annotation'>MsgType</td><td class='tag-raw bold'>35</td><td class='equals bold'>=</td><td class='value-raw bold'>8</td></tr>
//<tr class='field annotatedField'><td class='tag-annotation'>ExecType</td><td class='tag-raw bold'>150</td><td class='equals bold'>=</td><td class='value-raw bold'>F</td></tr>
//<tr class='field annotatedField'><td class='tag-annotation'>Symbol</td><td class='tag-raw bold'>55</td><td class='equals bold'>=</td><td class='value-raw bold' colspan='2'>AUD/USD</td></tr>
//</table>
//"""
//
//        assert parseToLines('-m D', fix) == """<table class='fields'>
//<tr class='field annotatedField'><td class='tag-annotation'>MsgType</td><td class='tag-raw bold'>35</td><td class='equals bold'>=</td><td class='value-raw bold'>D</td><td class='value-annotation'>NEWORDERSINGLE</td></tr>
//<tr class='field annotatedField'><td class='tag-annotation'>ClOrdID</td><td class='tag-raw bold'>11</td><td class='equals bold'>=</td><td class='value-raw bold' colspan='2'>ABC</td></tr>
//<tr class='field annotatedField'><td class='tag-annotation'>Symbol</td><td class='tag-raw bold'>55</td><td class='equals bold'>=</td><td class='value-raw bold' colspan='2'>AUD/USD</td></tr>
//</table>
//"""
//
//        assert parseToLines('-m D -v D', fix) == ''
//
        assert parseToLines('--output-delimiter :', fix) == ''
//
//        '--output-delimiter :'             | '[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1mD\u001b[22m[NEWORDERSINGLE]:[ClOrdID]\u001b[1m11\u001b[22m\u001b[1m=\u001b[22m\u001b[1mABC\u001b[22m:[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m\n[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1m8\u001b[22m[EXECUTIONREPORT]:[ExecType]\u001b[1m150\u001b[22m\u001b[1m=\u001b[22m\u001b[1mF\u001b[22m[TRADE_PARTIAL_FILL_OR_FILL]:[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m'
//        '-o :'                             | '[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1mD\u001b[22m[NEWORDERSINGLE]:[ClOrdID]\u001b[1m11\u001b[22m\u001b[1m=\u001b[22m\u001b[1mABC\u001b[22m:[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m\n[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1m8\u001b[22m[EXECUTIONREPORT]:[ExecType]\u001b[1m150\u001b[22m\u001b[1m=\u001b[22m\u001b[1mF\u001b[22m[TRADE_PARTIAL_FILL_OR_FILL]:[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m'
//        '--sort-by-tags 55,35'             | '[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m|[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1mD\u001b[22m[NEWORDERSINGLE]|[ClOrdID]\u001b[1m11\u001b[22m\u001b[1m=\u001b[22m\u001b[1mABC\u001b[22m\n[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m|[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1m8\u001b[22m[EXECUTIONREPORT]|[ExecType]\u001b[1m150\u001b[22m\u001b[1m=\u001b[22m\u001b[1mF\u001b[22m[TRADE_PARTIAL_FILL_OR_FILL]'
//        '-s 55,35'                         | '[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m|[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1mD\u001b[22m[NEWORDERSINGLE]|[ClOrdID]\u001b[1m11\u001b[22m\u001b[1m=\u001b[22m\u001b[1mABC\u001b[22m\n[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m|[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1m8\u001b[22m[EXECUTIONREPORT]|[ExecType]\u001b[1m150\u001b[22m\u001b[1m=\u001b[22m\u001b[1mF\u001b[22m[TRADE_PARTIAL_FILL_OR_FILL]'
//        '-s 55,11,35'                      | '[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m|[ClOrdID]\u001b[1m11\u001b[22m\u001b[1m=\u001b[22m\u001b[1mABC\u001b[22m|[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1mD\u001b[22m[NEWORDERSINGLE]\n[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m|[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1m8\u001b[22m[EXECUTIONREPORT]|[ExecType]\u001b[1m150\u001b[22m\u001b[1m=\u001b[22m\u001b[1mF\u001b[22m[TRADE_PARTIAL_FILL_OR_FILL]'
//        '--only-include-tags 35'           | '[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1mD\u001b[22m[NEWORDERSINGLE]\n[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1m8\u001b[22m[EXECUTIONREPORT]'
//        '--only-include-tags 35,55'        | '[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1mD\u001b[22m[NEWORDERSINGLE]|[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m\n[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1m8\u001b[22m[EXECUTIONREPORT]|[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m'
//        '-t 35,55'                         | '[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1mD\u001b[22m[NEWORDERSINGLE]|[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m\n[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1m8\u001b[22m[EXECUTIONREPORT]|[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m'
//        '-t 35,11'                         | '[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1mD\u001b[22m[NEWORDERSINGLE]|[ClOrdID]\u001b[1m11\u001b[22m\u001b[1m=\u001b[22m\u001b[1mABC\u001b[22m\n[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1m8\u001b[22m[EXECUTIONREPORT]'
//        '-t 666'                           | ''
//        '--exclude-tags 11'                | '[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1mD\u001b[22m[NEWORDERSINGLE]|[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m\n[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1m8\u001b[22m[EXECUTIONREPORT]|[ExecType]\u001b[1m150\u001b[22m\u001b[1m=\u001b[22m\u001b[1mF\u001b[22m[TRADE_PARTIAL_FILL_OR_FILL]|[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m'
//        '-e 11'                            | '[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1mD\u001b[22m[NEWORDERSINGLE]|[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m\n[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1m8\u001b[22m[EXECUTIONREPORT]|[ExecType]\u001b[1m150\u001b[22m\u001b[1m=\u001b[22m\u001b[1mF\u001b[22m[TRADE_PARTIAL_FILL_OR_FILL]|[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m'
//        '-t 11 -e 11'                      | ''
//        '--highlights 35'                  | '\u001b[31m[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1mD\u001b[22m[NEWORDERSINGLE]\u001b[0m|[ClOrdID]\u001b[1m11\u001b[22m\u001b[1m=\u001b[22m\u001b[1mABC\u001b[22m|[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m\n\u001b[31m[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1m8\u001b[22m[EXECUTIONREPORT]\u001b[0m|[ExecType]\u001b[1m150\u001b[22m\u001b[1m=\u001b[22m\u001b[1mF\u001b[22m[TRADE_PARTIAL_FILL_OR_FILL]|[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m'
//        '--highlights 35 -q'               | '\u001b[31m[MsgType]35=D[NEWORDERSINGLE]\u001b[0m|[ClOrdID]11=ABC|[Symbol]55=AUD/USD\n\u001b[31m[MsgType]35=8[EXECUTIONREPORT]\u001b[0m|[ExecType]150=F[TRADE_PARTIAL_FILL_OR_FILL]|[Symbol]55=AUD/USD'
//        '-h 35'                            | '\u001b[31m[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1mD\u001b[22m[NEWORDERSINGLE]\u001b[0m|[ClOrdID]\u001b[1m11\u001b[22m\u001b[1m=\u001b[22m\u001b[1mABC\u001b[22m|[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m\n\u001b[31m[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1m8\u001b[22m[EXECUTIONREPORT]\u001b[0m|[ExecType]\u001b[1m150\u001b[22m\u001b[1m=\u001b[22m\u001b[1mF\u001b[22m[TRADE_PARTIAL_FILL_OR_FILL]|[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m'
//        '-h 35,55'                         | '\u001b[31m[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1mD\u001b[22m[NEWORDERSINGLE]\u001b[0m|[ClOrdID]\u001b[1m11\u001b[22m\u001b[1m=\u001b[22m\u001b[1mABC\u001b[22m|\u001b[32m[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m\u001b[0m\n\u001b[31m[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1m8\u001b[22m[EXECUTIONREPORT]\u001b[0m|[ExecType]\u001b[1m150\u001b[22m\u001b[1m=\u001b[22m\u001b[1mF\u001b[22m[TRADE_PARTIAL_FILL_OR_FILL]|\u001b[32m[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m\u001b[0m'
//        '-h 35:Bg8,55:Bg9:Field'           | '\u001b[48;5;8m[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1mD\u001b[22m[NEWORDERSINGLE]\u001b[0m|[ClOrdID]\u001b[1m11\u001b[22m\u001b[1m=\u001b[22m\u001b[1mABC\u001b[22m|\u001b[48;5;9m[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m\u001b[0m\n\u001b[48;5;8m[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1m8\u001b[22m[EXECUTIONREPORT]\u001b[0m|[ExecType]\u001b[1m150\u001b[22m\u001b[1m=\u001b[22m\u001b[1mF\u001b[22m[TRADE_PARTIAL_FILL_OR_FILL]|\u001b[48;5;9m[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m\u001b[0m'
//        '-h 35=D:Msg,55'                  | '\u001b[31m[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1mD\u001b[22m[NEWORDERSINGLE]\u001b[0m\u001b[31m|\u001b[0m\u001b[31m[ClOrdID]\u001b[1m11\u001b[22m\u001b[1m=\u001b[22m\u001b[1mABC\u001b[22m\u001b[0m\u001b[31m|\u001b[0m\u001b[32m\u001b[31m\u001b[32m[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m\u001b[0m\n[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1m8\u001b[22m[EXECUTIONREPORT]|[ExecType]\u001b[1m150\u001b[22m\u001b[1m=\u001b[22m\u001b[1mF\u001b[22m[TRADE_PARTIAL_FILL_OR_FILL]|\u001b[32m[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m\u001b[0m'
//        '-h 35=D:Bold:Msg'                | '\u001b[1m[MsgType]35=D[NEWORDERSINGLE]\u001b[22m\u001b[1m|\u001b[22m\u001b[1m[ClOrdID]11=ABC\u001b[22m\u001b[1m|\u001b[22m\u001b[1m[Symbol]55=AUD/USD\u001b[22m\n[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1m8\u001b[22m[EXECUTIONREPORT]|[ExecType]\u001b[1m150\u001b[22m\u001b[1m=\u001b[22m\u001b[1mF\u001b[22m[TRADE_PARTIAL_FILL_OR_FILL]|[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m'
//        '-h 55:Bold:Msg,35'               | '\u001b[31m\u001b[1m\u001b[31m[MsgType]35=D[NEWORDERSINGLE]\u001b[0m\u001b[0m\u001b[22m\u001b[1m|\u001b[22m\u001b[1m[ClOrdID]11=ABC\u001b[22m\u001b[1m|\u001b[22m\u001b[1m[Symbol]55=AUD/USD\u001b[22m\n\u001b[31m\u001b[1m\u001b[31m[MsgType]35=8[EXECUTIONREPORT]\u001b[0m\u001b[0m\u001b[22m\u001b[1m|\u001b[22m\u001b[1m[ExecType]150=F[TRADE_PARTIAL_FILL_OR_FILL]\u001b[22m\u001b[1m|\u001b[22m\u001b[1m[Symbol]55=AUD/USD\u001b[22m'
//        '-h 55:Msg,35'                    | '\u001b[32m\u001b[31m\u001b[32m[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1mD\u001b[22m[NEWORDERSINGLE]\u001b[0m\u001b[31m|\u001b[0m\u001b[31m[ClOrdID]\u001b[1m11\u001b[22m\u001b[1m=\u001b[22m\u001b[1mABC\u001b[22m\u001b[0m\u001b[31m|\u001b[0m\u001b[31m[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m\u001b[0m\n\u001b[32m\u001b[31m\u001b[32m[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1m8\u001b[22m[EXECUTIONREPORT]\u001b[0m\u001b[31m|\u001b[0m\u001b[31m[ExecType]\u001b[1m150\u001b[22m\u001b[1m=\u001b[22m\u001b[1mF\u001b[22m[TRADE_PARTIAL_FILL_OR_FILL]\u001b[0m\u001b[31m|\u001b[0m\u001b[31m[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m\u001b[0m'
//        '-h 35,55:Msg'                    | '\u001b[31m\u001b[32m\u001b[31m[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1mD\u001b[22m[NEWORDERSINGLE]\u001b[0m\u001b[32m|\u001b[0m\u001b[32m[ClOrdID]\u001b[1m11\u001b[22m\u001b[1m=\u001b[22m\u001b[1mABC\u001b[22m\u001b[0m\u001b[32m|\u001b[0m\u001b[32m[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m\u001b[0m\n\u001b[31m\u001b[32m\u001b[31m[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1m8\u001b[22m[EXECUTIONREPORT]\u001b[0m\u001b[32m|\u001b[0m\u001b[32m[ExecType]\u001b[1m150\u001b[22m\u001b[1m=\u001b[22m\u001b[1mF\u001b[22m[TRADE_PARTIAL_FILL_OR_FILL]\u001b[0m\u001b[32m|\u001b[0m\u001b[32m[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m\u001b[0m'
//        '-h 55:Bold:Msg,35'               | '\u001b[31m\u001b[1m\u001b[31m[MsgType]35=D[NEWORDERSINGLE]\u001b[0m\u001b[0m\u001b[22m\u001b[1m|\u001b[22m\u001b[1m[ClOrdID]11=ABC\u001b[22m\u001b[1m|\u001b[22m\u001b[1m[Symbol]55=AUD/USD\u001b[22m\n\u001b[31m\u001b[1m\u001b[31m[MsgType]35=8[EXECUTIONREPORT]\u001b[0m\u001b[0m\u001b[22m\u001b[1m|\u001b[22m\u001b[1m[ExecType]150=F[TRADE_PARTIAL_FILL_OR_FILL]\u001b[22m\u001b[1m|\u001b[22m\u001b[1m[Symbol]55=AUD/USD\u001b[22m'
//        '-h 35,55:Bold:Msg'               | '\u001b[31m\u001b[1m\u001b[31m[MsgType]35=D[NEWORDERSINGLE]\u001b[0m\u001b[0m\u001b[22m\u001b[1m|\u001b[22m\u001b[1m[ClOrdID]11=ABC\u001b[22m\u001b[1m|\u001b[22m\u001b[1m[Symbol]55=AUD/USD\u001b[22m\n\u001b[31m\u001b[1m\u001b[31m[MsgType]35=8[EXECUTIONREPORT]\u001b[0m\u001b[0m\u001b[22m\u001b[1m|\u001b[22m\u001b[1m[ExecType]150=F[TRADE_PARTIAL_FILL_OR_FILL]\u001b[22m\u001b[1m|\u001b[22m\u001b[1m[Symbol]55=AUD/USD\u001b[22m'
//        '-h 35:Msg,55:Msg'               | '\u001b[31m\u001b[32m[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1mD\u001b[22m[NEWORDERSINGLE]\u001b[0m\u001b[31m\u001b[32m|\u001b[0m\u001b[31m\u001b[32m[ClOrdID]\u001b[1m11\u001b[22m\u001b[1m=\u001b[22m\u001b[1mABC\u001b[22m\u001b[0m\u001b[31m\u001b[32m|\u001b[0m\u001b[31m\u001b[32m[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m\u001b[0m\n\u001b[31m\u001b[32m[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1m8\u001b[22m[EXECUTIONREPORT]\u001b[0m\u001b[31m\u001b[32m|\u001b[0m\u001b[31m\u001b[32m[ExecType]\u001b[1m150\u001b[22m\u001b[1m=\u001b[22m\u001b[1mF\u001b[22m[TRADE_PARTIAL_FILL_OR_FILL]\u001b[0m\u001b[31m\u001b[32m|\u001b[0m\u001b[31m\u001b[32m[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m\u001b[0m'
//        '-h 55:Bold:Msg,35'               | '\u001b[31m\u001b[1m\u001b[31m[MsgType]35=D[NEWORDERSINGLE]\u001b[0m\u001b[0m\u001b[22m\u001b[1m|\u001b[22m\u001b[1m[ClOrdID]11=ABC\u001b[22m\u001b[1m|\u001b[22m\u001b[1m[Symbol]55=AUD/USD\u001b[22m\n\u001b[31m\u001b[1m\u001b[31m[MsgType]35=8[EXECUTIONREPORT]\u001b[0m\u001b[0m\u001b[22m\u001b[1m|\u001b[22m\u001b[1m[ExecType]150=F[TRADE_PARTIAL_FILL_OR_FILL]\u001b[22m\u001b[1m|\u001b[22m\u001b[1m[Symbol]55=AUD/USD\u001b[22m'
//        '-h 55:Msg,35'                    | '\u001b[32m\u001b[31m\u001b[32m[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1mD\u001b[22m[NEWORDERSINGLE]\u001b[0m\u001b[31m|\u001b[0m\u001b[31m[ClOrdID]\u001b[1m11\u001b[22m\u001b[1m=\u001b[22m\u001b[1mABC\u001b[22m\u001b[0m\u001b[31m|\u001b[0m\u001b[31m[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m\u001b[0m\n\u001b[32m\u001b[31m\u001b[32m[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1m8\u001b[22m[EXECUTIONREPORT]\u001b[0m\u001b[31m|\u001b[0m\u001b[31m[ExecType]\u001b[1m150\u001b[22m\u001b[1m=\u001b[22m\u001b[1mF\u001b[22m[TRADE_PARTIAL_FILL_OR_FILL]\u001b[0m\u001b[31m|\u001b[0m\u001b[31m[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m\u001b[0m'
//        '-h 35:Msg,55=AUD:BgBrightGreen'  | '\u001b[31m[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1mD\u001b[22m[NEWORDERSINGLE]\u001b[0m\u001b[31m|\u001b[0m\u001b[31m[ClOrdID]\u001b[1m11\u001b[22m\u001b[1m=\u001b[22m\u001b[1mABC\u001b[22m\u001b[0m\u001b[31m|\u001b[0m\u001b[42;1m\u001b[31m\u001b[42;1m[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m\u001b[0m\n\u001b[31m[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1m8\u001b[22m[EXECUTIONREPORT]\u001b[0m\u001b[31m|\u001b[0m\u001b[31m[ExecType]\u001b[1m150\u001b[22m\u001b[1m=\u001b[22m\u001b[1mF\u001b[22m[TRADE_PARTIAL_FILL_OR_FILL]\u001b[0m\u001b[31m|\u001b[0m\u001b[42;1m\u001b[31m\u001b[42;1m[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m\u001b[0m'
//        '-h 35:FgRed:Msg'                 | '\u001b[31m[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1mD\u001b[22m[NEWORDERSINGLE]\u001b[0m\u001b[31m|\u001b[0m\u001b[31m[ClOrdID]\u001b[1m11\u001b[22m\u001b[1m=\u001b[22m\u001b[1mABC\u001b[22m\u001b[0m\u001b[31m|\u001b[0m\u001b[31m[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m\u001b[0m\n\u001b[31m[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1m8\u001b[22m[EXECUTIONREPORT]\u001b[0m\u001b[31m|\u001b[0m\u001b[31m[ExecType]\u001b[1m150\u001b[22m\u001b[1m=\u001b[22m\u001b[1mF\u001b[22m[TRADE_PARTIAL_FILL_OR_FILL]\u001b[0m\u001b[31m|\u001b[0m\u001b[31m[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m\u001b[0m'
//        '-h 35:Fg9:Msg'                   | '\u001b[38;5;9m[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1mD\u001b[22m[NEWORDERSINGLE]\u001b[0m\u001b[38;5;9m|\u001b[0m\u001b[38;5;9m[ClOrdID]\u001b[1m11\u001b[22m\u001b[1m=\u001b[22m\u001b[1mABC\u001b[22m\u001b[0m\u001b[38;5;9m|\u001b[0m\u001b[38;5;9m[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m\u001b[0m\n\u001b[38;5;9m[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1m8\u001b[22m[EXECUTIONREPORT]\u001b[0m\u001b[38;5;9m|\u001b[0m\u001b[38;5;9m[ExecType]\u001b[1m150\u001b[22m\u001b[1m=\u001b[22m\u001b[1mF\u001b[22m[TRADE_PARTIAL_FILL_OR_FILL]\u001b[0m\u001b[38;5;9m|\u001b[0m\u001b[38;5;9m[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m\u001b[0m'
//        '-h 35:Fg9:Msg,55=AUD:BgBrightGreen'| '\u001b[38;5;9m[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1mD\u001b[22m[NEWORDERSINGLE]\u001b[0m\u001b[38;5;9m|\u001b[0m\u001b[38;5;9m[ClOrdID]\u001b[1m11\u001b[22m\u001b[1m=\u001b[22m\u001b[1mABC\u001b[22m\u001b[0m\u001b[38;5;9m|\u001b[0m\u001b[42;1m\u001b[38;5;9m\u001b[42;1m[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m\u001b[0m\n\u001b[38;5;9m[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1m8\u001b[22m[EXECUTIONREPORT]\u001b[0m\u001b[38;5;9m|\u001b[0m\u001b[38;5;9m[ExecType]\u001b[1m150\u001b[22m\u001b[1m=\u001b[22m\u001b[1mF\u001b[22m[TRADE_PARTIAL_FILL_OR_FILL]\u001b[0m\u001b[38;5;9m|\u001b[0m\u001b[42;1m\u001b[38;5;9m\u001b[42;1m[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m\u001b[0m'
//        '-h 35=D:Fg9:Msg,55=AUD:BgBrightGreen'| '\u001b[38;5;9m[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1mD\u001b[22m[NEWORDERSINGLE]\u001b[0m\u001b[38;5;9m|\u001b[0m\u001b[38;5;9m[ClOrdID]\u001b[1m11\u001b[22m\u001b[1m=\u001b[22m\u001b[1mABC\u001b[22m\u001b[0m\u001b[38;5;9m|\u001b[0m\u001b[42;1m\u001b[38;5;9m\u001b[42;1m[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m\u001b[0m\n[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1m8\u001b[22m[EXECUTIONREPORT]|[ExecType]\u001b[1m150\u001b[22m\u001b[1m=\u001b[22m\u001b[1mF\u001b[22m[TRADE_PARTIAL_FILL_OR_FILL]|\u001b[42;1m[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m\u001b[0m'
//        '-F "${msgColor}[${msgTypeName}]${colorReset} ${msgFix}"'| '\u001b[36m[NewOrderSingle]\u001b[0m [MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1mD\u001b[22m[NEWORDERSINGLE]|[ClOrdID]\u001b[1m11\u001b[22m\u001b[1m=\u001b[22m\u001b[1mABC\u001b[22m|[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m\n\u001b[32m[Exec.Trade]\u001b[0m [MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1m8\u001b[22m[EXECUTIONREPORT]|[ExecType]\u001b[1m150\u001b[22m\u001b[1m=\u001b[22m\u001b[1mF\u001b[22m[TRADE_PARTIAL_FILL_OR_FILL]|[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m'
//        '-h 35 -F "${msgColor}[${msgTypeName}]${colorReset} ${msgFix}"'| '\u001b[36m[NewOrderSingle]\u001b[0m \u001b[31m[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1mD\u001b[22m[NEWORDERSINGLE]\u001b[0m|[ClOrdID]\u001b[1m11\u001b[22m\u001b[1m=\u001b[22m\u001b[1mABC\u001b[22m|[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m\n\u001b[32m[Exec.Trade]\u001b[0m \u001b[31m[MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1m8\u001b[22m[EXECUTIONREPORT]\u001b[0m|[ExecType]\u001b[1m150\u001b[22m\u001b[1m=\u001b[22m\u001b[1mF\u001b[22m[TRADE_PARTIAL_FILL_OR_FILL]|[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m'
//        '-h 35 -n -F "${msgColor}[${msgTypeName}]${colorReset} ${msgFix}"'| '[NewOrderSingle] [MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1mD\u001b[22m[NEWORDERSINGLE]|[ClOrdID]\u001b[1m11\u001b[22m\u001b[1m=\u001b[22m\u001b[1mABC\u001b[22m|[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m\n[Exec.Trade] [MsgType]\u001b[1m35\u001b[22m\u001b[1m=\u001b[22m\u001b[1m8\u001b[22m[EXECUTIONREPORT]|[ExecType]\u001b[1m150\u001b[22m\u001b[1m=\u001b[22m\u001b[1mF\u001b[22m[TRADE_PARTIAL_FILL_OR_FILL]|[Symbol]\u001b[1m55\u001b[22m\u001b[1m=\u001b[22m\u001b[1mAUD/USD\u001b[22m'
    }

    @Unroll
    def 'single line test'(){
        when:
        final String fix = "35=D${a}11=ABC${a}55=AUD/USD\n35=8${a}150=F${a}55=AUD/USD"
        def lines = parseToLines('', fix)

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
        '--input-line-format "\\d\\d\\d\\d-\\d\\d-\\d\\d \\[\\d\\] (\\d+=.*)" --line-regexgroup-for-fix 1 '| '[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1mD\u001B[22m[NEWORDERSINGLE]|[ClOrdID]\u001B[1m11\u001B[22m\u001B[1m=\u001B[22m\u001B[1mABC\u001B[22m|[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[22m\n' + '[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1m8\u001B[22m[EXECUTIONREPORT]|[ExecType]\u001B[1m150\u001B[22m\u001B[1m=\u001B[22m\u001B[1mF\u001B[22m[TRADE_PARTIAL_FILL_OR_FILL]|[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[22m'
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

        def expectedOutput = 'Thread:6 [MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1mD\u001B[22m[NEWORDERSINGLE]|[ClOrdID]\u001B[1m11\u001B[22m\u001B[1m=\u001B[22m\u001B[1mABC\u001B[22m|[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[22m\n' +
                'Thread:7 [MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1m8\u001B[22m[EXECUTIONREPORT]|[ExecType]\u001B[1m150\u001B[22m\u001B[1m=\u001B[22m\u001B[1mF\u001B[22m[TRADE_PARTIAL_FILL_OR_FILL]|[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[22m'

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
        '--input-delimiter :'              | '[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1mD\u001B[22m[NEWORDERSINGLE]|[ClOrdID]\u001B[1m11\u001B[22m\u001B[1m=\u001B[22m\u001B[1mABC\u001B[22m|[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[22m\n' + '[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1m8\u001B[22m[EXECUTIONREPORT]|[ExecType]\u001B[1m150\u001B[22m\u001B[1m=\u001B[22m\u001B[1mF\u001B[22m[TRADE_PARTIAL_FILL_OR_FILL]|[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[22m'
        '-d :'                             | '[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1mD\u001B[22m[NEWORDERSINGLE]|[ClOrdID]\u001B[1m11\u001B[22m\u001B[1m=\u001B[22m\u001B[1mABC\u001B[22m|[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[22m\n' + '[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1m8\u001B[22m[EXECUTIONREPORT]|[ExecType]\u001B[1m150\u001B[22m\u001B[1m=\u001B[22m\u001B[1mF\u001B[22m[TRADE_PARTIAL_FILL_OR_FILL]|[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[22m'
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
