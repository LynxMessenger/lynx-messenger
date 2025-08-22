package com.example.lynx.viewmodel

import com.example.lynx.model.User
import com.example.lynx.repository.UserRepository
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class BlockViewModelTest {

    private lateinit var currentUser: User
    private lateinit var viewModel: BlockViewModel

    @Before
    fun setUp() {
        currentUser = User(
            uuid = "1",
            id = "@test",
            userName = "test",
            email = "test@mail.com",
            password = "qwerty123",
            bio = ""
        )
        currentUser.blockedUsers.clear()
        viewModel = BlockViewModel()
    }

    @Test
    fun givenUserNotBlocked_whenBlockUser_thenUserIsBlocked() {
        val targetUser = User(
            uuid = "2",
            id = "@target",
            userName = "targetUser",
            email = "target@mail.com",
            password = "123",
            bio = ""
        )
        assertFalse(currentUser.blockedUsers.contains(targetUser.uuid))

        viewModel.blockUser(targetUser.uuid, currentUser)

        assertTrue(currentUser.blockedUsers.contains(targetUser.uuid))
    }

    @Test
    fun givenAlreadyBlockedUser_whenBlockUser_thenNoDuplicate() {
        val targetUser = User(
            uuid = "2",
            id = "@target",
            userName = "targetUser",
            email = "target@mail.com",
            password = "123",
            bio = ""
        )

        assertFalse(currentUser.blockedUsers.contains(targetUser.uuid))

        viewModel.blockUser(targetUser.uuid, currentUser)
        assertTrue(currentUser.blockedUsers.contains(targetUser.uuid))
        assertEquals(1, currentUser.blockedUsers.size)

        viewModel.blockUser(targetUser.uuid, currentUser)
        assertEquals(1, currentUser.blockedUsers.size)
    }

    @Test
    fun givenAlreadyBlockedUser_whenUnblockUser_thenUserIsUnblocked() {
        val targetUser = User(
            uuid = "2",
            id = "@target",
            userName = "targetUser",
            email = "target@mail.com",
            password = "123",
            bio = ""
        )
        viewModel.blockUser(targetUser.uuid, currentUser)
        assertTrue(currentUser.blockedUsers.contains(targetUser.uuid))

        viewModel.unblockUser(targetUser.uuid, currentUser)
        assertFalse(currentUser.blockedUsers.contains(targetUser.uuid))
    }

    @Test
    fun givenUserNotBlocked_whenUnblockUser_thenNoChange() {
        val targetUser = User(
            uuid = "2",
            id = "@target",
            userName = "targetUser",
            email = "target@mail.com",
            password = "123",
            bio = ""
        )

        viewModel.unblockUser(targetUser.uuid, currentUser)

        assertFalse(currentUser.blockedUsers.contains(targetUser.uuid))
        assertEquals(0, currentUser.blockedUsers.size)
    }

    @Test
    fun givenBlockedUser_whenCheckIfUserBlocked_thenReturnTrue() {
        val targetUser = User(
            uuid = "2",
            id = "@target",
            userName = "targetUser",
            email = "target@mail.com",
            password = "123",
            bio = ""
        )
        currentUser.blockedUsers.add(targetUser.uuid)

        val result = viewModel.isUserBlocked(targetUser.uuid, currentUser)

        assertTrue(result)
    }

    @Test
    fun givenUnblockedUser_whenCheckIfUserBlocked_thenReturnFalse() {
        val targetUser = User(
            uuid = "2",
            id = "@target",
            userName = "targetUser",
            email = "target@mail.com",
            password = "123",
            bio = ""
        )
        assertTrue(currentUser.blockedUsers.isEmpty())

        val result = viewModel.isUserBlocked(targetUser.uuid, currentUser)

        assertFalse(result)
    }
}