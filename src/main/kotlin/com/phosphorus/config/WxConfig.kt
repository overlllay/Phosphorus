package com.phosphorus.config

import me.chanjar.weixin.mp.api.WxMpService
import me.chanjar.weixin.mp.api.impl.WxMpServiceOkHttpImpl
import me.chanjar.weixin.mp.config.WxMpConfigStorage
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * @author wuxin
 */
@Configuration
@EnableConfigurationProperties(WxMpProperties::class)
class WxConfig(val wxMpProperties: WxMpProperties) {

    @Bean
    fun wxMpService(): WxMpService {
        var service = WxMpServiceOkHttpImpl()
        service.wxMpConfigStorage = buildWxMpConfigStorage()
        return service
    }

    private fun buildWxMpConfigStorage(): WxMpConfigStorage {
        val config = WxMpDefaultConfigImpl()
        config.appId = wxMpProperties.appId
        config.secret = wxMpProperties.secret
        return config
    }

}
