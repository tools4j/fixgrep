package org.tools4j.properties

import java.util.*

/**
 * User: ben
 * Date: 10/11/17
 * Time: 4:51 PM
 */
class ResolvedString(private val str: String, vararg valuesUsedToResolveVariablesInString: Map<String, String>) {
    private val valuesUsedToResolveVariablesInString: Array<out Map<String, String>>

    init {
        this.valuesUsedToResolveVariablesInString = valuesUsedToResolveVariablesInString
    }

    fun resolve(): String? {
        val primaryMap = HashMap<String, String>()
        primaryMap[THE_KEY] = str
        val resolvedMap = ResolvedMap(primaryMap, *valuesUsedToResolveVariablesInString).resolve()
        return resolvedMap[THE_KEY]
    }

    companion object {
        val THE_KEY: String = "THE_STRING"
    }
}
