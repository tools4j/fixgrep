package org.tools4j.fix

import spock.lang.Specification

/**
 * User: ben
 * Date: 5/7/17
 * Time: 6:51 AM
 */
class FieldsFromAnnotatedMessageStringTest extends Specification {

    private final String MARKET_DATA_REQUEST =
            "[MsgType]35=V[MARKETDATAREQUEST]|" +
                    "[MDReqID]262=AASDJKG790|" +
                    "[SubscriptionRequestType]263=0[SNAPSHOT]|" +
                    "[MarketDepth]264=20|" +
                    "[NoMDEntryTypes]267=2|" +
                    "[MDEntryType]269=0[BID]|" +
                    "[MDEntryType]269=1[OFFER]|" +
                    "[NoRelatedSym]146=3|" +
                    "[Symbol]55=GBP/USD|" +
                    "[Symbol]55=AUD/USD|" +
                    "[Symbol]55=USD/JPY|";


    def "test parse expression"() {
        when:
        final List<Field> expression = new FieldsFromAnnotatedMessageString(MARKET_DATA_REQUEST, "\\|").getFields();

        then:
        assert expression.size() == 11;
        assert expression.get(0) == new Field(new SpecTag(35, "MsgType"), new EnumValue("V", "MARKETDATAREQUEST"))
        assert expression.get(1) == new Field(new SpecTag(262, "MDReqID"), new NonEnumValue("AASDJKG790"))
        assert expression.get(2) == new Field(new SpecTag(263, "SubscriptionRequestType"), new EnumValue("0", "SNAPSHOT"))
        assert expression.get(3) == new Field(new SpecTag(264, "MarketDepth"), new NonEnumValue("20"))
        assert expression.get(4) == new Field(new SpecTag(267, "NoMDEntryTypes"), new NonEnumValue("2"))
        assert expression.get(5) == new Field(new SpecTag(269, "MDEntryType"), new EnumValue("0", "BID"))
        assert expression.get(6) == new Field(new SpecTag(269, "MDEntryType"), new EnumValue("1", "OFFER"))
        assert expression.get(7) == new Field(new SpecTag(146, "NoRelatedSym"), new NonEnumValue("3"))
        assert expression.get(8) == new Field(new SpecTag(55, "Symbol"), new NonEnumValue("GBP/USD"))
        assert expression.get(9) == new Field(new SpecTag(55, "Symbol"), new NonEnumValue("AUD/USD"))
        assert expression.get(10) == new Field(new SpecTag(55, "Symbol"), new NonEnumValue("USD/JPY"))
    }
}

