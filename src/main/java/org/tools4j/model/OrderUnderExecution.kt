package org.tools4j.model

import org.tools4j.model.fix.messages.ExecutionReport

/**
 * User: ben
 * Date: 28/10/2016
 * Time: 6:33 AM
 */
interface OrderUnderExecution : Order {
    override val leavesQty: Long
    fun addExecution(execution: ExecutionReport)
    fun addVersion(version: OrderVersion)
}