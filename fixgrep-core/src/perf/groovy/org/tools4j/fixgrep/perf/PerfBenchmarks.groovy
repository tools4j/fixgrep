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
    def 'run fixgrep file test [#args]'(final List<String> args, final int expectedLineCount, final long expectedMaxMillis){
        given:
        args.addAll("--input-line-format", 'RawFix:(\\d+=.*$)')
        args.addAll("--line-regexgroup-for-fix", '1')

        String testLogLocation = "src/perf/resources/test.log"
        if(!new File(testLogLocation).exists()){
            testLogLocation = "fixgrep-core/$testLogLocation"
        }
        if(!new File(testLogLocation).exists()){
            throw new IllegalStateException("Could not find test.log at $testLogLocation")
        }

        args.add(testLogLocation)

        when:
        final CircularBufferedReaderWriter output = new CircularBufferedReaderWriter();

        final Future<Integer> future = Executors.newSingleThreadExecutor().submit(new Callable<Integer>(){
            @Override
            public Integer call() {
                long startTime = System.currentTimeMillis()
                new FixGrep(args, output.outputStream).go()
                long endTime = System.currentTimeMillis()
                long durationMs = endTime - startTime
                output.outputStream.flush()
                return durationMs
            }
        })
        List<String> lines = output.readLines()
        final Integer durationInMs = future.get()
        reportFile.append(args.toString() + "\t" + durationInMs + "ms\n");

        then:
        assert lines.size() == expectedLineCount
        assert durationInMs <= expectedMaxMillis

        where:
        args                                   | expectedLineCount | expectedMaxMillis
        []                                     | 373               | 10000 //warmup
        []                                     | 373               | 10000 //warmup
        []                                     | 373               | 10000 //warmup
        []                                     | 373               | 400
        ["--suppress-bold-tags-and-values"]    | 373               | 400
        ["--suppress-colors"]                  | 373               | 400
        ["--highlights","35"]                  | 373               | 400
        ["--sort-by-tags","35,55"]             | 373               | 400
        ["--only-include-tags","35,55"]        | 373               | 400
        ["--exclude-tags","35"]                | 373               | 400
        ["--tag-annotations","__"]             | 373               | 400
        ["--tag-annotations","ab"]             | 373               | 400
        ["--include-only-messages-of-type","8"]| 258               | 400
        ["--exclude-messages-of-type","8"]     | 115               | 400
    }
}
