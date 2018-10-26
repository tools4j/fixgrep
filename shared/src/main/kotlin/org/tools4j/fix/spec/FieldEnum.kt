package org.tools4j.fix.spec

data class FieldEnum(val enum: String, val description: String){
    override fun toString(): String {
        return "$enum=$description"
    }
}