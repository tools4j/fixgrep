package org.tools4j.model.state

/**
 * User: ben
 * Date: 20/7/17
 * Time: 7:03 AM
 */
interface StateMachine<S: State> {
    fun changeState(newState: S)
}
