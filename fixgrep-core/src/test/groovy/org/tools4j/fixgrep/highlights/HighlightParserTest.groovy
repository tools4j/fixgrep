package org.tools4j.fixgrep.highlights

import org.tools4j.fix.AnnotationPositions
import org.tools4j.fix.Fields
import org.tools4j.fix.FieldsImpl
import org.tools4j.fixgrep.formatting.HorizontalConsoleMsgFormatter
import org.tools4j.fixgrep.formatting.HorizontalHtmlMsgFormatter
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
        Fields fields = new FieldsImpl(fix, '|')

        when:
        final Highlight highlight = new HighlightParser().parse(expression)
        fields = highlight.apply(fields)
        final String actualConsoleOutput = new HorizontalConsoleMsgFormatter(fields, AnnotationPositions.OUTSIDE_ANNOTATED, true, '|').format()
        final String actualHtmlOutput = new HorizontalHtmlMsgFormatter(fields, AnnotationPositions.OUTSIDE_ANNOTATED, true, '|').format()
        println "Expected output: $expectedConsoleOutput"
        println "Actual output: $actualConsoleOutput"
        then:
        assert actualConsoleOutput == expectedConsoleOutput
        assert actualHtmlOutput == expectedHtmlOutput

        where:
        expression                                                   | expectedConsoleOutput                                                                                                                                            | expectedHtmlOutput
        '35:Blue:Field'                                              | "\u001B[34m\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1mblah\u001B[22m\u001B[0m|\u001B[1m150\u001B[22m\u001B[1m=\u001B[22m\u001B[1mA\u001B[22m|\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[22m" | "<div class='fields'><span class='field annotatedField FgBlue'><span class='tag tagRaw bold'>35</span><span class='equals bold'>=</span><span class='value valueRaw bold'>blah</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag tagRaw bold'>150</span><span class='equals bold'>=</span><span class='value valueRaw bold'>A</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag tagRaw bold'>55</span><span class='equals bold'>=</span><span class='value valueRaw bold'>AUD/USD</span></span></div>"
        '35=foo:Blue:Field'                                          | "\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1mblah\u001B[22m|\u001B[1m150\u001B[22m\u001B[1m=\u001B[22m\u001B[1mA\u001B[22m|\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[22m" | "<div class='fields'><span class='field annotatedField'><span class='tag tagRaw bold'>35</span><span class='equals bold'>=</span><span class='value valueRaw bold'>blah</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag tagRaw bold'>150</span><span class='equals bold'>=</span><span class='value valueRaw bold'>A</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag tagRaw bold'>55</span><span class='equals bold'>=</span><span class='value valueRaw bold'>AUD/USD</span></span></div>"
        '35:bold:Field'                                              | "\u001B[1m35=blah\u001B[22m|\u001B[1m150\u001B[22m\u001B[1m=\u001B[22m\u001B[1mA\u001B[22m|\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[22m" | "<div class='fields'><span class='field annotatedField bold'><span class='tag tagRaw bold'>35</span><span class='equals bold'>=</span><span class='value valueRaw bold'>blah</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag tagRaw bold'>150</span><span class='equals bold'>=</span><span class='value valueRaw bold'>A</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag tagRaw bold'>55</span><span class='equals bold'>=</span><span class='value valueRaw bold'>AUD/USD</span></span></div>"
        '35=blah:Blue:Field'                                         | "\u001B[34m\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1mblah\u001B[22m\u001B[0m|\u001B[1m150\u001B[22m\u001B[1m=\u001B[22m\u001B[1mA\u001B[22m|\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[22m" | "<div class='fields'><span class='field annotatedField FgBlue'><span class='tag tagRaw bold'>35</span><span class='equals bold'>=</span><span class='value valueRaw bold'>blah</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag tagRaw bold'>150</span><span class='equals bold'>=</span><span class='value valueRaw bold'>A</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag tagRaw bold'>55</span><span class='equals bold'>=</span><span class='value valueRaw bold'>AUD/USD</span></span></div>"
        '35=blah&&150=A:Blue:Red:Field'                              | "\u001B[34m\u001B[41m\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1mblah\u001B[22m\u001B[0m|\u001B[34m\u001B[41m\u001B[1m150\u001B[22m\u001B[1m=\u001B[22m\u001B[1mA\u001B[22m\u001B[0m|\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[22m" | "<div class='fields'><span class='field annotatedField FgBlue BgRed'><span class='tag tagRaw bold'>35</span><span class='equals bold'>=</span><span class='value valueRaw bold'>blah</span></span><span class='delim'>|</span><span class='field annotatedField FgBlue BgRed'><span class='tag tagRaw bold'>150</span><span class='equals bold'>=</span><span class='value valueRaw bold'>A</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag tagRaw bold'>55</span><span class='equals bold'>=</span><span class='value valueRaw bold'>AUD/USD</span></span></div>"
        '35=blah&&150=A:Blue:Red:Msg'                               | "\u001B[34m\u001B[41m\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1mblah\u001B[22m\u001B[0m\u001B[34m\u001B[41m|\u001B[0m\u001B[34m\u001B[41m\u001B[1m150\u001B[22m\u001B[1m=\u001B[22m\u001B[1mA\u001B[22m\u001B[0m\u001B[34m\u001B[41m|\u001B[0m\u001B[34m\u001B[41m\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[22m\u001B[0m" | "<div class='fields FgBlue BgRed'><span class='field annotatedField'><span class='tag tagRaw bold'>35</span><span class='equals bold'>=</span><span class='value valueRaw bold'>blah</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag tagRaw bold'>150</span><span class='equals bold'>=</span><span class='value valueRaw bold'>A</span></span><span class='delim'>|</span><span class='field annotatedField'><span class='tag tagRaw bold'>55</span><span class='equals bold'>=</span><span class='value valueRaw bold'>AUD/USD</span></span></div>"
        '150:Bg4,150=A:Fg2:Msg,35=blah&&55~AUD:Blue:Red:Field'      | "\u001B[34m\u001B[41m\u001B[38;5;2m\u001B[34m\u001B[41m\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1mblah\u001B[22m\u001B[0m\u001B[38;5;2m|\u001B[0m\u001B[48;5;4m\u001B[38;5;2m\u001B[48;5;4m\u001B[1m150\u001B[22m\u001B[1m=\u001B[22m\u001B[1mA\u001B[22m\u001B[0m\u001B[38;5;2m|\u001B[0m\u001B[34m\u001B[41m\u001B[38;5;2m\u001B[34m\u001B[41m\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[22m\u001B[0m"                    | "<div class='fields Fg2'><span class='field annotatedField FgBlue BgRed'><span class='tag tagRaw bold'>35</span><span class='equals bold'>=</span><span class='value valueRaw bold'>blah</span></span><span class='delim'>|</span><span class='field annotatedField Bg4'><span class='tag tagRaw bold'>150</span><span class='equals bold'>=</span><span class='value valueRaw bold'>A</span></span><span class='delim'>|</span><span class='field annotatedField FgBlue BgRed'><span class='tag tagRaw bold'>55</span><span class='equals bold'>=</span><span class='value valueRaw bold'>AUD/USD</span></span></div>"
        '150:Bg4,150=A:Fg2:Msg,35=blah&&55~AUD&&150:Blue:Red:Field' | "\u001B[34m\u001B[41m\u001B[38;5;2m\u001B[34m\u001B[41m\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1mblah\u001B[22m\u001B[0m\u001B[38;5;2m|\u001B[0m\u001B[48;5;4m\u001B[34m\u001B[41m\u001B[38;5;2m\u001B[48;5;4m\u001B[34m\u001B[41m\u001B[1m150\u001B[22m\u001B[1m=\u001B[22m\u001B[1mA\u001B[22m\u001B[0m\u001B[38;5;2m|\u001B[0m\u001B[34m\u001B[41m\u001B[38;5;2m\u001B[34m\u001B[41m\u001B[1m55\u001B[22m\u001B[1m=\u001B[22m\u001B[1mAUD/USD\u001B[22m\u001B[0m"   | "<div class='fields Fg2'><span class='field annotatedField FgBlue BgRed'><span class='tag tagRaw bold'>35</span><span class='equals bold'>=</span><span class='value valueRaw bold'>blah</span></span><span class='delim'>|</span><span class='field annotatedField Bg4 FgBlue BgRed'><span class='tag tagRaw bold'>150</span><span class='equals bold'>=</span><span class='value valueRaw bold'>A</span></span><span class='delim'>|</span><span class='field annotatedField FgBlue BgRed'><span class='tag tagRaw bold'>55</span><span class='equals bold'>=</span><span class='value valueRaw bold'>AUD/USD</span></span></div>"
    }
}
