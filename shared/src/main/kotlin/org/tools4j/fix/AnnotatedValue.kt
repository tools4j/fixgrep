package org.tools4j.fix


/**
 * User: ben
 * Date: 14/06/2017
 * Time: 5:39 PM
 */
class AnnotatedValue (override val rawValue: String, val annotation: String) : Value {

    fun toAnnotatedString(position: AnnotatedField.AnnotationPosition, boldValue: Boolean): String{
        val sb = StringBuilder()
        if(position == AnnotatedField.AnnotationPosition.NONE){
            appendValue(sb, boldValue)
        } else if(position == AnnotatedField.AnnotationPosition.BEFORE){
            appendAnnotation(sb)
            appendValue(sb, boldValue)
        } else {
            appendValue(sb, boldValue)
            appendAnnotation(sb)
        }
        return sb.toString()
    }

    private fun appendAnnotation(sb: StringBuilder) {
        sb.append("[").append(annotation).append("]")
    }

    private fun appendValue(sb: StringBuilder, boldTag: Boolean) {
        if (boldTag) sb.append(AnnotatedField.Bold)
        sb.append(rawValue)
        if (boldTag) sb.append(AnnotatedField.Normal)
    }

    override fun intValue(): Int {
        throw UnsupportedOperationException()
    }

    override fun doubleValue(): Double {
        throw UnsupportedOperationException()
    }

    override fun longValue(): Long {
        throw UnsupportedOperationException()
    }

    override fun booleanValue(): Boolean {
        throw UnsupportedOperationException()
    }

    override fun priceValue(): Price {
        throw UnsupportedOperationException()
    }

    override fun longValueFromUTCTimestamp(): Long {
        throw UnsupportedOperationException()
    }

    override fun sideValue(): Side {
        throw UnsupportedOperationException()
    }

    override fun orderTypeValue(): OrderType {
        throw UnsupportedOperationException()
    }

    override fun idValue(): Id {
        throw UnsupportedOperationException()
    }

    override fun execTypeValue(): ExecType {
        throw UnsupportedOperationException()
    }

    override fun ordStatusValue(): OrdStatus {
        throw UnsupportedOperationException()
    }

    override fun toString(): String {
        return rawValue
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AnnotatedValue) return false

        if (rawValue != other.rawValue) return false

        return true
    }

    override fun hashCode(): Int {
        return rawValue.hashCode()
    }
}
