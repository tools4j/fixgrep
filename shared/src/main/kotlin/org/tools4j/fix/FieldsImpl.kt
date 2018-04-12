package org.tools4j.fix

/**
 * User: ben
 * Date: 20/6/17
 * Time: 5:43 PM
 */
class FieldsImpl(val fields: List<Field>) : ArrayList<Field>(fields), Fields {
    constructor(str: String, delimiter: Char) : this(FieldsFromDelimitedString(str, delimiter).fields)
    constructor(str: String, delimiter: String) : this(FieldsFromDelimitedString(str, delimiter).fields)

    override fun countOfField(tag: Tag): Int {
        return countOfField(tag.tag)
    }

    override fun getField(tag: Tag): Field? {
        return getField(tag.tag)
    }

    override fun countOfField(tag: Int): Int {
        return fields.count { it.tag.tag == tag }
    }

    override fun getField(tag: Int): Field? {
        if(countOfField(tag) > 1) throw IllegalArgumentException("More than one tag [$tag] exists in fields: $fields")
        return fields.firstOrNull { it.tag.tag == tag }
    }

    override fun exists(tag: Tag): Boolean {
        return countOfField(tag) > 0
    }

    override fun exists(tag: Int): Boolean {
        return countOfField(tag) > 0
    }

    override fun toString(): String {
        return stringValue
    }

    private val stringValue: String by lazy {
        val prettyFields = AnnotationSpec.OUTSIDE_ANNOTATED.annotateFields(this)
        prettyFields.toPrettyString()
    }

    override fun sortBy(desiredOrder: List<Int>): Fields {
        val existingFields = ArrayList(fields)
        val outputFields = ArrayList<Field>()
        for(tag in desiredOrder){
            var foundAtIndex: Int? = null
            for(i in existingFields.indices){
                if(existingFields[i].tag.tag == tag){
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

    override fun toIntToStringMap(): Map<Int, String> {
        return fields.map { it.tag.tag to it.value.toString() }.toMap()
    }

    override fun toDelimitedString(delimiter: Char): String {
        return fields.joinToString(""+delimiter)
    }

    override val pipeDelimitedString: String by lazy {
        toDelimitedString()
    }

    override fun toPrettyString(delimiter: Char): String {
        val sb = StringBuilder()
        for (i in 0 until fields.size) {
            if (sb.length > 0) sb.append(delimiter)
            sb.append(fields[i].toPrettyString())
        }
        return sb.toString()
    }

    override val msgTypeCode: String by lazy {
        getField(35)!!.stringValue()
    }

    override val msgTypeAndExecTypeKey: String by lazy {
        Fields.getMsgTypeAndExecTypeKey(getField(35)!!.stringValue(), getField(150)?.stringValue())
    }
}
