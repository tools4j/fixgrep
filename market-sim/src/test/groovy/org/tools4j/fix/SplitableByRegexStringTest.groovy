package org.tools4j.fix

import spock.lang.Specification
import spock.lang.Unroll

/**
 * User: ben
 * Date: 6/7/17
 * Time: 6:38 AM
 */
class SplitableByRegexStringTest extends Specification {
    @Unroll
    def "Split str:#str, regexDelim:#regexDelim, result:#result"(final String str, final String regexDelim, final String[] result) {
        when:
        final SplitableByRegexString splitable = new SplitableByRegexString(str, regexDelim);

        then:
        assert splitable.split().equalsArray(result)

        where:
        str                 | regexDelim   | result
        "hello:there"       | ":"          | ["hello", "there"]
        "hello:there:Ben"   | ":"          | ["hello", "there", "Ben"]
        "hello|there|Ben"   | "\\|"        | ["hello", "there", "Ben"]
        "hello|there|Ben|"  | "\\|"        | ["hello", "there", "Ben"]
        "|hello|there|Ben|" | "\\|"        | ["", "hello", "there", "Ben"]
        "hello^Athere^ABen" | "\\^A"       | ["hello", "there", "Ben"]
        "hello"             | "\\^A"       | ["hello"]
        ""                  | "\\^A"       | []
    }

    @Unroll
    def "SplitAtFirst str:#str, regexDelim:#regexDelim, result:#result"(final String str, final String regexDelim, final String[] result) {
        when:
        final SplitableByRegexString splitable = new SplitableByRegexString(str, regexDelim);

        then:
        assert splitable.splitAtFirst().equalsArray(result)

        where:
        str                 | regexDelim   | result
        "hello:there"       | ":"          | ["hello", "there"]
        "hello:there:Ben"   | ":"          | ["hello", "there:Ben"]
        "hello|there|Ben"   | "\\|"        | ["hello", "there|Ben"]
        "hello|there|Ben|"  | "\\|"        | ["hello", "there|Ben|"]
        "|hello|there|Ben|" | "\\|"        | ["", "hello|there|Ben|"]
        "hello^Athere^ABen" | "\\^A"       | ["hello", "there^ABen"]
        "hello"             | "\\^A"       | ["hello"]
        ""                  | "\\^A"       | [""]
    }
}
