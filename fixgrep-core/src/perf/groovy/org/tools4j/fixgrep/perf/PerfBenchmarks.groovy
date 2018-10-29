package org.tools4j.fixgrep.perf

import org.tools4j.fixgrep.main.FixGrep
import org.tools4j.fixgrep.TestConfigBuilder
import org.tools4j.properties.Config
import org.tools4j.fixgrep.config.ConfigAndArguments
import org.tools4j.properties.ConfigImpl
import org.tools4j.util.CircularBufferedReaderWriter
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.Future

/**
 * User: ben
 * Date: 25/06/2018
 * Time: 6:30 AM
 */
public class PerfBenchmarks extends Specification {
    @Shared static File reportFile;

    def setupSpec(){
        final String reportFilePath = "build/reports/tests/perf/benchmarks.txt"
        reportFile = new File(reportFilePath)
        if(reportFile.exists()) reportFile.delete()
        reportFile.parentFile.mkdirs()
    }

    @Unroll
    def 'run fixgrep file test [#config]'(final Map<String, String> config, final int expectedLineCount, final long expectedMaxMillis){
        given:
        config.put("input.line.format", 'RawFix:(\\d+=.*$)')
        config.put("line.regexgroup.for.fix", '1')
        Config testConfig = TestConfigBuilder.load().overrideWith(new ConfigImpl(config))
        ConfigAndArguments configAndArguments = new ConfigAndArguments(testConfig, Arrays.asList("src/perf/resources/test.log"))

        when:
        final CircularBufferedReaderWriter output = new CircularBufferedReaderWriter();

        final Future<Integer> future = Executors.newSingleThreadExecutor().submit(new Callable<Integer>(){
            @Override
            public Integer call() {
                long startTime = System.currentTimeMillis()
                new FixGrep(null, output.outputStream, configAndArguments).go()
                long endTime = System.currentTimeMillis()
                long durationMs = endTime - startTime
                output.outputStream.flush()
                return durationMs
            }
        })
        List<String> lines = output.readLines()
        final Integer durationInMs = future.get()
        reportFile.append(config.toString() + "\t" + durationInMs + "ms\n");

        then:
        assert lines.size() == expectedLineCount
        assert durationInMs <= expectedMaxMillis

        where:
        config                                      | expectedLineCount | expectedMaxMillis
        [:]                                         | 373               | 10000 //warmup
        [:]                                         | 373               | 100
        ["suppress.bold.tags.and.values":"true"]    | 373               | 100
        ["suppress.colors":"true"]                  | 373               | 100
        ["highlights":"35"]                         | 373               | 100
        ["sort.by.tags":"35,55"]                    | 373               | 100
        ["only.include.tags":"35,55"]               | 373               | 100
        ["exclude.tags":"35"]                       | 373               | 100
        ["tag.annotations":"__"]                    | 373               | 100
        ["tag.annotations":"ab"]                    | 373               | 100
        ["include.only.messages.of.type":"8"]       | 258               | 100
        ["exclude.messages.of.type":"8"]            | 115               | 100
    }
}
