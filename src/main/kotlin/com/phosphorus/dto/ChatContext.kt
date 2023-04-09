package com.phosphorus.dto

import com.theokanning.openai.completion.chat.ChatMessage

data class ChatContext(
    val chatId: String,
    val subject: String,
    val hasMore: Boolean,
    val messages: List<ChatMessage>,
)
