package org.tools4j.extensions

import spock.lang.Specification

/**
 * User: ben
 * Date: 29/05/2018
 * Time: 8:47 AM
 */
class ExtensionsKtTest extends Specification {
    def "LengthNotIncludingAnsiCodes"(String input, int expectedLength) {
        when:
        int length = AnsiHelper.lengthNotIncludingAnsiCodes(input)

        then:
        assert length == expectedLength

        where:
        input                           | expectedLength
        "asdf"                          | 4
        "asdf\u001B[32masdf"            | 8
        "asdf\u001B[32:43:5masdf"       | 8
        "\u001B[32:43:5masdf"           | 4
        "asdf\u001B[32:43:5m"           | 4
    }

    def "PadStringContainingAnsiCodesEnd"(String input, int padLength, String expectedOutput) {
        when:
        String output = AnsiHelper.padStringContainingAnsiCodesEnd(input, padLength, (char) ' ')

        then:
        assert output == expectedOutput

        where:
        input                           | padLength | expectedOutput
        "asdf"                          |  8        | "asdf    "
        "asdf"                          |  3        | "asdf"
        "asdf\u001B[32masdf"            | 12        | "asdf\u001B[32masdf    "
        "asdf\u001B[32masdf"            |  6        | "asdf\u001B[32masdf"
        "\u001B[1m[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1mD|11=ABC|55=AUD/USD\u001B[22m" | 32 | "\u001B[1m[MsgType]\u001B[1m35\u001B[22m\u001B[1m=\u001B[22m\u001B[1mD|11=ABC|55=AUD/USD\u001B[22m "
    }
}
