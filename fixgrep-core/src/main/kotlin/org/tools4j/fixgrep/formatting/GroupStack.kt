package org.tools4j.fixgrep.formatting;

import org.tools4j.fix.spec.FieldsAndGroups
import org.tools4j.fix.spec.GroupSpec
import org.tools4j.fix.spec.MessageSpec
import java.util.*

/**
 * User: benjw
 * Date: 9/6/2018
 * Time: 5:14 PM
 */
class GroupStack(private val messageSpec: MessageSpec?, private val groupStack: Stack<CurrentGroupAndRepeatNumber>) {
    constructor(messageSpec: MessageSpec?): this(messageSpec, Stack<CurrentGroupAndRepeatNumber>())

    fun push(groupSpec: GroupSpec) = groupStack.push(CurrentGroupAndRepeatNumber(groupSpec))
    fun pop(): GroupSpec = groupStack.pop().group
    fun peek(): GroupSpec = groupStack.peek().group
    fun empty(): Boolean = groupStack.empty()
    fun size(): Int = groupStack.size

    fun incrementRepeatNumber(): Int{
        val currentGroupAndRepeatNumber = groupStack.pop()
        val currentGroupAndIncrementedRepeatNumber = currentGroupAndRepeatNumber.incrementRepeatNumber()
        groupStack.push(currentGroupAndIncrementedRepeatNumber)
        return currentGroupAndIncrementedRepeatNumber.currentRepeatNumber
    }

    fun getCurrentMessageOrGroupContext(): FieldsAndGroups? {
        return if(!groupStack.isEmpty()) groupStack.peek().group else messageSpec
    }

    fun getCurrentGroup(): GroupSpec? {
        return if(groupStack.isEmpty()) null else groupStack.peek().group
    }

    fun getCurrentRepeatNumber(): Int {
        return if(groupStack.isEmpty()) 0 else groupStack.peek().currentRepeatNumber
    }

    data class CurrentGroupAndRepeatNumber(val group: GroupSpec, val currentRepeatNumber: Int){
        constructor(group: GroupSpec): this(group, 0)

        fun incrementRepeatNumber(): CurrentGroupAndRepeatNumber{
            return CurrentGroupAndRepeatNumber(group, currentRepeatNumber + 1)
        }
    }
}
