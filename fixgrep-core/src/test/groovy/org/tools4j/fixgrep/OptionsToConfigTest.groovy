package org.tools4j.fixgrep

import joptsimple.OptionParser
import joptsimple.OptionSet
import org.tools4j.properties.Config
import spock.lang.Specification

/**
 * User: ben
 * Date: 3/04/2018
 * Time: 5:40 PM
 */
class OptionsToConfigTest extends Specification {
    def "GetProperties"() {
        given:
        final OptionParser optionParser = new OptionParserFactory().optionParser
        final OptionSet options = optionParser.parse("-n", "--highlights", "35:Blue,8:Yellow:Line,51=1:Purple:Tag,Side=Buy:Green", "-z", "88")
        final OptionsToConfig optionsToProperties = new OptionsToConfig(options);

        when:
        final Config config = optionsToProperties.getConfig();
        println config

        then:
        assert config.getAsBoolean("suppress.colors")
        assert config.getAsStringList("highlights") == ["35:Blue", "8:Yellow:Line", "51=1:Purple:Tag" ,"Side=Buy:Green"]
        assert config.getAsStringList("exclude.messages.of.type") == ["88"]
    }
}
