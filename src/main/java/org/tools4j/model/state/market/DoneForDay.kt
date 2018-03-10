package org.tools4j.model.state.market

import org.tools4j.model.OrdStatus
import org.tools4j.model.VersionedOrder
import org.tools4j.model.fix.messages.Message
import org.tools4j.model.fix.messages.MessageHandler
import java.util.function.Consumer

/**
 * User: ben
 * Date: 17/7/17
 * Time: 6:07 PM
 */
class DoneForDay(stateContext: StateContext) : AbstractTerminalState(stateContext) {
    override val ordStatus: OrdStatus
        get() = OrdStatus.DoneForDay
}
