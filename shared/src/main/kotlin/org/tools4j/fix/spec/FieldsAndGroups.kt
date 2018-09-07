package org.tools4j.fix.spec

/**
 * User: benjw
 * Date: 9/6/2018
 * Time: 6:28 AM
 */
interface FieldsAndGroups {
    val fields: Set<FieldSpec>
    val groups: Set<GroupSpec>
    val headerStr: String?
    val increaseIndentForChildren: Boolean

    fun getGroupByLeadingFieldNumber(leadingFieldNumber: Int): GroupSpec?
    fun toIndentedString(indent: String): String
    fun toGroupsIndentedString(indent: String): String
}