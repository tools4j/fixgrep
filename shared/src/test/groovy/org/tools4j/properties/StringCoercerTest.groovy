package org.tools4j.properties

import spock.lang.Specification
import spock.lang.Unroll

/**
 * User: ben
 * Date: 2/05/2018
 * Time: 5:33 PM
 */
class StringCoercerTest extends Specification {
    @Unroll
    def "GetAsDouble #str"(final String str, final double expectedOutput) {
        when:
        final StringCoercer coercer = new StringCoercer(str)

        then:
        assert coercer.getAsDouble() == expectedOutput

        where:
        str      | expectedOutput
        "0.1"    | 0.1d
        "1.1"    | 1.1d
        " 1.1 "  | 1.1d
        " 666 "  | 666.0d
    }

    @Unroll
    def "GetAsLong #str"(final String str, final long expectedOutput) {
        when:
        final StringCoercer coercer = new StringCoercer(str)

        then:
        assert coercer.getAsLong() == expectedOutput

        where:
        str      | expectedOutput
        "1"      | 1L
        "666"    | 666L
        " 666 "  | 666L
    }

    @Unroll
    def "GetAsInt #str"(final String str, final int expectedOutput) {
        when:
        final StringCoercer coercer = new StringCoercer(str)

        then:
        assert coercer.getAsInt() == expectedOutput

        where:
        str      | expectedOutput
        "1"      | 1
        "666"    | 666
        " 666 "  | 666
    }

    @Unroll
    def "GetAsString #str"(final String str, final String expectedOutput) {
        when:
        final StringCoercer coercer = new StringCoercer(str)

        then:
        assert coercer.getAsString() == expectedOutput

        where:
        str                     | expectedOutput
        "1"                     | "1"
        " rock and roll baby "  | "rock and roll baby"
        "   "                   | ""
        ""                      | ""
    }

    @Unroll
    def "GetAsDouble with default #str"(final String str, final double defaultVal, final double expectedOutput) {
        when:
        final StringCoercer coercer = new StringCoercer(str)

        then:
        assert coercer.getAsDouble(defaultVal) == expectedOutput

        where:
        str      | defaultVal | expectedOutput
        " 666 "  | 0.9d       | 666.0d
        null     | 0.9d       | 0.9d
    }

    @Unroll
    def "GetAsLong with default #str"(final String str, final long defaultVal, final double expectedOutput) {
        when:
        final StringCoercer coercer = new StringCoercer(str)

        then:
        assert coercer.getAsLong(defaultVal) == expectedOutput

        where:
        str      | defaultVal | expectedOutput
        " 666 "  | 222        | 666
        null     | 222        | 222
    }

    @Unroll
    def "GetAsInt with default #str"(final String str, final int defaultVal, final double expectedOutput) {
        when:
        final StringCoercer coercer = new StringCoercer(str)

        then:
        assert coercer.getAsInt(defaultVal) == expectedOutput

        where:
        str      | defaultVal | expectedOutput
        " 666 "  | 222        | 666
        null     | 222        | 222
    }

    @Unroll
    def "GetAsString with default #str"(final String str, final String defaultVal, final String expectedOutput) {
        when:
        final StringCoercer coercer = new StringCoercer(str)

        then:
        assert coercer.getAsString(defaultVal) == expectedOutput

        where:
        str      | defaultVal    | expectedOutput
        "foo "   | "blah"        | "foo"
        null     | "blah"        | "blah"
    }

    @Unroll
    def "GetAsDoubleList #str"(final String str, final List<Double> expectedOutput) {
        when:
        final StringCoercer coercer = new StringCoercer(str)

        then:
        assert coercer.getAsDoubleList() == expectedOutput

        where:
        str                   | expectedOutput
        "0.1, 0.2, 0.3"       | Arrays.asList(0.1, 0.2, 0.3)
        " 0.1   , 0.2 , 0.3"  | Arrays.asList(0.1, 0.2, 0.3)
        "0.1,0.2,0.3"         | Arrays.asList(0.1, 0.2, 0.3)
        "0.1"                 | Arrays.asList(0.1)
        ""                    | Collections.emptyList()
        "[0.1,0.2,0.3] "      | Arrays.asList(0.1, 0.2, 0.3)
    }

