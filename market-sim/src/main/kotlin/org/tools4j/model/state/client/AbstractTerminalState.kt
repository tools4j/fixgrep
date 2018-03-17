package org.tools4j.model.state.client

import org.tools4j.model.VersionedOrder
import org.tools4j.model.fix.messages.MessageHandler

/**
 * User: ben
 * Date: 17/7/17
 * Time: 6:07 PM
 */
abstract class AbstractTerminalState(stateContext: StateContext) : AbstractState(stateContext) {
    override fun isTerminal(): Boolean = true

    override val open: Boolean
        get() = false
}
