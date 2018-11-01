package org.tools4j.fixgrep.config

import mu.KLogging
import org.tools4j.properties.Config
import org.tools4j.properties.ConfigImpl
import org.tools4j.properties.ConfigLoader

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
        val cleanedArgs: List<String> = cleanArgsOfAnyHiddenNonStringObjects()

        val parsedOptions = OptionParserFactory().optionParser.parse(*cleanedArgs.toTypedArray())

        val optionsConfig = OptionsToConfig(parsedOptions).config
        optionsConfig.validateAllPropertyKeysAreOneOf(Option.optionsThatCanBePassedOnCommandLine.map { it.key })

        val classpathConfig = ConfigLoader.fromClasspath("application.properties")!!
        classpathConfig.validateAllPropertyKeysAreOneOf(Option.optionsThatCanBeConfigurableInPropertiesFile.map { it.key })

        val homeDirConfig = ConfigLoader.fromHomeDir(".fixgrep/application.properties")
        homeDirConfig?.validateAllPropertyKeysAreOneOf(Option.optionsThatCanBeConfigurableInPropertiesFile.map { it.key })

        val rawArguments = parsedOptions.nonOptionArguments()
        val cleanedArguments = rawArguments
                .filter { it != null && !it.toString().trim().isEmpty() }
                .map { it.toString().trim() }
                .flatMap { it.split(Regex("\\s")) }
                .toList()

        logger.info("================================")
        logger.info("Non-option arguments: "+ cleanedArguments)
        logger.info("================================")
        logger.info("Options: $cleanedArgs")
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
        ConfigAndArguments(resolvedConfig, cleanedArguments, cleanedArgs)
    }

    /**
     * If called from Groovy, String's can actually be GStrings.  Which will break later on.
     */
    private fun cleanArgsOfAnyHiddenNonStringObjects(): List<String> {
        val argsAsObjects: List<Any> = args
        val cleanedArgs: List<String> = argsAsObjects.map { it.toString() }
        return cleanedArgs
    }
}