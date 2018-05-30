package org.tools4j.fixgrep.help

import org.tools4j.fixgrep.texteffect.Ansi16BackgroundColor
import org.tools4j.fixgrep.texteffect.Ansi256Color
import org.tools4j.fixgrep.texteffect.AnsiForegroundBackground
import org.tools4j.fixgrep.texteffect.HtmlOnlyTextEffect
import spock.lang.Specification

/**
 * User: ben
 * Date: 23/05/2018
 * Time: 5:16 PM
 */
class HtmlTableBuilderTest extends Specification {
    def "GetTable"() {
        final HtmlWriter writer = new HtmlWriter()

        when:
        writer.writeHeading(1, "my table")
        writer.addTable()
                .startNewRow().addTableHeaderCell("one").addTableHeaderCell("two").addTableHeaderCell("three")
                .startNewRow().addCell("four").addCell("five").addCell("")
                .startNewRow().addCell("once").addCell("I").addCell("caught", HtmlOnlyTextEffect.Console)
                .startNewRow().addCell("a").addCell("fish").addCell("alive")
                .startNewRow().addCell("six", Ansi16BackgroundColor.Yellow).addCell("seven").addCell("eight")
                .startNewRow().addCell("nine").addCell("ten").addCell("then I let him go again", new Ansi256Color(44, AnsiForegroundBackground.FOREGROUND))
                .endTable()

        then:
        assert writer.toFormattedText() == "<h1>my table</h1>\n" +
                "<table class='doc-table'>\n" +
                "<tr><th>one</th><th>two</th><th>three</th></tr>\n" +
                "<tr><td>four</td><td>five</td><td></td></tr>\n" +
                "<tr><td>once</td><td>I</td><td class='console'>caught</td></tr>\n" +
                "<tr><td>a</td><td>fish</td><td>alive</td></tr>\n" +
                "<tr><td class='BgYellow'>six</td><td>seven</td><td>eight</td></tr>\n" +
                "<tr><td>nine</td><td>ten</td><td class='Fg44'>then I let him go again</td></tr>\n" +
                "</table>\n"
    }
}
