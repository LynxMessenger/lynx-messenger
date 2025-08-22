package com.example.lynx.model

data class Chat(
    var id: String,
    var userId: List<String>,
    val lastMessage: Message?,
    var messages: MutableList<Message>,
    var user: User
)