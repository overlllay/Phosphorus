package com.phosphorus.config

import com.lark.oapi.Client
import com.lark.oapi.core.Config
import com.lark.oapi.event.EventDispatcher
import com.lark.oapi.event.IEventHandler
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.ObjectProvider
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.util.ClassUtils
import org.springframework.util.ReflectionUtils
import java.lang.reflect.Method

/**
 * @author wuxin
 */
@Configuration
@EnableConfigurationProperties(LarkProperties::class)
class LarkConfig(val larkProperties: LarkProperties) {

    private val log = LoggerFactory.getLogger(LarkConfig::class.java)

    @Bean
    fun larkClient(): Client {
        val cfg: Config = larkProperties.client
        val builder = Client.newBuilder(cfg.appId, cfg.appSecret)
        if (cfg.isDisableTokenCache) {
            builder.disableTokenCache()
        }
        return builder.appType(cfg.appType)
            .tokenCache(cfg.cache)
            .openBaseUrl(cfg.baseUrl)
            .logReqAtDebug(cfg.isLogReqAtDebug)
            .httpTransport(cfg.httpTransport)
            .helpDeskCredential(cfg.helpDeskID, cfg.helpDeskToken)
            .requestTimeout(cfg.requestTimeOut, cfg.timeOutTimeUnit)
            .build()
    }

    @Bean
    fun eventDispatcher(eventHandlers: ObjectProvider<IEventHandler<*>>): EventDispatcher {
        val encryptKey = larkProperties.encryptKey
        val verificationToken = larkProperties.verificationToken
        val builder: EventDispatcher.Builder = EventDispatcher.newBuilder(verificationToken, encryptKey)
        for (eventHandler in eventHandlers) {
            val eventHandlerType = ClassUtils.getUserClass(eventHandler)
            val registryMethod = findRegistryMethod(eventHandlerType)
            if (registryMethod == null) {
                log.warn("Unknown event handler of {}", eventHandler)
                continue
            }
            ReflectionUtils.invokeMethod(registryMethod, builder, eventHandler)
            log.info("Registry lark event handler {}", eventHandler)
        }
        return builder.build()
    }

    private fun findRegistryMethod(eventHandlerType: Class<*>): Method? {
        return EventDispatcher.Builder::class.java.declaredMethods
            .firstOrNull {
                it.parameterCount == 1
                        && it.parameterTypes[0].isAssignableFrom(eventHandlerType)
                        && it.name.startsWith("on")
            }
    }

}
