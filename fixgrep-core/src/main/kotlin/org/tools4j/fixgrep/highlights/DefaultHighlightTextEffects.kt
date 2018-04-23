package org.tools4j.fixgrep.highlights

import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory
import org.tools4j.fixgrep.texteffect.TextEffect
import org.tools4j.fixgrep.texteffect.TextEffectParser
import java.util.stream.Collectors

class DefaultHighlightTextEffects(val effects: List<TextEffect>){
    var lastUsed = -1
    constructor(defaultHighlightTextEffects: DefaultHighlightTextEffects): this(defaultHighlightTextEffects.effects)
    constructor(str: String): this(TextEffectParser().parseToListOfTextEffects(str, ","))

    fun next(): TextEffect {
        return effects.get(++lastUsed % effects.size)
    }

    companion object {
        val DEFAULT = DefaultHighlightTextEffects("FgBrightRed,FgBrightGreen,FgBrightYellow,FgBrightBlue,FgBrightMagenta,FgBrightCyan")
    }
}

