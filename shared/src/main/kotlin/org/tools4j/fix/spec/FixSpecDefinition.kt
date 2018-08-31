package org.tools4j.fix.spec

data class FixSpecDefinition(val fieldsByName: Map<String, Field>,
                             val header: FieldsAndGroups,
                             val trailer: FieldsAndGroups,
                             val groupsByName: Map<String, Group>,
                             val messagesByName: Map<String, Message>){

    val fieldsByNumber: Map<Int, Field> by lazy {
        val map = LinkedHashMap<Int, Field>()
        fieldsByName.map { it.value.number to it }
        map
    }

    val messagesByMsgType: Map<String, Message> by lazy {
        messagesByName.map { it.value.msgType to it.value }.toMap()
    }

    val groupsByFirstFieldNumber: Map<Int, Group> by lazy {
        val map = LinkedHashMap<Int, Group>()
        groupsByName.map { it.value.fields.first().number to it }
        map
    }

    data class Field(val name: String, val number: Int, val type: String, val enums: Set<FieldEnum>){
        val enumsByCode: Map<String, String> by lazy {
            enums.map{it.enum to it.description}.toMap()
        }
    }

    data class FieldEnum(val enum: String, val description: String){
        override fun toString(): String {
            return "$enum=$description"
        }
    }

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

    data class FieldsAndGroups(private val _fields: MutableSet<Field>, private val _groups: MutableSet<Group>, private val _components: MutableSet<Component>){
        constructor(): this(LinkedHashSet(), LinkedHashSet(), LinkedHashSet())

        val fields: Set<Field> by lazy {
            val fields = LinkedHashSet<Field>(_fields)
            fields.addAll(_components.flatMap { it.fieldsAndGroups.fields })
            fields
        }

        val groups: Set<Group> by lazy {
            val groups = LinkedHashSet<Group>(_groups)
            groups.addAll(_components.flatMap { it.fieldsAndGroups.groups })
            groups
        }

        fun addField(field: Field) = _fields.add(field)
        fun addGroup(group: Group) = _groups.add(group)
        fun addComponent(component: Component) = _components.add(component)

        fun add(other: FieldsAndGroups){
            _fields.addAll(other._fields)
            _groups.addAll(other._groups)
            _components.addAll(other._components)
        }

        fun toIndentedString(previousIndent: String): String{
            val sb = StringBuilder()
            val indent = previousIndent + FixSpecParser.INDENT
            _fields.forEach { sb.append(indent).append(it).append("\n") }
            _groups.forEach { sb.append(it.toIndentedString(indent))}
            _components.forEach { sb.append(it.toIndentedString(indent))}
            return sb.toString()
        }
    }

    data class Component(val name: String, val fieldsAndGroups: FixSpecDefinition.FieldsAndGroups){
        fun toIndentedString(previousIndent: String): String {
            val sb = StringBuilder()
            sb.append(previousIndent).append("Component:").append(name).append("\n")
            sb.append(fieldsAndGroups.toIndentedString(previousIndent))
            return sb.toString()
        }
    }
}