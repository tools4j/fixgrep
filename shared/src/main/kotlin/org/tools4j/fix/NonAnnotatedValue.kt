package org.tools4j.fix

/**
 * User: ben
 * Date: 14/06/2017
 * Time: 5:39 PM
 */
class NonAnnotatedValue(rawValue: String, val bold: Boolean = false) : RawValue(rawValue) {
    override fun toHtml(): String {
        var html = "<span class='value rawValue"
        if(bold) html += " bold"
        html += "'>$rawValue</span>"
        return html
    }

    override fun toConsoleText(): String {
        var text = ""
        if(bold) text += Ansi.Bold
        text += rawValue
        if(bold) text += Ansi.Normal
        return text
    }
}
