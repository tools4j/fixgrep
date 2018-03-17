package org.tools4j.model.state.market

import org.tools4j.fix.OrdStatus

/**
 * User: ben
 * Date: 17/7/17
 * Time: 6:03 PM
 */
class Canceled(stateContext: StateContext) : AbstractTerminalState(stateContext) {

    override val ordStatus: OrdStatus
        get() = OrdStatus.Canceled
}
