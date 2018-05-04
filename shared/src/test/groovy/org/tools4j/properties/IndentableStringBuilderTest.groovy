package org.tools4j.properties

import spock.lang.Specification

/**
 * User: ben
 * Date: 31/10/17
 * Time: 6:45 AM
 */
class IndentableStringBuilderTest extends Specification {
    def "test append"() {
        given:
        final Person person = new Person();

        when:
        final String str = person.toPrettyString();

        then:
        str ==
"""Person {
    name=Ben Warner
    gender=Male
    address=Address{
        addressLine1=44 Marley Lane
        town=Haslemere
        postcode=GU273HJ
    }
}"""
    }

    private static class Person{
        private final String name = "Ben Warner"
        private final String gender = "Male"
        private final Address address = new Address();

        public String toPrettyString(){
            final IndentableStringBuilder sb = new IndentableStringBuilder("    ");
            sb.append("Person {\n")
            sb.activateIndent()
            sb.append("name=").append(name).append("\n")
            sb.append("gender=").append(gender).append("\n")
            sb.append("address=").append(address.toPrettyString()).append("\n")
            sb.decactivateIndent()
            sb.append("}")
        }
    }

    private static class Address {
        private final String addressLine1 = "44 Marley Lane";
        private final String town = "Haslemere"
        private final String postcode = "GU273HJ"

        public String toPrettyString(){
            final IndentableStringBuilder sb = new IndentableStringBuilder("    ");
            sb.append("Address{\n")
            sb.activateIndent()
            sb.append("addressLine1=").append(addressLine1).append("\n")
            sb.append("town=").append(town).append("\n")
            sb.append("postcode=").append(postcode).append("\n")
            sb.decactivateIndent()
            sb.append("}")
        }
    }



}
