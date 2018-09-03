package org.tools4j.fix.spec

import org.tools4j.utils.ExistingFileForInput
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.io.InputStream
import javax.xml.parsers.DocumentBuilderFactory

/**
 * User: benjw
 * Date: 8/21/2018
 * Time: 6:43 AM
 */
class FixSpecParser(private val specInputStream: InputStream) {
    constructor(specPath: String): this(ExistingFileForInput(specPath).inputStream)
    constructor(): this("FIX50SP2.xml")

    companion object {
        @JvmStatic
        fun main(args: Array<String>){
            val before = System.currentTimeMillis()
            val fixSpec = FixSpecParser("FIX50SP2.xml").parseSpec();
            val after = System.currentTimeMillis()
            //println(after - before)
        }
        val INDENT = "    "
    }

    fun parseSpec(): FixSpecDefinition{
        val timeMsStart = System.currentTimeMillis()
        val xmlDoc: Document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(specInputStream)
        //println("After initial xml parse: " + (System.currentTimeMillis() - timeMsStart))
        xmlDoc.documentElement.normalize()
        //println("After normalize: " + (System.currentTimeMillis() - timeMsStart))

        //FIELDS
        val fieldList: NodeList = xmlDoc.getElementsByTagName("fields").item(0).childNodes
        val fieldsByName = LinkedHashMap<String, FixSpecDefinition.Field>()
        fun extractFields(localFieldList: NodeList): Map<String, FixSpecDefinition.Field> {
            val localFieldsByName = LinkedHashMap<String, FixSpecDefinition.Field>()
            for (i in 0 until localFieldList.length) {
                val fieldNode: Node = localFieldList.item(i)
                if (fieldNode.getNodeType() != Node.ELEMENT_NODE) continue
                val fieldElement = fieldNode as Element

                val enums = LinkedHashSet<FixSpecDefinition.FieldEnum>()
                for (j in 0 until fieldElement.childNodes.length) {
                    val enumNode: Node = fieldElement.childNodes.item(j)
                    if (enumNode.getNodeType() != Node.ELEMENT_NODE) continue
                    val enumElement = enumNode as Element
                    if (enumElement.tagName != "value") continue
                    enums.add(FixSpecDefinition.FieldEnum(enumElement.getAttribute("enum"), enumElement.getAttribute("description")))
                }
                try {
                    val field = FixSpecDefinition.Field(fieldElement.getAttribute("name"), fieldElement.getAttribute("number").toInt(), fieldElement.getAttribute("type"), enums)
                    localFieldsByName.put(field.name, field)
                } catch(e: Exception) {
                    println("Error parsing field: " + fieldElement.getAttribute("name"))
                    throw e
                }
            }
            return localFieldsByName;
        }
        fieldsByName.putAll(extractFields(fieldList))

        //HEADER
        val headerNodes = xmlDoc.getElementsByTagName("header")
        if(headerNodes.length != 1) throw IllegalStateException("There must be one and only one 'header' node.  [${headerNodes.length}] found")
        val headerElement = headerNodes.item(0) as Element
        val header = extractConstituents(headerElement, fieldsByName)

        //TRAILER
        val trailerNodes = xmlDoc.getElementsByTagName("trailer")
        if(trailerNodes.length != 1) throw IllegalStateException("There must be one and only one 'trailer' node.  [${trailerNodes.length}] found")
        val trailerElement = trailerNodes.item(0) as Element
        val trailer = extractConstituents(trailerElement, fieldsByName)

        //COMPONENTS
        val componentsNodes = xmlDoc.getElementsByTagName("components")
        if(componentsNodes.length != 1) throw IllegalStateException("There must be one and only one 'componentsNode' node.  [${componentsNodes.length}] found")
        val componentsList = componentsNodes.item(0).childNodes
        val rawComponentsByName = LinkedHashMap<String, RawComponent>()
        for(i in 0 until componentsList.length){
            val componentNode: Node = componentsList.item(i)
            if (componentNode.getNodeType() != Node.ELEMENT_NODE) continue
            val componentElement = componentNode as Element
            val constituents = extractConstituents(componentElement, fieldsByName)
            val componentName = componentElement.getAttribute("name")
            rawComponentsByName.put(componentName, RawComponent(componentName, constituents))
        }
        //println("After component parsing: " + (System.currentTimeMillis() - timeMsStart))

        //MESSAGES
        val messagesNodes = xmlDoc.getElementsByTagName("messages")
        if(messagesNodes.length != 1) throw IllegalStateException("There must be one and only one 'messages' node.  [${messagesNodes.length}] found")
        val messagesList = messagesNodes.item(0).childNodes
        val rawMessagesByName = LinkedHashMap<String, RawMessage>()
        for(i in 0 until messagesList.length){
            val messageNode: Node = messagesList.item(i)
            if (messageNode.getNodeType() != Node.ELEMENT_NODE) continue
            val messageElement = messageNode as Element
            val constituents = extractConstituents(messageElement, fieldsByName)
            rawMessagesByName.put(messageElement.getAttribute("name"), RawMessage(messageElement.getAttribute("name"), messageElement.getAttribute("msgtype"), constituents))
        }
        //println("After message parsing: " + (System.currentTimeMillis() - timeMsStart))

        //EXPAND CONSTITUENTS
        val fixSpec = ConstituentExpander(fieldsByName, header, trailer, rawComponentsByName, rawMessagesByName).expand()
        //println("After expanding: " + (System.currentTimeMillis() - timeMsStart))
        return fixSpec
    }

