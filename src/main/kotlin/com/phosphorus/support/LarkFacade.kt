package com.phosphorus.support

import com.lark.oapi.Client
import com.lark.oapi.service.im.v1.model.CreateMessageReq
import com.lark.oapi.service.im.v1.model.CreateMessageReqBody
import com.lark.oapi.service.im.v1.model.ReplyMessageReq
import com.lark.oapi.service.im.v1.model.ReplyMessageReqBody
import com.phosphorus.dto.MessageContent
import com.phosphorus.extension.ensureData
import com.phosphorus.utils.LarkUtils
import org.springframework.stereotype.Component

/**
 * @author wuxin
 */
@Component
class LarkFacade(private val larkClient: Client) {

    companion object {
        private const val MSG_TYPE_OF_TEXT = "text"
    }

    fun sendOrReplyTextMessage(receiveId: String, message: String): String {
        val content = MessageContent.ofText(message)
        return if (LarkUtils.isMessageId(receiveId))
            replyMessage(receiveId, MSG_TYPE_OF_TEXT, content)
        else
            sendTextMessage(receiveId, message)
    }

    fun sendTextMessage(receiveId: String, message: String): String {
        return sendMessage(receiveId, MSG_TYPE_OF_TEXT, MessageContent.ofText(message))
    }

    fun replyTextMessage(messageId: String, message: String): String {
        return replyMessage(messageId, MSG_TYPE_OF_TEXT, MessageContent.ofText(message))
    }

    fun replyMessage(messageId: String, msgType: String, content: MessageContent): String {
        val body = ReplyMessageReqBody.newBuilder()
            .msgType(msgType)
            .content(content.toJson())
            .build()
        val request = ReplyMessageReq.newBuilder()
            .messageId(messageId)
            .replyMessageReqBody(body)
            .build()
        return larkClient.im().message().reply(request).ensureData().messageId
    }

    fun sendMessage(receiveId: String, msgType: String, content: MessageContent): String {
        val receiveIdType: String = LarkUtils.toReceiveIdType(receiveId)
        val body = CreateMessageReqBody.newBuilder()
            .msgType(msgType)
            .content(content.toJson())
            .receiveId(receiveId)
            .build()
        val request = CreateMessageReq.newBuilder()
            .createMessageReqBody(body)
            .receiveIdType(receiveIdType)
            .build()
        return larkClient.im().message().create(request).ensureData().messageId
    }

}
