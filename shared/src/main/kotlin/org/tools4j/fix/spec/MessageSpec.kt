package org.tools4j.fix.spec

data class MessageSpec(val name: String, val msgType: String, val fieldsAndGroups: FieldsAndGroups): FieldsAndGroups by fieldsAndGroups{
    override val headerStr: String by lazy {
        "Message:$msgType:$name"
    }

    override fun toString(): String {
        return toIndentedString("")
    }
}