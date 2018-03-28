package org.tools4j.fixgrep

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import spock.lang.Specification

/**
 * User: ben
 * Date: 12/03/2018
 * Time: 6:55 AM
 */
class FixGrepTest extends Specification {
    def 'run fixgrep'(){
        given:
        Config testSpecificConfig = ConfigFactory.parseMap(['fixgrep.log.line.regex': '^(\\d{4}-[01]\\d-[0-3]\\d[T\\s][0-2]\\d:[0-5]\\d:[0-5]\\d[\\.,]\\d+)?.*?RawFix:(\\d+=.*$)'])
        Config testConfig = testSpecificConfig.withFallback(ConfigFactory.load())

        when:
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream()
        new FixGrep(this.class.getResourceAsStream('/test.log'), outputStream, testConfig).go()
        final String actualOutput = outputStream.toString()
        final fileContainingExpectedOutput = new File("src/test/resources/test-expected-output.log")
        final String expectedOutput = fileContainingExpectedOutput.text
        println actualOutput.toString()

        then:
        assert actualOutput == expectedOutput
    }

    //Use this when the output changes, and need to change the expected output
    private OutputStream getOutputStreamToWriteToFile() {
        final File outputFile = new File('test-expected-output.log')
        if(outputFile.exists()){
            outputFile.delete()
        }
        outputFile.createNewFile()
        return new FileOutputStream(outputFile);
    }
}
