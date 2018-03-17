package org.tools4j.model.state.market

import org.tools4j.fix.OrdStatus

/**
 * User: ben
 * Date: 17/7/17
 * Time: 6:07 PM
 */
class PendingCancel(context: StateContext) : AbstractOpenState(context) {
    override val ordStatus: OrdStatus
        get() = OrdStatus.PendingCancel

    override val pending: Boolean
        get() = true
}
