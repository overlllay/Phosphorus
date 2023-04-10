package com.phosphorus

import org.jasypt.encryption.StringEncryptor
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.env.Environment

/**
 * @author wuxin
 */
@SpringBootTest(properties = ["debug=false"])
class JasyptTests {

    @Autowired
    private lateinit var encryptor: StringEncryptor

    @Autowired
    private lateinit var env: Environment

    @Test
    fun encryptKeys() {
        val keys = listOf(
            "openai.token",
            "openai.base-url",
            "lark.client.app-secret",
            "lark.verification-token",
            "spring.data.redis.password",
            "spring.data.redis.host",
            "wx.mp.secret"
        )
        keys.forEach {
            println(
                """
                key: $it
                value: ${env.getProperty(it)}
                encrypted: ${encryptor.encrypt(env.getProperty(it))}

            """.trimIndent()
            )
        }
    }

}
