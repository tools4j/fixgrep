package org.tools4j.model.state.market

import org.tools4j.fix.OrdStatus
import org.tools4j.model.fix.messages.ExecutionReport
import org.tools4j.model.fix.messages.NewOrderSingle

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

    override fun handleNewOrderSingle(message: NewOrderSingle) {
        val executionReportNew = message.createExecutionReportNew(stateContext.order.orderId, stateContext.execIDGenerator.get())
        onNew(executionReportNew)
        stateContext.messageHandler.handle(executionReportNew)
    }

    override fun onNew(message: ExecutionReport) {
        stateContext.order.addExecution(message)
        stateContext.stateMachine.changeState(New(stateContext))
    }
}