    class ConstituentExpander(val fieldsByName: MutableMap<String, FixSpecDefinition.Field>,
                              val rawHeader: ArrayList<Any>,
                              val rawTrailer: ArrayList<Any>,
                              val rawComponentsByName: LinkedHashMap<String, RawComponent>,
                              val rawMessagesByName: LinkedHashMap<String, RawMessage>){

        val groupsByName = LinkedHashMap<String, FixSpecDefinition.Group>()
        val messagesByName = LinkedHashMap<String, FixSpecDefinition.Message>()
        val componentsByName = LinkedHashMap<String, FixSpecDefinition.Component>()

        public fun expand(): FixSpecDefinition {
            for(rawMessage in rawMessagesByName.values){
                messagesByName.put(rawMessage.name, FixSpecDefinition.Message(rawMessage.name, rawMessage.msgType, expandConstituents(rawMessage.constituents)))
            }
            return FixSpecDefinition(fieldsByName, expandConstituents(rawHeader), expandConstituents(rawTrailer), groupsByName, messagesByName)
        }

        private fun expandConstituents(constituents: Collection<Any>): FixSpecDefinition.FieldsAndGroups {
            val fieldsGroupsAndComponents = FixSpecDefinition.FieldsAndGroups()
            for (constituent in constituents) {
                if (constituent is FixSpecDefinition.Field) {
                    fieldsGroupsAndComponents.addField(fieldsByName.get(constituent.name)!!)

                } else if (constituent is RawGroup) {
                    var group = groupsByName.get(constituent.name)
                    if (group == null) {
                        group = FixSpecDefinition.Group(constituent.name, expandConstituents(constituent.constituents))
                        groupsByName.put(group.name, group)
                    }
                    fieldsGroupsAndComponents.addGroup(group)

                } else if (constituent is ComponentReference) {
                    var component = componentsByName.get(constituent.name)
                    if (component == null) {
                        val rawComponent = rawComponentsByName.get(constituent.name)
                        if(rawComponent == null) throw IllegalStateException("Cannot find Component, or RawComponent with name [${constituent.name}]")
                        component = FixSpecDefinition.Component(constituent.name, expandConstituents(rawComponent.constituents))
                        componentsByName.put(component.name, component)
                    }
                    fieldsGroupsAndComponents.addComponent(component)

                } else if (constituent is RawMessage) {
                    throw IllegalArgumentException("constituent cannot be a message: $constituent")
                }
            }
            return fieldsGroupsAndComponents
        }
    }

    fun extractConstituents(parentElement: Element, fieldsMap: LinkedHashMap<String, FixSpecDefinition.Field>): ArrayList<Any> {
        val constituents = ArrayList<Any>()
        for (j in 0 until parentElement.childNodes.length) {
            val childNode: Node = parentElement.childNodes.item(j)
            if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                val childElement = childNode as Element
                if (childElement.tagName == "field") {
                    constituents.add(fieldsMap[childElement.getAttribute("name")!!]!!)
                } else if (childElement.tagName == "component") {
                    constituents.add(ComponentReference(childElement.getAttribute("name")))
                } else if (childElement.tagName == "group") {
                    constituents.add(RawGroup(childElement.getAttribute("name"), extractConstituents(childElement, fieldsMap)))
                }
            }
        }
        return constituents
    }

    data class ComponentReference(val name: String)
    data class RawComponent(val name: String, val constituents: List<Any>)
    data class RawMessage(val name: String, val msgType: String, val constituents: List<Any>)
    data class RawGroup(val name: String, val constituents: List<Any>)
    data class RawHeader(val constituents: List<Any>)
    data class RawTrailer(val constituents: List<Any>)
}