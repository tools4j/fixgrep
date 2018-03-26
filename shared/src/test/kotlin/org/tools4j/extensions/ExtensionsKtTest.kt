package org.tools4j.extensions

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

/**
 * User: ben
 * Date: 20/03/2018
 * Time: 5:44 PM
 */
class ExtensionsKtTest {
    @Test
    fun testConstantToCapitalCase(){
        assertThat("MY_TEST".constantToCapitalCase()).isEqualTo("MyTest")
        assertThat("MY_LONGER_TEST".constantToCapitalCase()).isEqualTo("MyLongerTest")
        assertThat("MY__MULTIPLE___UNDERSCORE____TEST".constantToCapitalCase()).isEqualTo("MyMultipleUnderscoreTest")
        assertThat("MYONEWORD".constantToCapitalCase()).isEqualTo("Myoneword")
        assertThat("MY_TRAILING_UNDERSCORE_".constantToCapitalCase()).isEqualTo("MyTrailingUnderscore")
        assertThat("_MY_LEADING_UNDERSCORE".constantToCapitalCase()).isEqualTo("MyLeadingUnderscore")
        assertThat("_TRAILING_AND_LEADING_UNDERSCORES_".constantToCapitalCase()).isEqualTo("TrailingAndLeadingUnderscores")
    }
}