package org.tools4j.properties

import java.util.HashMap
import java.util.HashSet
import java.util.LinkedHashMap
import java.util.regex.Matcher

/**
 * User: ben
 * Date: 26/10/17
 * Time: 5:40 PM
 */
class ResolvedMap(private val primaryMap: Map<String, String?>, vararg secondaryMaps: Map<String, String?>) {
    private val VARIABLE_PATTERN = java.util.regex.Pattern.compile("(?<!\\\\)\\$\\{([^\\}^(?:\\$\\{)]+)\\}")
    private val secondaryMap: MutableMap<String, String?>

    init {
        this.secondaryMap = HashMap()
        for (secondaryMap in secondaryMaps) {
            this.secondaryMap.putAll(secondaryMap)
        }
    }

    /**
     * @return A map with the same keys as given in primaryMap, but with 'resolved' values.
     * 'resolved' means that any values which originally had variables in them, denoted by ${varname} syntax
     * have been resolved using variables from either the primary or secondary maps.
     *
     * The primaryMap is the map which you want to resolve.  The secondaryMap is to provide additional variables
     * that might be used during resolution.  The key/value pairs of secondaryMap are not returned from this resolve
     * method.
     *
     * This method should be able to handle nested resolutions.  e.g. if a key pair value like this:
     *
     *
     * myValue=${${var1}.${var2}}
     * var1=one
     * var2=two
     * one.two=Hi there!
     *
     * var1 and var2 will be resolved first, to:
     *
     *
     * myValue=${one.two}
     * var1=one
     * var2=two
     * one.two=Hi there!
     *
     * And then one.two will be resolved:
     *
     *
     * myValue=Hi there!
     * var1=one
     * var2=two
     * one.two=Hi there!
     *
     */
    fun resolve(): Map<String, String> {
        val resolved = HashSet<String>(primaryMap.size)
        val unresolved = HashSet<String>(primaryMap.size)
        val map: MutableMap<String, String?> = LinkedHashMap(this.primaryMap.size + this.secondaryMap.size)

        //Initialize the maps and sets
        map.putAll(this.primaryMap)
        map.putAll(this.secondaryMap)
        unresolved.addAll(map.keys)

        //Loop
        for (i in 0..999) {
            var numberOfReplacementsInThisLoop = 0
            for (key in unresolved) {
                while (true) {
                    var numberOfReplacementsForThisKeyInThisLoop = 0
                    var findFrom = 0
                    while (true) {
                        val value = map[key]
                        if (findFrom >= value!!.length) {
                            break
                        }
                        val matcher = VARIABLE_PATTERN.matcher(value)
                        if (!matcher.find(findFrom)) {
                            break
                        }
                        val variableName = matcher.group(1)
                        val variableValue = map[variableName]
                        if (variableValue == null) {
                            resolved.add(key)
                        } else {
                            val newValue = value.replace(matcher.group(0), variableValue)
                            map[key] = newValue
                            numberOfReplacementsInThisLoop++
                            numberOfReplacementsForThisKeyInThisLoop++
                        }
                        findFrom = matcher.end()
                    }
                    if (numberOfReplacementsForThisKeyInThisLoop == 0) {
                        break
                    }
                }
            }
            unresolved.removeAll(resolved)
            if (numberOfReplacementsInThisLoop == 0) {
                break
            }
        }

        //Now just pull out items that were originally given in the primary map
        val returnMap:MutableMap<String, String> = LinkedHashMap(this.primaryMap.size + this.secondaryMap.size)
        for (key in primaryMap.keys) {
            returnMap[key] = map[key]!!
        }
        return returnMap
    }
}
