package org.tools4j.fix

import spock.lang.Shared
import spock.lang.Specification

/**
 * User: ben
 * Date: 30/6/17
 * Time: 5:32 PM
 */
class SortedFieldsByConfigListTest extends Specification {
    @Shared
    private FixSpec fixSpec = new Fix50SP2FixSpecFromClassPath().load();

    def "Compare"(Fields fixMessage, String desiredOrder) {
        given:
        sortAndAssertOrderInSortedFields(fixMessage, desiredOrder)

        where:
        fixMessage              | desiredOrder
        new Fields.FixtureNos() | "55,35,11"
        new Fields.FixtureNos() | "35,11"
        new Fields.FixtureNos() | "55"
        new Fields.FixtureNew() | "35,150"
    }

    protected void sortAndAssertOrderInSortedFields(Fields fixMessage, String desiredOrder) {
        final SortedFieldsByConfigList sortedFields = new SortedFieldsByConfigList(new AnnotatedFields(fixSpec, fixMessage), desiredOrder)
        println sortedFields.toString();

        final Iterator<Field> fields = sortedFields.getFields().iterator();
        for (String tagNumStr in desiredOrder.split(",")) {
            if (!fields.hasNext()) {
                break;
            }
            final int expectedTagNum = Integer.parseInt(tagNumStr)
            final int actualTagNum = fields.next().getTag().getTag()
            assert actualTagNum == expectedTagNum: "Expected fix tag: $expectedTagNum, but found fix tag: $actualTagNum" +
                    "\ndesiredOrder: $desiredOrder" +
                    "\nfixMessage: $sortedFields"

        }
    }
}
