package org.tools4j.fixgrep.formatting;

import org.tools4j.fix.spec.GroupSpec
import java.util.*

/**
 * User: benjw
 * Date: 9/6/2018
 * Time: 5:14 PM
 */
class GroupStack(private val groupStack: Stack<CurrentGroupAndRepeatNumber>) {
    constructor(): this(Stack<CurrentGroupAndRepeatNumber>())

    fun push(groupSpec: GroupSpec) = groupStack.push(CurrentGroupAndRepeatNumber(groupSpec))
    fun pop(): GroupSpec = groupStack.pop().group
    fun peek(): GroupSpec = groupStack.peek().group
    fun empty(): Boolean = groupStack.empty()
    fun size(): Int = groupStack.size

    fun incrementRepeatNumber(){
        groupStack.push(groupStack.pop().incrementRepeatNumber())
    }

    fun getCurrentGroup(): GroupSpec? {
        return if(groupStack.isEmpty()) null else groupStack.peek().group
    }

    fun getCurrentRepeatNumber(): Int? {
        return if(groupStack.isEmpty()) null else groupStack.peek().currentRepeatNumber
    }

    data class CurrentGroupAndRepeatNumber(val group: GroupSpec, val currentRepeatNumber: Int){
        constructor(group: GroupSpec): this(group, 0)

        fun incrementRepeatNumber(): CurrentGroupAndRepeatNumber{
            return CurrentGroupAndRepeatNumber(group, currentRepeatNumber + 1)
        }
    }
}
