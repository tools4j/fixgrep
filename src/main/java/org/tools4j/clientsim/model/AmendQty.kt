package org.tools4j.clientsim.model

import org.tools4j.clientsim.Config
import org.tools4j.model.OrderUnderExecution
import org.tools4j.model.Qty
import org.tools4j.model.RoundedUpQtyToNearestSliceQty

import java.util.Date
import java.util.Random

/**
 * User: ben
 * Date: 20/7/17
 * Time: 6:47 AM
 */
class AmendQty(
        private val order: OrderUnderExecution,
        private val minSliceQty: Long,
        private val averageAmendQtyAsFractionOfTotalOrderQty: Double) : Qty(0) {

    private val random: Random

    constructor(order: OrderUnderExecution, config: Config) : this(order, config.minSliceQty, config.averageAmendQtyAsFractionOfTotalOrderQty) {}

    init {
        this.random = Random(Date().time)
    }

    override fun toLong(): Long {
        val averageQty = (averageAmendQtyAsFractionOfTotalOrderQty * order.qty).toLong()
        val randomizedQty = (random.nextDouble() * 2.0 * averageQty.toDouble()).toLong()
        val roundedToNearest = RoundedUpQtyToNearestSliceQty(randomizedQty, minSliceQty).toLong()
        return Math.max(roundedToNearest, order.leavesQty).toInt().toLong()
    }
}
