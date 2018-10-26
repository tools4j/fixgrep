package org.tools4j.fixgrep.orders

import spock.lang.Specification

/**
 * User: benjw
 * Date: 22/10/2018
 * Time: 06:30
 */
class IdFilterTest extends Specification {
    def "MatchesFilter - empty filter means everything should match - matching against strings"() {
        when:
        final IdFilter idFilter = new IdFilter()

        then:
        assert idFilter.matchesFilter("asdf")
        assert idFilter.matchesFilter("")
    }

    def "MatchesFilter - single filter entry - matching against strings"() {
        when:
        final IdFilter idFilter = new IdFilter(["blah"])

        then:
        assert !idFilter.matchesFilter("asdf")
        assert !idFilter.matchesFilter("")
        assert idFilter.matchesFilter("blah")
        assert idFilter.matchesFilter("asdfblahasdf")
    }

    def "MatchesFilter - multiple filter entry - matching against strings"() {
        when:
        final IdFilter idFilter = new IdFilter(["blah", "asdf"])

        then:
        assert !idFilter.matchesFilter("")
        assert !idFilter.matchesFilter("xxx")
        assert idFilter.matchesFilter("asdf")
        assert idFilter.matchesFilter("blah")
        assert idFilter.matchesFilter("asdfblahasdf")
    }

    def "MatchesFilter - empty filter means everything should match - matching against single message"() {
        when:
        final IdFilter idFilter = new IdFilter()

        then:
        assert idFilter.matchesFilter([m(null, null, null)])
        assert idFilter.matchesFilter([m("blah", null, null)])
        assert idFilter.matchesFilter([m(null, "blah", null)])
        assert idFilter.matchesFilter([m(null, null, "blah")])
    }

    def "MatchesFilter - single filter entry - matching against single message"() {
        when:
        final IdFilter idFilter = new IdFilter(["blah"])

        then:
        assert !idFilter.matchesFilter([m(null, null, null)])
        assert !idFilter.matchesFilter([m("asdf", "qwerty", "zxcv")])
        assert idFilter.matchesFilter([m("blah", null, null)])
        assert idFilter.matchesFilter([m(null, "blah", null)])
        assert idFilter.matchesFilter([m(null, null, "blah")])
        assert idFilter.matchesFilter([m("blah", "blah", "blah")])
    }

    def "MatchesFilter - multiple filter entry - matching against single message"() {
        when:
        final IdFilter idFilter = new IdFilter(["blah", "asdf"])

        then:
        assert !idFilter.matchesFilter([m(null, null, null)])
        assert !idFilter.matchesFilter([m("dfgh", "qwerty", "zxcv")])
        assert idFilter.matchesFilter([m("asdf", "qwerty", "zxcv")])
        assert idFilter.matchesFilter([m("blah", null, null)])
        assert idFilter.matchesFilter([m(null, "blah", null)])
        assert idFilter.matchesFilter([m(null, null, "blah")])
        assert idFilter.matchesFilter([m("blah", "blah", "blah")])
    }


    def "MatchesFilter - empty filter means everything should match - matching against multiple messages"() {
        when:
        final IdFilter idFilter = new IdFilter()

        then:
        assert idFilter.matchesFilter([m(null, null, null), m("blah", null, null)])
        assert idFilter.matchesFilter([m("blah", null, null), m(null, "blah", null)])
    }

    def "MatchesFilter - single filter entry - matching against multiple messages"() {
        when:
        final IdFilter idFilter = new IdFilter(["blah"])

        then:
        assert !idFilter.matchesFilter([m(null, null, null), m("asdf", "qwerty", "zxcv")])
        assert idFilter.matchesFilter([m("blah", null, null), m("asdf", "qwerty", "zxcv")])
        assert idFilter.matchesFilter([m("asdf", "qwerty", "zxcv"), m("asdfblahasdf", null, null)])
        assert idFilter.matchesFilter([m(null, "adfblahadsf", null), m(null, null, "asdfblahadsf")])
    }

    def "MatchesFilter - multiple filter entry - matching against multiple messages"() {
        when:
        final IdFilter idFilter = new IdFilter(["blah", "asdf"])

        then:
        assert !idFilter.matchesFilter([m(null, null, null), m(null, "qwerty", null)])
        assert idFilter.matchesFilter([m("dfgh", "qwerty", "zxcv"), m("xxx_asdf_xxx", "qwerty", "zxcv")])
    }

    private IdentifiableOrder m(final String orderId, final String clOrdId, final String origClOrdId){
        new IdentifiableOrder.SimpleIdentifiableOrder(
                orderId == null ? null: new UniqueOrderId(orderId),
                clOrdId == null ? null: new UniqueClientOrderId(clOrdId),
                origClOrdId == null ? null: new UniqueClientOrderId(origClOrdId)
        )
    }


//
//    def "MatchesFilter - single filter entry - matching against messages"() {
//        when:
//        final IdFilter idFilter = new IdFilter(["blah"])
//
//        then:
//        assert !idFilter.matchesFilter("asdf")
//        assert !idFilter.matchesFilter("")
//        assert idFilter.matchesFilter("blah")
//        assert idFilter.matchesFilter("asdfblahasdf")
//    }
//
//    def "MatchesFilter - multiple filter entry - matching against messages"() {
//        when:
//        final IdFilter idFilter = new IdFilter(["blah", "asdf"])
//
//        then:
//        assert !idFilter.matchesFilter("")
//        assert !idFilter.matchesFilter("xxx")
//        assert idFilter.matchesFilter("asdf")
//        assert idFilter.matchesFilter("blah")
//        assert idFilter.matchesFilter("asdfblahasdf")
//    }
}
