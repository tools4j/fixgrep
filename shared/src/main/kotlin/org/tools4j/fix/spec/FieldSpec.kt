package org.tools4j.fix.spec

data class FieldSpec(val name: String, val number: Int, val type: String, val enums: Set<FieldEnum>){
    val enumsByCode: Map<String, String> by lazy {
        enums.map{it.enum to it.description}.toMap()
    }

    override fun toString(): String {
        return "Field:$number:$name"
    }
}