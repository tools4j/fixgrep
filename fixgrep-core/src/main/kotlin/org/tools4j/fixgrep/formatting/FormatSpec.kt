package org.tools4j.fixgrep.formatting

import org.tools4j.fix.AnnotationPositions
import org.tools4j.fix.Ascii1Char
import org.tools4j.fix.DelimiterImpl
import org.tools4j.fix.Fields
import org.tools4j.fix.spec.FixSpecDefinition
import org.tools4j.fix.spec.FixSpecParser
import org.tools4j.fixgrep.config.FixGrepConfig
import org.tools4j.fixgrep.highlights.Highlight
import org.tools4j.fixgrep.highlights.HighlightParser
import org.tools4j.fixgrep.linehandlers.FixLine
import org.tools4j.fixgrep.utils.Constants
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
        val groupBy: List<String>?,
        val indentGroupRepeats: Boolean,
        val inputDelimiter: String,
        val outputDelimiter: String,
        val outputFormatHorizontalConsole: String,
        val outputFormatHorizontalHtml: String,
        val outputFormatVerticalConsole: String,
        val outputFormatVerticalHtml: String,
        val outputFormatGroupedOrderHeaderConsole: String,
        val outputFormatGroupedOrderHeaderHtml: String,
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
        val fixSpec: FixSpecDefinition,
        val msgColors: MessageColors,
        val debug: Boolean,
        val formatInHtml: Boolean) {

    constructor() : this(
            false,
            false,
            Highlight.NO_HIGHLIGHT,
            null,
            true,
            Ascii1Char().toString(),
            "|",
            "\${senderToTargetCompIdDirection} \${msgColor}[\${msgTypeName}]\${colorReset} \${msgFix}",
            "\${senderToTargetCompIdDirection} \${msgColor}[\${msgTypeName}]\${colorReset} \${msgFix}",
            "${Constants.EQUALS_SEPARATOR}\n\${msgColor}[\${msgTypeName}]\${colorReset}\n${Constants.EQUALS_SEPARATOR}\n\${msgFix}",
            "${Constants.EQUALS_SEPARATOR}<br/>\${msgColor}[\${msgTypeName}]\${colorReset}<br/>${Constants.EQUALS_SEPARATOR}<br/>\${msgFix}",
            "${Constants.EQUALS_SEPARATOR}\nORDER orderId:\${orderId} clOrdId:\${origClOrdId}\n${Constants.EQUALS_SEPARATOR}\n",
            "${Constants.EQUALS_SEPARATOR}<br/>ORDER orderId:\${orderId} clOrdId:\${origClOrdId}<br/>${Constants.EQUALS_SEPARATOR}<br/>",
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
            FixSpecParser("FIX50SP2.xml").parseSpec(),
            MessageColors(),
            false,
            false)

    @JvmOverloads
    constructor(
            config: FixGrepConfig,
            msgColors: MessageColors = MessageColors()) : this(
            suppressColors = config.suppressColors,
            suppressBoldTagsAndValues = config.suppressBoldTagsAndValues,
            highlight = HighlightParser().parse(config.highlights),
            groupBy = config.getIdsToOrdersGroupsBy,
            indentGroupRepeats = config.indentGroupRepeats,
            inputDelimiter = config.inputDelimiter,
            outputDelimiter = config.outputDelimiter,
            outputFormatHorizontalConsole = config.outputFormatHorizontalConsole,
            outputFormatHorizontalHtml = config.outputFormatHorizontalHtml,
            outputFormatVerticalConsole = config.outputFormatVerticalConsole,
            outputFormatVerticalHtml = config.outputFormatVerticalHtml,
            outputFormatGroupedOrderHeaderConsole = config.outputFormatGroupedOrderHeaderConsole,
            outputFormatGroupedOrderHeaderHtml = config.outputFormatGroupedOrderHeaderHtml,
            lineRegex = config.inputLineFormatRegex,
            lineRegexGroupForFix = config.lineRegexGroupForFix,
            sortByTags = config.sortByTags,
            onlyIncludeTags = config.includeOnlyTags,
            excludeTags = config.excludeTags,
            tagAnnotations = config.tagAnnotations,
            verticalFormat = config.verticalFormat,
            alignVerticalColumns = config.alignVerticalColumns,
            includeOnlyMessagesOfType = config.includeOnlyMessagesOfType,
            excludeMessagesOfType = config.excludeMessagesOfType,
            fixSpec = FixSpecParser(config.fixSpecPath).parseSpec(),
            msgColors = msgColors,
            formatInHtml = config.htmlFormatting,
            debug = config.debugMode)

    fun copyWithModifications(
            suppressColors: Boolean = this.suppressColors,
            suppressBoldTagsAndValues: Boolean = this.suppressBoldTagsAndValues,
            highlight: Highlight = this.highlight,
            groupBy: List<String>? = this.groupBy,
            indentGroupRepeats: Boolean = this.indentGroupRepeats,
            inputDelimiter: String = this.inputDelimiter,
            outputDelimiter: String = this.outputDelimiter,
            outputFormatHorizontalConsole: String = this.outputFormatHorizontalConsole,
            outputFormatHorizontalHtml: String = this.outputFormatHorizontalHtml,
            outputFormatVerticalConsole: String = this.outputFormatVerticalConsole,
            outputFormatVerticalHtml: String = this.outputFormatVerticalHtml,
            outputFormatGroupedOrderHeaderConsole: String = this.outputFormatGroupedOrderHeaderConsole,
            outputFormatGroupedOrderHeaderHtml: String = this.outputFormatGroupedOrderHeaderHtml,
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
            fixSpec: FixSpecDefinition = this.fixSpec,
            msgColors: MessageColors = this.msgColors,
            debug: Boolean = this.debug,
            formatInHtml: Boolean = this.formatInHtml): FormatSpec {

        return FormatSpec(
                suppressColors,
                suppressBoldTagsAndValues,
                highlight,
                groupBy,
                indentGroupRepeats,
                inputDelimiter,
                outputDelimiter,
                outputFormatHorizontalConsole,
                outputFormatHorizontalHtml,
                outputFormatVerticalConsole,
                outputFormatVerticalHtml,
                outputFormatGroupedOrderHeaderConsole,
                outputFormatGroupedOrderHeaderHtml,
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
        val indentGroupRepeatsForThisMessage = indentGroupRepeats && sortByTags.isEmpty()
        val formattingContext = FormattingContext(fields, tagAnnotationPositions, !suppressBoldTagsAndValues, indentGroupRepeatsForThisMessage, onlyIncludeTags, excludeTags, fixSpec)
        if (formatInHtml) {
            if (verticalFormat) {
                if (alignVerticalColumns) {
                    return VerticalAlignedHtmlMsgFormatter(formattingContext)
                } else {
                    return VerticalNonAlignedHtmlMsgFormatter(formattingContext)
                }
            } else {
                return HorizontalHtmlMsgFormatter(formattingContext, DelimiterImpl(outputDelimiter))
            }
        } else {
            if (verticalFormat) {
                if (alignVerticalColumns) {
                    return VerticalAlignedConsoleMsgFormatter(formattingContext)
                } else {
                    return VerticalNonAlignedConsoleMsgFormatter(formattingContext)
                }
            } else {
                return HorizontalConsoleMsgFormatter(formattingContext, DelimiterImpl(outputDelimiter))
            }
        }
    }

    fun getOutputFormat(): String {
        if (formatInHtml) {
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

    fun getOutputFormatGroupedOrderHeader(): String {
        if (formatInHtml) {
            return outputFormatGroupedOrderHeaderHtml
        } else {
            return outputFormatGroupedOrderHeaderConsole
        }
    }

    fun shouldPrint(fixLine: FixLine): Boolean {
        if (!includeOnlyMessagesOfType.isEmpty()
                && !includeOnlyMessagesOfType.contains(fixLine.fields.msgTypeCode)) {
            return false
        } else if (!excludeMessagesOfType.isEmpty()
                && excludeMessagesOfType.contains(fixLine.fields.msgTypeCode)) {
            return false
        } else {
            return true
        }
    }

    fun getCarriageReturn(): String {
        return if(formatInHtml){
            "<br/>\n"
        } else {
            "\n"
        }
    }
}