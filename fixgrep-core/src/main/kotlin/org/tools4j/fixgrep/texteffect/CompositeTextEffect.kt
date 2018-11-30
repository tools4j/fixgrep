package org.tools4j.fixgrep.texteffect

import java.util.stream.Collectors

/**
 * User: ben
 * Date: 5/04/2018
 * Time: 6:55 AM
 */
class CompositeTextEffect(val textEffects: Set<TextEffect>) : TextEffect {
    constructor(textEffect1: TextEffect, textEffect2: TextEffect) : this(linkedSetOf(textEffect1, textEffect2))
    constructor(textEffects: List<TextEffect>) : this(LinkedHashSet(textEffects))

    override fun contains(textEffect: TextEffect): Boolean {
        textEffects.forEach {
            if (it.contains(textEffect)) {
                return true
            }
        }
        return false
    }

    override val consoleTextAfter: String by lazy {
        textEffects.stream().map { it.consoleTextAfter }.filter{ !it.isEmpty() }.collect(Collectors.toSet()).joinToString("")
    }
    override val consoleTextBefore: String by lazy {
        textEffects.stream().map { it.consoleTextBefore }.collect(Collectors.toList()).joinToString("")
    }

    override val htmlClass: String by lazy {
        textEffects.stream().map { it.htmlClass }.filter { !it.isEmpty() }.collect(Collectors.toList()).joinToString(" ")
    }

    override fun equals(other: Any?): Boolean {
        return TextEffect.equals(this, other)
    }

    override fun hashCode(): Int {
        return TextEffect.hashCode(this)
    }

    override fun toString(): String {
        return "CompositeTextEffect(textEffects=$textEffects)"
    }
}