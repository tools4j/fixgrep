package org.tools4j.fixgrep.texteffect

interface TextEffect {
    val ansiCode: String
    val ansiResetCode: String
    val htmlClass: String
    val name: String
}
