package org.tools4j.fixgrep.texteffect

/**
 * User: ben
 * Date: 5/04/2018
 * Time: 6:49 AM
 */
enum class AnsiForegroundBackground(val abbreviation: String) {
    FOREGROUND("Fg"),
    BACKGROUND("Bg");

    companion object {
        @JvmStatic
        fun contains(str: String): Boolean{
            AnsiForegroundBackground.values().forEach {
                if(it.name == str){
                    return true
                }
            }
            return false;
        }
    }
}


