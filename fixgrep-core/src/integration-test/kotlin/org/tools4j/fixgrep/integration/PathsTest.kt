package org.tools4j.fixgrep.integration

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import org.tools4j.fixgrep.integration.FixGrepCommand.Companion.HELP_CONTENT

/**
 * User: benjw
 * Date: 02/11/2018
 * Time: 05:56
 */
class PathsTest {

    @TestFactory
    fun `running in fixgrep dir`(): Collection<DynamicTest> {
        return buildTests("", "./fixgrep", "")
    }

    @TestFactory
    fun `running in parent dir`(): Collection<DynamicTest> {
        return buildTests("cd .. &&","./files/fixgrep", "./files/")
    }

    @TestFactory
    fun `with fixgrep on path`(): Collection<DynamicTest> {
        return buildTests("cd .. &&", "PATH=\$PATH:./files fixgrep", "./files/")
    }

    private fun buildTests(commandPrecursor: String, command: String, pathToFixGrepDir: String): List<DynamicTest> {
        return listOf(
                DynamicTest.dynamicTest("test with file") {
                    val bashCommand = FixGrepCommand("${commandPrecursor} ${command} ${pathToFixGrepDir}small-test.log").go()
                    assertThat(bashCommand.success).isTrue()
                    assertThat(bashCommand.stdOut).isEqualTo(FixGrepCommand.SMALL_LOG_FIX_WITH_BOLD)
                },

                DynamicTest.dynamicTest("test with piped file") {
                    val bashCommand = FixGrepCommand("${commandPrecursor} cat ${pathToFixGrepDir}small-test.log | ${command}").go()
                    assertThat(bashCommand.success).isTrue()
                    assertThat(bashCommand.stdOut).isEqualTo(FixGrepCommand.SMALL_LOG_FIX_WITH_BOLD)
                },

                DynamicTest.dynamicTest("test man page") {
                    val bashCommand = FixGrepCommand("${commandPrecursor} ${command} man").go()
                    assertThat(bashCommand.success).isTrue()
                    assertThat(bashCommand.stdOut).contains("WHAT IS FIXGREP")
                },

                DynamicTest.dynamicTest("test 256-color-demo") {
                    val bashCommand = FixGrepCommand("${commandPrecursor} ${command} --256-color-demo").go()
                    assertThat(bashCommand.success).isTrue()
                    assertThat(bashCommand.stdOut).contains("Below (should) be a table of numbers with colored backgrounds")
                },

                DynamicTest.dynamicTest("test 256-color-demo") {
                    val bashCommand = FixGrepCommand("${commandPrecursor} ${command} --256-color-demo").go()
                    assertThat(bashCommand.success).isTrue()
                    assertThat(bashCommand.stdOut).contains("Below (should) be a table of numbers with colored backgrounds")
                },

                DynamicTest.dynamicTest("test 16-color-demo") {
                    val bashCommand = FixGrepCommand("${commandPrecursor} ${command} --16-color-demo").go()
                    assertThat(bashCommand.success).isTrue()
                    assertThat(bashCommand.stdOut).contains("Below (should) be a list of foreground and background names")
                },

                DynamicTest.dynamicTest("test help") {
                    val bashCommand = FixGrepCommand("${commandPrecursor} ${command} --help").go()
                    assertThat(bashCommand.success).isTrue()
                    assertThat(bashCommand.stdOut).isEqualTo(HELP_CONTENT)
                },

                DynamicTest.dynamicTest("test unknown option prints help") {
                    val bashCommand = FixGrepCommand("${commandPrecursor} ${command} --asdf1234").go()
                    assertThat(bashCommand.success).isFalse()
                    assertThat(bashCommand.stdErr).isEqualTo("Error: asdf1234 is not a recognized option\n")
                    assertThat(bashCommand.stdOut).isEqualTo(HELP_CONTENT)
                },

                DynamicTest.dynamicTest("test no input files, and no input pipe times out") {
                    val bashCommand = FixGrepCommand(2000, "${commandPrecursor} ${command}").go()
                    assertThat(bashCommand.timedOut).isTrue()
                })
    }
    //Test that different java versions work (java 10, java 11)
    //test when java is not on the path
    //Test passing in different fix spec
}