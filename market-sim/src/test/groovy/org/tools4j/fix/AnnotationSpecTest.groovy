package org.tools4j.fix

import spock.lang.Specification
import spock.lang.Unroll

/**
 * User: ben
 * Date: 11/04/2018
 * Time: 6:03 PM
 */
class AnnotationSpecTest extends Specification {
    @Unroll
    def "test #spec"(final String spec, final String expectedAnnotatedOutput) {
        given:
        final Field field = new FieldImpl(new AnnotatedTag(35, "MsgType"), new AnnotatedValue("8", "ExecReport"))

        when:
        final AnnotatedField annotatedField = new AnnotatedField(field, AnnotationSpec.parse(spec))

        then:
        assert annotatedField.toPrettyString() == expectedAnnotatedOutput

        where:
        spec               | expectedAnnotatedOutput
        'none'             | '35=8'
        '__'               | '35=8'
        'outsideAnnotated' | '[MsgType]35=8[ExecReport]'
        'ba'               | '[MsgType]35=8[ExecReport]'
        'insideAnnotated'  | '35[MsgType]=[ExecReport]8'
        'ab'               | '35[MsgType]=[ExecReport]8'
        'aa'               | '35[MsgType]=8[ExecReport]'
        'a_'               | '35[MsgType]=8'
        '_a'               | '35=8[ExecReport]'
        'bb'               | '[MsgType]35=[ExecReport]8'
        'b_'               | '[MsgType]35=8'
        '_b'               | '35=[ExecReport]8'
    }
}
