package org.example

import PhoneUsersIndexBuilder
import Repositories.PhoneUsersRepository
import Services.ContactsService
import Services.SimpleMessageQueue
import User
import UsersService
import java.util.concurrent.Executors

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    val ziinaUserPhones = listOf("1", "2", "3")
    val phoneUsersRepository = PhoneUsersRepository()
    val usersService = UsersService(ziinaUserPhones)
    val messageQueue = SimpleMessageQueue()
    val indexBuilder = PhoneUsersIndexBuilder(messageQueue, phoneUsersRepository)
    val contactsService = ContactsService(messageQueue, usersService, phoneUsersRepository)

    val executor = Executors.newCachedThreadPool()
    executor.submit { indexBuilder.run() }

    val users = listOf(
        User("1", setOf("3", "4", "5")),
        User("2", setOf("4", "5")),
        User("3", setOf("5", "6"))
    )

    for (user in users) {
        contactsService.syncContacts(user)
    }
    Thread.sleep(1000)

    val prospectedUsersV2 = contactsService.getProspectiveUsersV2(users[0].id)
    val prospectedUsersV1 = contactsService.getProspectiveUsers(users[0].id)
    assert(prospectedUsersV1 == prospectedUsersV2)
    assert(prospectedUsersV1.size == 2)
    assert(prospectedUsersV1[0].phoneNumber == "4")
    assert(prospectedUsersV1[0].friendsOnZiina == 1L)
    assert(prospectedUsersV1[1].phoneNumber == "5")
    assert(prospectedUsersV1[1].friendsOnZiina == 2L)

    val changedUser = User("2", setOf("4"))
    contactsService.syncContacts(changedUser)
    Thread.sleep(1000)

    val prospectedUsersV2_2 = contactsService.getProspectiveUsersV2(users[0].id)
    val prospectedUsersV1_2 = contactsService.getProspectiveUsers(users[0].id)
    assert(prospectedUsersV1_2 == prospectedUsersV2_2)
    assert(prospectedUsersV1_2.size == 2)
    assert(prospectedUsersV1_2[0].phoneNumber == "4")
    assert(prospectedUsersV1_2[0].friendsOnZiina == 1L)
    assert(prospectedUsersV1_2[1].phoneNumber == "5")
    assert(prospectedUsersV1_2[1].friendsOnZiina == 1L)

    executor.shutdownNow()
}
