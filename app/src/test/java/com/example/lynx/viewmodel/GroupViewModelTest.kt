package com.example.lynx.viewmodel

import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import com.example.lynx.model.Group
import com.example.lynx.model.Message
import com.example.lynx.model.User
import com.example.lynx.repository.GroupRepository
import com.example.lynx.repository.MessagesRepository
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull


@RunWith(RobolectricTestRunner::class)
class GroupViewModelTest {

    private lateinit var viewModel: GroupViewModel
    private lateinit var currentUser: User
    private lateinit var testGroup: Group

    @Before
    fun setUp() {
        viewModel = GroupViewModel()

        currentUser = User(
            uuid = "u1",
            id = "@user",
            userName = "User",
            email = "user@mail.com",
            password = "123",
            bio = ""
        )

        testGroup = Group(
            uuid = "g1",
            name = "TestGroup",
            id = "@test",
            ownerId = currentUser.uuid,
            memberIds = mutableStateListOf<String>().apply {
                addAll(listOf("user1", "user2"))
            },
            avatarUri = null,
            messages = mutableListOf()
        )

        GroupRepository.clear()
        MessagesRepository.clear()
    }

    @Test
    fun givenNonEmptyState_whenReset_thenClearsAllFields() {
        viewModel.selectedAvatarUri = Uri.parse("test://avatar")
        viewModel.groupName.value = "Name"
        viewModel.groupId.value = "Id"
        viewModel.selectedMembers.add("m1")

        viewModel.reset()

        assertNull(viewModel.selectedAvatarUri)
        assertEquals("", viewModel.groupName.value)
        assertEquals("", viewModel.groupId.value)
        assertTrue(viewModel.selectedMembers.isEmpty())
    }

    @Test
    fun givenSelectedAvatarAndMembers_whenCreateNewGroup_thenGroupIsCreatedAndStateIsReset() {
        viewModel.selectedAvatarUri = Uri.parse("test://avatar")
        viewModel.selectedMembers.addAll(listOf("m1", "m2"))

        viewModel.createNewGroup(
            currentUserId = currentUser.uuid,
            groupName = "GroupName",
            groupId = "@grp"
        )

        val createdGroup = GroupRepository.getGroupsForUserUuid(currentUser.uuid).firstOrNull()
        assertNotNull(createdGroup)
        assertEquals("GroupName", createdGroup.name)
        assertEquals("@grp", createdGroup.id)
        assertEquals("test://avatar", createdGroup.avatarUri)
        assertTrue(viewModel.selectedMembers.isEmpty())
    }

    @Test
    fun givenMemberAlreadyInNewGroup_whenAddMember_thenDoNotDuplicate() {
        viewModel.selectedMembers.add("m1")

        viewModel.addMemberInNewGroup("m1")
        assertEquals(1, viewModel.selectedMembers.size)

        viewModel.addMemberInNewGroup("m2")
        assertEquals(2, viewModel.selectedMembers.size)
    }

    @Test
    fun givenGroupWithMessages_whenGetGroups_thenReturnGroupsWithMessages() {
        val createdGroup = GroupRepository.createGroup(
            name = "Test",
            ownerId = currentUser.uuid,
            id = "@t",
            memberIds = mutableStateListOf(),
            avatarURI = null
        )

        MessagesRepository.addMessage(
            Message(
                senderId = currentUser.uuid,
                groupUuid = createdGroup.uuid,
                text = "Hi"
            )
        )

        val result = viewModel.getGroups(currentUser)

        assertEquals(1, result.size)
        assertEquals(1, result.first().messages.size)
    }

    @Test
    fun givenExistingGroup_whenGetGroupById_thenReturnCorrectGroup() {
        GroupRepository.createGroup(
            "Test",
            currentUser.uuid,
            id = "@t",
            memberIds =  mutableStateListOf(),
            avatarURI = null
        )
        val created = GroupRepository.getGroupsForUserUuid(currentUser.uuid).first()

        val found = viewModel.getGroupById(created.id)

        assertEquals(created.uuid, found?.uuid)
    }

    @Test
    fun givenExistingGroup_whenGetGroupByUUID_thenReturnCorrectGroup() {
        GroupRepository.createGroup(
            "Test",
            currentUser.uuid,
            id = "@t",
           memberIds =  mutableStateListOf(),
           avatarURI =  null
        )
        val created = GroupRepository.getGroupsForUserUuid(currentUser.uuid).first()

        val found = viewModel.getGroupByUUID(created.uuid)

        assertEquals(created.id, found?.id)
    }

    @Test
    fun givenExistingGroup_whenApplyGroupEdit_thenUpdateNameIdAndAvatar() {
        GroupRepository.createGroup(
            "OldName",
            currentUser.uuid,
           id = "@old",
           memberIds =  mutableStateListOf(),
           avatarURI =  null
        )
        val created = GroupRepository.getGroupsForUserUuid(currentUser.uuid).first()

        viewModel.selectedAvatarUri = Uri.parse("test://avatar")
        viewModel.ApplyGroupEdit("NewName", "@new", created.uuid)

        val updated = GroupRepository.getGroupByUUID(created.uuid)
        assertEquals("NewName", updated?.name)
        assertEquals("@new", updated?.id)
        assertEquals("test://avatar", updated?.avatarUri)
    }

    @Test
    fun givenGroupWithoutMember_whenAddMemberByGroup_thenMemberIsAdded() {
        GroupRepository.createGroup(
            "Test",
            ownerId = currentUser.uuid,
            id = "@t",
            memberIds = mutableStateListOf(),
            avatarURI = null
        )
        val created = GroupRepository.getGroupsForUserUuid(currentUser.uuid).first()

        viewModel.addMemberByGroup("u2", created)

        val updated = GroupRepository.getGroupByUUID(created.uuid)
        assertTrue(updated!!.memberIds.contains("u2"))
    }

    @Test
    fun givenGroupAndNonOwner_whenRemoveMemberByGroup_thenMemberIsRemoved() {
        GroupRepository.createGroup(
            "Test",
            currentUser.uuid,
            id =  "@t",
           memberIds =  mutableStateListOf("u1", "u2"),
           avatarURI =  null
        )
        val created = GroupRepository.getGroupsForUserUuid(currentUser.uuid).first()

        viewModel.removeMemberByGroup("u2", "u1", created)

        val updated = GroupRepository.getGroupByUUID(created.uuid)
        assertFalse(updated!!.memberIds.contains("u2"))
    }

    @Test
    fun givenGroup_whenExitGroup_thenUserIsRemovedFromMembers() {
        GroupRepository.createGroup(
            "Test",
            currentUser.uuid,
           id =  "@t",
           memberIds =  mutableStateListOf("u1", "u2"),
           avatarURI =  null
        )
        val created = GroupRepository.getGroupsForUserUuid(currentUser.uuid).first()

        viewModel.exitGroup("u2", created)

        val updated = GroupRepository.getGroupByUUID(created.uuid)
        assertFalse(updated!!.memberIds.contains("u2"))
    }

    @Test
    fun givenGroup_whenCheckOwner_thenReturnTrueIfOwnerElseFalse() {
        assertTrue(viewModel.isOwner(testGroup, currentUser.uuid))
        assertFalse(viewModel.isOwner(testGroup, "u2"))
    }
}
