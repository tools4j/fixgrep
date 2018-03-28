package org.tools4j.fixgrep
/**
 * User: ben
 * Date: 13/03/2018
 * Time: 5:23 PM
 */
enum class AnsiColor(val ansiCode: String) {
    Reset("\u001B[0m"),
    Black("\u001B[30m"),
    Red("\u001B[31m"),
    Green("\u001B[32m"),
    Yellow("\u001B[33m"),
    Blue("\u001B[34m"),
    Purple("\u001B[35m"),
    Cyan("\u001B[36m"),
    White("\u001B[37m"),
    Gray("\u001b[30;1m"),
    BrightRed("\u001b[31;1m"),
    BrightGreen("\u001b[32;1m"),
    BrightYellow("\u001b[33;1m"),
    BrightBlue("\u001b[34;1m"),
    BrightMagenta("\u001b[35;1m"),
    BrightCyan("\u001b[36;1m"),
    BrightWhite("\u001b[37;1m");
}

