package org.tools4j.fix

import spock.lang.Specification
import spock.lang.Unroll

/**
 * User: ben
 * Date: 11/04/2018
 * Time: 6:15 PM
 */
class AnnotatedFieldsTest extends Specification {
    @Unroll
    def "test #spec"(final String spec, final String expectedAnnotatedOutput) {
        given:
        final Field field1 = new FieldImpl(new SpecTag(35, "MsgType"), new EnumValue("8", "ExecReport"))
        final Field field2 = new FieldImpl(new SpecTag(150, "ExecType"), new EnumValue("B", "Blah"))
        final Fields fields = new FieldsImpl(Arrays.asList(field1, field2))

        when:
        final AnnotatedFields annotatedFields = new AnnotatedFields(fields, spec)

        then:
        assert annotatedFields.fields.toPrettyString() == expectedAnnotatedOutput

        where:
        spec               | expectedAnnotatedOutput
        'none'             | '35=8|150=B'
        '__'               | '35=8|150=B'
        'outsideAnnotated' | '[MsgType]35=8[ExecReport]|[ExecType]150=B[Blah]'
        'insideAnnotated'  | '35[MsgType]=[ExecReport]8|150[ExecType]=[Blah]B'
        'a_'               | '35[MsgType]=8|150[ExecType]=B'
        '_a'               | '35=8[ExecReport]|150=B[Blah]'
    }
}
