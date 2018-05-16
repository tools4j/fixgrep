package org.tools4j.fixgrep

import org.tools4j.fix.AnnotationPositions
import org.tools4j.fix.AnnotationSpec
import org.tools4j.fix.Ascii1Char
import org.tools4j.fix.Fix50SP2FixSpecFromClassPath
import org.tools4j.fix.FixSpec
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
        val msgColors: MessageColors = MessageColors()) {

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
            lineFormat = config.getAsString("line.format"),
            lineRegex = config.getAsString("line.regex"),
            lineRegexGroupForFix = config.getAsInt("line.regexgroup.for.fix"),
            sortByTags = config.getAsIntList("sort.by.tags"),
            onlyIncludeTags = config.getAsIntList("only.include.tags"),
            excludeTags = config.getAsIntList("exclude.tags"),
            tagAnnotations = config.getAsString("tag.annotations"),
            verticalFormat = config.getAsBoolean("vertical.format"),
            includeOnlyMessagesOfType = config.getAsStringList("include.only.messages.of.type"),
            excludeMessagesOfType = config.getAsStringList("exclude.messages.of.type"),
            fixSpec = fixSpec,
            msgColors = msgColors)

    val tagAnnotationSpec: AnnotationSpec by lazy {
        val annotationPositions = AnnotationPositions.parse(tagAnnotations)
        val tagAndValuesBold = !(annotationPositions.neitherTagNorValueAnnotated || suppressBoldTagsAndValues)
        AnnotationSpec(annotationPositions, tagAndValuesBold)
    }
}