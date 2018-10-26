package org.tools4j.fix.spec

data class MessageSpec(val name: String, val msgType: String, val fieldsAndGroups: FieldsAndGroups): FieldsAndGroups by fieldsAndGroups{
    constructor(name: String, msgType: String, messageFieldsAndGroups: FieldsAndGroups, headerFieldsAndGroups: FieldsAndGroups, trailerFieldsAndGroups: FieldsAndGroups)
        :this(name, msgType, CompositeFieldsAndGroups(listOf(messageFieldsAndGroups, headerFieldsAndGroups, trailerFieldsAndGroups), "Message:$msgType:$name"))

    override val headerStr: String by lazy {
        "Message:$msgType:$name"
    }

    override fun toString(): String {
        return headerStr
    }
}