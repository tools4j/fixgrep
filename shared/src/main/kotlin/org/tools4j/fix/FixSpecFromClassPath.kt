package org.tools4j.fix

import org.tools4j.properties.PropertiesFile

/**
 * User: ben
 * Date: 20/6/17
 * Time: 5:29 PM
 */
open class FixSpecFromClassPath(fieldsAndEnumValuesPropertiesPath: String, headerFieldsPropertiesPath: String, trailerFieldsPropertiesPath: String, messageTypesPropertiesPath: String) : FixSpecPaths {

    val fieldsAndEnumValuesProperties: PropertiesFile by lazy {
        PropertiesFile(ClasspathResource(fieldsAndEnumValuesPropertiesPath))
    }

    val headerFieldsProperties: PropertiesFile by lazy {
        PropertiesFile(ClasspathResource(headerFieldsPropertiesPath))
    }

    val trailerFieldsProperties: PropertiesFile by lazy {
        PropertiesFile(ClasspathResource(trailerFieldsPropertiesPath))
    }

    val messageTypesProperties: PropertiesFile by lazy {
        PropertiesFile(ClasspathResource(messageTypesPropertiesPath))
    }

    override val spec: FixSpec by lazy{
        val fieldsAndEnumValues = fieldsAndEnumValuesProperties.mapValue
        val messageTypes = messageTypesProperties.mapValue
        val headerFields = headerFieldsProperties.setValue
        val trailerFields = trailerFieldsProperties.setValue
        FixSpec(fieldsAndEnumValues, headerFields, trailerFields, messageTypes)
    }
}
