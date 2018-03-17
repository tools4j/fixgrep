package org.tools4j.fix

import spock.lang.Shared
import spock.lang.Specification

import java.util.stream.Collectors

/**
 * User: ben
 * Date: 30/6/17
 * Time: 5:32 PM
 */
class FieldsSortTest extends Specification {
    @Shared
    private FixSpec fixSpec = new Fix50SP2FixSpecFromClassPath().load();

    def "Compare"(String fields, List<Integer> requestedOrder, List<Integer> expectedOutputOrder) {
        given:
        sortAndAssertOrderInSortedFields(fields, requestedOrder, expectedOutputOrder)

        where:
        fields                                  | requestedOrder    | expectedOutputOrder
        "35=8;11=asdf;22=3;55=ABC;99=qwerty"    | [55,35,11]        | [55,35,11,22,99]
        "5=8;4=asdf;3=3;2=ABC;1=qwerty"         | [55,35,11]        | [5,4,3,2,1]
        "5=8;4=asdf;3=3;2=ABC;1=qwerty"         | []                | [5,4,3,2,1]
        "1=8;35=asdf;3=3;4=ABC;5=qwerty"        | [55,35,11]        | [35,1,3,4,5]
        "1=8"                                   | [55,35,11]        | [1]
        ""                                      | [55,35,11]        | []
        ""                                      | []                | []
    }

    protected void sortAndAssertOrderInSortedFields(String fieldsStr, List<Integer> desiredOrder, List<Integer> expectedOutputOrder) {
        final Fields fields = new FieldsFromDelimitedString(fieldsStr, (char) ';').fields
        final Fields sortedFields = fields.sortBy(desiredOrder)
        final List<Integer> actualOutputOrder = sortedFields.stream().map({it.tag.tag}).collect(Collectors.toList())
        assert actualOutputOrder == expectedOutputOrder
    }
}
