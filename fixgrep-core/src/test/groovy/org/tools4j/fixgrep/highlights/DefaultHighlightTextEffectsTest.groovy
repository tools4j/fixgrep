package org.tools4j.fixgrep.highlights

import org.tools4j.fixgrep.texteffect.TextEffectParser
import spock.lang.Specification

/**
 * User: ben
 * Date: 11/04/2018
 * Time: 6:37 AM
 */
class DefaultHighlightTextEffectsTest extends Specification {
    def "test default values"() {
        expect:
        DefaultHighlightTextEffects effects = DefaultHighlightTextEffects.DEFAULT

        "FgBrightRed,FgBrightGreen,FgBrightYellow,FgBrightBlue,FgBrightMagenta,FgBrightCyan,FgBrightRed,FgBrightGreen,FgBrightYellow,FgBrightBlue,FgBrightMagenta,FgBrightCyan,FgBrightRed,FgBrightGreen,FgBrightYellow,FgBrightBlue,FgBrightMagenta,FgBrightCyan".split(',').toList().forEach {
            assert effects.next() == new TextEffectParser().parse(it)
        }
    }
}
