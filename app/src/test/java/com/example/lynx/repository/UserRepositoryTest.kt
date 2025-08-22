package com.example.lynx.repository

import com.example.lynx.model.User
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class UserRepositoryTest {

    @Before
    fun setup() {
        UserRepository.users.clear()
    }

    @Test
    fun addNewUser_shouldAddUser() {
        val user = User("uuid1", "@test", "TestUser", "Bio", "test@email.com", "pass")
        UserRepository.addNewUser(user)

        assertEquals(1, UserRepository.getAllUsers().size)
        assertTrue(UserRepository.getAllUsers().contains(user))
    }

    @Test
    fun getUserByName_shouldReturnCorrectUser() {
        val user = User("uuid1", "@test", "TestUser", "Bio", "test@email.com", "pass")
        UserRepository.addNewUser(user)

        val found = UserRepository.getUserByName("TestUser")
        assertEquals(user, found)
    }

    @Test
    fun getUserByName_shouldReturnNullIfNotFound() {
        assertNull(UserRepository.getUserByName("NoName"))
    }

    @Test
    fun getUserById_shouldReturnCorrectUser() {
        val user = User("uuid1", "@test", "TestUser", "Bio", "test@email.com", "pass")
        UserRepository.addNewUser(user)

        val found = UserRepository.getUserById("@test")
        assertEquals(user, found)
    }

    @Test
    fun getUserByUuid_shouldReturnCorrectUser() {
        val user = User("uuid1", "@test", "TestUser", "Bio", "test@email.com", "pass")
        UserRepository.addNewUser(user)

        val found = UserRepository.getUserByUuid("uuid1")
        assertEquals(user, found)
    }

    @Test
    fun getUsersByName_shouldReturnUsersIgnoringCase() {
        val user1 = User("uuid1", "@test1", "Alice", "", "", "")
        val user2 = User("uuid2", "@test2", "alice_wonder", "", "", "")
        UserRepository.addNewUser(user1)
        UserRepository.addNewUser(user2)

        val found = UserRepository.getUsersByName("alice")
        assertTrue(found.containsAll(listOf(user1, user2)))
    }

    @Test
    fun getUsersByName_shouldReturnEmptyListIfNoMatches() {
        assertTrue(UserRepository.getUsersByName("nope").isEmpty())
    }

    @Test
    fun getUsersById_shouldReturnUsersBySubstringIgnoringCase() {
        val user1 = User("uuid1", "@jack", "Jack", "", "", "")
        val user2 = User("uuid2", "@jackson", "Jackson", "", "", "")
        UserRepository.addNewUser(user1)
        UserRepository.addNewUser(user2)

        val found = UserRepository.getUsersById("jack")
        assertTrue(found.containsAll(listOf(user1, user2)))
    }

    @Test
    fun getUsersById_shouldReturnEmptyListIfNoMatches() {
        assertTrue(UserRepository.getUsersById("zzz").isEmpty())
    }
}
