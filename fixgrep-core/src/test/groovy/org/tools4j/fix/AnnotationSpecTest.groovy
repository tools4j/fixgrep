package org.tools4j.fix

import org.tools4j.fix.spec.FixSpecDefinition
import org.tools4j.fix.spec.FixSpecParser
import org.tools4j.fixgrep.formatting.FormattingContext
import org.tools4j.fixgrep.formatting.HorizontalConsoleMsgFormatter
import org.tools4j.fixgrep.formatting.MsgFormatter
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

/**
 * User: ben
 * Date: 11/04/2018
 * Time: 6:03 PM
 */
class AnnotationSpecTest extends Specification {
    @Shared FixSpecDefinition fixSpec;

    def setup(){
        fixSpec = new FixSpecParser().parseSpec()
    }

    @Unroll
    def "test #spec"(final String spec, final String expectedAnnotatedOutput) {
        given:
        final Field field = new FieldImpl(new RawTag(35), new RawValue("8"))

        when:
        final Field annotatedField = new FieldAnnotator(fixSpec).getField(field)
        final Fields annotatedFields = new FieldsImpl(Arrays.asList(annotatedField));

        then:
        final MsgFormatter msgFormatter = new HorizontalConsoleMsgFormatter(new FormattingContext(annotatedFields, AnnotationPositions.parse(spec), false), "|")
        assert msgFormatter.format() == expectedAnnotatedOutput

        where:
        spec               | expectedAnnotatedOutput
        'none'             | '35=8'
        '__'               | '35=8'
        '_'                | '35=8'
        'outsideAnnotated' | '[MsgType]35=8[EXECUTIONREPORT]'
        'ba'               | '[MsgType]35=8[EXECUTIONREPORT]'
        'insideAnnotated'  | '35[MsgType]=[EXECUTIONREPORT]8'
        'ab'               | '35[MsgType]=[EXECUTIONREPORT]8'
        'aa'               | '35[MsgType]=8[EXECUTIONREPORT]'
        'a'                | '35[MsgType]=8[EXECUTIONREPORT]'
        'a_'               | '35[MsgType]=8'
        '_a'               | '35=8[EXECUTIONREPORT]'
        'bb'               | '[MsgType]35=[EXECUTIONREPORT]8'
        'b'                | '[MsgType]35=[EXECUTIONREPORT]8'
        'b_'               | '[MsgType]35=8'
        '_b'               | '35=[EXECUTIONREPORT]8'
        'rr'               | 'MsgType=EXECUTIONREPORT'
        'r'                | 'MsgType=EXECUTIONREPORT'
    }
}
