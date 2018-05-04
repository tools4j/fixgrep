package org.tools4j.properties

/**
 * User: ben
 * Date: 30/10/17
 * Time: 5:51 PM
 */
class IndentableStringBuilder(private val indent: String) {
    private val sb: StringBuilder
    private var indentActivated: Boolean = false

    init {
        this.indentActivated = false
        sb = StringBuilder()
    }

    fun append(str: String): IndentableStringBuilder {
        var str = str
        if (indentActivated) {
            appendIndentIfLastCharacterWasLineEnding()
            //Append indents to any other line endings which are NOT at the end of the line
            str = str.replace("\n(?!$)".toRegex(), "\n" + indent)
        }
        sb.append(str)
        return this
    }

    fun append(obj: Any): IndentableStringBuilder {
        return append(obj.toString())
    }

    fun activateIndent() {
        indentActivated = true
        appendIndentIfLastCharacterWasLineEnding()
    }

    private fun appendIndentIfLastCharacterWasLineEnding() {
        if (sb.lastIndexOf("\n") == sb.length - 1) {
            sb.append(indent)
        }
    }

    fun decactivateIndent() {
        if (sb.lastIndexOf("\n" + indent) == sb.length - 1 - indent.length) {
            sb.replace(sb.length - 1 - indent.length, sb.length - 1, "\n")
        }
        indentActivated = false
    }

    override fun toString(): String {
        return sb.toString()
    }
}
