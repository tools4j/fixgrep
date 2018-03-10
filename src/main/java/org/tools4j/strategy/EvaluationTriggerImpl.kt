package org.tools4j.strategy

/**
 * User: ben
 * Date: 28/02/2018
 * Time: 6:36 AM
 */
class EvaluationTriggerImpl: EvaluationTrigger, Evaluatable {
    val subscribers = HashSet<Evaluatable>()

    override fun subscribe(evaluatable: Evaluatable) {
        subscribers.add(evaluatable)
    }

    override fun evaluate() {
        subscribers.forEach { it.evaluate() }
    }
}
