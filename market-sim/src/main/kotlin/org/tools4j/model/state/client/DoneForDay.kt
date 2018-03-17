package org.tools4j.model.state.client

import org.tools4j.fix.OrdStatus

/**
 * User: ben
 * Date: 17/7/17
 * Time: 6:07 PM
 */
class DoneForDay(stateContext: StateContext) : AbstractTerminalState(stateContext) {
    override val ordStatus: OrdStatus
        get() = OrdStatus.DoneForDay
}
