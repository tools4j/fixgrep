package org.tools4j.model.state.client

import org.tools4j.fix.Id
import org.tools4j.model.OrderVersion
import org.tools4j.model.state.State

/**
 * User: ben
 * Date: 8/03/2018
 * Time: 5:31 PM
 */
interface ClientState: State {
    fun amend(message: OrderVersion) {
        throw UnsupportedOperationException("amend not supported in state: " + this.javaClass.simpleName)
    }

    fun cancel(newClOrderId: Id){
        throw UnsupportedOperationException("cancel not supported in state: " + this.javaClass.simpleName)
    }
}