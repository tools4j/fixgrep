package org.tools4j.fix.spec;

/**
 * User: benjw
 * Date: 9/12/2018
 * Time: 4:58 PM
 */
class CompositeFieldsAndGroups(private val fieldsAndGroupsList: List<FieldsAndGroups>, private val header: String?): FieldsAndGroups {
    override val fields: Set<FieldSpec> by lazy {
        fieldsAndGroupsList.flatMap { it.fields }.toSet()
    }
    override val groups: Set<GroupSpec> by lazy {
        fieldsAndGroupsList.flatMap { it.groups }.toSet()
    }

    val groupsByLeadingFieldNumber: Map<Int, GroupSpec> by lazy {
        groups.map { it.leadingField.number to it }.toMap()
    }

    override val headerStr: String? by lazy {
        header
    }

    override val increaseIndentForChildren: Boolean by lazy {
        fieldsAndGroupsList.first().increaseIndentForChildren
    }

    override fun getGroupByLeadingFieldNumber(leadingFieldNumber: Int): GroupSpec? {
        return groupsByLeadingFieldNumber.get(leadingFieldNumber)
    }
}
