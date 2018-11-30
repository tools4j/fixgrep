package org.tools4j.fixgrep.texteffect

/**
 * User: ben
 * Date: 25/04/2018
 * Time: 6:21 AM
 */
class NullTextEffect(): TextEffect {
    override val consoleTextBefore: String = ""
    override val consoleTextAfter: String = ""
    override val htmlClass: String = ""

    override fun contains(textEffect: TextEffect): Boolean {
        return this.equals(textEffect)
    }

    override fun equals(other: Any?): Boolean {
        return TextEffect.equals(this, other)
    }

    override fun hashCode(): Int {
        return TextEffect.hashCode(this)
    }
}