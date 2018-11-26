package org.tools4j.fixgrep


import org.tools4j.fix.Ascii1Char
import org.tools4j.fixgrep.main.FixGrep
import spock.lang.Shared
import spock.lang.Specification

/**
 * User: ben
 * Date: 29/05/2018
 * Time: 9:27 AM
 */
class OutputToFileTest extends Specification {
    @Shared private final static String a = new Ascii1Char().toString()

    def 'read from file, output to file'() {
        when:
        final File inputFile = new File("test-input.log")
        inputFile.text = "35=D${a}11=ABC${a}55=AUD/USD\n35=8${a}150=F${a}55=AUD/USD"
        new FixGrep(Arrays.asList("-VA", "--to-given-file", "test-output.log", "test-input.log")).go()
        final File outputFile = new File("test-output.log")

        then:
        assert outputFile.exists()
        assert outputFile.text == """================================================================================
\u001B[36mNewOrderSingle\u001B[0m
================================================================================
 [MsgType]\u001B[1m35\u001B[22m \u001B[1m=\u001B[22m \u001B[1mD\u001B[22m[NEWORDERSINGLE] 
 [ClOrdID]\u001B[1m11\u001B[22m \u001B[1m=\u001B[22m \u001B[1mABC\u001B[22m               
  [Symbol]\u001B[1m55\u001B[22m \u001B[1m=\u001B[22m \u001B[1mAUD/USD\u001B[22m           


================================================================================
\u001B[32mExec.Trade\u001B[0m
================================================================================
   [MsgType]\u001B[1m35\u001B[22m \u001B[1m=\u001B[22m \u001B[1m8\u001B[22m[EXECUTIONREPORT]            
 [ExecType]\u001B[1m150\u001B[22m \u001B[1m=\u001B[22m \u001B[1mF\u001B[22m[TRADE_PARTIAL_FILL_OR_FILL] 
    [Symbol]\u001B[1m55\u001B[22m \u001B[1m=\u001B[22m \u001B[1mAUD/USD\u001B[22m                       


"""
        if(inputFile.exists()){assert inputFile.delete()}
        if(outputFile.exists()){assert outputFile.delete()}
    }

    def "Write to random file"() {
        given:
        //Hijack system.out
        final ByteArrayOutputStream sysOutBuffer = new ByteArrayOutputStream()
        final OutputStream originalSystemOut = System.out
        System.out = new PrintStream(sysOutBuffer)
        final File inputFile = new File("test-input.log")
        inputFile.text = "35=D${a}11=ABC${a}55=AUD/USD\n35=8${a}150=F${a}55=AUD/USD"

        when:
        new FixGrep(Arrays.asList("-VA", "--to-file", "test-input.log")).go()
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
        assert outputFile.text == """================================================================================
\u001B[36mNewOrderSingle\u001B[0m
================================================================================
 [MsgType]\u001B[1m35\u001B[22m \u001B[1m=\u001B[22m \u001B[1mD\u001B[22m[NEWORDERSINGLE] 
 [ClOrdID]\u001B[1m11\u001B[22m \u001B[1m=\u001B[22m \u001B[1mABC\u001B[22m               
  [Symbol]\u001B[1m55\u001B[22m \u001B[1m=\u001B[22m \u001B[1mAUD/USD\u001B[22m           


================================================================================
\u001B[32mExec.Trade\u001B[0m
================================================================================
   [MsgType]\u001B[1m35\u001B[22m \u001B[1m=\u001B[22m \u001B[1m8\u001B[22m[EXECUTIONREPORT]            
 [ExecType]\u001B[1m150\u001B[22m \u001B[1m=\u001B[22m \u001B[1mF\u001B[22m[TRADE_PARTIAL_FILL_OR_FILL] 
    [Symbol]\u001B[1m55\u001B[22m \u001B[1m=\u001B[22m \u001B[1mAUD/USD\u001B[22m                       


"""
        assert outputFile.delete()

        cleanup:
        System.out = originalSystemOut
    }
}
