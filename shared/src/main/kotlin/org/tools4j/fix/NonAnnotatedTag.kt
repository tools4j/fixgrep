package org.tools4j.fix

/**
 * User: ben
 * Date: 11/05/2018
 * Time: 5:39 PM
 */
class NonAnnotatedTag(tag: Int, val bold: Boolean): RawTag(tag) {
    override fun toHtml(): String {
        var html = "<span class='tag";
        if(bold) html += " bold"
        html += "'>$tag</span>"
        return html
    }

    override fun toConsoleText(): String {
        var text = ""
        if(bold) text += Ansi.Bold
        text += tag
        if(bold) text += Ansi.Normal
        return text
    }
}