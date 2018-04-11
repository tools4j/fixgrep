package org.tools4j.fixgrep

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import joptsimple.OptionSet
import java.util.stream.Collectors

/**
 * User: ben
 * Date: 3/04/2018
 * Time: 5:34 PM
 */
class OptionsToConfig(val optionSet: OptionSet) {
    val config: Config by lazy {
        val configMap: MutableMap<String, in Any> = LinkedHashMap()
        val options = optionSet.asMap().keys.stream().map { longest(it.options()) }.collect(Collectors.toList())

        for(option in options){
            val cleanedName = option.replace('-', '.')
            if(optionSet.has(option)){
                val values = optionSet.valuesOf(option)
                if(values.isEmpty()){
                    configMap.put(cleanedName, true)

                } else if(values.size == 1){
                    configMap.put(cleanedName, values.get(0)!!)

                } else {
                    configMap.put(cleanedName, values)
                }
            }
        }
        ConfigFactory.parseMap(configMap)
    }

    private fun longest(options: List<String>): String {
        return options.stream().sorted { o1, o2 -> Integer.compare(o2.length, o1.length)}.findFirst().get()
    }
}