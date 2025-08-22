package com.example.lynx.viewmodel

import com.example.lynx.model.User
import com.example.lynx.repository.UserRepository
import junit.framework.TestCase.assertNotNull
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class AuthorizationViewModelTest {

    private lateinit var viewModel: AuthorizationViewModel

    @Before
    fun setUp() {
        UserRepository.clearUsers()
        viewModel = AuthorizationViewModel()
    }

    @Test
    fun givenUniqueId_whenValidateRegistration_thenUserIsAdded() {
        val id = "newuser"
        val nickname = "Nick"
        val email = "mail@example.com"
        val password = "pass123"

        val result = viewModel.validateRegistration(id, nickname, email, password)

        assertNotNull(result)
        assertEquals("@newuser", result?.id)
        assertEquals(1, UserRepository.users.size)
    }

    @Test
    fun givenExistingId_whenValidateRegistration_thenReturnsNull() {
        UserRepository.addNewUser(
            User(
                uuid = "1",
                id = "@testuser",
                userName = "Tester",
                email = "mail@example.com",
                password = "pass123",
                bio = ""
            )
        )

        val result = viewModel.validateRegistration(
            id = "testuser",
            nickname = "Another",
            email = "other@mail.com",
            password = "newPass"
        )

        assertNull(result)
        assertEquals(1, UserRepository.users.size)
    }

    @Test
    fun givenValidCredentials_whenValidateLogin_thenReturnsUser() {
        val user = User(
            uuid = "1",
            id = "@jack",
            userName = "Jack",
            email = "jack@mail.com",
            password = "qwerty123",
            bio = ""
        )
        UserRepository.addNewUser(user)

        val result = viewModel.validateLogin("jack", "qwerty123")

        assertNotNull(result)
        assertEquals(user, result)
    }

    @Test
    fun givenWrongPassword_whenValidateLogin_thenReturnsNull() {
        val user = User(
            uuid = "1",
            id = "@jack",
            userName = "Jack",
            email = "jack@mail.com",
            password = "qwerty123",
            bio = ""
        )
        UserRepository.addNewUser(user)

        val result = viewModel.validateLogin("jack", "wrongPass")

        assertNull(result)
    }

    @Test
    fun givenNonExistentUser_whenValidateLogin_thenReturnsNull() {
        val nonExistentId = "noUser"
        val password = "pass"

        val result = viewModel.validateLogin(nonExistentId, password)

        assertNull(result)
    }
}