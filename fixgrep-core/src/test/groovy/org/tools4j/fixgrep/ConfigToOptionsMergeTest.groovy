package org.tools4j.fixgrep

import joptsimple.OptionParser
import joptsimple.OptionSet
import org.tools4j.properties.Config
import org.tools4j.properties.ConfigLoader
import spock.lang.Specification

/**
 * User: ben
 * Date: 3/04/2018
 * Time: 5:40 PM
 */
class ConfigToOptionsMergeTest extends Specification {
    def "test 1"() {
        given:
        final Config propertiesConfig = ConfigLoader.fromString("""
        suppress.colors=true
        vertical.format=false
        highlights=150:Cyan,asdf
        sort.by.tags=[1,2,3,4,5]""")

        final OptionParser optionParser = new OptionParserFactory().optionParser
        final OptionSet options = optionParser.parse("-n",
                "--highlights", "35:Blue,8:Yellow:Msg,51=1:Purple:Tag,Side=Buy:Green",
                "--sort-by-tags", "5,4,3,2,1")
        final OptionsToConfig optionsToProperties = new OptionsToConfig(options);

        when:
        final Config optionsConfig = optionsToProperties.getConfig();
        final Config config = propertiesConfig.overrideWith(optionsConfig)
        println config

        then:
        assert config.getAsBoolean("suppress.colors")
        assert config.getAsStringList("highlights") == ["35:Blue", "8:Yellow:Msg", "51=1:Purple:Tag" ,"Side=Buy:Green"]
        assert config.getAsIntList("sort.by.tags") == [5,4,3,2,1]
    }

    def "test 2"() {
        given:
        final Map<String, ?> configMap = new HashMap<>()
        final Config propertiesConfig = ConfigLoader.fromString("""
        suppress.colors=false
        vertical.format=false
        highlights=[150:Cyan,asdf]
        sort.by.tags=[1,2,3,4,5]""")

        final OptionParser optionParser = new OptionParserFactory().optionParser
        final OptionSet options = optionParser.parse("-n")
        final OptionsToConfig optionsToProperties = new OptionsToConfig(options);

        when:
        final Config optionsConfig = optionsToProperties.getConfig();
        final Config config = propertiesConfig.overrideWith(optionsConfig)
        println config

        then:
        assert config.getAsBoolean("suppress.colors")
        assert !config.getAsBoolean("vertical.format")
        assert config.getAsStringList("highlights") == ["150:Cyan", "asdf"]
        assert config.getAsIntList("sort.by.tags") == [1,2,3,4,5]
    }
}
