package org.tools4j.fixgrep

import org.tools4j.fix.*
import org.tools4j.fixgrep.formatting.*
import org.tools4j.fixgrep.highlights.Highlight
import org.tools4j.fixgrep.highlights.HighlightParser
import org.tools4j.properties.Config
import java.util.*

/**
 * User: ben
 * Date: 22/03/2018
 * Time: 6:49 AM
 */
class FormatSpec(
        val suppressColors: Boolean = false,
        val suppressBoldTagsAndValues: Boolean = false,
        val highlight: Highlight = Highlight.NO_HIGHLIGHT,
        val groupBy: GroupBy = GroupBy.NONE,
        val inputDelimiter: String = Ascii1Char().toString(),
        val outputDelimiter: String = "|",
        val lineFormat: String = "\${senderToTargetCompIdDirection} \${msgColor}[\${msgTypeName}]\${colorReset} \${msgFix}",
        val lineRegex: String = "^.*?(\\d+=.*?$)",
        val lineRegexGroupForFix: Int = 1,
        val sortByTags: List<Int> = Collections.emptyList(),
        val onlyIncludeTags: List<Int> = Collections.emptyList(),
        val excludeTags: List<Int> = Collections.emptyList(),
        val verticalFormat: Boolean = false,
        val alignVerticalColumns: Boolean = false,
        val includeOnlyMessagesOfType: List<String> = Collections.emptyList(),
        val excludeMessagesOfType: List<String> = Collections.emptyList(),
        val tagAnnotations: String,
        val fixSpec: FixSpec = Fix50SP2FixSpecFromClassPath().spec,
        val msgColors: MessageColors = MessageColors(),
        val debug: Boolean = false,
        val formatInHtml: Boolean = false) {

    constructor(): this(
        false,
            false,
        Highlight.NO_HIGHLIGHT,
        GroupBy.NONE,
        Ascii1Char().toString(),
        "|",
        "\${senderToTargetCompIdDirection} \${msgColor}[\${msgTypeName}]\${colorReset} \${msgFix}",
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
        MessageColors())

    @JvmOverloads
    constructor(
            config: Config,
            fixSpec: FixSpec = Fix50SP2FixSpecFromClassPath().spec,
            msgColors: MessageColors = MessageColors()) : this(

            suppressColors = config.getAsBoolean("suppress.colors"),
            suppressBoldTagsAndValues = config.getAsBoolean("suppress.bold.tags.and.values"),
            highlight = HighlightParser().parse(config.getAsStringList("highlights")),
            //groupBy = GroupByOrder(string = config.getString("group.by.order")),
            inputDelimiter = config.getAsString("input.delimiter"),
            outputDelimiter = config.getAsString("output.delimiter"),
            lineFormat = config.getAsString("output.line.format"),
            lineRegex = config.getAsString("input.line.format"),
            lineRegexGroupForFix = config.getAsInt("line.regexgroup.for.fix"),
            sortByTags = config.getAsIntList("sort.by.tags"),
            onlyIncludeTags = config.getAsIntList("only.include.tags"),
            excludeTags = config.getAsIntList("exclude.tags"),
            tagAnnotations = config.getAsString("tag.annotations"),
            verticalFormat = config.getAsBoolean("vertical.format"),
            alignVerticalColumns = config.getAsBoolean("align.vertical.columns"),
            includeOnlyMessagesOfType = config.getAsStringList("include.only.messages.of.type"),
            excludeMessagesOfType = config.getAsStringList("exclude.messages.of.type"),
            fixSpec = fixSpec,
            msgColors = msgColors,
            formatInHtml = config.hasPropertyAndIsNotFalse("html"),
            debug = config.getAsBoolean("debug", false))
    
    fun copyWithModifications(
        suppressColors: Boolean = this.suppressColors,
        suppressBoldTagsAndValues: Boolean = this.suppressBoldTagsAndValues,
        highlight: Highlight = this.highlight,
        groupBy: GroupBy = this.groupBy,
        inputDelimiter: String = this.inputDelimiter,
        outputDelimiter: String = this.outputDelimiter,
        lineFormat: String = this.lineFormat,
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
            lineFormat,
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
            return HorizontalHtmlMsgFormatter(fields, tagAnnotationPositions, !suppressBoldTagsAndValues, DelimiterImpl(outputDelimiter))
        } else {
            return HorizontalConsoleMsgFormatter(fields, tagAnnotationPositions, !suppressBoldTagsAndValues, DelimiterImpl(outputDelimiter))
        }
    }
}