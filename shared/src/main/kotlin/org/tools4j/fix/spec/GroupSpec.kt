package org.tools4j.fix.spec

data class GroupSpec(val leadingField: FieldSpec, val fieldsAndGroups: FieldsAndGroups): FieldsAndGroups by fieldsAndGroups {

    val name :String by lazy {
        leadingField.name
    }

    val firstField: FieldSpec? by lazy {
        if(fields.isEmpty()){
            null
        } else {
            fields.first()
        }
    }

    override val headerStr: String by lazy {
        "Group:" + name + ":" + (firstField?.name ?: "<n/a>")
    }
}