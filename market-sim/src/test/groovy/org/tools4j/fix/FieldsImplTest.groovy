package org.tools4j.fix

import spock.lang.Shared
import spock.lang.Specification

/**
 * User: ben
 * Date: 11/05/2018
 * Time: 6:17 AM
 */
class FieldsImplTest extends Specification {
    @Shared char a = new Ascii1Char().toChar()
    @Shared Fields fields = new FieldsFromDelimitedString("35=D${a}11=ABC${a}55=AUD/USD", a).fields
    @Shared Fields fieldsWithDupTag = new FieldsFromDelimitedString("35=D${a}11=ABC${a}55=AUD/USD${a}55=ABC/DEF", a).fields
    @Shared Fields emptyFields = new FieldsFromDelimitedString("", a).fields

    void setup() {
        fields
    }

    def "CountOfField_1"() {
        expect:
        assert fields.countOfField(55) == 1
    }

    def "CountOfField_0"() {
        expect:
        assert fields.countOfField(255) == 0
    }

    def "CountOfField_2"() {
        expect:
        assert fieldsWithDupTag.countOfField(55) == 2
    }

    def "CountOfField_byTag_1"() {
        expect:
        assert fields.countOfField(new RawTag(55)) == 1
    }

    def "CountOfField_byTag_0"() {
        expect:
        assert fields.countOfField(new RawTag(255)) == 0
    }

    def "CountOfField_byTag_2"() {
        expect:
        assert fieldsWithDupTag.countOfField(new RawTag(55)) == 2
    }

    def "GetField"() {
        expect:
        assert fields.getField(55) == new FieldImpl(new RawTag(55), new RawValue("AUD/USD"))
    }

    def "GetField_notPresent"() {
        expect:
        assert fields.getField(255) == null
    }

    def "GetField_dups"() {
        when:
        fieldsWithDupTag.getField(55)

        then:
        def throwable = thrown(IllegalArgumentException)
        assert throwable.getMessage().contains("More than one tag [55] exists in fields")
    }

    def "GetField_byTag"() {
        expect:
        assert fields.getField(new RawTag(55)) == new FieldImpl(new RawTag(55), new RawValue("AUD/USD"))
    }

    def "GetField_byTag_notPresent"() {
        expect:
        assert fields.getField(new RawTag(255)) == null
    }

    def "GetField_byTag_dups"() {
        when:
        fieldsWithDupTag.getField(new RawTag(55))

        then:
        def throwable = thrown(IllegalArgumentException)
        assert throwable.getMessage().contains("More than one tag [55] exists in fields")
    }

    def "Exists"() {
        expect:
        assert fields.exists(55)
        assert fields.exists(11)
        assert fieldsWithDupTag.exists(55)
        assert !fields.exists(255)
        assert !emptyFields.exists(55)
    }

    def "Exists_byTag"() {
        expect:
        assert fields.exists(new RawTag(55))
        assert fields.exists(new RawTag(11))
        assert fieldsWithDupTag.exists(new RawTag(55))
        assert !fields.exists(new RawTag(255))
        assert !emptyFields.exists(new RawTag(55))
    }

    def "ToString"() {
        expect:
        assert fields.toString() == "35=D|11=ABC|55=AUD/USD"
        assert fieldsWithDupTag.toString() == "35=D|11=ABC|55=AUD/USD|55=ABC/DEF"
        assert emptyFields.toString() == ""
    }

    def "Exclude"() {
        expect:
        assert fields.exclude([55]).toString() == "35=D|11=ABC"
        assert fields.exclude([255]).toString() == "35=D|11=ABC|55=AUD/USD"
        assert fieldsWithDupTag.exclude([55,11]).toString() == "35=D"
        assert emptyFields.exclude([55,11]).toString() == ""
    }

    def "IncludeOnly"() {
        expect:
        assert fields.includeOnly([55]).toString() == "55=AUD/USD"
        assert fields.includeOnly([255]).toString() == ""
        assert fieldsWithDupTag.includeOnly([55,11]).toString() == "11=ABC|55=AUD/USD|55=ABC/DEF"
        assert emptyFields.includeOnly([55,11]).toString() == ""
    }

