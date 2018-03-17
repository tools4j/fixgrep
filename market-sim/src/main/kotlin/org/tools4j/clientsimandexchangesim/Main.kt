package org.tools4j.clientsimandexchangesim

import org.tools4j.clientsim.ClientSimFactory
import org.tools4j.exhangesim.ExchangeFactory
import org.tools4j.fix.Fix50SP2FixSpecFromClassPath
import org.tools4j.model.RandomLongGivenAverage
import org.tools4j.strategy.EvaluationTriggerImpl
import java.io.File

/**
 * User: ben
 * Date: 8/02/2018
 * Time: 6:20 PM
 */
object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        val fixSpec = Fix50SP2FixSpecFromClassPath().load();
        val evaluationTrigger = EvaluationTriggerImpl()

        //ClientSim
        val clientSimFactory = ClientSimFactory("CLIENT_SIM", "ACME_EXCHANGE", fixSpec, evaluationTrigger)

        //Exchange
        val exchangeStrategyFactory = ExchangeFactory("ACME_EXCHANGE", "CLIENT_SIM", clientSimFactory.fixSession, fixSpec, evaluationTrigger)

        //Wire the exchange back into the clientSim
        clientSimFactory.fixSession.counterparty = exchangeStrategyFactory.fixSession

        //Run
        while(!Thread.currentThread().isInterrupted){
            evaluationTrigger.evaluate()
            try {
                Thread.sleep(RandomLongGivenAverage(100).toLong())
            } catch (e: InterruptedException) {
                e.printStackTrace()
                Thread.currentThread().interrupt()
                return
            }
        }
    }
}