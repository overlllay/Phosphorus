package com.phosphorus.utils

import com.fasterxml.jackson.databind.ObjectMapper

/**
 * @author wuxin
 */
object JsonUtils {

    private val objectMapper: ObjectMapper = ObjectMapper()

    fun toJson(value: Any?): String {
        return objectMapper.writeValueAsString(value)
    }

    fun <T> fromJson(text: String?, type: Class<T>): T? {
        return objectMapper.readValue(text, type)
    }

}
