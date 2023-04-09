package com.phosphorus.config

import com.lark.oapi.core.Config
import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * @author wuxin
 */
@ConfigurationProperties("lark")
data class LarkProperties(
    val client: Config,
    val encryptKey: String?,
    val verificationToken: String?,
)
