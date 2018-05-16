package org.tools4j.fixgrep

import joptsimple.OptionSpec
import org.tools4j.fixgrep.help.OptionsHelp
import org.tools4j.fixgrep.help.OptionsHelp.OptionHelp
import spock.lang.Specification

/**
 * User: ben
 * Date: 1/05/2018
 * Time: 6:22 AM
 */
class OptionsAndHelpConsistencyTest extends Specification {

    def validateThatOnlyOneLongestOptionExistsPerOptionDescriptor() {
        when:
        final Map<String, OptionSpec<?>> commandLineOptions = new OptionParserFactory().optionParser.recognizedOptions()
        def parametersWithTwoLongestOptions = new LinkedHashSet<String>()
        def validated = new LinkedHashSet<String>()
        for(String key: commandLineOptions.keySet()){
            def associatedKeys = new ArrayList<String>(commandLineOptions.get(key).options())
            if(validated.contains(key) || validated.containsAll(associatedKeys)) continue
            associatedKeys.sort(new Comparator<String>() {
                @Override
                int compare(final String s1, final String s2) {
                    -1*Integer.compare(s1.length(), s2.length())
                }
            })
            if(associatedKeys.size() >=2 && associatedKeys.get(0).length() == associatedKeys.get(1).length()){
                parametersWithTwoLongestOptions.add(associatedKeys.get(0) + ":" + associatedKeys.get(1))
            }
        }

        then:
        assert parametersWithTwoLongestOptions.isEmpty(): "Found parameter with two (or more) longest options of equal length.  There must be only one option of the greatest length per OptionDescription. " + parametersWithTwoLongestOptions
    }

    def validateThatHelpItemsExistForEveryConfiguredCommandLineOption() {
        when:
        final Map<String, OptionSpec<?>> commandLineOptions = new OptionParserFactory().optionParser.recognizedOptions()
        def helpItems = new OptionsHelp()

        def optionsWithoutHelp = new LinkedHashSet<String>()
        def validated = new LinkedHashSet<String>()
        for(String key: commandLineOptions.keySet()){
            if(validated.contains(key)) continue
            for(String associatedKey: commandLineOptions.get(key).options()){
                if(validated.contains(associatedKey)) continue
                validated.add(associatedKey)
                if(!helpItems.optionsThatDoNotNeedHelpItems.contains(associatedKey)){
                    OptionHelp helpItem = helpItems.helpByOptions[associatedKey]
                    if(helpItem == null) {
                        optionsWithoutHelp.add("[$associatedKey:(associated with $key)]")
                    }
                }
            }
        }

        then:
        assert optionsWithoutHelp.isEmpty(): "Illegal state. The following options do not have associated OptionHelp elements defined: " + optionsWithoutHelp
    }


    def validateThatCommandLineOptionsExistForEveryHelpItem() {
        when:
        final Map<String, OptionSpec<?>> commandLineOptions = new OptionParserFactory().optionParser.recognizedOptions()
        final Map<String, OptionHelp> helpItems = new OptionsHelp().helpByOptions

        def helpWithoutCommandLineOption = new LinkedHashSet<String>()
        def validated = new LinkedHashSet<String>()
        for(String key: helpItems.keySet()){
            if(validated.contains(key)) continue
            for(String associatedKey: helpItems.get(key).getOptionVariations()){
                if(validated.contains(associatedKey)) continue
                validated.add(associatedKey)
                final OptionSpec<?> optionSpec = commandLineOptions[associatedKey]
                if(optionSpec == null && associatedKey != "[arguments]"){
                    helpWithoutCommandLineOption.add("[$associatedKey:(associated with $key)]")
                }
            }
        }

        then:
        assert helpWithoutCommandLineOption.isEmpty(): "Illegal state. The following help items do not have associated command line options defined: " + helpWithoutCommandLineOption
    }
}
