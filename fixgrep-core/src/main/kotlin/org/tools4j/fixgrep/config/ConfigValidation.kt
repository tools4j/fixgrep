package org.tools4j.fixgrep.config

/**
 * User: benjw
 * Date: 26/10/2018
 * Time: 16:47
 */
class ConfigValidation(val config: ConfigKeyedWithOption) {
    fun validate(): List<Error>{
        val errors = ArrayList<Error>()
        if(config.getAsBoolean(Option.group_by_order) && config.getAsBoolean(Option.vertical_format)){
            errors.add(Error("Cannot group-by-order, with vertical formatting.  If you wish to group-by-order, please use horizontal formatting."))
        }
        return errors;
    }
}

data class Error(val msg: String)

/*
tag_annotations("a", "tag-annotations", null, true, true, {it.withRequiredArg().ofType(String::class.java).withValuesConvertedBy(RegexMatcher.regex("((none|outsideAnnotated|insideAnnotated|replaced)|([abr_]([abr_])?))"))}),
align_vertical_columns("A", "align-vertical-columns", "align", true, true, {}),
input_delimiter("d", "input-delimiter", "input-delim", true, true, {it.withRequiredArg().ofType(String::class.java)}),
exclude_tags("e", "exclude-tags", null, true, true, {it.withRequiredArg().ofType(Integer::class.java).withValuesSeparatedBy(",")}),
to_file("f", "to-file", null, false, true, {it.withOptionalArg().ofType(String::class.java)}),
line_regexgroup_for_fix("g", "line-regexgroup-for-fix", null, true, true, {it.withRequiredArg().ofType(Integer::class.java)}),
indent_group_repeats("G", "indent-group-repeats", null, true, true, {it.withOptionalArg().ofType(kotlin.String::class.java).withValuesSeparatedBy(",")}),
highlights("h", "highlights", "highlight", true, true, {it.withRequiredArg().ofType(String::class.java).withValuesSeparatedBy(",")}),
launch_browser("l", "launch-browser", null, false, true, {}),
include_only_messages_of_type("m", "include-only-messages-of-type", null, true, true, {it.withRequiredArg().ofType(String::class.java).withValuesSeparatedBy(",")}),
suppress_colors("n", "suppress-colors", "no-color", true, true, {}),
output_delimiter("o", "output-delimiter", "output-delim", true, true, {it.withRequiredArg().ofType(String::class.java)}),
group_by_order("O", "group-by-order", null, true, true, {it.withOptionalArg().ofType(kotlin.String::class.java).withValuesSeparatedBy(",")}),
piped_input("p", "piped-input", "piped", false, true, {}),
suppress_bold_tags_and_values("q", "suppress-bold-tags-and-values", null, true, true, {it.withOptionalArg().ofType(Boolean::class.java)}),
input_line_format("r", "input-line-format", null, true, true, {it.withRequiredArg().ofType(String::class.java)}),
sort_by_tags("s", "sort-by-tags", null, true, true, {it.withRequiredArg().ofType(Integer::class.java).withValuesSeparatedBy(",")}),
only_include_tags("t", "only-include-tags", null, true, true, {it.withRequiredArg().ofType(Integer::class.java).withValuesSeparatedBy(",")}),
exclude_messages_of_type("v", "exclude-messages-of-type", null, true, true, {it.withRequiredArg().ofType(String::class.java).withValuesSeparatedBy(",")}),
debug("x", "debug", null, false, true, {}),
install(null, "install", null, false, true, {}),
vertical_format("V", "vertical-format", null, true, true, {}),
output_format_horizontal_console(null, "output-format-horizontal-console", null, true, true, {it.withRequiredArg().ofType(String::class.java)}),
output_format_horizontal_html(null, "output-format-horizontal-html", null, true, true, {it.withRequiredArg().ofType(String::class.java)}),
output_format_vertical_console(null, "output-format-vertical-console", null, true, true, {it.withRequiredArg().ofType(String::class.java)}),
output_format_vertical_html(null, "output-format-vertical-html", null, true, true, {it.withRequiredArg().ofType(String::class.java)}),
color_demo_256(null, "256-color-demo", null, false, true, {}),
color_demo_16(null, "16-color-demo", null, false, true, {}),
man(null, "man", null, false, true, {}),
html(null, "html", null, false, true, {it.withOptionalArg().ofType(String::class.java).withValuesConvertedBy(RegexMatcher.regex("(page)"))}),
gimme_css(null, "gimme-css", null, false, true, {}),
help("?", "help", null, false, true, {}),
online_help_url(null, "online-help-url", null, true, false, null),
download_url(null, "download-url", null, true, false, null),
vcs_home_url(null, "vcs-home-url", null, true, false, null),
fix_spec_path(null, "fix-spec-path", "fix-spec", true, true, {it.withRequiredArg().ofType(String::class.java)}),
output_format_grouped_order_header_console(null, "output-format-grouped-order-header-console", null, true, false, null),
output_format_grouped_order_header_html(null, "output-format-grouped-order-header-html", null, true, false, null);
 */

