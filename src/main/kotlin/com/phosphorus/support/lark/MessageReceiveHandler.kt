package com.phosphorus.support.lark

import com.lark.oapi.service.im.v1.ImService.P2MessageReceiveV1Handler
import com.lark.oapi.service.im.v1.model.P2MessageReceiveV1
import com.phosphorus.support.LarkFacade
import com.phosphorus.utils.LarkUtils
import org.springframework.stereotype.Component

/**
 * @author wuxin
 */
@Component
class MessageReceiveHandler(val larkFacade: LarkFacade) : P2MessageReceiveV1Handler() {

    override fun handle(event: P2MessageReceiveV1) {
        val message = event.event.message
        if (!LarkUtils.isTextMessage(message)) {
            larkFacade.replyTextMessage(message.messageId, "仅支持文本消息")
            return
        }
        val senderId = event.event.sender.senderId.openId
        val text = LarkUtils.getTextMessageContentAndReplaceMentions(message)
    }

}
