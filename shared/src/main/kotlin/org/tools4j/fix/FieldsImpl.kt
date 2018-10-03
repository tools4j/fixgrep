package org.tools4j.fix

import java.util.stream.Collectors

/**
 * User: ben
 * Date: 20/6/17
 * Time: 5:43 PM
 */
open class FieldsImpl(val fields: List<Field>) : ArrayList<Field>(fields), Fields {
    constructor(str: String, inputDelimiter: String) : this(FieldsFromDelimitedString(str, inputDelimiter).fields)

    override val fieldsAsSet: Set<Field> by lazy {
        HashSet<Field>(fields)
    }

    override fun filterFields(predicate: (Field) -> Boolean): Fields {
        return FieldsImpl(this.filter(predicate).toList())
    }

    override fun accept(fieldsVisitor: FieldsVisitor) {
        fieldsVisitor.visit(this)
        for(field in fields){
            field.accept(fieldsVisitor.getFieldVisitor())
        }
    }

    override fun countOfField(tag: Tag): Int {
        return countOfField(tag.number)
    }

    override fun getField(tag: Tag): Field? {
        return getField(tag.number)
    }

    override fun countOfField(tag: Int): Int {
        return fields.count { it.tag.number == tag }
    }

    override fun getField(tag: Int): Field? {
        if(countOfField(tag) > 1) throw IllegalArgumentException("More than one tag [$tag] exists in fields: $fields")
        return fields.firstOrNull { it.tag.number == tag }
    }

    override fun exists(tag: Tag): Boolean {
        return countOfField(tag) > 0
    }

    override fun exists(tag: Int): Boolean {
        return countOfField(tag) > 0
    }

    override fun toString(): String {
        return fields.joinToString("|")
    }

    override fun exclude(excludeTags: List<Int>): Fields {
        if(excludeTags.isEmpty()){
            return this
        }
        val outputFields = FieldsImpl(ArrayList(fields))
        excludeTags.forEach { exclude ->
            outputFields.removeIf {it.tag.number == exclude}
        }
        return FieldsImpl(outputFields)
    }

    override fun includeOnly(onlyIncludeTags: List<Int>): Fields {
        if(onlyIncludeTags.isEmpty()){
            return this
        }
        val newFields = fields.stream().filter { onlyIncludeTags.contains(it.tag.number) }.collect(Collectors.toList())
        return FieldsImpl(newFields)
    }

    override fun sortBy(desiredOrder: List<Int>): Fields {
        if(desiredOrder.isEmpty()){
            return this
        }
        val existingFields = ArrayList(fields)
        val outputFields = ArrayList<Field>()
        for(tag in desiredOrder){
            var foundAtIndex: Int? = null
            for(i in existingFields.indices){
                if(existingFields[i].tag.number == tag){
                    if(foundAtIndex != null){
                        //We have already found this tag, this means it's probably part of a repeating group.  For now
                        //sorting of members inside a repeating group is not supported.
                        foundAtIndex = null
                        break
                    }
                    foundAtIndex = i
                }
            }
            if(foundAtIndex != null){
                outputFields.add(existingFields.removeAt(foundAtIndex))
            }
        }
        outputFields.addAll(existingFields)
        return FieldsImpl(outputFields)
    }

    override fun hasRepeatingTags(): Boolean {
        return HashSet(fields.map { it.tag.number }).size < fields.size
    }

    override fun toIntToStringMap(): Map<Int, String> {
        if(hasRepeatingTags()){
            throw UnsupportedOperationException("Repeating tags have been detected in these fields, therefore it is not possible to return an intToStringMap")
        }
        return fields.map { it.tag.number to it.value.toString() }.toMap()
    }

    override fun toDelimitedString(delimiter: Char): String {
        return fields.joinToString(""+delimiter)
    }

    override val pipeDelimitedString: String by lazy {
        toDelimitedString()
    }

    override val msgTypeCode: String by lazy {
        getField(35)!!.stringValue()
    }

    override val msgTypeAndExecTypeKey: String by lazy {
        Fields.getMsgTypeAndExecTypeKey(getField(35)!!.stringValue(), getField(150)?.stringValue())
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is FieldsImpl) return false
        if (fields != other.fields) return false
        return true
    }

    override fun hashCode(): Int {
        return fields.hashCode()
    }
}
