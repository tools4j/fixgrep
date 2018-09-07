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
class VerticalTest extends Specification {
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


    @Unroll
    def 'test vertical non-aligned format - prices'(){
        when:
        final String fix = "35=V${a}262=request123${a}263=0${a}264=20${a}267=2${a}269=0${a}269=1${a}146=1${a}55=AUD/USD\n35=X${a}262=ABCD${a}268=4${a}279=0${a}269=0${a}55=AUD/USD${a}270=1.12345${a}279=0${a}269=1${a}55=AUD/USD${a}270=1.12355${a}279=0${a}269=0${a}55=AUD/USD${a}270=1.12335${a}279=0${a}269=1${a}55=AUD/USD${a}270=1.12365";

        def lines = parseToLines('', fix)

        then:
        assert lines == """<div class='msg-header'>
================================================================================</br>
<span class='FgYellow'>MarketDataRequest</span><br/>
================================================================================
</div>
<div class='fields'>
<div class='field annotatedField'><span class='tag-annotation'>[MsgType]</span><span class='tag-raw bold'>35</span><span class='equals bold'>=</span><span class='value-raw bold'>V</span><span class='value-annotation'>[MARKETDATAREQUEST]</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[MDReqID]</span><span class='tag-raw bold'>262</span><span class='equals bold'>=</span><span class='value-raw bold'>request123</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[SubscriptionRequestType]</span><span class='tag-raw bold'>263</span><span class='equals bold'>=</span><span class='value-raw bold'>0</span><span class='value-annotation'>[SNAPSHOT]</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[MarketDepth]</span><span class='tag-raw bold'>264</span><span class='equals bold'>=</span><span class='value-raw bold'>20</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[NoMDEntryTypes]</span><span class='tag-raw bold'>267</span><span class='equals bold'>=</span><span class='value-raw bold'>2</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[MDEntryType]</span><span class='tag-raw bold'>269</span><span class='equals bold'>=</span><span class='value-raw bold'>0</span><span class='value-annotation'>[BID]</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[MDEntryType]</span><span class='tag-raw bold'>269</span><span class='equals bold'>=</span><span class='value-raw bold'>1</span><span class='value-annotation'>[OFFER]</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[NoRelatedSym]</span><span class='tag-raw bold'>146</span><span class='equals bold'>=</span><span class='value-raw bold'>1</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[Symbol]</span><span class='tag-raw bold'>55</span><span class='equals bold'>=</span><span class='value-raw bold'>AUD/USD</span></div>
</div>

<br/>
<div class='msg-header'>
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
<div class='field annotatedField'><span class='tag-annotation'>[Symbol]</span><span class='tag-raw bold'>55</span><span class='equals bold'>=</span><span class='value-raw bold'>AUD/USD</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[MDEntryPx]</span><span class='tag-raw bold'>270</span><span class='equals bold'>=</span><span class='value-raw bold'>1.12345</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[MDUpdateAction]</span><span class='tag-raw bold'>279</span><span class='equals bold'>=</span><span class='value-raw bold'>0</span><span class='value-annotation'>[NEW]</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[MDEntryType]</span><span class='tag-raw bold'>269</span><span class='equals bold'>=</span><span class='value-raw bold'>1</span><span class='value-annotation'>[OFFER]</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[Symbol]</span><span class='tag-raw bold'>55</span><span class='equals bold'>=</span><span class='value-raw bold'>AUD/USD</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[MDEntryPx]</span><span class='tag-raw bold'>270</span><span class='equals bold'>=</span><span class='value-raw bold'>1.12355</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[MDUpdateAction]</span><span class='tag-raw bold'>279</span><span class='equals bold'>=</span><span class='value-raw bold'>0</span><span class='value-annotation'>[NEW]</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[MDEntryType]</span><span class='tag-raw bold'>269</span><span class='equals bold'>=</span><span class='value-raw bold'>0</span><span class='value-annotation'>[BID]</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[Symbol]</span><span class='tag-raw bold'>55</span><span class='equals bold'>=</span><span class='value-raw bold'>AUD/USD</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[MDEntryPx]</span><span class='tag-raw bold'>270</span><span class='equals bold'>=</span><span class='value-raw bold'>1.12335</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[MDUpdateAction]</span><span class='tag-raw bold'>279</span><span class='equals bold'>=</span><span class='value-raw bold'>0</span><span class='value-annotation'>[NEW]</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[MDEntryType]</span><span class='tag-raw bold'>269</span><span class='equals bold'>=</span><span class='value-raw bold'>1</span><span class='value-annotation'>[OFFER]</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[Symbol]</span><span class='tag-raw bold'>55</span><span class='equals bold'>=</span><span class='value-raw bold'>AUD/USD</span></div>
<div class='field annotatedField'><span class='tag-annotation'>[MDEntryPx]</span><span class='tag-raw bold'>270</span><span class='equals bold'>=</span><span class='value-raw bold'>1.12365</span></div>
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
