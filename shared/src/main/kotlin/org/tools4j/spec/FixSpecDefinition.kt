package org.tools4j.spec

data class FixSpecDefinition(val fieldsByName: Map<String, Field>,
                             val groupsByName: Map<String, Group>,
                             val messagesByName: Map<String, Message>){

    val fieldsByNumber: Map<Int, Field> by lazy {
        val map = LinkedHashMap<Int, Field>()
        fieldsByName.map { it.value.number to it }
        map
    }

    val messagesByMsgType: Map<String, Field> by lazy {
        val map = LinkedHashMap<String, Field>()
        messagesByName.map { it.value.msgType to it }
        map
    }

    data class Field(val name: String, val number: Int, val type: String, val enums: Map<String, String>)

    data class FieldEnum(val enum: String, val description: String)

    data class Group(val name: String, val fieldsAndGroups: FieldsAndGroups) {
        val fields: Set<Field> = fieldsAndGroups.fields
        val groups: Set<Group> = fieldsAndGroups.groups

        fun toIndentedString(previousIndent: String): String {
            val sb = StringBuilder()
            sb.append(previousIndent).append("Group:").append(name).append("\n")
            sb.append(fieldsAndGroups.toIndentedString(previousIndent))
            return sb.toString()
        }
    }

    data class Message(val name: String, val msgType: String, val fieldsAndGroups: FieldsAndGroups){
        val fields: Set<Field> = fieldsAndGroups.fields
        val groups: Set<Group> = fieldsAndGroups.groups

        override fun toString(): String {
            val sb = StringBuilder()
            sb.append("Message:$name:$msgType\n")
            sb.append(fieldsAndGroups.toIndentedString(""))
            return sb.toString()
        }
    }

    data class FieldsAndGroups(val fields: MutableSet<Field>, val groups: MutableSet<Group>){
        constructor(): this(HashSet(), HashSet())
        fun addField(field: Field) = fields.add(field)
        fun addGroup(group: Group) = groups.add(group)

        fun add(fieldsAndGroups: FieldsAndGroups){
            fields.addAll(fieldsAndGroups.fields)
            groups.addAll(fieldsAndGroups.groups)
        }

        fun toIndentedString(previousIndent: String): String{
            val sb = StringBuilder()
            val indent = previousIndent + FixSpecParser.INDENT
            fields.forEach { sb.append(indent).append(it).append("\n") }
            groups.forEach { sb.append(it.toIndentedString(indent))}
            return sb.toString()
        }
    }
}