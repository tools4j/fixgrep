package org.tools4j.fixgrep.texteffect

/**
 * User: ben
 * Date: 25/04/2018
 * Time: 6:21 AM
 */
class HtmlOnlyTextEffect(override val name: String): TextEffect {
    override val consoleTextAfter: String = ""
    override val consoleTextBefore: String = ""
    override val htmlClass: String by lazy { name }
}