package org.tools4j.fix.spec

data class FieldsAndGroupsImpl(private val _fields: Set<FieldSpec>, private val _groups: Set<GroupSpec>, private val _components: Set<ComponentSpec>) : FieldsAndGroups {
    val groupsByLeadingFieldNumber: Map<Int, GroupSpec> by lazy {
        groups.map { it.leadingField.number to it }.toMap()
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
}