package org.tools4j.fixgrep.highlights

enum class  HighlightScope {
    Line,
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
    }
}