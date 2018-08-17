package org.tools4j.fixgrep.highlights

enum class  HighlightScope {
    Msg,
    Field;

    companion object {
        fun contains(str: String): Boolean{
            HighlightScope.values().forEach {
                if(it.name == str){
                    return true
                }
            }
            return false;
        }

        fun containsIgnoringCase(str: String): Boolean{
            HighlightScope.values().forEach {
                if(it.name.toLowerCase() == str.toLowerCase()){
                    return true
                }
            }
            return false;
        }
    }
}