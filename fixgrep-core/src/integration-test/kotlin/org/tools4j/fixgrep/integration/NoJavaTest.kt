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
class NoJavaTest {
    @Test
    fun `test no java`(){
        val bashCommand = FixGrepCommand("PATH=\$(echo \"\$PATH\" | awk -v RS=: -v ORS=: '/(Java|java|jre|jdk)/ {next} {print}') ./fixgrep small-test.log").go()
        assertThat(bashCommand.success).isFalse()
        assertThat(bashCommand.stdErr.equals("Could not find java.  Please ensure that the java bin directory to your path."))
    }
}
