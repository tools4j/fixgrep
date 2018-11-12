package org.tools4j.fixgrep.integration

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.tools4j.fixgrep.integration.FixGrepCommand.Companion.HELP_CONTENT

/**
 * User: benjw
 * Date: 02/11/2018
 * Time: 05:56
 */
class AdditionalBehavourTest {

    @Test
    fun `test man page`(){
        val bashCommand = FixGrepCommand("./fixgrep man").go()
        assertThat(bashCommand.success).isTrue()
        assertThat(bashCommand.stdOut).contains("WHAT IS FIXGREP")
    }

    @Test
    fun `test 256-color-demo`(){
        val bashCommand = FixGrepCommand("./fixgrep --256-color-demo").go()
        assertThat(bashCommand.success).isTrue()
        assertThat(bashCommand.stdOut).contains("Below (should) be a table of numbers with colored backgrounds")
    }

    @Test
    fun `test 16-color-demo`(){
        val bashCommand = FixGrepCommand("./fixgrep --16-color-demo").go()
        assertThat(bashCommand.success).isTrue()
        assertThat(bashCommand.stdOut).contains("Below (should) be a list of foreground and background names")
    }

    @Test
    fun `test help`(){
        val bashCommand = FixGrepCommand("./fixgrep --help").go()
        assertThat(bashCommand.success).isTrue()
        assertThat(bashCommand.stdOut).isEqualTo(HELP_CONTENT)
    }

    @Test
    fun `test unknown option prints help`(){
        val bashCommand = FixGrepCommand("./fixgrep --asdf1234 test2.log").go()
        assertThat(bashCommand.success).isFalse()
        assertThat(bashCommand.stdErr).isEqualTo("Error: asdf1234 is not a recognized option\n")
        assertThat(bashCommand.stdOut).isEqualTo(HELP_CONTENT)
    }

    @Test
    fun `test no input files, and no input pipe times out`(){
        val bashCommand = FixGrepCommand(2000, "./fixgrep").go()
        assertThat(bashCommand.timedOut).isTrue()
    }
}