package com.phosphorus.service

import com.phosphorus.dto.ChatContext
import com.phosphorus.extension.ofMessage
import com.phosphorus.support.RedisFacade
import com.theokanning.openai.completion.chat.ChatCompletionRequest
import com.theokanning.openai.completion.chat.ChatMessage
import com.theokanning.openai.completion.chat.ChatMessageRole.USER
import com.theokanning.openai.service.OpenAiService
import org.springframework.stereotype.Service

/**
 * @author wuxin
 */
@Service
class ChatService(
    private val redisFacade: RedisFacade,
    private val openAiService: OpenAiService,
) {

    companion object {
        const val CHAT_CONTEXT_MSG_KEY = "p:chat-context-msg:%s"
        const val CHAT_CONTEXT_KEY = "p:chat-context:%s"
    }

    fun chat(chatId: String, message: String): ChatMessage {
        val chatContext = getChatContext(chatId)
        return doChat(message, chatContext)
    }

    private fun doChat(message: String, preContext: ChatContext): ChatMessage {
        val messages: MutableList<ChatMessage> = preContext.messages.toMutableList()
        messages.add(USER.ofMessage(message))

        val request = ChatCompletionRequest.builder()
            .model("gpt-3.5-turbo")
            .messages(messages)
            .build()
        return doOpenAiChat(request)
    }

    private fun doOpenAiChat(request: ChatCompletionRequest): ChatMessage {
        var o = ""
        var role = ""
        openAiService.streamChatCompletion(request)
            .blockingForEach { chunk ->
                chunk.choices.filter { msg -> msg.message.content != null }
                    .forEach { choice ->
                        o += choice.message.content
                        if (role.isBlank() && choice.message.role != null) {
                            role = choice.message.role
                        }
                    }
            }
        return ChatMessage(role, o)
    }

    private fun getChatContext(chatId: String): ChatContext {
        val size: Long = 6
        val cacheKey = CHAT_CONTEXT_MSG_KEY.format(chatId)
        val messages = redisFacade.top(cacheKey, ChatMessage::class.java, size)
        val hasMore = redisFacade.size(cacheKey) > size
        return ChatContext(chatId, "编程助手", hasMore, messages)
    }

}
