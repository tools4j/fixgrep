package org.tools4j.clientsim;

import mu.KLogging
import org.tools4j.model.ClientOrder
import org.tools4j.model.DateTimeService
import org.tools4j.model.IdGenerator
import org.tools4j.model.LimitPrice
import org.tools4j.model.Price
import org.tools4j.model.VersionedOrder
import org.tools4j.model.state.client.New
import org.tools4j.model.state.client.PendingNew
import org.tools4j.strategy.Strategy
import org.tools4j.util.Randomize


/**
 * User: ben
 * Date: 20/7/17
 * Time: 7:25 PM
 *
 * A MarketFacingStrategy has the option of creating one to many
 * new market orders.
 *
 */
class ClientSimStrategy(
        val order: ClientOrder,
        private val dateTimeService: DateTimeService,
        private val config: Config,
        private val orderIdGenerator: IdGenerator): Strategy {

    val random = Randomize()
    companion object: KLogging()

    override fun evaluate() {
        if( order.isTerminal() || order.currentState.pending || !order.currentState.open ){
            return

        } else if(random.shouldDoConsideringPossibility(config.possibilityOfAmendPerEvaluation)){
            var newPrice = order.price
            var newQty = order.qty

            while(newPrice == order.price && newQty == order.qty) {
                logger.info { "Calculating amend info.  $newQty@$newPrice" }
                if (order.price.hasPrice()) {
                    newPrice = LimitPrice(order.price.get() + (random.randomSign() * 0.1))
                } else {
                    newPrice = order.price
                }
                newQty = order.qty + (random.nextIntBetweenZeroAndBoundExclusive(11) * 100)
            }
            order.amend(orderIdGenerator.get(), newQty, newPrice)

        } else if(random.shouldDoConsideringPossibility(config.possibilityOfCancelPerEvaluation)){
            order.cancel(orderIdGenerator.get())
        }
    }
}
