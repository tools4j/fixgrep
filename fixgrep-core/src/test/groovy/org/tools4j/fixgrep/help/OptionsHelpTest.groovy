package org.tools4j.fixgrep.help

import spock.lang.Specification
import spock.lang.Unroll

/**
 * User: ben
 * Date: 29/05/2018
 * Time: 9:27 AM
 */
class OptionsHelpTest extends Specification {
    @Unroll
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
| -h 35=D:Fg8:Line,55=AUD:BgBrightGreen    | \u001B[38;5;8m[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1mD\u001B[22m[NEWORDERSINGLE]\u001B[0m\u001B[38;5;8m|\u001B[0m\u001B[38;5;8m[ClOrdID]\u001B[1m11\u001B[22m\u001B[1m=\u001B[22m\u001B[1mABC\u001B[0m\u001B[38;5;8m|\u001B[0m\u001B[42;1m\u001B[38;5;8m\u001B[42;1m[Symbol]\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[0m |
"""
    }

    def "HtmlTextWriter_writeFormatExamplesTable"() {
        given:
        String fix = "35=D|11=ABC|55=AUD/USD"

        when:
        String output = new HtmlWriter().writeFormatExamplesTable(fix)
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
        assert output == """<table class='doc-table example-table'>
<tr><th>expression</th><th>message</th></tr>
<tr><td>-h 35,55</td><td class='console'><div class='line'><span class='fields'><span class='field FgGreen'><span class='tag annotation'>[MsgType]</span><span class='tag tagNumber bold'>35</span><span class='equals'>=</span><span class='value valueRaw bold'>D</span><span class='value annotation'>[NEWORDERSINGLE]</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[ClOrdID]</span><span class='tag tagNumber bold'>11</span><span class='equals bold'>=</span><span class='value valueRaw bold'>ABC</span></span><span class='delim'>|</span><span class='field FgRed'><span class='tag annotation'>[Symbol]</span><span class='tag tagNumber bold'>55</span><span class='equals'>=</span><span class='value valueRaw bold'>AUD/USD</span></span></span></div></td></tr>
<tr><td>-h 35=D,55=AUD</td><td class='console'><div class='line'><span class='fields'><span class='field FgGreen'><span class='tag annotation'>[MsgType]</span><span class='tag tagNumber bold'>35</span><span class='equals'>=</span><span class='value valueRaw bold'>D</span><span class='value annotation'>[NEWORDERSINGLE]</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[ClOrdID]</span><span class='tag tagNumber bold'>11</span><span class='equals bold'>=</span><span class='value valueRaw bold'>ABC</span></span><span class='delim'>|</span><span class='field FgRed'><span class='tag annotation'>[Symbol]</span><span class='tag tagNumber bold'>55</span><span class='equals'>=</span><span class='value valueRaw bold'>AUD/USD</span></span></span></div></td></tr>
<tr><td>-h 35=D,55=XYZ</td><td class='console'><div class='line'><span class='fields'><span class='field FgGreen'><span class='tag annotation'>[MsgType]</span><span class='tag tagNumber bold'>35</span><span class='equals'>=</span><span class='value valueRaw bold'>D</span><span class='value annotation'>[NEWORDERSINGLE]</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[ClOrdID]</span><span class='tag tagNumber bold'>11</span><span class='equals bold'>=</span><span class='value valueRaw bold'>ABC</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[Symbol]</span><span class='tag tagNumber bold'>55</span><span class='equals bold'>=</span><span class='value valueRaw bold'>AUD/USD</span></span></span></div></td></tr>
<tr><td>-h 35,55,11</td><td class='console'><div class='line'><span class='fields'><span class='field FgYellow'><span class='tag annotation'>[MsgType]</span><span class='tag tagNumber bold'>35</span><span class='equals'>=</span><span class='value valueRaw bold'>D</span><span class='value annotation'>[NEWORDERSINGLE]</span></span><span class='delim'>|</span><span class='field FgRed'><span class='tag annotation'>[ClOrdID]</span><span class='tag tagNumber bold'>11</span><span class='equals'>=</span><span class='value valueRaw bold'>ABC</span></span><span class='delim'>|</span><span class='field FgGreen'><span class='tag annotation'>[Symbol]</span><span class='tag tagNumber bold'>55</span><span class='equals'>=</span><span class='value valueRaw bold'>AUD/USD</span></span></span></div></td></tr>
<tr><td>-h 35:Bold,55</td><td class='console'><div class='line'><span class='fields'><span class='field bold'><span class='tag annotation'>[MsgType]</span><span class='tag tagNumber bold'>35</span><span class='equals'>=</span><span class='value valueRaw bold'>D</span><span class='value annotation'>[NEWORDERSINGLE]</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag annotation'>[ClOrdID]</span><span class='tag tagNumber bold'>11</span><span class='equals bold'>=</span><span class='value valueRaw bold'>ABC</span></span><span class='delim'>|</span><span class='field FgRed'><span class='tag annotation'>[Symbol]</span><span class='tag tagNumber bold'>55</span><span class='equals'>=</span><span class='value valueRaw bold'>AUD/USD</span></span></span></div></td></tr>
<tr><td>-h 35=D:Fg8,55=AUD&&11=AB:BgBrightYellow</td><td class='console'><div class='line'><span class='fields'><span class='field Fg8'><span class='tag annotation'>[MsgType]</span><span class='tag tagNumber bold'>35</span><span class='equals'>=</span><span class='value valueRaw bold'>D</span><span class='value annotation'>[NEWORDERSINGLE]</span></span><span class='delim'>|</span><span class='field BgBrightYellow'><span class='tag annotation'>[ClOrdID]</span><span class='tag tagNumber bold'>11</span><span class='equals'>=</span><span class='value valueRaw bold'>ABC</span></span><span class='delim'>|</span><span class='field BgBrightYellow'><span class='tag annotation'>[Symbol]</span><span class='tag tagNumber bold'>55</span><span class='equals'>=</span><span class='value valueRaw bold'>AUD/USD</span></span></span></div></td></tr>
<tr><td>-h 35=D:Fg8:Line,55=AUD:BgBrightGreen</td><td class='console'><div class='line'><span class='fields'><span class='field Fg8'><span class='tag annotation'>[MsgType]</span><span class='tag tagNumber bold'>35</span><span class='equals'>=</span><span class='value valueRaw bold'>D</span><span class='value annotation'>[NEWORDERSINGLE]</span></span><span class='delim Fg8'>|</span><span class='field Fg8'><span class='tag annotation'>[ClOrdID]</span><span class='tag tagNumber bold'>11</span><span class='equals'>=</span><span class='value valueRaw bold'>ABC</span></span><span class='delim Fg8'>|</span><span class='field BgBrightGreen Fg8'><span class='tag annotation'>[Symbol]</span><span class='tag tagNumber bold'>55</span><span class='equals'>=</span><span class='value valueRaw bold'>AUD/USD</span></span></span></div></td></tr>
</table>
"""
    }
}
