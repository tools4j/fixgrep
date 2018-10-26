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
 * Time: 6:15 PM
 */
class AnnotatedFieldsTest extends Specification {
    @Shared FixSpecDefinition fixSpec;

    def setup(){
        fixSpec = new FixSpecParser().parseSpec()
    }

    @Unroll
    def "test #spec"(final String spec, final String expectedAnnotatedOutput) {
        given:
        final Field field1 = new FieldImpl(new RawTag(35), new RawValue("8"))
        final Field field2 = new FieldImpl(new RawTag(150), new RawValue("B"))
        final Fields fields = new FieldsImpl(Arrays.asList(field1, field2))

        when:
        final Fields annotatedFields = new FieldsAnnotator(fields, fixSpec).fields

        then:
        final MsgFormatter msgFormatter = new HorizontalConsoleMsgFormatter(new FormattingContext(annotatedFields, AnnotationPositions.parse(spec), false), "|")
        assert msgFormatter.format() == expectedAnnotatedOutput

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
