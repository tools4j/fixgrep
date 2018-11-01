package org.tools4j.fixgrep.orders

import org.tools4j.fix.ClasspathResource
import org.tools4j.fixgrep.TestFixGrep
import org.tools4j.fixgrep.config.ConfigAndArguments
import org.tools4j.fixgrep.main.FixGrep
import org.tools4j.fixgrep.TestConfigBuilder
import org.tools4j.properties.Config
import org.tools4j.properties.ConfigImpl
import spock.lang.Specification

/**
 * User: ben
 * Date: 12/03/2018
 * Time: 6:55 AM
 */
class FixGrepOnLargeFileTest extends Specification {
    def 'run fixgrep file test'(){
        given:
        final List<String> args = ['--input-line-format', '^(\\d{4}-[01]\\d-[0-3]\\d[T\\s][0-2]\\d:[0-5]\\d:[0-5]\\d[\\.,]\\d+)?.*?RawFix:(\\d+=.*$)',
                            '--output-format-horizontal-console', '$1 ${senderToTargetCompIdDirection} ${msgColor}[${msgTypeName}]${colorReset} ${msgFix}',
                            '--piped-input', 'true',
                            '--group-by-order', 'true']

        when:
        final File actualOutputFile = new File("fixgrep-file-test-output.log")
        if(actualOutputFile.exists()) actualOutputFile.delete()
        final OutputStream outputStream = new FileOutputStream(actualOutputFile);
        new FixGrep(args, this.class.getResourceAsStream('/test.log'), outputStream).go()
        final expectedOutputFile = new ClasspathResource("/test-orders-expected-output.log").asBufferedReader()

        then:
        assert assertTwoFilesAreEqual(actualOutputFile, expectedOutputFile)

        cleanup:
        if(actualOutputFile != null && actualOutputFile.exists()) actualOutputFile.delete()
    }

    boolean assertTwoFilesAreEqual(final File actualFile, final BufferedReader expected) {
        final BufferedReader actual = actualFile.newReader()

        while(true){
            final String actualLine = actual.readLine()
            final String expectedLine = expected.readLine()
            if(actualLine == null){
                println "Reached end of actual file"
            }
            if(expectedLine == null){
                println "Reached end of expected file"
            }
            if(actualLine == null || expectedLine == null) return true
            else {
                assert actualLine == expectedLine
            }
        }
    }
}
