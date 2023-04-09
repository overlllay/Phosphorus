package com.phosphorus.config

import lombok.Data
import org.springframework.boot.context.properties.ConfigurationProperties
import java.time.Duration

@Data
@ConfigurationProperties("openai")
data class OpenAiProperties(
    val token: String,
    val baseUrl: String = "",
    val readTimeout: Duration = Duration.ofSeconds(60),
)
