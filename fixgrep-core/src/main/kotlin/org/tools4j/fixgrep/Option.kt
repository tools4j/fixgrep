package org.tools4j.fixgrep

import joptsimple.OptionParser
import joptsimple.OptionSpecBuilder
import joptsimple.util.RegexMatcher

/**
 * User: benjw
 * Date: 8/15/2018
 * Time: 5:32 PM
 */
enum class Option(val abbreviation: String?, val longForm: String, val otherForm: String? = null, val canHaveEquivalentPropertyInPropertiesFile: Boolean, val canBePassedAsCommandLineOption: Boolean, val commandLineOptionConfig: ((OptionSpecBuilder) -> Any)?){
    tag_annotations("a", "tag-annotations", null, true, true, {it.withRequiredArg().ofType(String::class.java).withValuesConvertedBy(RegexMatcher.regex("(none|outsideAnnotated|insideAnnotated|ba|ab|aa|bb|b_|a_|_a|_b|__)"))}),
    align_vertical_columns("A", "align-vertical-columns", "align", true, true, {}),
//    group_by_order("g", "group-by-order", null, true, true, {it.withOptionalArg().ofType(String::class.java)}),
    input_delimiter("d", "input-delimiter", "input-delim", true, true, {it.withRequiredArg().ofType(String::class.java)}),
    exclude_tags("e", "exclude-tags", null, true, true, {it.withRequiredArg().ofType(Integer::class.java).withValuesSeparatedBy(",")}),
    to_file("f", "to-file", null, false, true, {it.withOptionalArg().ofType(String::class.java)}),
    output_format_horizontal_console(null, "output-format-horizontal-console", null, true, true, {it.withRequiredArg().ofType(String::class.java)}),
    output_format_horizontal_html(null, "output-format-horizontal-html", null, true, true, {it.withRequiredArg().ofType(String::class.java)}),
    output_format_vertical_console(null, "output-format-vertical-console", null, true, true, {it.withRequiredArg().ofType(String::class.java)}),
    output_format_vertical_html(null, "output-format-vertical-html", null, true, true, {it.withRequiredArg().ofType(String::class.java)}),
    line_regexgroup_for_fix("g", "line-regexgroup-for-fix", null, true, true, {it.withRequiredArg().ofType(Integer::class.java)}),
    highlights("h", "highlights", "highlight", true, true, {it.withRequiredArg().ofType(String::class.java).withValuesSeparatedBy(",")}),
    launch_browser("l", "launch-browser", null, false, true, {}),
    include_only_messages_of_type("m", "include-only-messages-of-type", null, true, true, {it.withRequiredArg().ofType(String::class.java).withValuesSeparatedBy(",")}),
    suppress_colors("n", "suppress-colors", "no-color", true, true, {}),
    output_delimiter("o", "output-delimiter", "output-delim", true, true, {it.withRequiredArg().ofType(String::class.java)}),
    piped_input("p", "piped-input", "piped", false, true, {}),
    suppress_bold_tags_and_values("q", "suppress-bold-tags-and-values", null, true, true, {}),
    input_line_format("r", "input-line-format", null, true, true, {it.withRequiredArg().ofType(String::class.java)}),
    sort_by_tags("s", "sort-by-tags", null, true, true, {it.withRequiredArg().ofType(Integer::class.java).withValuesSeparatedBy(",")}),
    only_include_tags("t", "only-include-tags", null, true, true, {it.withRequiredArg().ofType(Integer::class.java).withValuesSeparatedBy(",")}),
    exclude_messages_of_type("v", "exclude-messages-of-type", null, true, true, {it.withRequiredArg().ofType(String::class.java).withValuesSeparatedBy(",")}),
    debug("x", "debug", null, false, true, {}),
    install(null, "install", null, false, true, {}),
    vertical_format("V", "vertical-format", null, true, true, {}),
    color_demo_256(null, "256-color-demo", null, false, true, {}),
    color_demo_16(null, "16-color-demo", null, false, true, {}),
    man(null, "man", null, false, true, {}),
    html(null, "html", null, false, true, {it.withOptionalArg().ofType(String::class.java).withValuesConvertedBy(RegexMatcher.regex("(page)"))}),
    gimme_css(null, "gimme-css", null, false, true, {}),
    help("?", "help", null, false, true, {}),
    online_help_url(null, "online-help-url", null, true, false, null),
    download_url(null, "download-url", null, true, false, null),
    vcs_home_url(null, "vcs-home-url", null, true, false, null);

    init {
        if(otherForm != null && otherForm.length >= longForm.length){
            throw IllegalArgumentException("Other form [$otherForm] is longer than or equal to length of long form [$longForm]")
        }
        if(canBePassedAsCommandLineOption && commandLineOptionConfig == null){
            throw IllegalArgumentException("Option $name is configured with canBePassedAsCommandLineOption==true.  Therefore commandLineOptionConfig must not be null.  If commandLineOptionConfig does not need to be specified, then a blank lambda {} can be passed." )
        }
        if(!canBePassedAsCommandLineOption && commandLineOptionConfig != null){
            throw IllegalArgumentException("Option $name is configured with canBePassedAsCommandLineOption==false.  And a non-null commandLineOptionConfig was specified.  These settings are not consistent." )
        }
    }

    val optionVariations: List<String> by lazy {
        val options = ArrayList<String>()
        if(abbreviation != null) options.add(abbreviation)
        options.add(longForm)
        if(otherForm != null) options.add(otherForm)
        options
    }

    val optionVariationsAsCommaDelimitedString: String by lazy {
        optionVariations.joinToString(",")
    }

    val optionVariationsWithDashPrefixesAsCommaDelimitedString: String by lazy {
        optionVariations.map{if(it.length == 1) "-$it" else "--$it"}.joinToString(",")
    }

    val key: String by lazy {
        longForm.replace("-", ".")
    }

    fun configureOptions(optionParser: OptionParser){
        if(commandLineOptionConfig != null) {
            val specBuilder = optionParser.acceptsAll(optionVariations)
            commandLineOptionConfig.invoke(specBuilder)
        }
    }

    companion object {
        val optionsThatCanBeConfigurableInPropertiesFile: List<Option> by lazy {
            values().toList().filter{ it.canHaveEquivalentPropertyInPropertiesFile }.toList()
        }

        val optionsThatCanBePassedOnCommandLine: List<Option> by lazy {
            values().toList().filter{ it.canBePassedAsCommandLineOption }.toList()
        }

        fun configureAllOptions(optionParser: OptionParser){
            for(option in values()){
                option.configureOptions(optionParser)
            }
        }
    }
}