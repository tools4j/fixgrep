package org.tools4j.model.state.client

import org.tools4j.fix.Id
import org.tools4j.fix.OrdStatus
import org.tools4j.model.OrderVersion
import org.tools4j.model.fix.messages.CancelReplaceRequest
import org.tools4j.model.fix.messages.CancelRequest
import org.tools4j.model.fix.messages.ExecutionReport

/**
 * User: ben
 * Date: 17/7/17
 * Time: 6:07 PM
 */
abstract class AbstractOpenState(val context: StateContext) : AbstractState(context) {

    override val ordStatus: OrdStatus
        get() {
            val leavesQty = context.order.leavesQty
            return if (leavesQty == 0L) {
                OrdStatus.Filled
            } else if (leavesQty == context.order.qty){
                OrdStatus.New
            } else {
                OrdStatus.PartiallyFilled
            }
        }

    override fun onPartialFilled(msg: ExecutionReport) {
        onFilled(msg)
    }

    override fun onFilled(message: ExecutionReport) {
        context.order.addExecution(message)
        if (context.order.leavesQty > 0) {
            context.stateMachine.changeState(PartiallyFilled(context))
        } else {
            context.stateMachine.changeState(DoneForDay(context))
        }
    }

    override fun onReplaced(message: ExecutionReport) {
        if (context.order.leavesQty > 0) {
            context.stateMachine.changeState(Replaced(context))
        } else {
            context.stateMachine.changeState(DoneForDay(context))
        }
    }

    override fun handleCancelRequest(message: CancelRequest) {
        context.stateMachine.changeState(Canceled(context))
    }

    override fun amend(newOrderVersion: OrderVersion) {
        logger.info { "Before amend::${context.order.latestVersion}::" + stateContext.order.senderCompId + "::" + stateContext.order.clOrdId + "::leavesQty=" + stateContext.order.leavesQty + "::qty=" + stateContext.order.qty }
        val cancelReplaceRequest =  CancelReplaceRequest(
                //The order instance we have called into, is the client instance. We need
                //this message to travel across to the market instance.  So sender/target comp
                //ids remain the same.
                senderCompId = context.order.senderCompId,
                targetCompId = context.order.targetCompId,
                origClOrderId = context.order.origClOrdId,
                clOrdId = newOrderVersion.clOrdId,
                instrument = context.order.instrument,
                transactTime = context.dateTimeService.now(),
                side = context.order.side,
                orderQty = newOrderVersion.qty,
                orderType = context.order.orderType,
                price = newOrderVersion.price,
                fixSpec = context.fixSpec,
                orderId = null
        )
        //this.handleCancelReplaceRequest(cancelReplaceRequest)
        stateContext.messageHandler.handle(cancelReplaceRequest)
        logger.info { "After amend::${context.order.latestVersion}::" + stateContext.order.senderCompId + "::" + stateContext.order.clOrdId + "::leavesQty=" + stateContext.order.leavesQty + "::qty=" + stateContext.order.qty }
    }

    override fun cancel(newClOrderId: Id) {
        val cancelRequest = CancelRequest(
                //The order instance we have called into, is the client instance. We need
                //this message to travel across to the market instance.  So sender/target comp
                //ids remain the same.
                senderCompId = context.order.senderCompId,
                targetCompId = context.order.targetCompId,
                origClOrderId = context.order.origClOrdId,
                clOrdId = newClOrderId,
                instrument = context.order.instrument,
                transactTime = context.dateTimeService.now(),
                side = context.order.side,
                orderQty = context.order.qty,
                orderType = context.order.orderType,
                price = context.order.price,
                fixSpec = context.fixSpec,
                orderId = null
        )
        //this.handleCancelRequest(cancelRequest)
        stateContext.messageHandler.handle(cancelRequest)
    }

    override fun equals(other: Any?): Boolean {
        return this.javaClass == other!!.javaClass
    }

    override fun hashCode(): Int {
        return this.javaClass.hashCode()
    }
}
