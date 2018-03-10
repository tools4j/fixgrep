package org.tools4j.model.state.client

import org.tools4j.model.OrdStatus
import org.tools4j.model.VersionedOrder
import org.tools4j.model.fix.messages.MessageHandler

/**
 * User: ben
 * Date: 17/7/17
 * Time: 6:03 PM
 */
class Rejected(stateContext: StateContext) : AbstractTerminalState(stateContext) {
    override val ordStatus: OrdStatus
        get() = OrdStatus.Rejected
}
