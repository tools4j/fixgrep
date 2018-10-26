package org.tools4j.fix.spec

data class FixSpecDefinition(val fieldsByName: Map<String, FieldSpec>,
                             val header: FieldsAndGroupsImpl,
                             val trailer: FieldsAndGroupsImpl,
                             val messagesByName: Map<String, MessageSpec>){

    companion object {
        val INDENT = "    "
    }

    val fieldsByNumber: Map<Int, FieldSpec> by lazy {
        fieldsByName.map { it.value.number to it.value }.toMap()
    }

    val messagesByMsgType: Map<String, MessageSpec> by lazy {
        messagesByName.map { it.value.msgType to it.value }.toMap()
    }

    fun printMessagesAndGroups(): String {
        val sb = StringBuilder()
        for(message in messagesByName.values){
            sb.append(message.toGroupsIndentedString(""))
        }
        return sb.toString()
    }
}