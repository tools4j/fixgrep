package org.tools4j.fixgrep.highlights

import org.tools4j.fix.AnnotationPositions
import org.tools4j.fix.Fields
import org.tools4j.fix.FieldsImpl
import org.tools4j.fix.spec.FixSpecParser
import org.tools4j.fixgrep.formatting.FormattingContext
import org.tools4j.fixgrep.formatting.HorizontalConsoleMsgFormatter
import org.tools4j.fixgrep.formatting.HorizontalHtmlMsgFormatter
import org.tools4j.fixgrep.texteffect.Ansi
import org.tools4j.fixgrep.texteffect.Ansi16ForegroundColor
import spock.lang.Specification
import spock.lang.Unroll

/**
 * User: ben
 * Date: 6/04/2018
 * Time: 6:34 AM
 */
class HighlightActionTest extends Specification {
    private final static String color = Ansi16ForegroundColor.Blue.consoleTextBefore
    private final static String normal = Ansi.Reset

    @Unroll
    def "ConsoleText highlight #expressions #fix #expectedOutput"(final List<String> expressions, final String fix, final String expectedOutput) {
        given:
        final Fields fields = new FieldsImpl(fix, "|")

        when:
        final Highlight highlight = new HighlightParser().parse(expressions)
        final String output = new HorizontalConsoleMsgFormatter(new FormattingContext(highlight.apply(fields), AnnotationPositions.NO_ANNOTATION, false, new FixSpecParser().parseSpec()), "|").format()
        println output

        then:
        assert output == expectedOutput

         where:
        expressions                            | fix                       | expectedOutput
        ['35:FgBlue']                           |'35=blah|150=A|55=AUD/USD' | "${color}35=blah${normal}|150=A|55=AUD/USD"
        ['35:FgBlue','55:FgBlue']               |'35=blah|150=A|55=AUD/USD' | "${color}35=blah${normal}|150=A|${color}55=AUD/USD${normal}"
        ['35:FgBlue','55:FgBlue','150:FgBlue']  |'35=blah|150=A|55=AUD/USD' | "${color}35=blah${normal}|${color}150=A${normal}|${color}55=AUD/USD${normal}"
        ['22:FgBlue']                           |'35=blah|150=A|55=AUD/USD' | "35=blah|150=A|55=AUD/USD"
        ['22:FgBlue:Msg']                      |'35=blah|150=A|55=AUD/USD' | "35=blah|150=A|55=AUD/USD"
    }

    @Unroll
    def "html highlight #expressions #fix #expectedOutput"(final List<String> expressions, final String fix, final String expectedOutput) {
        given:
        Fields fields = new FieldsImpl(fix, '|')

        when:
        final Highlight highlight = new HighlightParser().parse(expressions)
        final String output = new HorizontalHtmlMsgFormatter(new FormattingContext(highlight.apply(fields), AnnotationPositions.NO_ANNOTATION, false, new FixSpecParser().parseSpec()), "|").format()
        println output

        then:
        assert output == expectedOutput

        where:
        expressions                                 | fix                       | expectedOutput
        ['35:FgBlue']                               |'35=blah|150=A|55=AUD/USD' | "<div class='fields'><span class='field FgBlue'><span class='tag raw'>35</span><span class='equals'>=</span><span class='value raw'>blah</span></span><span class='delim'>|</span><span class='field'><span class='tag raw'>150</span><span class='equals'>=</span><span class='value raw'>A</span></span><span class='delim'>|</span><span class='field'><span class='tag raw'>55</span><span class='equals'>=</span><span class='value raw'>AUD/USD</span></span></div>"
        ['35:FgBlue','55:FgRed']                    |'35=blah|150=A|55=AUD/USD' | "<div class='fields'><span class='field FgBlue'><span class='tag raw'>35</span><span class='equals'>=</span><span class='value raw'>blah</span></span><span class='delim'>|</span><span class='field'><span class='tag raw'>150</span><span class='equals'>=</span><span class='value raw'>A</span></span><span class='delim'>|</span><span class='field FgRed'><span class='tag raw'>55</span><span class='equals'>=</span><span class='value raw'>AUD/USD</span></span></div>"
        ['35:FgBlue','55:FgGreen','150:FgYellow']   |'35=blah|150=A|55=AUD/USD' | "<div class='fields'><span class='field FgBlue'><span class='tag raw'>35</span><span class='equals'>=</span><span class='value raw'>blah</span></span><span class='delim'>|</span><span class='field FgYellow'><span class='tag raw'>150</span><span class='equals'>=</span><span class='value raw'>A</span></span><span class='delim'>|</span><span class='field FgGreen'><span class='tag raw'>55</span><span class='equals'>=</span><span class='value raw'>AUD/USD</span></span></div>"
        ['22:FgBlue']                               |'35=blah|150=A|55=AUD/USD' | "<div class='fields'><span class='field'><span class='tag raw'>35</span><span class='equals'>=</span><span class='value raw'>blah</span></span><span class='delim'>|</span><span class='field'><span class='tag raw'>150</span><span class='equals'>=</span><span class='value raw'>A</span></span><span class='delim'>|</span><span class='field'><span class='tag raw'>55</span><span class='equals'>=</span><span class='value raw'>AUD/USD</span></span></div>"
        ['22:FgBlue:Msg']                           |'35=blah|150=A|55=AUD/USD' | "<div class='fields'><span class='field'><span class='tag raw'>35</span><span class='equals'>=</span><span class='value raw'>blah</span></span><span class='delim'>|</span><span class='field'><span class='tag raw'>150</span><span class='equals'>=</span><span class='value raw'>A</span></span><span class='delim'>|</span><span class='field'><span class='tag raw'>55</span><span class='equals'>=</span><span class='value raw'>AUD/USD</span></span></div>"
    }
}
