package org.tools4j.fix

/**
 * User: ben
 * Date: 15/05/2018
 * Time: 5:34 AM
 */
class AnnotatedField(tag: Tag, value: Value, val annotationSpec: AnnotationSpec): FieldImpl(tag, value) {
    override fun toConsoleText(): String {
        val sb = StringBuilder()
        sb.append(tag.toConsoleText())
        if(annotationSpec.boldTagAndValue) sb.append(Ansi.Bold)
        sb.append("=")
        if(annotationSpec.boldTagAndValue) sb.append(Ansi.Normal)
        sb.append(value.toConsoleText())
        return sb.toString()
    }

    override fun toHtml(): String {
        val sb = StringBuilder()
        sb.append("<span class='field annotatedField'>")
        sb.append(tag.toHtml())
        sb.append("<span class='equals");
        if(annotationSpec.boldTagAndValue) sb.append(" bold")
        sb.append("'>=</span>")
        sb.append(value.toHtml())
        sb.append("</span>")
        return sb.toString()
    }
}