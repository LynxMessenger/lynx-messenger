package com.example.lynx.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.lynx.model.User
import com.example.lynx.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UserViewModel : ViewModel(){
    fun GetUserByUuid(uuid: String): User?{
        return UserRepository.getUserByUuid(uuid)
    }
    fun getUserFlowByUuid(uuid: String): Flow<User?> = flow {
        emit(UserRepository.getUserByUuid(uuid))
    }
    var selectedAvatarUri: Uri? = null
    fun ApplyUserEdit(nickname: String, bio: String, currentUserId: String) {
        val user = UserRepository.getUserByUuid(currentUserId)
        user?.userName = nickname
        user?.bio = bio
        user?.avatarUri = selectedAvatarUri.toString()
        selectedAvatarUri = null
    }
    fun ApplyUserAccountEdit(password: String, id: String, email: String, currentUserId: String){
        val user = UserRepository.getUserByUuid(currentUserId)
        val normalizedId = if (id.startsWith("@")) id else "@$id"
        user?.id = normalizedId
        user?.password = password
        user?.email = email
    }
}