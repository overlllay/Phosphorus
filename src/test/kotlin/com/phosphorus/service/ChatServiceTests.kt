package com.phosphorus.service

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

/**
 * @author wuxin
 */
@ActiveProfiles("test")
@SpringBootTest
class ChatServiceTests {

    @Autowired
    lateinit var chatService: ChatService

    @Test
    fun test() {
        val message = chatService.chat("", "编写 typescript Hello world 程序")
        println(message)
    }

}
