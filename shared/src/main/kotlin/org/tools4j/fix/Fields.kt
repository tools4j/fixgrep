package org.tools4j.fix

import java.util.stream.Collectors

/**
 * User: ben
 * Date: 15/03/2018
 * Time: 6:47 AM
 */
interface Fields: MutableList<Field>{
    fun countOfField(tag: Tag): Int
    fun getField(tag: Tag): Field?
    fun countOfField(tag: Int): Int
    fun getField(tag: Int): Field?
    fun exists(tag: Tag): Boolean
    fun exists(tag: Int): Boolean
    fun sortBy(desiredOrder: List<Int>): Fields
    fun toIntToStringMap(): Map<Int, String>
    fun toDelimitedString(delimiter: Char = '|'): String
    fun toPrettyString(delimiter: Char = '|'): String
    val pipeDelimitedString: String
    val msgTypeCode: String
    val msgTypeAndExecTypeKey: String

    fun toDelimitedString(delimiter: String): String {
        if(delimiter.length != 1) throw IllegalArgumentException("Delimiter must be one character long: [$delimiter]")
        return toDelimitedString(delimiter.toCharArray()[0])
    }

    fun toPrettyString(delimiter: String): String {
        if(delimiter.length != 1) throw IllegalArgumentException("Delimiter must be one character long: [$delimiter]")
        return toPrettyString(delimiter.toCharArray()[0])
    }

    class AnnotationSpec(val tagAnnotationPosition: AnnotationPosition, val valueAnnotationPosition: AnnotationPosition){
        fun annotateFields(fields: Fields): Fields{
            return FieldsImpl(fields.stream().map { annotateField(it) }.collect(Collectors.toList()))
        }
        
        fun annotateField(field: Field): Field{
            return AnnotatedField(field, this)
        }

        companion object {
            val specs: MutableMap<String, AnnotationSpec> = HashMap()
            val OUTSIDE_ANNOTATED = AnnotationSpec(AnnotationPosition.BEFORE, AnnotationPosition.AFTER)

            init{
                specs["outsideAnnotated"] = AnnotationSpec(AnnotationPosition.BEFORE, AnnotationPosition.AFTER)
                specs["insideAnnotated"] = AnnotationSpec(AnnotationPosition.AFTER, AnnotationPosition.BEFORE)
                specs["ab"] = AnnotationSpec(AnnotationPosition.AFTER, AnnotationPosition.BEFORE)
                specs["ba"] = AnnotationSpec(AnnotationPosition.BEFORE, AnnotationPosition.AFTER)
                specs["bb"] = AnnotationSpec(AnnotationPosition.BEFORE, AnnotationPosition.BEFORE)
                specs["aa"] = AnnotationSpec(AnnotationPosition.AFTER, AnnotationPosition.AFTER)
                specs["a_"] = AnnotationSpec(AnnotationPosition.AFTER, AnnotationPosition.NONE)
                specs["b_"] = AnnotationSpec(AnnotationPosition.BEFORE, AnnotationPosition.NONE)
                specs["_a"] = AnnotationSpec(AnnotationPosition.NONE, AnnotationPosition.AFTER)
                specs["_b"] = AnnotationSpec(AnnotationPosition.NONE, AnnotationPosition.BEFORE)
                specs["__"] = AnnotationSpec(AnnotationPosition.NONE, AnnotationPosition.NONE)
            }

            fun parseAnnotationSpec(spec: String): AnnotationSpec {
                var returnSpec = specs.get(spec)
                if(returnSpec == null){
                    throw IllegalArgumentException("Unknown spec [" + spec + "] legal specs are: " + specs.keys)
                }
                return returnSpec
            }
        }
    }

    enum class AnnotationPosition{
        BEFORE,
        AFTER,
        NONE;
    }

    companion object {
        fun getMsgTypeAndExecTypeKey(msgType: String, execType: String?): String{
            return if(execType != null) (msgType + "." + execType) else msgType
        }

        fun getMsgTypeAndExecTypeKey(msgType: FixMessageType, execType: ExecType?): String{
            return if(execType != null) (msgType.code + "." + execType.code) else msgType.code
        }
    }
}