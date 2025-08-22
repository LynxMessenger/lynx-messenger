package com.example.lynx.model

sealed class SearchResult {
    data class UserResult(val user: User) : SearchResult()
    data class GroupResult(val group: Group) : SearchResult()
    data class ChannelResult(val channel: Channel) : SearchResult()
}