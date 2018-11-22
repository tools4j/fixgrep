package org.tools4j.fixgrep.config

/**
 * User: Ben
 * Date: 4/05/2018
 * Time: 9:16 AM
 */
class FixGrepConfig(config: ConfigKeyedWithOption) {
    val groupByGivenOrders: List<String> by lazy {config.getAsStringList(Option.group_by_given_orders, emptyList())!!}
    val groupByOrders: Boolean by lazy { !groupByGivenOrders.isEmpty() || config.hasPropertyAndIsNotFalse(Option.group_by_order) }
    val install: Boolean by lazy { config.getAsBoolean(Option.install, false) }
    val help: Boolean by lazy { config.getAsBoolean(Option.help, false) }
    val man: Boolean by lazy { config.getAsBoolean(Option.man, false) }
    val colorDemo16: Boolean by lazy { config.getAsBoolean(Option.color_demo_16, false) }
    val colorDemo256: Boolean by lazy { config.getAsBoolean(Option.color_demo_256, false) }
    val onlineHelpUrl: String by lazy { config.getAsString(Option.online_help_url) }
    val htmlFormatting: Boolean by lazy { config.hasPropertyAndIsNotFalse(Option.html) }
    val consoleFormatting: Boolean by lazy { !htmlFormatting }
    val debugMode: Boolean by lazy { config.getAsBoolean(Option.debug, false) }
    val fixgrepDownloadUrl: String by lazy { config.getAsString(Option.download_url) }
    val vcsHomeUrl: String by lazy { config.getAsString(Option.vcs_home_url) }
    val outputFormatHorizontalConsole: String by lazy { config.getAsString(Option.output_format_horizontal_console) }
    val outputFormatHorizontalHtml: String by lazy { config.getAsString(Option.output_format_horizontal_html) }
    val outputFormatVerticalConsole: String by lazy { config.getAsString(Option.output_format_vertical_console) }
    val outputFormatVerticalHtml: String by lazy { config.getAsString(Option.output_format_vertical_html) }
    val suppressColors: Boolean by lazy { config.getAsBoolean(Option.suppress_colors) }
    val suppressBoldTagsAndValues: Boolean by lazy { config.getAsBoolean(Option.suppress_bold_tags_and_values) }
    val highlights: List<String> by lazy { config.getAsStringList(Option.highlights) }
    val suppressIndentGroupRepeats: Boolean by lazy { config.getAsBoolean(Option.suppress_indent_group_repeats) }
    val inputDelimiter: String by lazy { config.getAsString(Option.input_delimiter) }
    val outputDelimiter: String by lazy { config.getAsString(Option.output_delimiter) }
    val outputFormatGroupedOrderHeaderConsole: String by lazy { config.getAsString(Option.output_format_grouped_order_header_console) }
    val outputFormatGroupedOrderHeaderHtml: String by lazy { config.getAsString(Option.output_format_grouped_order_header_html) }
    val inputLineFormatRegex: String by lazy { config.getAsString(Option.input_line_format) }
    val lineRegexGroupForFix: Int by lazy { config.getAsInt(Option.line_regexgroup_for_fix) }
    val sortByTags: List<Int> by lazy { config.getAsIntList(Option.sort_by_tags) }
    val includeOnlyTags: List<Int> by lazy { config.getAsIntList(Option.only_include_tags) }
    val excludeTags: List<Int> by lazy { config.getAsIntList(Option.exclude_tags) }
    val tagAnnotations: String by lazy { config.getAsString(Option.tag_annotations) }
    val verticalFormat: Boolean by lazy { config.getAsBoolean (Option.vertical_format)}
    val alignVerticalColumns: Boolean by lazy { config.getAsBoolean(Option.align_vertical_columns) }
    val includeOnlyMessagesOfType: List<String> by lazy { config.getAsStringList(Option.include_only_messages_of_type) }
    val excludeMessagesOfType: List<String> by lazy { config.getAsStringList(Option.exclude_messages_of_type) }
    val fixSpecPath: String by lazy { config.getAsString(Option.fix_spec_path) }
    val launchInBrowser: Boolean by lazy { config.hasPropertyAndIsNotFalse(Option.launch_browser) }
    val outputToGivenFile: Boolean by lazy { config.hasProperty(Option.to_given_file) || outputToFileButFilenameNotGiven }
    val outputToFileButFilenameNotGiven: Boolean by lazy { config.hasProperty(Option.to_file) }
    val outputFileName: String by lazy { config.getAsString(Option.to_given_file) }
    val outputToHtmlPage: Boolean by lazy { config.getAsString(Option.html, "") == "page" }
    val isFullPageHtml: Boolean by lazy { config.hasProperty(Option.html) && (man || outputToGivenFile || outputToHtmlPage || launchInBrowser) }
}