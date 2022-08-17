package com.raedev.wink.utils

import java.lang.reflect.Field

/**
 *
 * @author RAE
 * @date 2022/08/15
 * @copyright Copyright (c) https://github.com/raedev All rights reserved.
 */
internal object ReflectionUtil {


    internal fun <T> getField(clazz: Class<T>, name: String): Field {
        val fields = mutableListOf<Field>()
        fields.addAll(clazz.declaredFields)
        fields.addAll(clazz.fields)
        var field = fields.singleOrNull { it.name == name }
        if (field == null) {
            field = clazz.superclass?.let { getField(it, name) }
        }
        field ?: throw NoSuchFieldException(name)
        field.isAccessible = true
        return field
    }

    internal fun <T> getFieldValue(clazz: Class<*>, obj: Any, name: String): T? {
        return getField(clazz, name).get(obj) as T?
    }

    internal fun <T> setFieldValue(clazz: Class<T>, obj: T, name: String, value: Any) {
        getField(clazz, name).set(obj, value)
    }
}