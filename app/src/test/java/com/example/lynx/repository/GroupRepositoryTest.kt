package com.example.lynx.repository


import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class GroupRepositoryTest {

    @Before
    fun setup() {
        GroupRepository.groups.clear()
    }

    @Test
    fun createGroup_addsGroupWithAtPrefixIfMissing() {
        val group = GroupRepository.createGroup(
            name = "Test Group",
            ownerId = "owner1",
            memberIds = listOf("user1"),
            id = "group1",
            avatarURI = null
        )

        assertTrue(group.id.startsWith("@"))
        assertEquals(1, GroupRepository.groups.size)
        assertEquals(group, GroupRepository.groups.first())
    }

    @Test
    fun createGroup_preservesAtPrefixIfExists() {
        val group = GroupRepository.createGroup(
            name = "Test Group",
            ownerId = "owner1",
            memberIds = listOf("user1"),
            id = "@group1",
            avatarURI = null
        )

        assertEquals("@group1", group.id)
    }

    @Test
    fun getGroupById_returnsCorrectGroup() {
        val group = GroupRepository.createGroup("Test", "owner1", listOf(), "group1", null)
        val found = GroupRepository.getGroupById(group.id)

        assertEquals(group, found)
    }

    @Test
    fun getGroupsByName_returnsMatchingGroups() {
        val g1 = GroupRepository.createGroup("Chat", "owner", listOf(), "g1", null)
        val g2 = GroupRepository.createGroup("Chatter", "owner", listOf(), "g2", null)
        val results = GroupRepository.getGroupsByName("chat")

        assertTrue(results.contains(g1))
        assertTrue(results.contains(g2))
    }

    @Test
    fun addUser_addsUserIfNotExists() {
        val group = GroupRepository.createGroup("Test", "owner", mutableListOf(), "g1", null)
        GroupRepository.addUser(group, "userX")

        assertTrue(group.memberIds.contains("userX"))
    }

    @Test
    fun addUser_doesNotAddDuplicate() {
        val group = GroupRepository.createGroup("Test", "owner", mutableListOf("userX"), "g1", null)
        GroupRepository.addUser(group, "userX")

        assertEquals(1, group.memberIds.count { it == "userX" })
    }

    @Test
    fun removeUserByGroup_removesUser() {
        val group = GroupRepository.createGroup("Test", "owner", mutableListOf("userX"), "g1", null)
        GroupRepository.removeUserByGroup("userX", group)

        assertFalse(group.memberIds.contains("userX"))
    }
}