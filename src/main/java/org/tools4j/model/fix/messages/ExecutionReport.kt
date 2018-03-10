package org.tools4j.model.fix.messages

import org.tools4j.fix.Field
import org.tools4j.fix.FieldMap
import org.tools4j.fix.Fields
import org.tools4j.fix.FieldsFromDelimitedString
import org.tools4j.fix.FixFieldTypes
import org.tools4j.fix.FixMessageTypes
import org.tools4j.fix.FixSpec
import org.tools4j.model.ExecType
import org.tools4j.model.FieldsBuilder
import org.tools4j.model.FormatUtils
import org.tools4j.model.Id
import org.tools4j.model.OrdStatus
import org.tools4j.model.OrderVersion
import org.tools4j.model.Price
import org.tools4j.model.Side
import org.tools4j.model.SimpleId

/**
 * User: ben
 * Date: 6/06/2017
 * Time: 5:07 PM
 */
class ExecutionReport(
        override val senderCompId: String,
        override val targetCompId: String,
        override val orderId: Id,
        override val clOrdId: Id,
        val execId: Id,
        val execType: ExecType,
        val ordStatus: OrdStatus,
        val instrument: String,
        val side: Side,
        val lastQty: Long?,
        val leavesQty: Long,
        val cumQty: Long,
        override val transactTime: Long,
        override val origClOrderId: Id,
        val price: Price,
        val text: String?,
        fixSpec: FixSpec) : FixMessage(fixSpec) {

    override val fields: List<Field> by lazy {
        FieldsBuilder()
                .with(FixFieldTypes.MsgType, "8")
                .with(FixFieldTypes.SenderCompID, senderCompId)
                .with(FixFieldTypes.TargetCompID, targetCompId)
                .with(FixFieldTypes.OrderID, orderId)
                .with(FixFieldTypes.ClOrdID, clOrdId)
                .with(FixFieldTypes.OrigClOrdID, origClOrderId)
                .with(FixFieldTypes.Symbol, instrument)
                .with(FixFieldTypes.Side, side.code.toLong())
                .with(FixFieldTypes.TransactTime, FormatUtils.toUTCTimestamp(transactTime))
                .with(FixFieldTypes.ExecID, execId)
                .with(FixFieldTypes.ExecType, execType.code)
                .with(FixFieldTypes.OrdStatus, ordStatus.code)
                .with(FixFieldTypes.LeavesQty, leavesQty)
                .with(FixFieldTypes.CumQty, cumQty)
                .with(FixFieldTypes.LastQty, lastQty)
                .with(FixFieldTypes.Price, price)
                .with(FixFieldTypes.Text, text)
                .fields
    }

    constructor(fixSpec: FixSpec, fields: Fields) : this(fixSpec, FieldMap(fields).mapValue)

    constructor(fixSpec: FixSpec, fields: Map<Number, Field>) : this(
            senderCompId = fields[FixFieldTypes.SenderCompID]!!.stringValue(),
            targetCompId = fields[FixFieldTypes.TargetCompID]!!.stringValue(),
            orderId = fields[FixFieldTypes.OrderID]!!.idValue(),
            clOrdId = fields[FixFieldTypes.ClOrdID]!!.idValue(),
            execId = fields[FixFieldTypes.ExecID]!!.idValue(),
            execType = fields[FixFieldTypes.ExecType]!!.execTypeValue(),
            ordStatus = fields[FixFieldTypes.OrdStatus]!!.ordStatusValue(),
            instrument = fields[FixFieldTypes.Symbol]!!.stringValue(),
            side = fields[FixFieldTypes.Side]!!.sideValue(),
            lastQty = fields[FixFieldTypes.LastQty]?.longValue(),
            leavesQty = fields[FixFieldTypes.LeavesQty]!!.longValue(),
            cumQty = fields[FixFieldTypes.CumQty]!!.longValue(),
            transactTime = fields[FixFieldTypes.TransactTime]!!.longValueFromUTCTimestamp(),
            origClOrderId = fields[FixFieldTypes.OrigClOrdID]!!.idValue(),
            price = fields[FixFieldTypes.Price]!!.priceValue(),
            text = fields[FixFieldTypes.Text]?.stringValue(),
            fixSpec = fixSpec
    )


    override fun dispatchBackTo(messageHandler: DoubleDispatchingMessageHandler) {
        when (execType) {
            ExecType.New -> messageHandler.onNew(this)
            ExecType.PartialFill -> messageHandler.onPartialFilled(this)
            ExecType.Fill -> messageHandler.onFilled(this)
            ExecType.DoneForDay -> messageHandler.onDoneForDay(this)
            ExecType.Canceled -> messageHandler.onCanceled(this)
            ExecType.Replaced -> messageHandler.onReplaced(this)
            ExecType.PendingCancel -> messageHandler.onPendingCancel(this)
            ExecType.Stopped -> messageHandler.onStopped(this)
            ExecType.Rejected -> messageHandler.onRejected(this)
            ExecType.Suspended -> messageHandler.onSuspended(this)
            ExecType.PendingNew -> messageHandler.onPendingNew(this)
            ExecType.Calculated -> messageHandler.onCalculated(this)
            ExecType.Expired -> messageHandler.onExpired(this)
            ExecType.PendingReplace -> messageHandler.onPendingReplace(this)
            ExecType.Trade -> messageHandler.onTrade(this)
            ExecType.TradeCorrect -> messageHandler.onTradeCorrect(this)
            ExecType.TradeCancel -> messageHandler.onTradeCancel(this)
            ExecType.OrderStatus -> messageHandler.onOrderStatus(this)
            ExecType.TradeInAClearingHold -> messageHandler.onTradeInAClearingHold(this)
            ExecType.TradeHasBeenReleasedToClearing -> messageHandler.onTradeHasBeenReleasedToClearing(this)
            ExecType.TriggeredOrActivatedBySystem -> messageHandler.onTriggeredOrActivatedBySystem(this)
            else -> throw IllegalArgumentException("Unknown execType: " + execType)
        }
    }

    fun createOrderVersion(): OrderVersion {
        return OrderVersion(clOrdId, cumQty, price, transactTime)
    }

    class Decoder(val fixSpec: FixSpec) : FixMessageDecoder<ExecutionReport> {
        override val msgType: String
            get() = MSG_TYPE

        override fun createMessage(str: String, delimiter: Char): ExecutionReport {
            val fields = FieldsFromDelimitedString(str, delimiter)
            return createMessage(fields)
        }

        override fun createMessage(fields: Fields): ExecutionReport {
            val fieldMap = FieldMap(fields).mapValue
            return ExecutionReport(fixSpec, fieldMap)
        }

        companion object {
            private val MSG_TYPE = FixMessageTypes.ExecutionReport
        }
    }

    class Builder {
        private var senderCompId: String? = null
        private var targetCompId: String? = null
        private var orderId: Id? = null
        private var clOrderId: Id? = null
        private var execId: Id? = null
        private var execType: ExecType? = null
        private var ordStatus: OrdStatus? = null
        private var instrument: String? = null
        private var side: Side? = null
        private var leavesQty: Long? = null
        private var lastQty: Long? = null
        private var cumQty: Long? = null
        private var transactTime: Long? = null
        private var origClOrderId: Id? = null
        private var price: Price? = null
        private var text: String? = null
        private var fixSpec: FixSpec? = null

        fun build(): ExecutionReport {
            return ExecutionReport(
                    senderCompId = senderCompId!!,
                    targetCompId = targetCompId!!,
                    orderId = orderId!!,
                    clOrdId = clOrderId!!,
                    execId = execId!!,
                    execType = execType!!,
                    ordStatus = ordStatus!!,
                    instrument = instrument!!,
                    side = side!!,
                    lastQty = lastQty,
                    leavesQty = leavesQty!!,
                    cumQty = cumQty!!,
                    transactTime = transactTime!!,
                    origClOrderId = origClOrderId!!,
                    price = price!!,
                    text = text,
                    fixSpec = fixSpec!!
            )    
        }
        
        fun withSenderCompId(senderCompId: String): Builder {
            this.senderCompId = senderCompId
            return this
        }

        fun withTargetCompId(targetCompId: String): Builder {
            this.targetCompId = targetCompId
            return this
        }

        fun withOrderId(orderId: Id): Builder {
            this.orderId = orderId
            return this
        }

        fun withClOrderId(clOrderId: Id): Builder {
            this.clOrderId = clOrderId
            return this
        }

        fun withExecId(execId: String): Builder {
            this.execId = SimpleId(execId)
            return this
        }

        fun withExecId(execId: Id): Builder {
            this.execId = execId
            return this
        }

        fun withExecType(execType: ExecType): Builder {
            this.execType = execType
            return this
        }

        fun withOrdStatus(ordStatus: OrdStatus): Builder {
            this.ordStatus = ordStatus
            return this
        }

        fun withInstrument(instrument: String): Builder {
            this.instrument = instrument
            return this
        }

        fun withSide(side: Side): Builder {
            this.side = side
            return this
        }

        fun withLastQty(lastQty: Long?): Builder {
            this.lastQty = lastQty
            return this
        }

        fun withFixSpec(fixSpec: FixSpec): Builder {
            this.fixSpec = fixSpec
            return this
        }

        fun withLeavesQty(leavesQty: Long): Builder {
            this.leavesQty = leavesQty
            return this
        }

        fun withCumQty(cumQty: Long): Builder {
            this.cumQty = cumQty
            return this
        }

        fun withTransactTime(transactTime: Long): Builder {
            this.transactTime = transactTime
            return this
        }

        fun withOrigClOrderId(origClOrderId: Id): Builder {
            this.origClOrderId = origClOrderId
            return this
        }

        fun withPrice(price: Price): Builder {
            this.price = price
            return this
        }

        fun withText(text: String?): Builder {
            this.text = text
            return this
        }
    }

    override fun toString(): String {
        return "ExecutionReport(lastQty=$lastQty, leavesQty=$leavesQty, price=$price)"
    }
}

