package org.tools4j.model.fix.messages

/**
 * User: ben
 * Date: 7/9/17
 * Time: 6:36 AM
 */
interface DoubleDispatchingMessageHandler : MessageHandler {
    override fun handle(msg: Message){
        msg.dispatchBackTo(this);
    }

    fun throwUnsupportedMessageException(message: Message) {
        throw UnsupportedOperationException()
    }

    fun onNew(message: ExecutionReport) {
        throwUnsupportedMessageException(message)
    }

    fun onFilled(message: ExecutionReport) {
        throwUnsupportedMessageException(message)
    }

    fun onPartialFilled(message: ExecutionReport) {
        throwUnsupportedMessageException(message)
    }

    fun onDoneForDay(message: ExecutionReport) {
        throwUnsupportedMessageException(message)
    }

    fun onCanceled(message: ExecutionReport) {
        throwUnsupportedMessageException(message)
    }

    fun onReplaced(message: ExecutionReport) {
        throwUnsupportedMessageException(message)
    }

    fun onPendingCancel(message: ExecutionReport) {
        throwUnsupportedMessageException(message)
    }

    fun onStopped(message: ExecutionReport) {
        throwUnsupportedMessageException(message)
    }

    fun onRejected(message: ExecutionReport) {
        throwUnsupportedMessageException(message)
    }

    fun onSuspended(message: ExecutionReport) {
        throwUnsupportedMessageException(message)
    }

    fun onPendingNew(message: ExecutionReport) {
        throwUnsupportedMessageException(message)
    }

    fun onCalculated(message: ExecutionReport) {
        throwUnsupportedMessageException(message)
    }

    fun onExpired(message: ExecutionReport) {
        throwUnsupportedMessageException(message)
    }

    fun onPendingReplace(message: ExecutionReport) {
        throwUnsupportedMessageException(message)
    }

    fun onTrade(message: ExecutionReport) {
        throwUnsupportedMessageException(message)
    }

    fun onTradeCorrect(message: ExecutionReport) {
        throwUnsupportedMessageException(message)
    }

    fun onTradeCancel(message: ExecutionReport) {
        throwUnsupportedMessageException(message)
    }

    fun onOrderStatus(message: ExecutionReport) {
        throwUnsupportedMessageException(message)
    }

    fun onTradeInAClearingHold(message: ExecutionReport) {
        throwUnsupportedMessageException(message)
    }

    fun onTradeHasBeenReleasedToClearing(message: ExecutionReport) {
        throwUnsupportedMessageException(message)
    }

    fun onTriggeredOrActivatedBySystem(message: ExecutionReport) {
        throwUnsupportedMessageException(message)
    }

    fun handleCancelReplaceRequest(message: CancelReplaceRequest) {
        throwUnsupportedMessageException(message)
    }

    fun handleCancelRequest(message: CancelRequest) {
        throwUnsupportedMessageException(message)
    }

    fun handleNewOrderSingle(message: NewOrderSingle) {
        throwUnsupportedMessageException(message)
    }
}
