package org.tools4j.fixgrep.integration

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.tools4j.fixgrep.integration.FixGrepCommand.Companion.HELP_CONTENT

/**
 * User: benjw
 * Date: 02/11/2018
 * Time: 05:56
 */
class InstallTest {
    @Test
    fun `test install - success`(){
        val bashCommand = FixGrepCommand("./fixgrep --install").go()
        assertThat(bashCommand.success).isTrue()
        assertThat(bashCommand.stdOut).isEqualTo(HELP_CONTENT)
    }
}