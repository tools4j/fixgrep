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

        "Fg9,Fg10,Fg11,Fg12,Fg13,Fg14,Fg9,Fg10,Fg11,Fg12,Fg13,Fg14,Fg9,Fg10,Fg11,Fg12,Fg13,Fg14".split(',').toList().forEach {
            assert effects.next() == new TextEffectParser().parse(it)
        }
    }
}
