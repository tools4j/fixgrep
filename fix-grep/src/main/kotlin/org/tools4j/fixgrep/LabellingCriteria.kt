package org.tools4j.fixgrep

import org.tools4j.fix.ExecType
import org.tools4j.fix.FixMessageType

/**
 * User: ben
 * Date: 16/03/2018
 * Time: 6:22 AM
 */
interface LabellingCriteria{
    fun shouldLabel(messageString: MessageString): Boolean

    class AlwaysTrueCriteria: LabellingCriteria {
        override fun shouldLabel(messageString: MessageString): Boolean {
            return true
        }
    }

    class AlwaysFalseCriteria: LabellingCriteria {
        override fun shouldLabel(messageString: MessageString): Boolean {
            return false
        }
    }

    class Nos() : TagsMatchCriteria(hashMapOf(35 to FixMessageType.NewOrderSingle.code))
    class OrderCancelRequest() : TagsMatchCriteria(hashMapOf(35 to FixMessageType.OrderCancelRequest.code))
    class OrderCancelReplaceRequest() : TagsMatchCriteria(hashMapOf(35 to FixMessageType.OrderCancelReplaceRequest.code))
    class New() : TagsMatchCriteria(hashMapOf(35 to FixMessageType.ExecutionReport.code, 150 to ExecType.New.code))
    class PartialFill() : TagsMatchCriteria(hashMapOf(35 to FixMessageType.ExecutionReport.code, 150 to ExecType.PartialFill.code))
    class Fill() : TagsMatchCriteria(hashMapOf(35 to FixMessageType.ExecutionReport.code, 150 to ExecType.Fill.code))
    class DoneForDay() : TagsMatchCriteria(hashMapOf(35 to FixMessageType.ExecutionReport.code, 150 to ExecType.DoneForDay.code))
    class Canceled() : TagsMatchCriteria(hashMapOf(35 to FixMessageType.ExecutionReport.code, 150 to ExecType.Canceled.code))
    class Replaced() : TagsMatchCriteria(hashMapOf(35 to FixMessageType.ExecutionReport.code, 150 to ExecType.Replaced.code))
    class PendingCancel() : TagsMatchCriteria(hashMapOf(35 to FixMessageType.ExecutionReport.code, 150 to ExecType.PendingCancel.code))
    class Stopped() : TagsMatchCriteria(hashMapOf(35 to FixMessageType.ExecutionReport.code, 150 to ExecType.Stopped.code))
    class Rejected() : TagsMatchCriteria(hashMapOf(35 to FixMessageType.ExecutionReport.code, 150 to ExecType.Rejected.code))
    class Suspended() : TagsMatchCriteria(hashMapOf(35 to FixMessageType.ExecutionReport.code, 150 to ExecType.Suspended.code))
    class PendingNew() : TagsMatchCriteria(hashMapOf(35 to FixMessageType.ExecutionReport.code, 150 to ExecType.PendingNew.code))
    class Calculated() : TagsMatchCriteria(hashMapOf(35 to FixMessageType.ExecutionReport.code, 150 to ExecType.Calculated.code))
    class Expired() : TagsMatchCriteria(hashMapOf(35 to FixMessageType.ExecutionReport.code, 150 to ExecType.Expired.code))
    class PendingReplace() : TagsMatchCriteria(hashMapOf(35 to FixMessageType.ExecutionReport.code, 150 to ExecType.PendingReplace.code))
    class Trade() : TagsMatchCriteria(hashMapOf(35 to FixMessageType.ExecutionReport.code, 150 to ExecType.Trade.code))
    class TradeCorrect() : TagsMatchCriteria(hashMapOf(35 to FixMessageType.ExecutionReport.code, 150 to ExecType.TradeCorrect.code))
    class TradeCancel() : TagsMatchCriteria(hashMapOf(35 to FixMessageType.ExecutionReport.code, 150 to ExecType.TradeCancel.code))
    class OrderStatus() : TagsMatchCriteria(hashMapOf(35 to FixMessageType.ExecutionReport.code, 150 to ExecType.OrderStatus.code))
    class TradeInAClearingHold() : TagsMatchCriteria(hashMapOf(35 to FixMessageType.ExecutionReport.code, 150 to ExecType.TradeInAClearingHold.code))
    class TradeHasBeenReleasedToClearing() : TagsMatchCriteria(hashMapOf(35 to FixMessageType.ExecutionReport.code, 150 to ExecType.TradeHasBeenReleasedToClearing.code))
    class TriggeredOrActivatedBySystem() : TagsMatchCriteria(hashMapOf(35 to FixMessageType.ExecutionReport.code, 150 to ExecType.TriggeredOrActivatedBySystem.code))
}

open class TagsMatchCriteria(val criteria: Map<Int, String>): LabellingCriteria{
    override fun shouldLabel(messageString: MessageString): Boolean {
        for(check in criteria){
            val field = messageString.originalFields.getField(check.key)
            if(field == null) return false
            if(!field.value.toString().equals(check.value)){
                return false
            }
        }
        return true
    }

    class Builder{
        val map: MutableMap<Int, String> = HashMap()

        fun with(tag:Int, value:String): Builder {
            map.put(tag, value)
            return this
        }

        fun build(): TagsMatchCriteria {
            return TagsMatchCriteria(map)
        }
    }
}