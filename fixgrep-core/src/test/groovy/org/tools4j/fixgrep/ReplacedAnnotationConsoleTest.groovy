package org.tools4j.fixgrep

import org.tools4j.fix.Ascii1Char
import org.tools4j.util.CircularBufferedReaderWriter
import org.tools4j.utils.ArgsAsString
import spock.lang.Shared
import spock.lang.Specification

/**
 * User: ben
 * Date: 12/03/2018
 * Time: 6:55 AM
 */
class ReplacedAnnotationConsoleTest extends Specification {
    @Shared private final static String a = new Ascii1Char().toString()
    @Shared private String testOverrides;

    private static File newAssertionsFile = new File("new-assertions.txt")
    private static File resultsFile = new File("results.txt")
    private static boolean logResultsToFile = false;
    private static boolean logNewAssertionsToFile = false;

    def setupSpec() {
        if(logNewAssertionsToFile) deleteAndCreateNewFile(newAssertionsFile)
        if(logResultsToFile) deleteAndCreateNewFile(resultsFile)
        testOverrides = ' -V -p'
    }

    def 'test vertical non-aligned format'(){
        when:
        def lines = parseToLines('-a rr', VerticalTestUtil.PRICES_FIX)

        then:
        assert lines == """================================================================================
\u001B[33mMarketDataIncrementalRefresh\u001B[0m
================================================================================
MsgType=MARKETDATAINCREMENTALREFRESH
MDReqID=ABCD
NoMDEntries=4
    1.  MDUpdateAction=NEW
        MDEntryType=BID
        9999=unknownField
        Symbol=AUD/USD
        MDEntryPx=1.12345
        NoPartyIDs=2
            1.  PartyID=Ben
                9999=unknownField
                PartyIDSource=AUSTRALIAN_TAX_FILE_NUMBER
            2.  PartyID=Andy
    2.  MDUpdateAction=NEW
        MDEntryType=OFFER
        NoPartyIDs=2
            1.  PartyID=Amy
                PartyIDSource=AUSTRALIAN_TAX_FILE_NUMBER
            2.  PartyID=Milly
        Symbol=AUD/USD
        MDEntryPx=1.12355
    3.  MDUpdateAction=NEW
        MDEntryType=OFFER
        NoPartyIDs=2
            1.  PartyID=Amy
                PartyIDSource=AUSTRALIAN_TAX_FILE_NUMBER
            2.  PartyID=Milly
                9999=unknownField
        Symbol=AUD/USD
        MDEntryPx=1.12355
    4.  MDUpdateAction=NEW
        MDEntryType=BID
        Symbol=AUD/USD
        MDEntryPx=1.12335
        NoPartyIDs=2
            1.  PartyID=Amy
                PartyIDSource=AUSTRALIAN_TAX_FILE_NUMBER
            2.  PartyID=Milly
NoRoutingIDs=2
    1.  RoutingType=BLOCK_FIRM
        RoutingID=routingId1
    2.  RoutingType=TARGET_LIST
        RoutingID=routingId2
MDFeedType=asdf
"""
    }

    def 'test vertical aligned format - highlighted message'(){
        when:
        final String fix = "35=D${a}11=ABC${a}55=AUD/USD\n35=8${a}150=F${a}55=AUD/USD"
        def lines = parseToLines('-a rr -V -A -h 35=D:Msg', fix)

        then:
        assert lines == """================================================================================
\u001B[36mNewOrderSingle\u001B[0m
================================================================================
\u001B[31m MsgType = NEWORDERSINGLE \u001B[0m
\u001B[31m ClOrdID = ABC            \u001B[0m
\u001B[31m  Symbol = AUD/USD        \u001B[0m


================================================================================
\u001B[32mExec.Trade\u001B[0m
================================================================================
  MsgType = EXECUTIONREPORT            
 ExecType = TRADE_PARTIAL_FILL_OR_FILL 
   Symbol = AUD/USD                    

"""
    }

    def 'test vertical non-aligned format - indentGroupRepeats - prices - excluding some fields'(){
        when:
        def lines = parseToLines('-e 268,448,55,215,217,270 -a r -q', VerticalTestUtil.PRICES_FIX)

        then:
        assert lines == """================================================================================
\u001B[33mMarketDataIncrementalRefresh\u001B[0m
================================================================================
MsgType=MARKETDATAINCREMENTALREFRESH
MDReqID=ABCD
    1.  MDUpdateAction=NEW
        MDEntryType=BID
        9999=unknownField
        NoPartyIDs=2
            1.  9999=unknownField
                PartyIDSource=AUSTRALIAN_TAX_FILE_NUMBER
    2.  MDUpdateAction=NEW
        MDEntryType=OFFER
        NoPartyIDs=2
            1.  PartyIDSource=AUSTRALIAN_TAX_FILE_NUMBER
    3.  MDUpdateAction=NEW
        MDEntryType=OFFER
        NoPartyIDs=2
            1.  PartyIDSource=AUSTRALIAN_TAX_FILE_NUMBER
            2.  9999=unknownField
    4.  MDUpdateAction=NEW
        MDEntryType=BID
        NoPartyIDs=2
            1.  PartyIDSource=AUSTRALIAN_TAX_FILE_NUMBER
    1.  RoutingType=BLOCK_FIRM
    2.  RoutingType=TARGET_LIST
MDFeedType=asdf
"""
    }


    private String parseToLines(String args, final String fix){
        args = args + testOverrides
        final List<String> argsList = new ArgsAsString(args).toArgs()

        final CircularBufferedReaderWriter input = new CircularBufferedReaderWriter();
        final CircularBufferedReaderWriter output = new CircularBufferedReaderWriter();

        input.writer.write(fix)
        input.writer.flush()
        input.writer.close()

        new FixGrepMain(input.inputStream, output.outputStream, argsList).go()

        output.outputStream.flush()
        String lines = output.readLines('\n')

        if(logNewAssertionsToFile) {
            def testCriteriaIfActualIsCorrect = ("'" + args + "'").padRight(35) + "| '" + lines.replace("\n", "\\" + "n").replace('\u001b', '\\' + 'u001b') + "'"
            newAssertionsFile.append(testCriteriaIfActualIsCorrect + '\n')
        }
        if(logResultsToFile) {
            def testCriteriaIfActualIsCorrect = ("'" + args + "'").padRight(35) + "| '" + lines.replace("\n", "\\" + "n").replace('\u001b', '\\' + 'u001b') + "'"
            resultsFile.append(args)
            resultsFile.append('\n')
            resultsFile.append(lines)
            resultsFile.append('\n')
            resultsFile.append('\n')
        }
        println lines
        return lines
    }

    protected void deleteAndCreateNewFile(final File file) {
        if (file) {
            if (file.exists()) {
                file.delete()
            }
            file.createNewFile()
        }
    }
}
