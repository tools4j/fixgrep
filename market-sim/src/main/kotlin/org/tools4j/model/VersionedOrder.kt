package org.tools4j.model

import org.tools4j.extensions.sumByLong
import org.tools4j.fix.Id
import org.tools4j.fix.OrdStatus
import org.tools4j.fix.Price
import org.tools4j.fix.Priceable
import org.tools4j.fix.Side
import org.tools4j.model.fix.messages.DelegatingMessageHandler
import org.tools4j.model.fix.messages.ExecutionReport
import org.tools4j.model.fix.messages.Message
import org.tools4j.model.fix.messages.MessageHandler
import org.tools4j.model.fix.messages.NewOrderSingle
import org.tools4j.model.state.State
import org.tools4j.model.state.StateMachine
import org.tools4j.model.state.StateMessageListener
import org.tools4j.oms.OrigClOrdIdAndSenderCompId
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * User: ben
 * Date: 6/06/2017
 * Time: 5:07 PM
 */
abstract class VersionedOrder<S: State>(
        val nos: NewOrderSingle,
        val dateTimeService: DateTimeService,
        messageHandler: MessageHandler)
            : Priceable, OrderUnderExecution, StateMachine<S>, DelegatingMessageHandler {

    protected val versions: LinkedList<OrderVersion>
    protected val executions: LinkedList<ExecutionReport>
    protected val stateHistory: LinkedList<S>
    protected val listeners: MutableList<StateMessageListener>
    protected val arrivalTime: Long

    init {
        executions = LinkedList()
        versions = LinkedList()
        versions.add(OrderVersion(nos.clOrdId, nos.orderQty, nos.price, dateTimeService.now()))
        stateHistory = LinkedList()
        listeners = ArrayList();
        arrivalTime = dateTimeService.now()
    }

    override val side: Side
        get() = nos.side

    override val instrument: String
        get() = nos.instrument

    override val senderCompId: String
        get() = nos.senderCompId

    override val targetCompId: String
        get() = nos.targetCompId

    val origClOrdIdAndSenderCompId: OrigClOrdIdAndSenderCompId by lazy {
        OrigClOrdIdAndSenderCompId(origClOrdId, senderCompId)
    }

    val latestVersion: OrderVersion
        get() = versions.last

    val latestExecution: ExecutionReport?
        get() = if(executions.isEmpty()) null else executions.last

    val currentState: S
        get() = stateHistory.last

    val ordStatus: OrdStatus
        get() = if (filled) {
            OrdStatus.Filled
        } else if (cumQty > 0) {
            OrdStatus.PartiallyFilled
        } else {
            OrdStatus.New
        }

    override val leavesQty: Long
        get() = latestVersion.qty - filledQty

    val lastQty: Long?
        get() {
            for(i in (executions.size-1) downTo 0){
                val lastQtyInExecution = executions.get(i).lastQty
                if(lastQtyInExecution != null){
                    return lastQtyInExecution
                }
            }
            return null
        }

    override val cumQty: Long
        get() = filledQty

    override val status: OrdStatus
        get() = currentState.ordStatus

    private val origVersion: OrderVersion
        get() = versions.first

    val filledQty: Long
        get() = executions.sumByLong { it.lastQty ?: 0 }

    override val formattedPrice: String
        get() = latestVersion.formattedPrice(side)

    override val price: Price
        get() = latestVersion.price

    override val qty: Long
        get() = latestVersion.qty

    override val clOrdId: Id
        get() = latestVersion.clOrdId

    override val origClOrdId: Id
        get() = firstVersion.clOrdId

    val firstVersion: OrderVersion
        get() = versions.first

    fun addListener(listener: StateMessageListener){
        listeners.add(listener)
    }

    fun removeListener(listener: StateMessageListener){
        listeners.remove(listener)
    }

    override fun getDelegate(message: Message): MessageHandler? {
        return currentState
    }

    override fun delegateAllMessages(): Boolean {
        return true
    }

    override fun addExecution(execution: ExecutionReport) {
        executions.add(execution)
    }

    override fun addVersion(version: OrderVersion) {
        versions.add(version)
    }

    override fun toString(): String {
        return NumberFormat.getIntegerInstance().format(leavesQty) + "@" + formattedPrice
    }

    fun toStringWithAge(): String {
        val agePostfix = if(ageMs != versionAgeMs) "[" + versionAgeMs + "|" + ageMs + "]" else "[" + ageMs + "]"
        return NumberFormat.getIntegerInstance().format(leavesQty) + "@" + formattedPrice + agePostfix
    }

    override fun changeState(newState: S) {
        stateHistory.add(newState)
    }

    fun leavesQtyAssumingAdditionalFill(qty: Long): Long {
        return leavesQty - qty
    }

    fun filledQtyAssumingAdditionalFill(qty: Long): Long {
        return filledQty + qty
    }

    fun ordStatusAssumingAdditionalFill(qty: Long): OrdStatus {
        return if (filledQtyAssumingAdditionalFill(qty) >= this.qty) {
            OrdStatus.Filled
        } else if (cumQty > 0) {
            OrdStatus.PartiallyFilled
        } else {
            OrdStatus.New
        }
    }

    fun wouldOverfill(qty: Long): Boolean{
        return (filledQty + qty > this.qty)
    }

    fun crossesWith(other: VersionedOrder<*>): Boolean {
        if(this.side == other.side){
            throw IllegalArgumentException("Cannot cross with orders on the same side: " + this.side)
        }
        if(!this.price.hasPrice() || !other.price.hasPrice()){
            return true
        }
        return this.isEqualToOrMoreAggressiveThan(other)
    }

    val versionAgeMs: Long
        get() = (dateTimeService.now() - latestVersion.arrivalTime)

    val ageMs: Long
        get() = (dateTimeService.now() - arrivalTime)

    fun isTerminal(): Boolean {
        return currentState.isTerminal()
    }
}



