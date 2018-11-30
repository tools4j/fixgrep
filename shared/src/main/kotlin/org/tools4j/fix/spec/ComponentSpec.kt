package org.tools4j.fix.spec

data class ComponentSpec(val name: String, val fieldsAndGroups: FieldsAndGroups): FieldsAndGroups by fieldsAndGroups{
    override val increaseIndentForChildren = false
}