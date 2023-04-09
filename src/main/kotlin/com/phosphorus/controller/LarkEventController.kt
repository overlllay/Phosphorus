package com.phosphorus.controller

import com.lark.oapi.core.request.EventReq
import com.lark.oapi.event.EventDispatcher
import com.phosphorus.utils.LarkUtils
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.context.request.NativeWebRequest

/**
 * @author wuxin
 */
@RestController
@RequestMapping("/lark")
class LarkEventController(val eventDispatcher: EventDispatcher) {

    @RequestMapping("/botEvent")
    fun onBotEvent(
        @RequestBody body: ByteArray,
        @RequestHeader headers: Map<String, List<String>>,
        request: NativeWebRequest,
    ): ResponseEntity<String> {
        val eventReq = EventReq().apply {
            this.body = body
            this.httpPath = request.contextPath
            this.headers = headers
        }
        val eventResp = eventDispatcher.handle(eventReq)
        return LarkUtils.toResponseEntity(eventResp)
    }

}
