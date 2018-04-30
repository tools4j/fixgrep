package org.tools4j.fixgrep

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import spock.lang.Specification

/**
 * User: ben
 * Date: 12/03/2018
 * Time: 6:55 AM
 */
class FixGrepOnFileTest extends Specification {
    def 'run fixgrep file test'(){
        given:
        Config testSpecificConfig = ConfigFactory.parseMap(['line.regex': '^(\\d{4}-[01]\\d-[0-3]\\d[T\\s][0-2]\\d:[0-5]\\d:[0-5]\\d[\\.,]\\d+)?.*?RawFix:(\\d+=.*$)'])
        Config testConfig = testSpecificConfig.withFallback(ConfigFactory.load())

        when:
        final File actualOutputFile = new File("fixgrep-file-test-output.log")
        if(actualOutputFile.exists()) actualOutputFile.delete()
        final OutputStream outputStream = new FileOutputStream(actualOutputFile);
        new FixGrep(this.class.getResourceAsStream('/test.log'), outputStream, testConfig).go()
        final expectedOutputFile = new File("fixgrep-core/src/test/resources/test-expected-output.log")

        then:
        assert assertTwoFilesAreEqual(actualOutputFile, expectedOutputFile)
    }

    boolean assertTwoFilesAreEqual(final File actualFile, final File expectedFile) {
        final BufferedReader actual = actualFile.newReader()
        final BufferedReader expected = expectedFile.newReader()

        int i = 1
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
                assert actualLine == expectedLine//: "Asserting line number: " + (i++)
            }
        }
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