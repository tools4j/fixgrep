package org.tools4j.clientsim.model

import org.tools4j.clientsim.Config
import org.tools4j.model.OrderVersion
import org.tools4j.model.Qty

/**
 * User: ben
 * Date: 19/7/17
 * Time: 5:13 PM
 */
class AverageAmendQty(private val order: OrderVersion, private val config: Config) : Qty(0) {
    override fun toLong(): Long {
        return (config.averageAmendQtyAsFractionOfTotalOrderQty * order.qty).toLong()
    }
}
