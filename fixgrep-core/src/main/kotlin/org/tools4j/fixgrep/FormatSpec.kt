package org.tools4j.fixgrep

import org.tools4j.fix.*
import org.tools4j.fixgrep.formatting.FieldsFormatterHorizontalConsoleText
import org.tools4j.fixgrep.formatting.FieldsFormatterHorizontalHtml
import org.tools4j.fixgrep.formatting.FieldsFormatterVerticalConsoleText
import org.tools4j.fixgrep.formatting.FieldsFormatterVerticalHtml
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
            includeOnlyMessagesOfType = config.getAsStringList("include.only.messages.of.type"),
            excludeMessagesOfType = config.getAsStringList("exclude.messages.of.type"),
            fixSpec = fixSpec,
            msgColors = msgColors,
            formatInHtml = config.hasPropertyAndIsNotFalse("html"),
            debug = config.getAsBoolean("debug", false))

    val tagAnnotationSpec: AnnotationSpec by lazy {
        val annotationPositions = AnnotationPositions.parse(tagAnnotations)
        val tagAndValuesBold = !(annotationPositions.neitherTagNorValueAnnotated || suppressBoldTagsAndValues)
        AnnotationSpec(annotationPositions, tagAndValuesBold)
    }

    val fieldsFormatter: FieldsFormatter by lazy {
        if(verticalFormat){
            if(formatInHtml){
                FieldsFormatterVerticalHtml()
            } else {
                FieldsFormatterVerticalConsoleText()
            }
        } else {
            if(formatInHtml){
                FieldsFormatterHorizontalHtml(DelimiterImpl(outputDelimiter))
            } else {
                FieldsFormatterHorizontalConsoleText(DelimiterImpl(outputDelimiter))
            }
        }
    }
}