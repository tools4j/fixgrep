package org.tools4j.fixgrep

import com.typesafe.config.Config
import joptsimple.OptionParser
import joptsimple.OptionSet
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
        assert config.getBoolean("suppress.colors")
        assert config.getStringList("highlights") == ["35:Blue", "8:Yellow:Line", "51=1:Purple:Tag" ,"Side=Buy:Green"]
        assert config.getStringList("exclude.messages.of.type") == ["88"]
    }
}
