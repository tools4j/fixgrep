package org.tools4j.fixgrep.config

import joptsimple.ArgumentAcceptingOptionSpec
import joptsimple.OptionSet
import joptsimple.OptionSpec
import org.tools4j.properties.Config
import org.tools4j.properties.ConfigImpl
import java.util.stream.Collectors

/**
 * User: ben
 * Date: 3/04/2018
 * Time: 5:34 PM
 */
class OptionsToConfig(val optionSet: OptionSet) {
    val config: Config by lazy {
        val configMap: MutableMap<String, String?> = LinkedHashMap()
        val options = optionSet.asMap().keys.stream().map { longest(it.options()) }.collect(Collectors.toList())

        for(option in options){
            val cleanedName = option.replace('-', '.')
            if(optionSet.has(option)){
                val values = optionSet.valuesOf(option)
                if(values.isEmpty()) {
                    configMap.put(cleanedName, null)
                } else if(isOptionDefinedAsList(optionSet, option)){
                    configMap.put(cleanedName, values.toString())
                } else {
                    configMap.put(cleanedName, values.get(0).toString())
                }
            }
        }
        ConfigImpl(configMap)
    }

    private fun longest(options: List<String>): String {
        return options.stream().sorted { o1, o2 -> Integer.compare(o2.length, o1.length)}.findFirst().get()
    }

    private fun getSpec(optionSet: OptionSet, option: String): OptionSpec<*>{
        return optionSet.specs().filter { it.options().contains(option) }.first()
    }

    private fun isOptionDefinedAsList(optionSet: OptionSet, option: String): Boolean{
        return isOptionDefinedAsList(getSpec(optionSet, option))
    }

    private fun isOptionDefinedAsList(spec: OptionSpec<*>): Boolean{
        if (ArgumentAcceptingOptionSpec::class.java.isAssignableFrom(spec::class.java)) {
            val valueSeparatorField = ArgumentAcceptingOptionSpec::class.java.getDeclaredField("valueSeparator")
            valueSeparatorField.isAccessible = true
            val valueSeparator: String? = valueSeparatorField.get(spec) as String?
            return (valueSeparator != null && !valueSeparator.equals("\u0000"))
        } else {
            return false
        }
    }
}