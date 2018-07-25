package org.tools4j.fix

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

/**
 * User: ben
 * Date: 15/05/2018
 * Time: 5:50 AM
 */
class AnnotatedFieldTest extends Specification {
    @Shared Fields fields = new FieldsFromDelimitedString("35=D|11=ABC|55=AUD/USD","|", "|").fields
    @Shared FixSpec fixSpec = new Fix50SP2FixSpecFromClassPath().spec

    @Unroll
    def "ToConsoleText #annotationPositions:#bold"(AnnotationPositions annotationPositions, boolean bold, String expectedConsoleText) {
        when:
        final Fields annotatedFields = new FieldsAnnotator(fields, fixSpec, annotationPositions).fields
        def actualConsoleText = annotatedFields.toConsoleText()
        println actualConsoleText

        then:
        assert actualConsoleText == expectedConsoleText

        where:
        annotationPositions                     | bold      | expectedConsoleText
        AnnotationPositions.OUTSIDE_ANNOTATED   | true      | "[MsgType][1m35[22m[1m=[22m[1mD[22m[NEWORDERSINGLE]|[ClOrdID][1m11[22m[1m=[22m[1mABC[0m|[Symbol][1m55[22m[1m=[22m[1mAUD/USD[0m"
        AnnotationPositions.LEFT_ANNOTATED      | true      | "[MsgType][1m35[22m[1m=[22m[NEWORDERSINGLE][1mD[22m|[ClOrdID][1m11[22m[1m=[22m[1mABC[0m|[Symbol][1m55[22m[1m=[22m[1mAUD/USD[0m"
        AnnotationPositions.OUTSIDE_ANNOTATED   | false     | "[MsgType]35=D[NEWORDERSINGLE]|[ClOrdID]11=ABC|[Symbol]55=AUD/USD"
        AnnotationPositions.LEFT_ANNOTATED      | false     | "[MsgType]35=[NEWORDERSINGLE]D|[ClOrdID]11=ABC|[Symbol]55=AUD/USD"
    }

    def "ToHtml - Outside annotations - bold=true"() {
        when:
        final Fields annotatedFields = new FieldsAnnotator(fields, fixSpec, AnnotationPositions.OUTSIDE_ANNOTATED).fields
        def actualHtml = annotatedFields.toHtml()
        println actualHtml

        then:
        assert actualHtml ==
                "<span class='fields'>" +
                    "<span class='field annotatedField'>" +
                        "<span class='tag annotation'>[MsgType]</span>" +
                        "<span class='tag tagNumber bold'>35</span>" +
                        "<span class='equals bold'>=</span>" +
                        "<span class='value valueRaw bold'>D</span>" +
                        "<span class='value annotation'>[NEWORDERSINGLE]</span>" +
                    "</span>" +
                    "<span class='delim'>|</span>" +
                    "<span class='field annotatedField'>" +
                        "<span class='tag annotation'>[ClOrdID]</span>" +
                        "<span class='tag tagNumber bold'>11</span>" +
                        "<span class='equals bold'>=</span>" +
                        "<span class='value valueRaw bold'>ABC</span>" +
                    "</span>" +
                    "<span class='delim'>|</span>" +
                    "<span class='field annotatedField'>" +
                        "<span class='tag annotation'>[Symbol]</span>" +
                        "<span class='tag tagNumber bold'>55</span>" +
                        "<span class='equals bold'>=</span>" +
                        "<span class='value valueRaw bold'>AUD/USD</span>" +
                    "</span>" +
                "</span>"
    }

