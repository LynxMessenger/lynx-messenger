package com.example.lynx.viewmodel

import androidx.lifecycle.ViewModel
import com.example.lynx.model.User
import com.example.lynx.repository.UserRepository
import java.util.UUID

class AuthorizationViewModel : ViewModel() {

    fun validateLogin(id: String, password: String): User? {
        val normalizedId = if (id.startsWith("@")) id else "@$id"
        val lowerId = normalizedId.lowercase()

        val user = UserRepository.getUserById(lowerId)
        return if (user?.password == password) user else null
    }

    fun validateRegistration(
        id: String,
        nickname: String,
        email: String,
        password: String
    ) : User?{
        val normalizedId = if (id.startsWith("@")) id else "@$id"
        val lowerId = normalizedId.lowercase()

        val userExists = UserRepository.users.any { it.id == lowerId }

        if(userExists){
            return null
        }
        else {
            val user = User(
                uuid = UUID.randomUUID().toString(),
                id = lowerId,
                userName = nickname,
                email = email,
                password = password,
                bio = "hello! it's $nickname!",
            )

            UserRepository.addNewUser(user)

            return user
        }
    }
}