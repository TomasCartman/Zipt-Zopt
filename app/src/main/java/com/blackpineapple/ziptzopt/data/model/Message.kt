package com.blackpineapple.ziptzopt.data.model

data class Message(
        var messageId: String? = null,
        var sender: String? = null,
        var timestamp: Long? = null,
        var hasSeen: Boolean? = null,
        var messageText: String? = null
)