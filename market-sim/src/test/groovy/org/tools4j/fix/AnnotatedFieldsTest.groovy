package org.tools4j.fix

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

/**
 * User: ben
 * Date: 11/04/2018
 * Time: 6:15 PM
 */
class AnnotatedFieldsTest extends Specification {
    @Shared FixSpec fixSpec = new Fix50SP2FixSpecFromClassPath().spec

    @Unroll
    def "test #spec"(final String spec, final String expectedAnnotatedOutput) {
        given:
        final Field field1 = new FieldImpl(new RawTag(35), new RawValue("8"))
        final Field field2 = new FieldImpl(new RawTag(150), new RawValue("B"))
        final Fields fields = new FieldsImpl(Arrays.asList(field1, field2))

        when:
        final AnnotatedFields annotatedFields = new AnnotatedFields(fields, new AnnotationSpec(AnnotationPositions.parse(spec), false), fixSpec)

        then:
        assert annotatedFields.fields.toConsoleText() == expectedAnnotatedOutput

        where:
        spec               | expectedAnnotatedOutput
        'none'             | '35=8|150=B'
        '__'               | '35=8|150=B'
        'outsideAnnotated' | '[MsgType]35=8[EXECUTIONREPORT]|[ExecType]150=B[CALCULATED]'
        'insideAnnotated'  | '35[MsgType]=[EXECUTIONREPORT]8|150[ExecType]=[CALCULATED]B'
        'a_'               | '35[MsgType]=8|150[ExecType]=B'
        '_a'               | '35=8[EXECUTIONREPORT]|150=B[CALCULATED]'
        'aa'               | '35[MsgType]=8[EXECUTIONREPORT]|150[ExecType]=B[CALCULATED]'
        'bb'               | '[MsgType]35=[EXECUTIONREPORT]8|[ExecType]150=[CALCULATED]B'
    }
}
