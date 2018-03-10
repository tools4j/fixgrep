package org.tools4j.model.fix.messages;

/**
 * User: ben
 * Date: 26/02/2018
 * Time: 6:18 PM
 */
interface DelegatingMessageHandler: MessageHandler {

    override fun handle(msg: Message) {
        if(!delegateIfRequired(msg)){
            beforeDelegation(msg)
            handleNonDelegated(msg)
            afterDelegation(msg)
        }
    }

    fun beforeDelegation(msg: Message){
        //override as required
    }

    fun afterDelegation(msg: Message){
        //override as required
    }

    fun handleNonDelegated(msg: Message){
        throw UnsupportedOperationException("Message received which was not delegated, and handleNonDelegated has not been overridden: $msg")
    }

    fun getDelegate(message: Message): MessageHandler?{
        return null;
    }

    fun delegateMessage(message: Message): Boolean{
        return false;
    }

    fun delegateAllMessages(): Boolean{
        return false;
    }

    fun delegateIfRequired(message: Message): Boolean {
        if (( delegateAllMessages() || delegateMessage(message)
                        && getDelegate(message) != null)){

            getDelegate(message)!!.handle(message)
            return true
        }
        return false
    }
}
