package org.tools4j.fixgrep

import org.tools4j.fix.*
import org.tools4j.fixgrep.formatting.*
import org.tools4j.fixgrep.highlights.Highlight
import org.tools4j.fixgrep.highlights.HighlightParser
import org.tools4j.fixgrep.utils.Constants
import org.tools4j.properties.Config
import java.util.*

/**
 * User: ben
 * Date: 22/03/2018
 * Time: 6:49 AM
 */
class FormatSpec(
        val suppressColors: Boolean,
        val suppressBoldTagsAndValues: Boolean,
        val highlight: Highlight,
        val groupBy: GroupBy,
        val inputDelimiter: String,
        val outputDelimiter: String,
        val outputFormatHorizontalConsole: String,
        val outputFormatHorizontalHtml: String,
        val outputFormatVerticalConsole: String,
        val outputFormatVerticalHtml: String,
        val lineRegex: String,
        val lineRegexGroupForFix: Int,
        val sortByTags: List<Int>,
        val onlyIncludeTags: List<Int>,
        val excludeTags: List<Int>,
        val verticalFormat: Boolean,
        val alignVerticalColumns: Boolean,
        val includeOnlyMessagesOfType: List<String>,
        val excludeMessagesOfType: List<String>,
        val tagAnnotations: String,
        val fixSpec: FixSpec,
        val msgColors: MessageColors,
        val debug: Boolean,
        val formatInHtml: Boolean) {

    constructor(): this(
        false,
        false,
        Highlight.NO_HIGHLIGHT,
        GroupBy.NONE,
        Ascii1Char().toString(),
        "|",
        "\${senderToTargetCompIdDirection} \${msgColor}[\${msgTypeName}]\${colorReset} \${msgFix}",
        "\${senderToTargetCompIdDirection} \${msgColor}[\${msgTypeName}]\${colorReset} \${msgFix}",
        "${Constants.EQUALS_SEPARATOR}\n\${msgColor}[\${msgTypeName}]\${colorReset}\n${Constants.EQUALS_SEPARATOR}\n\${msgFix}",
        "${Constants.EQUALS_SEPARATOR}<br/>\${msgColor}[\${msgTypeName}]\${colorReset}<br/>${Constants.EQUALS_SEPARATOR}<br/>\${msgFix}",
        "^.*?(\\d+=.*?$)",
        1,
        Collections.emptyList(),
        Collections.emptyList(),
        Collections.emptyList(),
        false,
        false,
        Collections.emptyList(),
        Collections.emptyList(),
        "outsideAnnotated",
        Fix50SP2FixSpecFromClassPath().spec,
        MessageColors(),
        false,
        false)

    @JvmOverloads
    constructor(
            config: ConfigKeyedWithOption,
            fixSpec: FixSpec = Fix50SP2FixSpecFromClassPath().spec,
            msgColors: MessageColors = MessageColors()) : this(
                suppressColors = config.getAsBoolean(Option.suppress_colors),
                suppressBoldTagsAndValues = config.getAsBoolean(Option.suppress_bold_tags_and_values),
                highlight = HighlightParser().parse(config.getAsStringList(Option.highlights)),
                groupBy = GroupBy.NONE,
                inputDelimiter = config.getAsString(Option.input_delimiter),
                outputDelimiter = config.getAsString(Option.output_delimiter),
                outputFormatHorizontalConsole = config.getAsString(Option.output_format_horizontal_console),
                outputFormatHorizontalHtml = config.getAsString(Option.output_format_horizontal_html),
                outputFormatVerticalConsole = config.getAsString(Option.output_format_vertical_console),
                outputFormatVerticalHtml = config.getAsString(Option.output_format_vertical_html),
                lineRegex = config.getAsString(Option.input_line_format),
                lineRegexGroupForFix = config.getAsInt(Option.line_regexgroup_for_fix),
                sortByTags = config.getAsIntList(Option.sort_by_tags),
                onlyIncludeTags = config.getAsIntList(Option.only_include_tags),
                excludeTags = config.getAsIntList(Option.exclude_tags),
                tagAnnotations = config.getAsString(Option.tag_annotations),
                verticalFormat = config.getAsBoolean(Option.vertical_format),
                alignVerticalColumns = config.getAsBoolean(Option.align_vertical_columns),
                includeOnlyMessagesOfType = config.getAsStringList(Option.include_only_messages_of_type),
                excludeMessagesOfType = config.getAsStringList(Option.exclude_messages_of_type),
                fixSpec = fixSpec,
                msgColors = msgColors,
                formatInHtml = config.hasPropertyAndIsNotFalse(Option.html),
                debug = config.getAsBoolean(Option.debug, false))
    
    fun copyWithModifications(
            suppressColors: Boolean = this.suppressColors,
            suppressBoldTagsAndValues: Boolean = this.suppressBoldTagsAndValues,
            highlight: Highlight = this.highlight,
            groupBy: GroupBy = this.groupBy,
            inputDelimiter: String = this.inputDelimiter,
            outputDelimiter: String = this.outputDelimiter,
            outputFormatHorizontalConsole: String = this.outputFormatHorizontalConsole,
            outputFormatHorizontalHtml: String = this.outputFormatHorizontalHtml,
            outputFormatVerticalConsole: String = this.outputFormatVerticalConsole,
            outputFormatVerticalHtml: String = this.outputFormatVerticalHtml,
            lineRegex: String = this.lineRegex,
            lineRegexGroupForFix: Int = this.lineRegexGroupForFix,
            sortByTags: List<Int> = this.sortByTags,
            onlyIncludeTags: List<Int> = this.onlyIncludeTags,
            excludeTags: List<Int> = this.excludeTags,
            verticalFormat: Boolean = this.verticalFormat,
            alignVerticalColumns: Boolean = this.alignVerticalColumns,
            includeOnlyMessagesOfType: List<String> = this.includeOnlyMessagesOfType,
            excludeMessagesOfType: List<String> = this.excludeMessagesOfType,
            tagAnnotations: String = this.tagAnnotations,
            fixSpec: FixSpec = this.fixSpec,
            msgColors: MessageColors = this.msgColors,
            debug: Boolean = this.debug,
            formatInHtml: Boolean = this.formatInHtml): FormatSpec {

        return FormatSpec(
            suppressColors,
            suppressBoldTagsAndValues,
            highlight,
            groupBy,
            inputDelimiter,
            outputDelimiter,
            outputFormatHorizontalConsole,
            outputFormatHorizontalHtml,
            outputFormatVerticalConsole,
            outputFormatVerticalHtml,
            lineRegex,
            lineRegexGroupForFix,
            sortByTags,
            onlyIncludeTags,
            excludeTags,
            verticalFormat,
            alignVerticalColumns,
            includeOnlyMessagesOfType,
            excludeMessagesOfType,
            tagAnnotations,
            fixSpec,
            msgColors,
            debug,
            formatInHtml)
    }
    
    val tagAnnotationPositions: AnnotationPositions by lazy {
        AnnotationPositions.parse(tagAnnotations)
    }

    fun getMsgFormatter(fields: Fields): MsgFormatter {
        if(formatInHtml){
            if(verticalFormat) {
                return VerticalHtmlMsgFormatter(fields, tagAnnotationPositions, !suppressBoldTagsAndValues)
            } else {
                return HorizontalHtmlMsgFormatter(fields, tagAnnotationPositions, !suppressBoldTagsAndValues, DelimiterImpl(outputDelimiter))
            }
        } else {
            if(verticalFormat) {
                throw UnsupportedOperationException()
            } else {
                return HorizontalConsoleMsgFormatter(fields, tagAnnotationPositions, !suppressBoldTagsAndValues, DelimiterImpl(outputDelimiter))
            }
        }
    }
    
    fun getOutputFormat(): String{
        if(formatInHtml) {
            if (verticalFormat) {
                return outputFormatVerticalHtml
            } else {
                return outputFormatHorizontalHtml
            }
        } else {
            if (verticalFormat) {
                return outputFormatVerticalConsole
            } else {
                return outputFormatHorizontalConsole
            }
        }
    }
}