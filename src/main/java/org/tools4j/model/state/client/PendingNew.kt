package org.tools4j.model.state.client

import org.tools4j.model.OrdStatus
import org.tools4j.model.fix.messages.ExecutionReport

/**
 * User: ben
 * Date: 17/7/17
 * Time: 5:58 PM
 */
class PendingNew(stateContext: StateContext) : AbstractState(stateContext) {

    override val pending: Boolean
        get() = true

    override val open: Boolean
        get() = false


    override val ordStatus: OrdStatus
        get() {
            return OrdStatus.PendingNew
        }

    override fun onNew(message: ExecutionReport) {
        stateContext.order.addExecution(message)
        stateContext.stateMachine.changeState(New(stateContext))
    }
}
