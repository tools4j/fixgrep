package org.tools4j.exhangesim.model

import java.util.ArrayList

/**
 * User: ben
 * Date: 26/10/2016
 * Time: 5:45 PM
 */
class MatchResults(private val matches: Collection<Match>): Collection<Match> by matches {
    fun toPrettyString(): String {
        val sb = StringBuilder()
        matches.forEach { it -> sb.append("   ").append(it).append("\n") }
        return sb.toString()
    }

    companion object {
        val EMPTY = MatchResults(ArrayList())
    }
}