    def "ToHtml - Outside annotations - bold=false"() {
        when:
        final Fields annotatedFields = new FieldsAnnotator(fields, fixSpec, AnnotationPositions.OUTSIDE_ANNOTATED).fields
        def actualHtml = annotatedFields.toHtml()
        println actualHtml

        then:
        assert actualHtml ==
                "<span class='fields'>" +
                    "<span class='field annotatedField'>" +
                        "<span class='tag annotation'>[MsgType]</span>" +
                        "<span class='tag tagNumber'>35</span>" +
                        "<span class='equals'>=</span>" +
                        "<span class='value valueRaw'>D</span>" +
                        "<span class='value annotation'>[NEWORDERSINGLE]</span>" +
                    "</span>" +
                    "<span class='delim'>|</span>" +
                    "<span class='field annotatedField'>" +
                        "<span class='tag annotation'>[ClOrdID]</span>" +
                        "<span class='tag tagNumber'>11</span>" +
                        "<span class='equals'>=</span>" +
                        "<span class='value valueRaw'>ABC</span>" +
                    "</span>" +
                    "<span class='delim'>|</span>" +
                    "<span class='field annotatedField'>" +
                        "<span class='tag annotation'>[Symbol]</span>" +
                        "<span class='tag tagNumber'>55</span>" +
                        "<span class='equals'>=</span>" +
                        "<span class='value valueRaw'>AUD/USD</span>" +
                    "</span>" +
                "</span>"
    }


    def "ToHtml - Left annotations - bold=true"() {
        when:
        final Fields annotatedFields = new FieldsAnnotator(fields, fixSpec, AnnotationPositions.LEFT_ANNOTATED).fields
        def actualHtml = annotatedFields.toHtml()
        println actualHtml

        then:
        assert actualHtml ==
                "<span class='fields'>" +
                    "<span class='field annotatedField'>" +
                        "<span class='tag annotation'>[MsgType]</span>" +
                        "<span class='tag tagNumber bold'>35</span>" +
                        "<span class='equals bold'>=</span>" +
                        "<span class='value annotation'>[NEWORDERSINGLE]</span>" +
                        "<span class='value valueRaw bold'>D</span>" +
                    "</span>" +
                    "<span class='delim'>|</span>" +
                    "<span class='field annotatedField'>" +
                        "<span class='tag annotation'>[ClOrdID]</span>" +
                        "<span class='tag tagNumber bold'>11</span>" +
                        "<span class='equals bold'>=</span>" +
                        "<span class='value valueRaw bold'>ABC</span>" +
                    "</span>" +
                    "<span class='delim'>|</span>" +
                    "<span class='field annotatedField'>" +
                        "<span class='tag annotation'>[Symbol]</span>" +
                        "<span class='tag tagNumber bold'>55</span>" +
                        "<span class='equals bold'>=</span>" +
                        "<span class='value valueRaw bold'>AUD/USD</span>" +
                    "</span>" +
                "</span>"
    }



    def "ToHtml - Left annotations - bold=false"() {
        when:
        final Fields annotatedFields = new FieldsAnnotator(fields, fixSpec, AnnotationPositions.LEFT_ANNOTATED).fields
        def actualHtml = annotatedFields.toHtml()
        println actualHtml

        then:
        assert actualHtml ==
                "<span class='fields'>" +
                    "<span class='field annotatedField'>" +
                        "<span class='tag annotation'>[MsgType]</span>" +
                        "<span class='tag tagNumber'>35</span>" +
                        "<span class='equals'>=</span>" +
                        "<span class='value annotation'>[NEWORDERSINGLE]</span>" +
                        "<span class='value valueRaw'>D</span>" +
                    "</span>" +
                    "<span class='delim'>|</span>" +
                    "<span class='field annotatedField'>" +
                        "<span class='tag annotation'>[ClOrdID]</span>" +
                        "<span class='tag tagNumber'>11</span>" +
                        "<span class='equals'>=</span>" +
                        "<span class='value valueRaw'>ABC</span>" +
                    "</span>" +
                    "<span class='delim'>|</span>" +
                    "<span class='field annotatedField'>" +
                        "<span class='tag annotation'>[Symbol]</span>" +
                        "<span class='tag tagNumber'>55</span>" +
                        "<span class='equals'>=</span>" +
                        "<span class='value valueRaw'>AUD/USD</span>" +
                    "</span>" +
                "</span>"
    }
}
