package com.example.lynx.repository

import com.example.lynx.R
import com.example.lynx.model.User
import java.util.UUID

object UserRepository {
    val users = mutableListOf<User>()
    var firstUsers = mutableListOf<User>(
        User( uuid = "7f9e5c02-2f3a-4d1a-8c09-c1b7e2a67a12", id =  "@jack",userName =  "Jack",bio ="Hello its Jack!", email = "jack@email.com",password = "qwerty123"),
        User(uuid = "f3aeb1cd-4596-4a7a-bf34-811cfab7d4b7", id =  "@daniil",userName = "Daniil",bio ="Hello its Daniil!", email = "daniil@email.com", password = "qwerty123"),
        User(uuid = "f8d2a5f3-77c6-4bdb-9d7a-9843a6e3c4c5", id =  "@dasha",userName = "Dasha",bio ="Hello its Dasha!" ,email = "dasha@email.com" , password = "qwerty123"),
        User(uuid = "fae78a9d-8d6c-4fba-bd6e-55d41d23f87a", id =  "@evan",userName = "Evan",bio ="Hello its Evan!" ,email = "evan@email.com" , password = "qwerty123"),
        User(uuid = "9bb46e2c-2f17-40ed-9c8a-bce1b7a7a3a9", id =  "@lynx",userName = "Lynx", bio = "Hello its Lynx!",email = "Lynx@email.com" , password = "qwerty123")
    )
    fun addNewUser(user: User) {
        users.removeAll { it.uuid == user.uuid }
        users.add(user)
    }
    fun getAllUsers(): List<User> {
        return users
    }
    fun getUserByName(userName: String): User?{
        return users.find { it.userName == userName }
    }
    fun getUserById(id: String): User?{
        return users.find { it.id == id }
    }
    fun getUserByUuid(uuid:String): User?{
        return users.find { it.uuid == uuid }
    }
    fun getUsersByName(userName: String): List<User>{
        return users.filter { it.userName.contains(userName, ignoreCase = true)}
    }
    fun getUsersById(id: String): List<User>{
        return  users.filter { it.id.contains(id, ignoreCase = true) }
    }
    fun clearUsers() {
        users.clear()
    }
}