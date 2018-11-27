package org.tools4j.fixgrep.help

import org.tools4j.fixgrep.utils.WrappedFixGrep
import org.tools4j.fixgrep.main.FixGrep
import spock.lang.Ignore
import spock.lang.Specification
import spock.lang.Unroll

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
        final WrappedFixGrep fixgrep = new WrappedFixGrep("--man")

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
        System.out = originalSystemOut
        println output

        then:
        assert outputFilePath != null
        final File outputFile = new File(outputFilePath)
        assert outputFile != null
        assert outputFile.exists()
        assert outputFile.text =~ "WHAT IS FIXGREP"
        assert outputFile.delete()
    }

    @Unroll
    def "Generate html man file, arg [#arg]"(final String arg, final String desc) {
        when:
        new FixGrep(Arrays.asList("--man", arg, "--to-given-file", "myman.html")).go()
        final File outputFile = new File("myman.html")

        then:
        assert outputFile.exists()
        assert outputFile.text =~ "<html>"
        assert outputFile.text =~ "What is fixgrep"
        assert outputFile.text =~ "<div class='fields'><span class='field"
        assert outputFile.delete()

        where:
        arg           | desc
        "--html"      | "With html flag, because we are printing out man, should still appear in full html page"
        "--html-page" | "With full page specified"
    }

    @Unroll
    def "Generate html man to output stream, arg [#arg]"(final String arg, final String desc) {
        when:
        final WrappedFixGrep fixgrep = new WrappedFixGrep("--man $arg")
        final String output = fixgrep.go()

        then:
        assert output =~ "<html>"
        assert output =~ "What is fixgrep"
        assert output =~ "<div class='fields'><span class='field"

        where:
        arg           | desc
        "--html"      | "With html flag, because we are printing out man, should still appear in full html page"
        "--html-page" | "With full page specified"
    }

    @Unroll
    def "Generate html man to random file, arg [#arg]"(final String arg, final String desc) {
        given:
        //Hijack system.out
        final ByteArrayOutputStream sysOutBuffer = new ByteArrayOutputStream()
        final OutputStream originalSystemOut = System.out
        System.out = new PrintStream(sysOutBuffer)

        when:
        new FixGrep(Arrays.asList("--man", "--html", "--to-file")).go()
        final String output = sysOutBuffer.toString()
        String outputFilePath = null
        output.eachLine {
            if (it =~ /Output written to:/) {
                outputFilePath = it.replaceAll(/Output written to: ([^\s]+.html)/, "\$1")
            }
        }
        System.out = originalSystemOut
        println output

        then:
        assert outputFilePath != null
        final File outputFile = new File(outputFilePath)
        assert outputFile.exists()
        assert outputFile.text =~ "<html>"
        assert outputFile.text =~ "What is fixgrep"
        assert outputFile.text =~ "<div class='fields'><span class='field"
        assert outputFile.delete()

        where:
        arg           | desc
        "--html"      | "With html flag, because we are printing out man, should still appear in full html page"
        "--html-page" | "With full page specified"
    }

    @Ignore
    @Unroll
    def "Generate html man to file and launch browser - for manual testing purposes"() {
        when:
        new FixGrep(Arrays.asList("--man", "--html", "-l")).go()

        then:
        assert 1 == (1+1)
    }
}
