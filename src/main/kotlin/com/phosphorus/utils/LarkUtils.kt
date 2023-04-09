package com.phosphorus.utils

import com.lark.oapi.core.response.EventResp
import com.lark.oapi.core.utils.Jsons
import com.lark.oapi.service.im.v1.model.EventMessage
import com.phosphorus.dto.MessageContent.TextMessageContent
import org.apache.commons.lang3.StringUtils
import org.springframework.http.ResponseEntity
import java.nio.charset.StandardCharsets

/**
 * @author wuxin
 */
object LarkUtils {

    const val OPEN_ID = "open_id"

    const val USER_ID = "user_id"

    const val UNION_ID = "union_id"

    fun toUserIdType(id: String): String {
        if (id.startsWith("on_")) {
            return UNION_ID
        } else if (id.startsWith("ou_")) {
            return OPEN_ID
        } else if (StringUtils.isAlphanumeric(id)) {
            return USER_ID
        }
        throw IllegalArgumentException("unknown lark user id type")
    }

    fun toReceiveIdType(id: String): String {
        if (id.startsWith("oc_")) {
            return "chat_id"
        } else if (id.contains("@")) {
            return "email"
        }
        return toUserIdType(id)
    }

    fun isTextMessage(message: EventMessage) = "text" == message.messageType

    fun isMessageId(receiveId: String) = receiveId.startsWith("om_")

    fun toResponseEntity(eventResp: EventResp): ResponseEntity<String> {
        return ResponseEntity.status(eventResp.statusCode)
            .headers { it.putAll(eventResp.headers) }
            .body(eventResp.body.toString(StandardCharsets.UTF_8))
    }

    fun getTextMessageContent(message: EventMessage): TextMessageContent {
        return Jsons.DEFAULT.fromJson(message.content, TextMessageContent::class.java)
    }

    fun getTextMessageContentAndReplaceMentions(message: EventMessage): String {
        var text = getTextMessageContent(message).text
        if (message.mentions == null) {
            return text
        }
        for (mention in message.mentions) {
            text = text.replace(mention.key, "")
        }
        return text
    }

}