    def "SortBy"() {
        expect:
        assert fields.sortBy([55]).toString() == "55=AUD/USD|35=D|11=ABC"
        assert fields.sortBy([55,11]).toString() == "55=AUD/USD|11=ABC|35=D"
        assert fields.sortBy([55,11,35]).toString() == "55=AUD/USD|11=ABC|35=D"
        assert fields.sortBy([55,11,35,123]).toString() == "55=AUD/USD|11=ABC|35=D"

        //At the moment, support of repeating tags (which indicate repeating groups) is not defined/supported
        assert fieldsWithDupTag.sortBy([55]).toString() == "35=D|11=ABC|55=AUD/USD|55=ABC/DEF"
        assert fieldsWithDupTag.sortBy([55,11]).toString() == "11=ABC|35=D|55=AUD/USD|55=ABC/DEF"
        assert fieldsWithDupTag.sortBy([55,11,35]).toString() == "11=ABC|35=D|55=AUD/USD|55=ABC/DEF"
        assert fieldsWithDupTag.sortBy([55,11,35,123]).toString() == "11=ABC|35=D|55=AUD/USD|55=ABC/DEF"

        assert emptyFields.sortBy([55,11,35,123]).toString() == ""
    }

    def "HasRepeatingTags"(){
        expect:
        assert !fields.hasRepeatingTags()
        assert fieldsWithDupTag.hasRepeatingTags()
        assert !emptyFields.hasRepeatingTags()
    }

    def "ToIntToStringMap"() {
        expect:
        assert fields.toIntToStringMap() == [35: "D", 11: "ABC", 55: "AUD/USD"]
        assert emptyFields.toIntToStringMap() == Collections.emptyMap()
    }

    def "ToIntToStringMap_duplicateTags"() {
        when:
        fieldsWithDupTag.toIntToStringMap()

        then:
        def e = thrown(UnsupportedOperationException)
        assert e.message == "Repeating tags have been detected in these fields, therefore it is not possible to return an intToStringMap"
    }

    def "ToDelimitedString"() {
        expect:
        assert fields.toDelimitedString(";") == "35=D;11=ABC;55=AUD/USD"
        assert fieldsWithDupTag.toDelimitedString(";") == "35=D;11=ABC;55=AUD/USD;55=ABC/DEF"
        assert emptyFields.toDelimitedString(";") == ""
    }

    def "GetPipeDelimitedString"() {
        expect:
        assert fields.getPipeDelimitedString() == "35=D|11=ABC|55=AUD/USD"
        assert fieldsWithDupTag.getPipeDelimitedString() == "35=D|11=ABC|55=AUD/USD|55=ABC/DEF"
        assert emptyFields.getPipeDelimitedString() == ""
    }

    def "ToConsoleText"() {
        expect:
        assert fields.toConsoleText() == "35=D|11=ABC|55=AUD/USD"
        assert fieldsWithDupTag.toConsoleText() == "35=D|11=ABC|55=AUD/USD|55=ABC/DEF"
        assert emptyFields.toConsoleText() == ""
    }

    def "ToHtml"() {
        expect:
        assert fields.toHtml() ==
            "<div class='fields'>" +
                "<span class='field'>" +
                    "<span class='tag rawTag'>35</span>" +
                    "<span class='equals'>=</span>" +
                    "<span class='value rawValue'>D</span>" +
                "</span>" +
                "<span class='field'>" +
                    "<span class='tag rawTag'>11</span>" +
                    "<span class='equals'>=</span>" +
                    "<span class='value rawValue'>ABC</span>" +
                "</span>" +
                "<span class='field'>" +
                    "<span class='tag rawTag'>55</span>" +
                    "<span class='equals'>=</span>" +
                    "<span class='value rawValue'>AUD/USD</span>" +
                "</span>" +
            "</div>"

        assert fieldsWithDupTag.toHtml() ==
            "<div class='fields'>" +
                "<span class='field'>" +
                    "<span class='tag rawTag'>35</span>" +
                    "<span class='equals'>=</span>" +
                    "<span class='value rawValue'>D</span>" +
                "</span>" +
                "<span class='field'>" +
                    "<span class='tag rawTag'>11</span>" +
                    "<span class='equals'>=</span>" +
                    "<span class='value rawValue'>ABC</span>" +
                "</span>" +
                "<span class='field'>" +
                    "<span class='tag rawTag'>55</span>" +
                    "<span class='equals'>=</span>" +
                    "<span class='value rawValue'>AUD/USD</span>" +
                "</span>" +
                "<span class='field'>" +
                    "<span class='tag rawTag'>55</span>" +
                    "<span class='equals'>=</span>" +
                    "<span class='value rawValue'>ABC/DEF</span>" +
                "</span>" +
            "</div>"

        assert emptyFields.toHtml() == "<div class='fields'></div>"
    }

    def "GetMsgTypeCode"() {
        expect:
        assert fields.getMsgTypeCode() == "D"
    }

    def "GetMsgTypeAndExecTypeKey"() {
        expect:
        assert new FieldsFromDelimitedString("35=8${a}150=B", a).fields.getMsgTypeAndExecTypeKey() == "8.B"
    }
}
