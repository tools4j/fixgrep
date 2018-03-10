package org.tools4j.properties

import java.util.Arrays

/**
 * User: ben
 * Date: 15/12/2016
 * Time: 5:37 PM
 */
class ListProperty<T>(vararg list: T) : Property<List<T>>(Arrays.asList<T>(*list))
