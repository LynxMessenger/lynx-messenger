package com.example.lynx.viewmodel

import androidx.lifecycle.ViewModel
import com.example.lynx.model.User
import com.example.lynx.repository.UserRepository

class BlockViewModel : ViewModel() {
    fun blockUser(targetUserId: String, currentUser: User) {
        currentUser.let {
            if (!it.blockedUsers.contains(targetUserId)) {
                it.blockedUsers.add(targetUserId)
            }
        }
    }

    fun unblockUser(targetUserId: String, currentUser: User) {
        currentUser.blockedUsers.remove(targetUserId)
    }

    fun isUserBlocked(targetUserId: String, currentUser: User): Boolean {
        return currentUser.blockedUsers.contains(targetUserId) == true
    }
}