package com.phosphorus.support

import com.phosphorus.utils.JsonUtils
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service

/**
 * @author wuxin
 */
@Service
class RedisFacade(private val redisTemplate: StringRedisTemplate) {

    fun <T> add(key: String, value: T) {
        redisTemplate.opsForList().rightPush(key, JsonUtils.toJson(value))
    }

    fun <T> top(key: String, type: Class<T>, size: Long): List<T> {
        return range(key, type, 0, size - 1)
    }

    fun <T> range(key: String, type: Class<T>, start: Long, end: Long): List<T> {
        val values = redisTemplate.opsForList().range(key, start, end)
        if (values.isNullOrEmpty()) {
            return emptyList()
        }
        return values.stream()
            .map { JsonUtils.fromJson(it.toString(), type)!! }
            .toList()
    }

    fun size(key: String): Long {
        val size = redisTemplate.opsForList().size(key)
        return size ?: 0
    }

}
