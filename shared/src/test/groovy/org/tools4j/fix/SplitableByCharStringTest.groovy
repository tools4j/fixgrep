package org.tools4j.fix

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

/**
 * User: ben
 * Date: 6/7/17
 * Time: 6:38 AM
 */
class SplitableByCharStringTest extends Specification {
    @Shared private String a1 = new Ascii1Char().toString()

    @Unroll
    def "Split str:#str, regexDelim:#regexDelim, result:#result"(final String str, final char regexDelim, final String[] result) {
        when:
        final SplitableByCharString splitable = new SplitableByCharString(str, regexDelim)

        then:
        assert splitable.split().equalsArray(result)

        where:
        str                       | regexDelim | result
        "hello:there"             | ':'        | ["hello", "there"]
        "hello:there:Ben"         | ':'        | ["hello", "there", "Ben"]
        "hello|there|Ben"         | '|'        | ["hello", "there", "Ben"]
        "hello|there|Ben|"        | '|'        | ["hello", "there", "Ben"]
        "|hello|there|Ben|"       | '|'        | ["", "hello", "there", "Ben"]
        "hello${a1}there${a1}Ben" | a1         | ["hello", "there", "Ben"]
        "hello"                   | a1         | ["hello"]
        ""                        | a1         | []
    }

    @Unroll
    def "SplitAtFirst str:#str, regexDelim:#regexDelim, result:#result"(final String str, final char regexDelim, final String[] result) {
        when:
        final SplitableByCharString splitable = new SplitableByCharString(str, regexDelim);

        then:
        assert splitable.splitAtFirst().equalsArray(result)

        where:
        str                       | regexDelim | result
        "hello:there"             | ':'        | ["hello", "there"]
        "hello:there:Ben"         | ':'        | ["hello", "there:Ben"]
        "hello|there|Ben"         | '|'        | ["hello", "there|Ben"]
        "hello|there|Ben|"        | '|'        | ["hello", "there|Ben|"]
        "|hello|there|Ben|"       | '|'        | ["", "hello|there|Ben|"]
        "hello${a1}there${a1}Ben" | a1         | ["hello", "there${a1}Ben"]
        "hello"                   | a1         | ["hello"]
        ""                        | a1         | [""]
    }
}
