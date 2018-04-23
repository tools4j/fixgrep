package org.tools4j.fixgrep

import com.typesafe.config.Config
import org.tools4j.fix.AnnotationSpec
import org.tools4j.fix.Ascii1Char
import org.tools4j.fix.Fix50SP2FixSpecFromClassPath
import org.tools4j.fix.FixSpec
import org.tools4j.fixgrep.highlights.Highlight
import org.tools4j.fixgrep.highlights.HighlightParser
import java.util.*
import java.util.regex.Pattern

/**
 * User: ben
 * Date: 22/03/2018
 * Time: 6:49 AM
 */
class FormatSpec(
    val suppressColors: Boolean = false,
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
    val tagAnnotationSpec: AnnotationSpec = AnnotationSpec.OUTSIDE_ANNOTATED,
    val verticalFormat: Boolean = false,
    val includeOnlyMessagesOfType: List<String> = Collections.emptyList(),
    val excludeMessagesOfType: List<String> = Collections.emptyList(),
    val fixSpec: FixSpec = Fix50SP2FixSpecFromClassPath().load(),
    val msgColors: MessageColors = MessageColors()) {

    constructor(): this(
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
        AnnotationSpec.OUTSIDE_ANNOTATED,
        false,
        Collections.emptyList(),
        Collections.emptyList(),
        Fix50SP2FixSpecFromClassPath().load(),
        MessageColors())

    @JvmOverloads
    constructor(
            config: Config,
            fixSpec: FixSpec = Fix50SP2FixSpecFromClassPath().load(),
            msgColors: MessageColors = MessageColors()) : this(

            suppressColors = config.getBoolean("suppress.colors"),
            highlight = HighlightParser().parse(config.getStringList("highlights")),
            //groupBy = GroupByOrder(string = config.getString("group.by.order")),
            inputDelimiter = config.getString("input.delimiter"),
            outputDelimiter = config.getString("output.delimiter"),
            lineFormat = config.getString("line.format"),
            lineRegex = config.getString("line.regex"),
            lineRegexGroupForFix = config.getInt("line.regexgroup.for.fix"),
            sortByTags = config.getIntList("sort.by.tags"),
            onlyIncludeTags = config.getIntList("only.include.tags"),
            excludeTags = config.getIntList("exclude.tags"),
            tagAnnotationSpec = AnnotationSpec.parse(config.getString("tag.annotations")),
            verticalFormat = config.getBoolean("vertical.format"),
            includeOnlyMessagesOfType = config.getStringList("include.only.messages.of.type"),
            excludeMessagesOfType = config.getStringList("exclude.messages.of.type"),
            fixSpec = fixSpec,
            msgColors = msgColors)

    val lineRegexPattern: Pattern by lazy {
        Pattern.compile(lineRegex)
    }
}