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
    private final static String Fg2 = Ansi256Color.parse("Fg2").getConsoleTextBefore
    private final static String Bg4 = Ansi256Color.parse("Bg4").getConsoleTextBefore
    private final static String Bold = MiscTextEffect.Bold.getConsoleTextBefore
    private final static String reset = Ansi.Reset.ansiCode

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
        expression                                                   | expectedConsoleOutput                                                                                                         | expectedHtmlOutput
        '35:Blue:Field'                                              | "${FgBlue}35=blah${reset}|150=A|55=AUD/USD"                                                                                   | "<span class='fields'><span class='field FgBlue'><span class='tag rawTag'>35</span><span class='equals'>=</span><span class='value rawValue'>blah</span></span><span class='delim'>|</span><span class='field'><span class='tag rawTag'>150</span><span class='equals'>=</span><span class='value rawValue'>A</span></span><span class='delim'>|</span><span class='field'><span class='tag rawTag'>55</span><span class='equals'>=</span><span class='value rawValue'>AUD/USD</span></span></span>"
        '35=foo:Blue:Field'                                          | "35=blah|150=A|55=AUD/USD"                                                                                                    | "<span class='fields'><span class='field'><span class='tag rawTag'>35</span><span class='equals'>=</span><span class='value rawValue'>blah</span></span><span class='delim'>|</span><span class='field'><span class='tag rawTag'>150</span><span class='equals'>=</span><span class='value rawValue'>A</span></span><span class='delim'>|</span><span class='field'><span class='tag rawTag'>55</span><span class='equals'>=</span><span class='value rawValue'>AUD/USD</span></span></span>"
        '35:Bold:Field'                                              | "${Bold}35=blah${reset}|150=A|55=AUD/USD"                                                                                     | "<span class='fields'><span class='field Bold'><span class='tag rawTag'>35</span><span class='equals'>=</span><span class='value rawValue'>blah</span></span><span class='delim'>|</span><span class='field'><span class='tag rawTag'>150</span><span class='equals'>=</span><span class='value rawValue'>A</span></span><span class='delim'>|</span><span class='field'><span class='tag rawTag'>55</span><span class='equals'>=</span><span class='value rawValue'>AUD/USD</span></span></span>"
        '35=blah:Blue:Field'                                         | "${FgBlue}35=blah${reset}|150=A|55=AUD/USD"                                                                                   | "<span class='fields'><span class='field FgBlue'><span class='tag rawTag'>35</span><span class='equals'>=</span><span class='value rawValue'>blah</span></span><span class='delim'>|</span><span class='field'><span class='tag rawTag'>150</span><span class='equals'>=</span><span class='value rawValue'>A</span></span><span class='delim'>|</span><span class='field'><span class='tag rawTag'>55</span><span class='equals'>=</span><span class='value rawValue'>AUD/USD</span></span></span>"
        '35=blah&&150=A:Blue:Red:Field'                              | "${FgBlue}${BgRed}35=blah${reset}|${FgBlue}${BgRed}150=A${reset}|55=AUD/USD"                                                  | "<span class='fields'><span class='field FgBlue BgRed'><span class='tag rawTag'>35</span><span class='equals'>=</span><span class='value rawValue'>blah</span></span><span class='delim'>|</span><span class='field FgBlue BgRed'><span class='tag rawTag'>150</span><span class='equals'>=</span><span class='value rawValue'>A</span></span><span class='delim'>|</span><span class='field'><span class='tag rawTag'>55</span><span class='equals'>=</span><span class='value rawValue'>AUD/USD</span></span></span>"
        '35=blah&&150=A:Blue:Red:Line'                               | "${FgBlue}${BgRed}35=blah${reset}|${FgBlue}${BgRed}150=A${reset}|${FgBlue}${BgRed}55=AUD/USD${reset}"                         | "<span class='fields'><span class='field FgBlue BgRed'><span class='tag rawTag'>35</span><span class='equals'>=</span><span class='value rawValue'>blah</span></span><span class='delim'>|</span><span class='field FgBlue BgRed'><span class='tag rawTag'>150</span><span class='equals'>=</span><span class='value rawValue'>A</span></span><span class='delim'>|</span><span class='field FgBlue BgRed'><span class='tag rawTag'>55</span><span class='equals'>=</span><span class='value rawValue'>AUD/USD</span></span></span>"
        '35=blah&&150=A:Bold:Red:Line'                               | "${Bold}${BgRed}35=blah${reset}|${Bold}${BgRed}150=A${reset}|${Bold}${BgRed}55=AUD/USD${reset}"                               | "<span class='fields'><span class='field Bold BgRed'><span class='tag rawTag'>35</span><span class='equals'>=</span><span class='value rawValue'>blah</span></span><span class='delim'>|</span><span class='field Bold BgRed'><span class='tag rawTag'>150</span><span class='equals'>=</span><span class='value rawValue'>A</span></span><span class='delim'>|</span><span class='field Bold BgRed'><span class='tag rawTag'>55</span><span class='equals'>=</span><span class='value rawValue'>AUD/USD</span></span></span>"
        '150:Bg4,150=A:Fg2:Line,35=blah&&55~AUD:Blue:Red:Field'      | "${Fg2}${FgBlue}${BgRed}35=blah${reset}|${Bg4}${Fg2}150=A${reset}|${Fg2}${FgBlue}${BgRed}55=AUD/USD${reset}"                  | "<span class='fields'><span class='field Fg2'><span class='tag rawTag'>35</span><span class='equals'>=</span><span class='value rawValue'>blah</span></span><span class='delim'>|</span><span class='field Bg4'><span class='tag rawTag'>150</span><span class='equals'>=</span><span class='value rawValue'>A</span></span><span class='delim'>|</span><span class='field Fg2'><span class='tag rawTag'>55</span><span class='equals'>=</span><span class='value rawValue'>AUD/USD</span></span></span>"
        '150:Bg4,150=A:Fg2:Line,35=blah&&55~AUD&&150:Blue:Red:Field' | "${Fg2}${FgBlue}${BgRed}35=blah${reset}|${Bg4}${Fg2}${FgBlue}${BgRed}150=A${reset}|${Fg2}${FgBlue}${BgRed}55=AUD/USD${reset}" | "<span class='fields'><span class='field Fg2'><span class='tag rawTag'>35</span><span class='equals'>=</span><span class='value rawValue'>blah</span></span><span class='delim'>|</span><span class='field Bg4'><span class='tag rawTag'>150</span><span class='equals'>=</span><span class='value rawValue'>A</span></span><span class='delim'>|</span><span class='field Fg2'><span class='tag rawTag'>55</span><span class='equals'>=</span><span class='value rawValue'>AUD/USD</span></span></span>"
    }
}
