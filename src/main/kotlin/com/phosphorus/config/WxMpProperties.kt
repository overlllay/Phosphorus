package com.phosphorus.config

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * @author wuxin
 */
@ConfigurationProperties("wx.mp")
data class WxMpProperties(val appId: String, val secret: String)
