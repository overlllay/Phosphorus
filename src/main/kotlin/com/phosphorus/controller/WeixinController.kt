package com.phosphorus.controller

import me.chanjar.weixin.mp.api.WxMpService
import mu.KotlinLogging
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.web.bind.annotation.*

/**
 * @author wuxin
 */
@RestController
@RequestMapping("/weixin")
class WeixinController(val wxMpService: WxMpService) {

    private val log = KotlinLogging.logger { }

    @RequestMapping(
        path = ["/event"],
        method = [RequestMethod.GET, RequestMethod.POST]
    )
    suspend fun event(
        timestamp: String, nonce: String, signature: String,
        @RequestBody(required = false) body: String?,
        @RequestParam(required = false) openid: String?,
        @RequestParam(required = false) echostr: String?,
        exchange: ServerHttpResponse,
    ): String {
        log.info { "handle weixin event $timestamp" }
        val checked = wxMpService.checkSignature(timestamp, nonce, signature)
        if (!checked) {
            log.error { "check signature failed: timestamp=$timestamp, nonce=$nonce, signature=$signature" }
            return "failed"
        }
        if (echostr != null) {
            log.info { "weixin touch request, echostr: $echostr" }
            return echostr
        }
        log.info { "handle weixin event, body: $body" }
        return "success"
    }

}
