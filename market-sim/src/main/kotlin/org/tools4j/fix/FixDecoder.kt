package org.tools4j.fix

import org.tools4j.model.fix.messages.FixMessage

interface FixDecoder {
    fun decode(messageStr: String, delimiter: String): FixMessage
}