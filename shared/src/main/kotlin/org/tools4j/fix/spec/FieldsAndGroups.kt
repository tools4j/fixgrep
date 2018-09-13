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

    fun toIndentedString(indent: String): String{
        val sb = StringBuilder()
        if(headerStr != null) sb.append(headerStr).append("\n")
        val childIndent = if(increaseIndentForChildren) indent + FixSpecDefinition.INDENT else indent
        fields.forEach { sb.append(childIndent).append(it).append("\n") }
        groups.forEach { sb.append(it.toIndentedString(childIndent))}
        return sb.toString()
    }

    fun toGroupsIndentedString(indent: String): String{
        val sb = StringBuilder()
        if(headerStr != null) sb.append(headerStr).append("\n")
        val childIndent = if(increaseIndentForChildren) indent + FixSpecDefinition.INDENT else indent
        groups.forEach { sb.append(it.toGroupsIndentedString(childIndent)) }
        return sb.toString()
    }
}