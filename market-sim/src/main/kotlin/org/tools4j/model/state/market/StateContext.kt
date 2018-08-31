package org.tools4j.model.state.market

import org.tools4j.fix.FixSpec
import org.tools4j.fix.spec.FixSpecDefinition
import org.tools4j.model.DateTimeService
import org.tools4j.model.IdGenerator
import org.tools4j.model.MarketOrder
import org.tools4j.model.fix.messages.MessageHandler
import org.tools4j.model.state.StateMachine
import org.tools4j.model.state.StateMessageListener

/**
 * User: ben
 * Date: 14/02/2018
 * Time: 6:31 AM
 */
class StateContext(
        val order: MarketOrder,
        val stateMachine: StateMachine<MarketState>,
        val messageHandler: MessageHandler,
        val dateTimeService: DateTimeService,
        val stateMessageListeners: Collection<StateMessageListener>,
        val fixSpec: FixSpecDefinition,
        val execIDGenerator: IdGenerator)
