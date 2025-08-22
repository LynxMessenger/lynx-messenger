package com.example.lynx.viewmodel

import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.lynx.model.User
import com.example.lynx.repository.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class UserViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: UserViewModel
    private lateinit var user: User

    @Before
    fun setUp() {
        viewModel = UserViewModel()
        UserRepository.clearUsers()

        user = User(
            uuid = "123",
            userName = "Daniel",
            id = "@dan",
            email = "dan@mail.com",
            password = "123",
            bio = "",
            avatarUri = null
        )
        UserRepository.addNewUser(user)
    }

    @Test
    fun givenUuid_whenGetUserByUuid_thenReturnUser() {
        val result = viewModel.GetUserByUuid("123")
        assertNotNull(result)
        assertEquals("Daniel", result?.userName)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun givenUuid_whenGetUserFlowByUuid_thenReturnUser() {
        runTest {
            val result = viewModel.getUserFlowByUuid("123").first()
            assertNotNull(result)
            assertEquals("123", result?.uuid)
        }
    }

    @Test
    fun givenEditProfile_whenApplyUserEdit_thenUserUpdated() {
        viewModel.selectedAvatarUri = Uri.parse("content://avatar/test")
        viewModel.ApplyUserEdit(
            nickname = "NewName",
            bio = "New bio",
            currentUserId = "123"
        )

        val updatedUser = UserRepository.getUserByUuid("123")
        assertEquals("NewName", updatedUser?.userName)
        assertEquals("New bio", updatedUser?.bio)
        assertEquals("content://avatar/test", updatedUser?.avatarUri)
        assertNull(viewModel.selectedAvatarUri)
    }

    @Test
    fun givenEditAccount_whenApplyUserAccountEdit_thenUserUpdated() {
        viewModel.ApplyUserAccountEdit(
            password = "newPass",
            id = "newid",
            email = "new@mail.com",
            currentUserId = "123"
        )

        val updatedUser = UserRepository.getUserByUuid("123")
        assertEquals("newPass", updatedUser?.password)
        assertEquals("@newid", updatedUser?.id)
        assertEquals("new@mail.com", updatedUser?.email)
    }
}