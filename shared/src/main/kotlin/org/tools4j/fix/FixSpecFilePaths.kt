package org.tools4j.fix

import org.tools4j.properties.PropertiesFile

/**
 * User: ben
 * Date: 20/6/17
 * Time: 5:29 PM
 */
class FixSpecFilePaths(fieldsAndEnumValuesPropertiesPath: String, headerFieldsPropertiesPath: String, trailerFieldsPropertiesPath: String, messageTypesPropertiesPath: String) : FixSpecPaths {
    private val fieldsAndEnumValuesProperties: PropertiesFile by lazy {
        PropertiesFile(FilepathResource(fieldsAndEnumValuesPropertiesPath))
    }
    private val headerFieldsProperties: PropertiesFile by lazy {
        PropertiesFile(FilepathResource(headerFieldsPropertiesPath))
    }
    private val trailerFieldsProperties: PropertiesFile by lazy {
        PropertiesFile(FilepathResource(trailerFieldsPropertiesPath))
    }
    private val messageTypesProperties: PropertiesFile by lazy {
        PropertiesFile(FilepathResource(messageTypesPropertiesPath))
    }

    override val spec: FixSpec by lazy {
        val fieldsAndEnumValues = fieldsAndEnumValuesProperties.mapValue
        val messageTypes = messageTypesProperties.mapValue
        val headerFields = headerFieldsProperties.setValue
        val trailerFields = trailerFieldsProperties.setValue
        FixSpec(fieldsAndEnumValues, headerFields, trailerFields, messageTypes)
    }
}
