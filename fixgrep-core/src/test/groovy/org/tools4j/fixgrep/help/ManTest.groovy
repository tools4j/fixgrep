package org.tools4j.fixgrep.help

import org.tools4j.fixgrep.TestFixGrep
import org.tools4j.fixgrep.main.FixGrep
import spock.lang.Specification

/**
 * User: ben
 * Date: 29/05/2018
 * Time: 9:27 AM
 */
class ManTest extends Specification {
    def "Generate man file"() {
        when:
        new FixGrep(Arrays.asList("--man", "--to-given-file", "myman.txt")).go()
        final File outputFile = new File("myman.txt")

        then:
        assert outputFile.exists()
        assert outputFile.text =~ "WHAT IS FIXGREP"
        assert outputFile.delete()
    }

    def "Generate man to output stream"() {
        when:
        final TestFixGrep fixgrep = new TestFixGrep("--man")

        then:
        assert fixgrep.go() =~ "WHAT IS FIXGREP"
    }

    def "Generate man to random file"() {
        given:
        //Hijack system.out
        final ByteArrayOutputStream sysOutBuffer = new ByteArrayOutputStream()
        final OutputStream originalSystemOut = System.out
        System.out = new PrintStream(sysOutBuffer)

        when:
        new FixGrep(Arrays.asList("--man", "--to-file")).go()
        final String output = sysOutBuffer.toString()
        String outputFilePath = null
        output.eachLine {
            if (it =~ /Output written to:/) {
                outputFilePath = it.replaceAll(/Output written to: ([^\s]+.log)/, "\$1")
            }
        }

        then:
        assert outputFilePath != null
        final File outputFile = new File(outputFilePath)
        assert outputFile.exists()
        assert outputFile.text =~ "WHAT IS FIXGREP"

        cleanup:
        assert outputFile.delete()
        System.out = originalSystemOut
        println output
    }
}
