package com.phosphorus

import com.phosphorus.extension.ofMessage
import com.phosphorus.support.RedisFacade
import com.theokanning.openai.completion.chat.ChatMessage
import com.theokanning.openai.completion.chat.ChatMessageRole.SYSTEM
import com.theokanning.openai.completion.chat.ChatMessageRole.USER
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.test.context.ActiveProfiles

/**
 * @author wuxin
 */
@ActiveProfiles("test")
@SpringBootTest
class ApplicationTests {

    @Autowired
    lateinit var redisTemplate: StringRedisTemplate

    @Autowired
    lateinit var redisFacade: RedisFacade

    @Test
    fun test() {
        Assertions.assertTrue(true)
    }

    @Test
    fun testRedis() {
        redisFacade.add("messages", SYSTEM.ofMessage("Hi 1"))
        redisFacade.add("messages", USER.ofMessage("Hi 2"))

        val messages = redisFacade.top("messages", ChatMessage::class.java, 2)
        println(messages)
    }

}
