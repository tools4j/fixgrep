package org.tools4j.fix


/**
 * User: ben
 * Date: 14/06/2017
 * Time: 5:39 PM
 */
class AnnotatedValue(
        override val rawValue: String,
        val annotation: String,
        val position: AnnotationPosition,
        val boldValue: Boolean) : Value {

    constructor(rawValue: String, annotation: String): this(rawValue, annotation, AnnotationPosition.AFTER, false)

    val consoleAppender = ConsoleAppender()
    val htmlAppender = HtmlAppender()

    override fun toHtml(): String {
        return toDecoratedText(htmlAppender, boldValue)
    }

    override fun toConsoleText(): String {
        return toDecoratedText(consoleAppender, boldValue)
    }

    override fun toString(): String {
        return toDecoratedText(consoleAppender, false)
    }

    fun toDecoratedText(appender: Appender, doBold: Boolean): String{
        val sb = StringBuilder()
        if(position == AnnotationPosition.NONE){
            appender.appendValue(sb, doBold)
        } else if(position == AnnotationPosition.BEFORE){
            appender.appendAnnotation(sb)
            appender.appendValue(sb, doBold)
        } else {
            appender.appendValue(sb, doBold)
            appender.appendAnnotation(sb)
        }
        return sb.toString()
    }

    inner class ConsoleAppender: Appender {
        override fun appendAnnotation(sb: StringBuilder) {
            sb.append("[").append(annotation).append("]")
        }

        override fun appendValue(sb: StringBuilder, boldTag: Boolean) {
            if (boldTag) sb.append(Ansi.Bold)
            sb.append(rawValue)
            if (boldTag) sb.append(Ansi.Normal)
        }
    }

    inner class HtmlAppender: Appender {
        override fun appendAnnotation(sb: StringBuilder) {
            sb.append("<span class='value annotation'>[").append(annotation).append("]</span>")
        }

        override fun appendValue(sb: StringBuilder, boldTag: Boolean) {
            sb.append("<span class='value rawValue")
            if (boldTag) sb.append(" bold")
            sb.append("'>").append(rawValue).append("</span>")
        }
    }

    interface Appender{
        fun appendAnnotation(sb: StringBuilder);
        fun appendValue(sb: StringBuilder, boldTag: Boolean);
    }


    /**
     * Unsupported operations (as this _must_ only have a string value, because it is an ENUM value defined in the spec)
     */

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
