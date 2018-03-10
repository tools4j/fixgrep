package org.tools4j.model.state.client

import org.tools4j.model.OrdStatus

/**
 * User: ben
 * Date: 17/7/17
 * Time: 6:03 PM
 */
class New(context: StateContext) : AbstractOpenState(context) {
    override val ordStatus: OrdStatus
        get() = OrdStatus.New
}
