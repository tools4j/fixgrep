package org.tools4j.model.state.market

import org.tools4j.fix.ExecType
import org.tools4j.fix.OrdStatus
import org.tools4j.model.OrderVersion
import org.tools4j.fix.Price
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

    override fun handleCancelReplaceRequest(message: CancelReplaceRequest) {
        if(message.orderQty < ( context.order.qty - context.order.leavesQty) ){
            val replaceRejected = message.createRejectedMessage(context.order.orderId, context.execIDGenerator.get(), context.order.ordStatus, context.order.leavesQty, context.order.cumQty, "Requested amend qty ${message.orderQty} is less the cumQty ${context.order.cumQty}" )
            context.messageHandler.handle(replaceRejected)
        } else {
            val orderVersion = OrderVersion(message.clOrdId, message.orderQty, message.price, context.dateTimeService.now())
            context.order.addVersion(orderVersion)
            val replaceAccepted = message.createAcceptedMessage(context.order.orderId, context.execIDGenerator.get(), context.order.ordStatus, context.order.leavesQty, context.order.cumQty)
            context.messageHandler.handle(replaceAccepted)
        }
    }

    override fun handleCancelRequest(message: CancelRequest) {
        context.stateMachine.changeState(Canceled(context))
    }

    override fun fill(qty: Long, price: Price) {
        if(context.order.wouldOverfill(qty)){
            throw RuntimeException("Fill of qty $qty would cause over fill. orderCumQty:${context.order.cumQty}")
        }
        logger.info { "Before fill of qty::$qty::" + stateContext.order.senderCompId + "::" + stateContext.order.orderId + "::leavesQty=" + stateContext.order.leavesQty + "::qty=" + stateContext.order.qty }
        val executionReport =  ExecutionReport(
                //The order instance we have called into, is the market instance. We need
                //this message to travel back to the client instance.  Because sender/target in the order
                // is in reference to the _client_ (NOS), we need to switch the sendar/target comp ids.
                senderCompId = context.order.targetCompId,
                targetCompId = context.order.senderCompId,
                orderId = context.order.orderId,
                clOrdId = context.order.clOrdId,
                execId = context.execIDGenerator.get(),
                execType = ExecType.Fill,
                ordStatus = context.order.ordStatusAssumingAdditionalFill(qty),
                instrument = context.order.instrument,
                side = context.order.side,
                lastQty = qty,
                leavesQty = context.order.leavesQtyAssumingAdditionalFill(qty),
                cumQty = context.order.filledQtyAssumingAdditionalFill(qty),
                transactTime = context.dateTimeService.now(),
                origClOrderId = context.order.origClOrdId,
                price = price,
                text = "Executed",
                fixSpec = context.fixSpec
        )

        this.onFilled(executionReport)
        stateContext.messageHandler.handle(executionReport)
        logger.info { "After fill::$qty::" + stateContext.order.senderCompId + "::" + stateContext.order.orderId + "::leavesQty=" + stateContext.order.leavesQty + "::qty=" + stateContext.order.qty }
    }

    override fun equals(other: Any?): Boolean {
        return this.javaClass == other!!.javaClass
    }

    override fun hashCode(): Int {
        return this.javaClass.hashCode()
    }
}