    @Unroll
    def "GetAsLongList #str"(final String str, final List<Long> expectedOutput) {
        when:
        final StringCoercer coercer = new StringCoercer(str)

        then:
        assert coercer.getAsLongList() == expectedOutput

        where:
        str             | expectedOutput
        "1, 2, 3"       | Arrays.asList(1, 2, 3)
        " 1   , 2 , 3"  | Arrays.asList(1, 2, 3)
        "1,2,3"         | Arrays.asList(1, 2, 3)
        "1"             | Arrays.asList(1)
        ""              | Collections.emptyList()
        "[1,2,3] "      | Arrays.asList(1, 2, 3)
    }

    @Unroll
    def "GetAsIntList #str"(final String str, final List<Integer> expectedOutput) {
        when:
        final StringCoercer coercer = new StringCoercer(str)

        then:
        assert coercer.getAsIntList() == expectedOutput

        where:
        str             | expectedOutput
        "1, 2, 3"       | Arrays.asList(1, 2, 3)
        " 1   , 2 , 3"  | Arrays.asList(1, 2, 3)
        "1,2,3"         | Arrays.asList(1, 2, 3)
        "1"             | Arrays.asList(1)
        ""              | Collections.emptyList()
        "[1,2,3] "      | Arrays.asList(1, 2, 3)
    }

    @Unroll
    def "GetAsStringList #str"(final String str, final List<String> expectedOutput) {
        when:
        final StringCoercer coercer = new StringCoercer(str)

        then:
        assert coercer.getAsStringList() == expectedOutput

        where:
        str                     | expectedOutput
        "1, 2, 3"               | Arrays.asList("1", "2", "3")
        " blah, de , blah"      | Arrays.asList("blah", "de", "blah")
        " sup "                 | Arrays.asList("sup")
        " "                     | Collections.emptyList()
    }

    def "TrimOfSquareBrackets #str"(final String str, final String expectedOutput) {
        when:
        final StringCoercer coercer = new StringCoercer("blah")

        then:
        assert coercer.trimOfSquareBrackets(str) == expectedOutput

        where:
        str        | expectedOutput
        "abc"      | "abc"
        "[abc]"    | "abc"
        " [abc] "  | "abc"
        "[abc"     | "abc"
        "abc] "    | "abc"
    }

    @Unroll
    def "GetAsDoubleList with default #str"(final String str, final List<Double> expectedOutput) {
        when:
        final StringCoercer coercer = new StringCoercer(str)

        then:
        assert coercer.getAsDoubleList(Arrays.asList(0.3d, 0.4d)) == expectedOutput

        where:
        str                   | expectedOutput
        "0.1, 0.2, 0.3"       | Arrays.asList(0.1, 0.2, 0.3)
        "   "                 | Arrays.asList(0.3d, 0.4d)
        null                  | Arrays.asList(0.3d, 0.4d)
    }

    @Unroll
    def "GetAsLongList with default #str"(final String str, final List<Long> expectedOutput) {
        when:
        final StringCoercer coercer = new StringCoercer(str)

        then:
        assert coercer.getAsLongList(Arrays.asList(3L, 4L)) == expectedOutput

        where:
        str                   | expectedOutput
        "1, 2, 3"             | Arrays.asList(1, 2, 3)
        "   "                 | Arrays.asList(3, 4)
        null                  | Arrays.asList(3, 4)
    }

    @Unroll
    def "GetAsIntList with default #str"(final String str, final List<Integer> expectedOutput) {
        when:
        final StringCoercer coercer = new StringCoercer(str)

        then:
        assert coercer.getAsIntList(Arrays.asList(3, 4)) == expectedOutput

        where:
        str                   | expectedOutput
        "1, 2, 3"             | Arrays.asList(1, 2, 3)
        "   "                 | Arrays.asList(3, 4)
        null                  | Arrays.asList(3, 4)
    }

    def "GetAsStringList with default #str"(final String str, final List<String> expectedOutput) {
        when:
        final StringCoercer coercer = new StringCoercer(str)

        then:
        assert coercer.getAsStringList(Arrays.asList("boo", "yeah")) == expectedOutput

        where:
        str                   | expectedOutput
        "goll , ee "          | Arrays.asList("goll", "ee")
        "   "                 | Arrays.asList("boo", "yeah")
        null                  | Arrays.asList("boo", "yeah")
    }
}
