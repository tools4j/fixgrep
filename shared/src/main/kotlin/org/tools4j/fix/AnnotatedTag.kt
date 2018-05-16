package org.tools4j.fix

/**
 * User: ben
 * Date: 14/06/2017
 * Time: 5:36 PM
 */
class AnnotatedTag(
        override val tag: Int,
        val annotation: String,
        val position: AnnotationPosition,
        val boldTag: Boolean) : Tag {

    val consoleAppender = ConsoleAppender()
    val htmlAppender = HtmlAppender()

    override fun toString(): String {
        return toDecoratedText(consoleAppender, false)
    }

    override fun toHtml(): String {
        return toDecoratedText(htmlAppender, boldTag)
    }

    override fun toConsoleText(): String{
        return toDecoratedText(consoleAppender, boldTag)
    }

    fun toDecoratedText(appender: Appender, doBold: Boolean): String{
        val sb = StringBuilder()
        if(position == AnnotationPosition.NONE){
            appender.appendTag(sb, doBold)
        } else if(position == AnnotationPosition.BEFORE){
            appender.appendAnnotation(sb)
            appender.appendTag(sb, doBold)
        } else {
            appender.appendTag(sb, doBold)
            appender.appendAnnotation(sb)
        }
        return sb.toString()
    }

    inner class ConsoleAppender: Appender {
        override fun appendAnnotation(sb: StringBuilder) {
            sb.append("[").append(annotation).append("]")
        }

        override fun appendTag(sb: StringBuilder, boldTag: Boolean) {
            if (boldTag) sb.append(Ansi.Bold)
            sb.append(tag)
            if (boldTag) sb.append(Ansi.Normal)
        }
    }

    inner class HtmlAppender: Appender {
        override fun appendAnnotation(sb: StringBuilder) {
            sb.append("<span class='tag annotation'>[").append(annotation).append("]</span>")
        }

        override fun appendTag(sb: StringBuilder, boldTag: Boolean) {
            sb.append("<span class='tag tagNumber")
            if (boldTag) sb.append(" bold")
            sb.append("'>").append(tag).append("</span>")
        }
    }

    interface Appender{
        fun appendAnnotation(sb: StringBuilder);
        fun appendTag(sb: StringBuilder, boldTag: Boolean);
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
    constructor(tag: Int, description: String): this(tag, description, AnnotationPosition.BEFORE, true)
}
