package org.tools4j.properties

import java.util.*

/**
 * User: ben
 * Date: 7/06/2018
 * Time: 7:04 AM
 */
class ConfigAndArguments(val config: Config, val arguments: List<*>, val originalApplicationArguments: List<String>){
    constructor(config: Config): this(config, ArrayList<Any>(), Collections.emptyList())
    constructor(config: Config, arguments: List<*>): this(config, arguments, Collections.emptyList())
}