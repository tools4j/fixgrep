package org.tools4j.fix

/**
 * User: ben
 * Date: 14/06/2017
 * Time: 5:36 PM
 */
class AnnotatedTag(
        override val tag: Int,
        val annotation: String) : Tag {

    override fun toString(): String {
        return toAnnotatedString(AnnotatedField.AnnotationPosition.BEFORE, false)
    }

    fun toAnnotatedString(position: AnnotatedField.AnnotationPosition, boldTag: Boolean): String{
        val sb = StringBuilder()
        if(position == AnnotatedField.AnnotationPosition.NONE){
            appendTag(sb, boldTag)
        } else if(position == AnnotatedField.AnnotationPosition.BEFORE){
            appendAnnotation(sb)
            appendTag(sb, boldTag)
        } else {
            appendTag(sb, boldTag)
            appendAnnotation(sb)
        }
        return sb.toString()
    }

    private fun appendAnnotation(sb: StringBuilder) {
        sb.append("[").append(annotation).append("]")
    }

    private fun appendTag(sb: StringBuilder, boldTag: Boolean) {
        if (boldTag) sb.append(AnnotatedField.Bold)
        sb.append(tag)
        if (boldTag) sb.append(AnnotatedField.Normal)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AnnotatedTag) return false

        if (tag != other.tag) return false

        return true
    }

    override fun hashCode(): Int {
        return tag.hashCode()
    }

    constructor(tag: String, description: String): this(Integer.parseInt(tag), description)
}
