package org.tools4j.fixgrep.integration

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

/**
 * User: benjw
 * Date: 02/11/2018
 * Time: 05:56
 */
class FormattingTest {
    @Test
    fun `test simple run`(){
        val bashCommand = FixGrepCommand("./fixgrep small-test.log").go()
        assertThat(bashCommand.success).isTrue()
        assertThat(bashCommand.stdOut).isEqualTo(FixGrepCommand.SMALL_LOG_FIX_WITH_BOLD)
    }

    @Test
    fun `piped input`(){
        val bashCommand = FixGrepCommand("cat small-test.log | ./fixgrep").go()
        assertThat(bashCommand.success).isTrue()
        assertThat(bashCommand.stdOut).isEqualTo(FixGrepCommand.SMALL_LOG_FIX_WITH_BOLD)
    }

    @Test
    fun `test in debug mode`(){
        val bashCommand = FixGrepCommand("./fixgrep -x small-test.log").go()
        assertThat(bashCommand.success).isTrue()
        assertThat(bashCommand.stdOut).isEqualTo(FixGrepCommand.SMALL_LOG_FIX_WITH_BOLD_IN_DEBUG)
    }
}