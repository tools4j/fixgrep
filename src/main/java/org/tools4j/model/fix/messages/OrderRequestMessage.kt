package org.tools4j.model.fix.messages

import org.tools4j.model.Id
import org.tools4j.model.Side

/**
 * User: ben
 * Date: 13/9/17
 * Time: 6:19 AM
 */
interface OrderRequestMessage : Command {
    override val clOrdId: Id
    val instrument: String
    val side: Side
}
