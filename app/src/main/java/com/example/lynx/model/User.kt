package com.example.lynx.model

import com.example.lynx.R

data class User(
    val uuid: String,
    var id: String,
    var userName: String,
    var bio: String,
    var email: String,
    var password: String,
    var avatarUri: String? = null,
    var blockedUsers: MutableList<String> = mutableListOf()
)