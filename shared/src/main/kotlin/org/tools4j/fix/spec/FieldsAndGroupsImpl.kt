package org.tools4j.fix.spec

data class FieldsAndGroupsImpl(private val _fields: Set<FieldSpec>, private val _groups: Set<GroupSpec>, private val _components: Set<ComponentSpec>) : FieldsAndGroups {

    val groupsByLeadingFieldNumber: Map<Int, GroupSpec> by lazy {
        _groups.map { it.leadingField.number to it }.toMap()
    }

    override val headerStr: String?
        get() = null

    override val increaseIndentForChildren = true

    override fun getGroupByLeadingFieldNumber(leadingFieldNumber: Int): GroupSpec? {
        return groupsByLeadingFieldNumber[leadingFieldNumber]
    }

    override val fields: Set<FieldSpec> by lazy {
        val fields = LinkedHashSet<FieldSpec>(_fields)
        fields.addAll(_components.flatMap { it.fieldsAndGroups.fields })
        fields
    }

    override val groups: Set<GroupSpec> by lazy {
        val groups = LinkedHashSet<GroupSpec>(_groups)
        groups.addAll(_components.flatMap { it.fieldsAndGroups.groups })
        groups
    }

    override fun toIndentedString(indent: String): String{
        val sb = StringBuilder()
        if(headerStr != null) sb.append(headerStr).append("\n")
        val childIndent = if(increaseIndentForChildren) indent + FixSpecDefinition.INDENT else indent
        _fields.forEach { sb.append(childIndent).append(it).append("\n") }
        _groups.forEach { sb.append(it.toIndentedString(childIndent))}
        _components.forEach { sb.append(it.toIndentedString(indent))}
        return sb.toString()
    }

    override fun toGroupsIndentedString(indent: String): String{
        val sb = StringBuilder()
        if(headerStr != null) sb.append(headerStr).append("\n")
        val childIndent = if(increaseIndentForChildren) indent + FixSpecDefinition.INDENT else indent
        _groups.forEach { sb.append(it.toGroupsIndentedString(childIndent)) }
        _components.forEach { sb.append(it.toGroupsIndentedString(childIndent)) }
        return sb.toString()
    }
}