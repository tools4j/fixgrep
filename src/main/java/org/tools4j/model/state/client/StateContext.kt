package org.tools4j.model.state.client

import org.tools4j.fix.FixSpec
import org.tools4j.model.ClientOrder
import org.tools4j.model.DateTimeService
import org.tools4j.model.fix.messages.MessageHandler
import org.tools4j.model.state.StateMachine
import org.tools4j.model.state.StateMessageListener

/**
 * User: ben
 * Date: 14/02/2018
 * Time: 6:31 AM
 */
class StateContext(
        val order: ClientOrder,
        val stateMachine: StateMachine<ClientState>,
        val messageHandler: MessageHandler,
        val dateTimeService: DateTimeService,
        val stateMessageListeners: Collection<StateMessageListener>,
        val fixSpec: FixSpec)
