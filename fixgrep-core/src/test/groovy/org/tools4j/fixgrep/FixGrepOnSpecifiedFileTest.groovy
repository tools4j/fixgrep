package org.tools4j.fixgrep

import org.tools4j.fixgrep.config.ConfigAndArguments
import org.tools4j.fixgrep.main.FixGrep
import org.tools4j.properties.Config
import org.tools4j.properties.ConfigImpl
import org.tools4j.util.CircularBufferedReaderWriter
import spock.lang.Specification

/**
 * User: ben
 * Date: 12/03/2018
 * Time: 6:55 AM
 */
class FixGrepOnSpecifiedFileTest extends Specification {
    def 'run fixgrep file test'(){
        given:
        String pathPrefix = new File(System.getProperty('user.dir')).name == 'fixgrep-core' ? '': 'fixgrep-core/'

        println '1.' + new File(System.getProperty('user.dir')).parentFile.name
        println '2.' + (new File(System.getProperty('user.dir')).parentFile.name == 'fixgrep-core')

        Config testConfig = TestConfigBuilder.load().overrideWith(new ConfigImpl(["suppress.colors": "true", "suppress.bold.tags.and.values": "true"]))
        ConfigAndArguments configAndArguments = new ConfigAndArguments(testConfig, Arrays.asList("${pathPrefix}src/test/resources/small-log1.log"))

        when:
        final CircularBufferedReaderWriter output = new CircularBufferedReaderWriter();
        new FixGrep(null, output.outputStream, configAndArguments).go()
        output.outputStream.flush()
        String lines = output.readLines('\n')

        then:
        assert lines == "[MsgType]35=D[NEWORDERSINGLE]|[SenderCompID]49=CLIENT_SIM|[TargetCompID]56=ACME_EXCHANGE|[ClOrdID]11=C1|[Symbol]55=AUD/USD|[Side]54=1[BUY]|[TransactTime]60=20180312-17:37:29.302|[OrderQty]38=545710|[OrdType]40=2[LIMIT]|[Price]44=99.99279924932014\n" +
                        "[MsgType]35=8[EXECUTIONREPORT]|[SenderCompID]49=ACME_EXCHANGE|[TargetCompID]56=CLIENT_SIM|[OrderID]37=O1|[ClOrdID]11=C1|[OrigClOrdID]41=C1|[Symbol]55=AUD/USD|[Side]54=1[BUY]|[TransactTime]60=20180312-17:37:29.302|[ExecID]17=O1_1|[ExecType]150=0[NEW]|[OrdStatus]39=A[PENDING_NEW]|[LeavesQty]151=545710|[CumQty]14=0|[Price]44=99.99279924932014"
    }

    def 'run fixgrep multiple file test'(){
        given:
        String pathPrefix = new File(System.getProperty('user.dir')).name == 'fixgrep-core' ? '': 'fixgrep-core/'
        Config testConfig = TestConfigBuilder.load().overrideWith(new ConfigImpl(["suppress.colors": "true", "suppress.bold.tags.and.values": "true"]))
        ConfigAndArguments configAndArguments = new ConfigAndArguments(testConfig, Arrays.asList("", "${pathPrefix}src/test/resources/small-log1.log", "${pathPrefix}src/test/resources/small-log2.log"))

        when:
        final CircularBufferedReaderWriter output = new CircularBufferedReaderWriter();
        new FixGrep(null, output.outputStream, configAndArguments).go()
        output.outputStream.flush()
        String lines = output.readLines('\n')

        then:
        assert lines == "[MsgType]35=D[NEWORDERSINGLE]|[SenderCompID]49=CLIENT_SIM|[TargetCompID]56=ACME_EXCHANGE|[ClOrdID]11=C1|[Symbol]55=AUD/USD|[Side]54=1[BUY]|[TransactTime]60=20180312-17:37:29.302|[OrderQty]38=545710|[OrdType]40=2[LIMIT]|[Price]44=99.99279924932014\n" +
                "[MsgType]35=8[EXECUTIONREPORT]|[SenderCompID]49=ACME_EXCHANGE|[TargetCompID]56=CLIENT_SIM|[OrderID]37=O1|[ClOrdID]11=C1|[OrigClOrdID]41=C1|[Symbol]55=AUD/USD|[Side]54=1[BUY]|[TransactTime]60=20180312-17:37:29.302|[ExecID]17=O1_1|[ExecType]150=0[NEW]|[OrdStatus]39=A[PENDING_NEW]|[LeavesQty]151=545710|[CumQty]14=0|[Price]44=99.99279924932014\n" +
                "[MsgType]35=D[NEWORDERSINGLE]|[SenderCompID]49=CLIENT_SIM|[TargetCompID]56=ACME_EXCHANGE|[ClOrdID]11=C2|[Symbol]55=AUD/USD|[Side]54=2[SELL]|[TransactTime]60=20180312-17:37:30.201|[OrderQty]38=342180|[OrdType]40=2[LIMIT]|[Price]44=99.97800707655466\n" +
                "[MsgType]35=8[EXECUTIONREPORT]|[SenderCompID]49=ACME_EXCHANGE|[TargetCompID]56=CLIENT_SIM|[OrderID]37=O2|[ClOrdID]11=C2|[OrigClOrdID]41=C2|[Symbol]55=AUD/USD|[Side]54=2[SELL]|[TransactTime]60=20180312-17:37:30.201|[ExecID]17=O2_1|[ExecType]150=0[NEW]|[OrdStatus]39=A[PENDING_NEW]|[LeavesQty]151=342180|[CumQty]14=0|[Price]44=99.97800707655466"
    }
}
