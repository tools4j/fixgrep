package org.tools4j.fixgrep

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory

/**
 * User: ben
 * Date: 16/04/2018
 * Time: 6:42 AM
 */
class ConfigBuilder(val args: Array<String>, val overrides: Config?){
    constructor(args: Array<String>): this(args, null)
    constructor(args: List<String>): this(args.toTypedArray(), null)
    constructor(args: List<String>, overrides: Config?): this(args.toTypedArray(), overrides)

    val config: Config by lazy {
        val optionsConfig = OptionsToConfig(OptionParserFactory().optionParser.parse(*args)).config
        val standardConfig = ConfigFactory.load()
        if(overrides != null){
            optionsConfig.withFallback(overrides).withFallback(standardConfig)
        } else {
            optionsConfig.withFallback(standardConfig)
        }
    }
}