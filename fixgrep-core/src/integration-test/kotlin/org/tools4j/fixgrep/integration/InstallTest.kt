package org.tools4j.fixgrep.integration

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.tools4j.fixgrep.help.ExampleAppPropertiesFileCreator

/**
 * User: benjw
 * Date: 02/11/2018
 * Time: 05:56
 */
class InstallTest {
    @BeforeEach
    fun setup(){
        deleteInstallFiles()
    }

    @AfterEach
    fun tearDown(){
        deleteInstallFiles()
    }

    @Test
    fun `test install - success`(){
        val bashCommand = FixGrepCommand("./fixgrep --install").go()
        assertThat(bashCommand.success).isTrue()
        assertThat(ExampleAppPropertiesFileCreator.FIXGREP_HOME_DIR.exists()).isTrue()
        assertThat(ExampleAppPropertiesFileCreator.FIXGREP_APP_PROPERTIES_FILE.readText()).contains("# output.delimiter=;")
    }

    @Test
    fun `test install - test that installed properties file is being used to configure fixgrep`(){
        var bashCommand = FixGrepCommand("./fixgrep --install").go()
        assertThat(bashCommand.success).isTrue()
        val appPropertiesText = ExampleAppPropertiesFileCreator.FIXGREP_APP_PROPERTIES_FILE.readText()
        val modifiedAppPropertiesText = appPropertiesText.replace("# output.delimiter=;", "output.delimiter=;")
        ExampleAppPropertiesFileCreator.FIXGREP_APP_PROPERTIES_FILE.writeText(modifiedAppPropertiesText)
        assertThat(ExampleAppPropertiesFileCreator.FIXGREP_APP_PROPERTIES_FILE.exists()).isTrue()

        bashCommand = FixGrepCommand("./fixgrep -q small-test.log").go()
        assertThat(bashCommand.success).isTrue()
        assertThat(bashCommand.stdOut).isEqualTo("""[MsgType]35=D[NEWORDERSINGLE];[SenderCompID]49=CLIENT_SIM;[TargetCompID]56=ACME_EXCHANGE;[ClOrdID]11=C1;[Symbol]55=AUD/USD;[Side]54=1[BUY];[TransactTime]60=20180312-17:37:29.302;[OrderQty]38=545710;[OrdType]40=2[LIMIT];[Price]44=99.99279924932014
[MsgType]35=8[EXECUTIONREPORT];[SenderCompID]49=ACME_EXCHANGE;[TargetCompID]56=CLIENT_SIM;[OrderID]37=O1;[ClOrdID]11=C1;[OrigClOrdID]41=C1;[Symbol]55=AUD/USD;[Side]54=1[BUY];[TransactTime]60=20180312-17:37:29.302;[ExecID]17=O1_1;[ExecType]150=0[NEW];[OrdStatus]39=A[PENDING_NEW];[LeavesQty]151=545710;[CumQty]14=0;[Price]44=99.99279924932014
[MsgType]35=D[NEWORDERSINGLE];[SenderCompID]49=CLIENT_SIM;[TargetCompID]56=ACME_EXCHANGE;[ClOrdID]11=C2;[Symbol]55=AUD/USD;[Side]54=2[SELL];[TransactTime]60=20180312-17:37:30.201;[OrderQty]38=342180;[OrdType]40=2[LIMIT];[Price]44=99.97800707655466
[MsgType]35=8[EXECUTIONREPORT];[SenderCompID]49=ACME_EXCHANGE;[TargetCompID]56=CLIENT_SIM;[OrderID]37=O2;[ClOrdID]11=C2;[OrigClOrdID]41=C2;[Symbol]55=AUD/USD;[Side]54=2[SELL];[TransactTime]60=20180312-17:37:30.201;[ExecID]17=O2_1;[ExecType]150=0[NEW];[OrdStatus]39=A[PENDING_NEW];[LeavesQty]151=342180;[CumQty]14=0;[Price]44=99.97800707655466
[MsgType]35=8[EXECUTIONREPORT];[SenderCompID]49=ACME_EXCHANGE;[TargetCompID]56=CLIENT_SIM;[OrderID]37=O1;[ClOrdID]11=C1;[OrigClOrdID]41=C1;[Symbol]55=AUD/USD;[Side]54=1[BUY];[TransactTime]60=20180312-17:37:30.225;[ExecID]17=O1_2;[ExecType]150=F;[OrdStatus]39=0[NEW];[LeavesQty]151=203530;[CumQty]14=342180;[LastQty]32=342180;[Price]44=99.9854031629;[Text]58=Executed
[MsgType]35=8[EXECUTIONREPORT];[SenderCompID]49=ACME_EXCHANGE;[TargetCompID]56=CLIENT_SIM;[OrderID]37=O2;[ClOrdID]11=C2;[OrigClOrdID]41=C2;[Symbol]55=AUD/USD;[Side]54=2[SELL];[TransactTime]60=20180312-17:37:30.230;[ExecID]17=O2_2;[ExecType]150=F;[OrdStatus]39=2[FILLED];[LeavesQty]151=0;[CumQty]14=342180;[LastQty]32=342180;[Price]44=99.9854031629;[Text]58=Executed
[MsgType]35=D[NEWORDERSINGLE];[SenderCompID]49=CLIENT_SIM;[TargetCompID]56=ACME_EXCHANGE;[ClOrdID]11=C3;[Symbol]55=AUD/USD;[Side]54=1[BUY];[TransactTime]60=20180312-17:37:30.240;[OrderQty]38=375330;[OrdType]40=1[MARKET];[Price]44=MARKET
[MsgType]35=8[EXECUTIONREPORT];[SenderCompID]49=ACME_EXCHANGE;[TargetCompID]56=CLIENT_SIM;[OrderID]37=O3;[ClOrdID]11=C3;[OrigClOrdID]41=C3;[Symbol]55=AUD/USD;[Side]54=1[BUY];[TransactTime]60=20180312-17:37:30.240;[ExecID]17=O3_1;[ExecType]150=0[NEW];[OrdStatus]39=A[PENDING_NEW];[LeavesQty]151=375330;[CumQty]14=0;[Price]44=MARKET
[MsgType]35=D[NEWORDERSINGLE];[SenderCompID]49=CLIENT_SIM;[TargetCompID]56=ACME_EXCHANGE;[ClOrdID]11=C4;[Symbol]55=AUD/USD;[Side]54=1[BUY];[TransactTime]60=20180312-17:37:30.282;[OrderQty]38=368520;[OrdType]40=1[MARKET];[Price]44=MARKET
[MsgType]35=8[EXECUTIONREPORT];[SenderCompID]49=ACME_EXCHANGE;[TargetCompID]56=CLIENT_SIM;[OrderID]37=O4;[ClOrdID]11=C4;[OrigClOrdID]41=C4;[Symbol]55=AUD/USD;[Side]54=1[BUY];[TransactTime]60=20180312-17:37:30.282;[ExecID]17=O4_1;[ExecType]150=0[NEW];[OrdStatus]39=A[PENDING_NEW];[LeavesQty]151=368520;[CumQty]14=0;[Price]44=MARKET
""")
    }

    private fun deleteInstallFiles() {
        if (ExampleAppPropertiesFileCreator.FIXGREP_HOME_DIR.exists()) {
            if (ExampleAppPropertiesFileCreator.FIXGREP_HOME_DIR.isDirectory) {
                assertThat(ExampleAppPropertiesFileCreator.FIXGREP_HOME_DIR.deleteRecursively()).isTrue()
            } else {
                assertThat(ExampleAppPropertiesFileCreator.FIXGREP_HOME_DIR.delete()).isTrue()
            }
        }
    }
}
