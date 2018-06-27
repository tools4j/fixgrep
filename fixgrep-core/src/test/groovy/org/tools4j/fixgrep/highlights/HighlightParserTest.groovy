package org.tools4j.fixgrep.highlights

import org.tools4j.fix.Fields
import org.tools4j.fix.FieldsImpl
import org.tools4j.fixgrep.texteffect.*
import spock.lang.Specification
import spock.lang.Unroll

/**
 * User: ben
 * Date: 6/04/2018
 * Time: 6:34 AM
 */
class HighlightParserTest extends Specification {
    private final static String FgBlue = Ansi16ForegroundColor.Blue.consoleTextBefore
    private final static String BgRed = Ansi16BackgroundColor.Red.consoleTextBefore
    private final static String Fg2 = Ansi256Color.parse("Fg2").consoleTextBefore
    private final static String Bg4 = Ansi256Color.parse("Bg4").consoleTextBefore
    private final static String bold = MiscTextEffect.Bold.consoleTextBefore
    private final static String Reset = Ansi.Reset
    private final static String Normal = Ansi.Normal

    @Unroll
    def "test parse #expression"(final String expression, final String expectedConsoleOutput, final String expectedHtmlOutput) {
        given:
        final String fix = '35=blah|150=A|55=AUD/USD'
        final Fields fields = new FieldsImpl(fix, '|')

        when:
        final Highlight highlight = new HighlightParser().parse(expression)
        final Fields highlightedFields = highlight.apply(fields)
        final String actualConsoleOutput = highlightedFields.toConsoleText()
        final String actualHtmlOutput = highlightedFields.toHtml()
        println "Expected output: $expectedConsoleOutput"
        println "Actual output: $actualConsoleOutput"
        then:
        assert actualConsoleOutput == expectedConsoleOutput
        assert actualHtmlOutput == expectedHtmlOutput

        where:
        expression                                                   | expectedConsoleOutput                                                                                                                                            | expectedHtmlOutput
        '35:Blue:Field'                                              | "[34m35=blah[0m|150=A|55=AUD/USD"                                                                                                                     | "<span class='fields'><span class='field FgBlue'><span class='tag rawTag'>35</span><span class='equals'>=</span><span class='value rawValue'>blah</span></span><span class='delim'>|</span><span class='field'><span class='tag rawTag'>150</span><span class='equals'>=</span><span class='value rawValue'>A</span></span><span class='delim'>|</span><span class='field'><span class='tag rawTag'>55</span><span class='equals'>=</span><span class='value rawValue'>AUD/USD</span></span></span>"
        '35=foo:Blue:Field'                                          | "35=blah|150=A|55=AUD/USD"                                                                                                                                       | "<span class='fields'><span class='field'><span class='tag rawTag'>35</span><span class='equals'>=</span><span class='value rawValue'>blah</span></span><span class='delim'>|</span><span class='field'><span class='tag rawTag'>150</span><span class='equals'>=</span><span class='value rawValue'>A</span></span><span class='delim'>|</span><span class='field'><span class='tag rawTag'>55</span><span class='equals'>=</span><span class='value rawValue'>AUD/USD</span></span></span>"
        '35:bold:Field'                                              | "[1m35=blah[22m|150=A|55=AUD/USD"                                                                                                                       | "<span class='fields'><span class='field bold'><span class='tag rawTag'>35</span><span class='equals'>=</span><span class='value rawValue'>blah</span></span><span class='delim'>|</span><span class='field'><span class='tag rawTag'>150</span><span class='equals'>=</span><span class='value rawValue'>A</span></span><span class='delim'>|</span><span class='field'><span class='tag rawTag'>55</span><span class='equals'>=</span><span class='value rawValue'>AUD/USD</span></span></span>"
        '35=blah:Blue:Field'                                         | "[34m35=blah[0m|150=A|55=AUD/USD"                                                                                                                     | "<span class='fields'><span class='field FgBlue'><span class='tag rawTag'>35</span><span class='equals'>=</span><span class='value rawValue'>blah</span></span><span class='delim'>|</span><span class='field'><span class='tag rawTag'>150</span><span class='equals'>=</span><span class='value rawValue'>A</span></span><span class='delim'>|</span><span class='field'><span class='tag rawTag'>55</span><span class='equals'>=</span><span class='value rawValue'>AUD/USD</span></span></span>"
        '35=blah&&150=A:Blue:Red:Field'                              | "[34m[41m35=blah[0m|[34m[41m150=A[0m|55=AUD/USD"                                                                                   | "<span class='fields'><span class='field FgBlue BgRed'><span class='tag rawTag'>35</span><span class='equals'>=</span><span class='value rawValue'>blah</span></span><span class='delim'>|</span><span class='field FgBlue BgRed'><span class='tag rawTag'>150</span><span class='equals'>=</span><span class='value rawValue'>A</span></span><span class='delim'>|</span><span class='field'><span class='tag rawTag'>55</span><span class='equals'>=</span><span class='value rawValue'>AUD/USD</span></span></span>"
        '35=blah&&150=A:Blue:Red:Line'                               | "[34m[41m35=blah[0m[34m[41m|[0m[34m[41m150=A[0m[34m[41m|[0m[34m[41m55=AUD/USD[0m"     | "<span class='fields'><span class='field FgBlue BgRed'><span class='tag rawTag'>35</span><span class='equals'>=</span><span class='value rawValue'>blah</span></span><span class='delim FgBlue BgRed'>|</span><span class='field FgBlue BgRed'><span class='tag rawTag'>150</span><span class='equals'>=</span><span class='value rawValue'>A</span></span><span class='delim FgBlue BgRed'>|</span><span class='field FgBlue BgRed'><span class='tag rawTag'>55</span><span class='equals'>=</span><span class='value rawValue'>AUD/USD</span></span></span>"
        '35=blah&&150=A:Bold:Red:Line'                               | "[1m[41m35=blah[0m[1m[41m|[0m[1m[41m150=A[0m[1m[41m|[0m[1m[41m55=AUD/USD[0m"               | "<span class='fields'><span class='field bold BgRed'><span class='tag rawTag'>35</span><span class='equals'>=</span><span class='value rawValue'>blah</span></span><span class='delim bold BgRed'>|</span><span class='field bold BgRed'><span class='tag rawTag'>150</span><span class='equals'>=</span><span class='value rawValue'>A</span></span><span class='delim bold BgRed'>|</span><span class='field bold BgRed'><span class='tag rawTag'>55</span><span class='equals'>=</span><span class='value rawValue'>AUD/USD</span></span></span>"
        '150:Bg4,150=A:Fg2:Line,35=blah&&55~AUD:Blue:Red:Field'      | "[34m[41m[38;5;2m[34m[41m35=blah[0m[38;5;2m|[0m[38;5;2m[48;5;4m[38;5;2m150=A[0m[38;5;2m|[0m[34m[41m[38;5;2m[34m[41m55=AUD/USD[0m"                    | "<span class='fields'><span class='field FgBlue BgRed Fg2'><span class='tag rawTag'>35</span><span class='equals'>=</span><span class='value rawValue'>blah</span></span><span class='delim Fg2'>|</span><span class='field Fg2 Bg4'><span class='tag rawTag'>150</span><span class='equals'>=</span><span class='value rawValue'>A</span></span><span class='delim Fg2'>|</span><span class='field FgBlue BgRed Fg2'><span class='tag rawTag'>55</span><span class='equals'>=</span><span class='value rawValue'>AUD/USD</span></span></span>"
        '150:Bg4,150=A:Fg2:Line,35=blah&&55~AUD&&150:Blue:Red:Field' | "[34m[41m[38;5;2m[34m[41m35=blah[0m[38;5;2m|[0m[34m[41m[38;5;2m[48;5;4m[34m[41m[38;5;2m[34m[41m150=A[0m[38;5;2m|[0m[34m[41m[38;5;2m[34m[41m55=AUD/USD[0m"   | "<span class='fields'><span class='field FgBlue BgRed Fg2'><span class='tag rawTag'>35</span><span class='equals'>=</span><span class='value rawValue'>blah</span></span><span class='delim Fg2'>|</span><span class='field FgBlue BgRed Fg2 Bg4'><span class='tag rawTag'>150</span><span class='equals'>=</span><span class='value rawValue'>A</span></span><span class='delim Fg2'>|</span><span class='field FgBlue BgRed Fg2'><span class='tag rawTag'>55</span><span class='equals'>=</span><span class='value rawValue'>AUD/USD</span></span></span>"
    }
}
