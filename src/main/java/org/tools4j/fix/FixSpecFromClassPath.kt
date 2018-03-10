package org.tools4j.fix

import org.tools4j.properties.PropertiesFile

/**
 * User: ben
 * Date: 20/6/17
 * Time: 5:29 PM
 */
open class FixSpecFromClassPath(fieldsAndEnumValuesProperties: String, headerFieldsPropertiesPath: String, trailerFieldsPropertiesPath: String, messageTypesPropertiesPath: String) : FixSpecPaths {
    private val fieldsAndEnumValuesProperties: PropertiesFile
    private val headerFieldsPropertiesPath: PropertiesFile
    private val trailerFieldsPropertiesPath: PropertiesFile
    private val messageTypesPropertiesPath: PropertiesFile

    init {
        this.fieldsAndEnumValuesProperties = PropertiesFile(ClasspathResource(fieldsAndEnumValuesProperties))
        this.headerFieldsPropertiesPath = PropertiesFile(ClasspathResource(headerFieldsPropertiesPath))
        this.trailerFieldsPropertiesPath = PropertiesFile(ClasspathResource(trailerFieldsPropertiesPath))
        this.messageTypesPropertiesPath = PropertiesFile(ClasspathResource(messageTypesPropertiesPath))
    }

    override fun load(): FixSpec {
        val fieldsAndEnumValues = fieldsAndEnumValuesProperties.mapValue
        val messageTypes = messageTypesPropertiesPath.mapValue
        val headerFields = headerFieldsPropertiesPath.setValue
        val trailerFields = trailerFieldsPropertiesPath.setValue
        return FixSpec(fieldsAndEnumValues, headerFields, trailerFields, messageTypes)
    }
}
