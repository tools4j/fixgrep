package org.tools4j.properties

import java.io.StringReader
import java.util.*

/**
 * User: ben
 * Date: 2/05/2018
 * Time: 6:43 AM
 */
class ConfigLoader {
    companion object {
        val ENDS_WITH_FILE_EXTENSION = Regex.fromLiteral("\\.\\w+$")

        @JvmStatic
        fun fromFileSystem(propertiesPathAndFileName: String): Config? {
            return FromFileSystem(propertiesPathAndFileName).config
        }

        @JvmStatic
        fun fromHomeDir(propertiesPathAndFileName: String): Config? {
            return FromHomeDir(propertiesPathAndFileName).config
        }

        @JvmStatic
        fun fromClasspath(propertiesPathAndFileName: String): Config? {
            return FromClasspath(propertiesPathAndFileName).config
        }

        @JvmStatic
        fun fromString(str: String): Config? {
            return FromString(str).config
        }

        @JvmStatic
        fun fromHomeDirOrClasspath(propertiesPathAndFileName: String): Config? {
            val propertiesRepo = FromHomeDir(propertiesPathAndFileName + ".properties").config
            if(propertiesRepo != null) return propertiesRepo
            else return FromClasspath(propertiesPathAndFileName).config
        }

        @JvmStatic
        fun fromHomeDirOrWorkingDirOrClasspath(propertiesPathAndFileName: String): Config? {
            var propertiesRepo = FromHomeDir(propertiesPathAndFileName + ".properties").config
            if(propertiesRepo != null) return propertiesRepo
            propertiesRepo = FromFileSystem(propertiesPathAndFileName).config
            if(propertiesRepo != null) return propertiesRepo
            else return FromClasspath(propertiesPathAndFileName).config
        }

        @JvmStatic
        fun cleanPathForClasspath(path: String): String{
            var returnPath = path
            if(!returnPath.matches(ENDS_WITH_FILE_EXTENSION)){
                returnPath = returnPath.replace(ENDS_WITH_FILE_EXTENSION, "")
            }
            if(!returnPath.startsWith("/")){
                returnPath = "/" + returnPath
            }
            return returnPath
        }

        @JvmStatic
        fun cleanPathForFileSystem(path: String): String{
            var returnPath = path
            if(!returnPath.matches(ENDS_WITH_FILE_EXTENSION)){
                returnPath = returnPath.replace(ENDS_WITH_FILE_EXTENSION, "")
            }
            return returnPath
        }
    }


    open class FromFileSystem(val propertiesPathAndFileName: String): ConfigSource {
        override val config: Config? by lazy {
            try {
                val config = Properties()
                val file = java.io.File(cleanPathForFileSystem(propertiesPathAndFileName))
                if(!file.exists()){
                    null
                } else {
                    val fis = java.io.FileInputStream(file)
                    config.load(fis)
                    fis.close()
                    ConfigImpl(config)
                }
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        }
    }

    class FromClasspath(val propertiesPathAndFileName: String): ConfigSource {
        override val config: Config? by lazy {
            try {
                val config = Properties()
                val inputStream = this.javaClass.getResourceAsStream(cleanPathForClasspath(propertiesPathAndFileName))
                if(inputStream == null){
                    null
                } else {
                    config.load(inputStream)
                    inputStream.close()
                    ConfigImpl(config)
                }
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        }
    }

    class FromHomeDir(propertiesPathAndFileNameWithExtension: String): FromFileSystem(System.getProperty("user.dir") + System.lineSeparator() + propertiesPathAndFileNameWithExtension)

    class FromString(private val str: String): ConfigSource {
        override val config: Config? by lazy{
            try {
                val properties = Properties()
                properties.load(StringReader(str))
                ConfigImpl(properties)
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        }
    }
}