package org.tools4j.fixgrep

import spock.lang.Specification

/**
 * User: ben
 * Date: 13/03/2018
 * Time: 5:23 PM
 */
class AnsiTest extends Specification {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    def 'test ansi colors'(){
        expect:
        println "${ANSI_RED}To be or not to be${ANSI_RESET}"
    }
}
