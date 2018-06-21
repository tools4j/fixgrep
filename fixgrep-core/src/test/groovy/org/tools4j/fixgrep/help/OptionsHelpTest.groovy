package org.tools4j.fixgrep.help

import spock.lang.Specification

/**
 * User: ben
 * Date: 29/05/2018
 * Time: 9:27 AM
 */
class OptionsHelpTest extends Specification {
    def "ConsoleTextWriter_writeFormatExamplesTable"() {
        given:
        String fix = "35=D|11=ABC|55=AUD/USD"

        when:
        String output = new ConsoleTextWriter().writeFormatExamplesTable(fix)
                .add("35,55")
                .add("35=D,55=AUD")
                .add("35=D,55=XYZ")
                .add("35,55,11")
                .add("35:Bold,55")
                .add("35=D:Fg8,55=AUD&&11=AB:BgBrightYellow")
                .add("35=D:Fg8:Line,55=AUD:BgBrightGreen")
                .endTable()
                .toFormattedText()

        println output

        then:
        assert output == """|\u001B[1m expression                               \u001B[22m|\u001B[1m message                                                          \u001B[22m|
| -h 35,55                                 | \u001B[32m[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1mD\u001B[22m[NEWORDERSINGLE]\u001B[0m|[ClOrdID]\u001B[1m11\u001B[22m\u001B[1m=\u001B[22m\u001B[1mABC\u001B[0m|\u001B[31m[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[0m |
| -h 35=D,55=AUD                           | \u001B[32m[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1mD\u001B[22m[NEWORDERSINGLE]\u001B[0m|[ClOrdID]\u001B[1m11\u001B[22m\u001B[1m=\u001B[22m\u001B[1mABC\u001B[0m|\u001B[31m[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[0m |
| -h 35=D,55=XYZ                           | \u001B[32m[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1mD\u001B[22m[NEWORDERSINGLE]\u001B[0m|[ClOrdID]\u001B[1m11\u001B[22m\u001B[1m=\u001B[22m\u001B[1mABC\u001B[0m|[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[0m |
| -h 35,55,11                              | \u001B[33m[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1mD\u001B[22m[NEWORDERSINGLE]\u001B[0m|\u001B[31m[ClOrdID]\u001B[1m11\u001B[22m\u001B[1m=\u001B[22m\u001B[1mABC\u001B[0m|\u001B[32m[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[0m |
| -h 35:Bold,55                            | \u001B[1m[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1mD\u001B[22m[NEWORDERSINGLE]\u001B[22m|[ClOrdID]\u001B[1m11\u001B[22m\u001B[1m=\u001B[22m\u001B[1mABC\u001B[0m|\u001B[31m[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[0m |
| -h 35=D:Fg8,55=AUD&&11=AB:BgBrightYellow | \u001B[38;5;8m[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1mD\u001B[22m[NEWORDERSINGLE]\u001B[0m|\u001B[43;1m[ClOrdID]\u001B[1m11\u001B[22m\u001B[1m=\u001B[22m\u001B[1mABC\u001B[0m|\u001B[43;1m[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[0m |
| -h 35=D:Fg8:Line,55=AUD:BgBrightGreen    | \u001B[38;5;8m[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1mD\u001B[22m[NEWORDERSINGLE]\u001B[0m\u001B[38;5;8m|\u001B[0m\u001B[38;5;8m[ClOrdID]\u001B[1m11\u001B[22m\u001B[1m=\u001B[22m\u001B[1mABC\u001B[0m\u001B[38;5;8m|\u001B[0m\u001B[38;5;8m\u001B[42;1m[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[0m |
"""
    }

    def "HtmlTextWriter_writeFormatExamplesTable"() {
        given:
        String fix = "35=D|11=ABC|55=AUD/USD"

        when:
        String output = new HtmlWriter().writeFormatExamplesTable(fix)
//                .add("35,55")
//                .add("35=D,55=AUD")
//                .add("35=D,55=XYZ")
//                .add("35,55,11")
//                .add("35:Bold,55")
//                .add("35=D:Fg8,55=AUD&&11=AB:BgBrightYellow")
                .add("35=D:Fg8:Line,55=AUD:BgBrightGreen")
                .endTable()
                .toFormattedText()

        println output

        then:
        assert output == """|\u001B[1m expression                               \u001B[22m|\u001B[1m message                                                          \u001B[22m|
| -h 35,55                                 | \u001B[32m[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1mD\u001B[22m[NEWORDERSINGLE]\u001B[0m|[ClOrdID]\u001B[1m11\u001B[22m\u001B[1m=\u001B[22m\u001B[1mABC\u001B[0m|\u001B[31m[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[0m |
| -h 35=D,55=AUD                           | \u001B[32m[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1mD\u001B[22m[NEWORDERSINGLE]\u001B[0m|[ClOrdID]\u001B[1m11\u001B[22m\u001B[1m=\u001B[22m\u001B[1mABC\u001B[0m|\u001B[31m[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[0m |
| -h 35=D,55=XYZ                           | \u001B[32m[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1mD\u001B[22m[NEWORDERSINGLE]\u001B[0m|[ClOrdID]\u001B[1m11\u001B[22m\u001B[1m=\u001B[22m\u001B[1mABC\u001B[0m|[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[0m |
| -h 35,55,11                              | \u001B[33m[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1mD\u001B[22m[NEWORDERSINGLE]\u001B[0m|\u001B[31m[ClOrdID]\u001B[1m11\u001B[22m\u001B[1m=\u001B[22m\u001B[1mABC\u001B[0m|\u001B[32m[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[0m |
| -h 35:Bold,55                            | \u001B[1m[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1mD\u001B[22m[NEWORDERSINGLE]\u001B[22m|[ClOrdID]\u001B[1m11\u001B[22m\u001B[1m=\u001B[22m\u001B[1mABC\u001B[0m|\u001B[31m[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[0m |
| -h 35=D:Fg8,55=AUD&&11=AB:BgBrightYellow | \u001B[38;5;8m[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1mD\u001B[22m[NEWORDERSINGLE]\u001B[0m|\u001B[43;1m[ClOrdID]\u001B[1m11\u001B[22m\u001B[1m=\u001B[22m\u001B[1mABC\u001B[0m|\u001B[43;1m[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[0m |
| -h 35=D:Fg8:Line,55=AUD:BgBrightGreen    | \u001B[38;5;8m[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1mD\u001B[22m[NEWORDERSINGLE]\u001B[0m\u001B[38;5;8m|\u001B[0m\u001B[38;5;8m[ClOrdID]\u001B[1m11\u001B[22m\u001B[1m=\u001B[22m\u001B[1mABC\u001B[0m\u001B[38;5;8m|\u001B[0m\u001B[38;5;8m\u001B[42;1m[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[0m |
"""
    }
}
