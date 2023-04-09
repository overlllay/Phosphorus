package com.phosphorus.extension

import com.theokanning.openai.completion.chat.ChatMessage
import com.theokanning.openai.completion.chat.ChatMessageRole

fun ChatMessageRole.ofMessage(content: String): ChatMessage {
    return ChatMessage(this.value(), content)
}
