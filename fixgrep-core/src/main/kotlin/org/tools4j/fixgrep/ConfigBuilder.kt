package org.tools4j.fixgrep

import mu.KLogging
import org.tools4j.properties.ConfigLoader
import org.tools4j.properties.Config
import org.tools4j.properties.ConfigAndArguments
import org.tools4j.properties.ConfigImpl

/**
 * User: ben
 * Date: 16/04/2018
 * Time: 6:42 AM
 */
class ConfigBuilder(val args: List<String>, val overrides: Config?){
    constructor(args: Array<String>): this(args.toList(), null)
    constructor(args: List<String>): this(args, null)

    companion object: KLogging()

    val configAndArguments: ConfigAndArguments by lazy {
        val parsedOptions = OptionParserFactory().optionParser.parse(*args.toTypedArray())
        val optionsConfig = OptionsToConfig(parsedOptions).config
        val classpathConfig = ConfigLoader.fromClasspath("application.properties")!!
        val homeDirConfig = ConfigLoader.fromHomeDir(".fixgrep/application.properties")

        logger.info("================================")
        logger.info("Non-option arguments: "+ parsedOptions.nonOptionArguments())
        logger.info("================================")
        logger.info("Options: "+ args.toList())
        logger.info("================================")
        logger.info("Options config:\n"+ optionsConfig.toPrettyString())
        logger.info("================================")
        logger.info("Classpath config:\n"+ classpathConfig.toPrettyString())
        logger.info("================================")
        logger.info("HomeDir config:\n"+ homeDirConfig?.toPrettyString())


        val config: Config = ConfigImpl()
        val resolvedConfig = config
                .overrideWith(classpathConfig)
                .overrideWith(homeDirConfig)
                .overrideWith(overrides)
                .overrideWith(optionsConfig)

        logger.info("================================")
        logger.info("Resolved config:\n"+ resolvedConfig.toPrettyString())
        ConfigAndArguments(resolvedConfig, parsedOptions.nonOptionArguments(), args)
    }
}