package org.tools4j.fixgrep

import org.tools4j.properties.Config
import java.util.*
import kotlin.collections.ArrayList

/**
 * User: ben
 * Date: 7/06/2018
 * Time: 7:04 AM
 */
class ConfigAndArguments(val config: ConfigKeyedWithOption, val arguments: List<*>, val originalApplicationArguments: List<String>){
    constructor(config: ConfigKeyedWithOption): this(config, ArrayList<Any>(), Collections.emptyList())
    constructor(config: ConfigKeyedWithOption, arguments: List<*>): this(config, arguments, Collections.emptyList())
    constructor(config: Config): this(ConfigKeyedWithOption(config))
    constructor(config: Config, arguments: List<*>): this(ConfigKeyedWithOption(config), arguments)
    constructor(config: Config, arguments: List<*>, originalApplicationArguments: List<String>): this(ConfigKeyedWithOption(config), arguments, originalApplicationArguments)
}