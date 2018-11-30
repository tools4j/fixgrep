package org.tools4j.fixgrep.texteffect

import java.util.stream.Collectors

/**
 * User: ben
 * Date: 6/04/2018
 * Time: 6:31 PM
 */
class TextEffectParser{
    fun parse(expression: String): TextEffect{
        return parse(expression, ":")
    }

    fun parse(expression: String, delimiter: String): TextEffect{
        if(expression.contains(delimiter)){
            return parseCompositeTextEffect(expression)
        } else {
            if (TextEffectImpl.containsEscapeCode(expression)) {
                return TextEffectImpl(expression)
            } else if (MiscTextEffect.contains(expression)) {
                return MiscTextEffect.parse(expression)
            } else if (Ansi16Color.contains(expression)) {
                return Ansi16Color.parse(expression)
            } else if (Ansi256Color.contains(expression)) {
                return Ansi256Color.parse(expression)
            } else {
                throw IllegalArgumentException("Cannot parse textEffect expression [$expression]")
            }
        }
    }

    public fun parseCompositeTextEffect(expression: String): TextEffect {
        val effects = parseToListOfTextEffects(expression, ":")
        return CompositeTextEffect(LinkedHashSet(effects))
    }

    public fun parseToListOfTextEffects(expression: String, delimiter: String): ArrayList<TextEffect> {
        val effects = ArrayList<TextEffect>()
        val split = expression.split(delimiter)
        //If there are two, assume the first is a foreground, second is a background
        if (split.size == 2) {
            val first = split.get(0)
            try {
                if (TextEffectImpl.containsEscapeCode(first)) {
                    effects.add(TextEffectImpl(first))
                } else if (Ansi16ForegroundColor.contains(first)) {
                    effects.add(Ansi16ForegroundColor.parse(first))
                } else if (first.matches(Regex("\\d+"))) {
                    effects.add(Ansi256Color(first.toInt(), AnsiForegroundBackground.FOREGROUND))
                } else {
                    effects.add(parse(first))
                }
            } catch (e: Exception) {
                throw IllegalArgumentException("Cannot parse textEffect expression [$first] of expression [$expression]", e)
            }
            val second = split.get(1)
            try {
                if (TextEffectImpl.containsEscapeCode(second)) {
                    effects.add(TextEffectImpl(second))
                } else if (Ansi16BackgroundColor.contains(second)) {
                    effects.add(Ansi16BackgroundColor.parse(second))
                } else if (second.matches(Regex("\\d+"))) {
                    effects.add(Ansi256Color(second.toInt(), AnsiForegroundBackground.BACKGROUND))
                } else {
                    effects.add(parse(second))
                }
            } catch (e: Exception) {
                throw IllegalArgumentException("Cannot parse textEffect expression [$second] of expression [$expression]", e)
            }
            //Otherwise, assume neither
        } else {
            effects.addAll(split.stream().map {
                try {
                    parse(it)
                } catch (e: Exception) {
                    throw IllegalArgumentException("Cannot parse textEffect expression [$it] of expression [$expression]", e)
                }
            }.collect(Collectors.toList()))
        }
        return effects
    }
}