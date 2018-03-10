package org.tools4j.strategy

/**
 * User: ben
 * Date: 28/02/2018
 * Time: 6:33 AM
 */
interface EvaluationTrigger {
    fun subscribe(evaluatable: Evaluatable)
}