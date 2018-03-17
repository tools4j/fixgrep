package org.tools4j.fixgrep

import org.tools4j.fix.Fields

/**
 * User: ben
 * Date: 16/03/2018
 * Time: 6:22 AM
 */
interface LabellingCriteria{
    fun shouldLabel(originalFields: Fields, line: String): Boolean

    class AlwaysTrueCriteria: LabellingCriteria {
        override fun shouldLabel(originalFields: Fields, line: String): Boolean {
            return true
        }
    }

    class AlwaysFalseCriteria: LabellingCriteria {
        override fun shouldLabel(originalFields: Fields, line: String): Boolean {
            return false
        }
    }
}

class TagsMatchCriteria(val criteria: Map<Int, String>): LabellingCriteria{
    override fun shouldLabel(originalFields: Fields, line: String): Boolean {
        for(check in criteria){
            val field = originalFields.getField(check.key)
            if(field == null) return false
            if(!field.value.toString().equals(check.value)){
                return false
            }
        }
        return true
    }

    class Builder{
        val map: MutableMap<Int, String> = HashMap()

        fun with(tag:Int, value:String): Builder {
            map.put(tag, value)
            return this
        }

        fun build(): TagsMatchCriteria {
            return TagsMatchCriteria(map)
        }
    }
}