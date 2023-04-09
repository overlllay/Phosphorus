package com.phosphorus.dto

import com.phosphorus.utils.JsonUtils
import lombok.Data

/**
 * @author wuxin
 */
interface MessageContent {

    fun toJson(): String = JsonUtils.toJson(this)

    companion object {

        fun ofText(text: String): TextMessageContent {
            return TextMessageContent(text)
        }

    }


    @Data
    class TextMessageContent(val text: String) : MessageContent

}
