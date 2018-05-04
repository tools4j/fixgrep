package org.tools4j.fixgrep

import org.tools4j.properties.ConfigLoader
import org.tools4j.properties.Config
import org.tools4j.properties.ConfigImpl

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
        val classpathConfig = ConfigLoader.fromClasspath("application.properties")!!
        val homeDirConfig = ConfigLoader.fromHomeDir(".fixgrep/application.properties")
        val workingDirConfig = ConfigLoader.fromHomeDir("application.properties")

        var finalConfig: Config = ConfigImpl()
        finalConfig = finalConfig.overrideWith(classpathConfig)
        if(homeDirConfig != null) {
            finalConfig = finalConfig.overrideWith(homeDirConfig)
        } else if(workingDirConfig != null){
            finalConfig = finalConfig.overrideWith(workingDirConfig)
        }

        finalConfig = finalConfig.overrideWith(overrides)
        finalConfig = finalConfig.overrideWith(optionsConfig)
        finalConfig
    }
}