package Services

import ProspectiveUser
import Repositories.ContactsRepository
import Repositories.PhoneUsersRepository
import User
import UsersService

class ContactsService(
    private val messageQueue: SimpleMessageQueue,
    private val usersService: UsersService,
    private val phoneUsersRepository: PhoneUsersRepository
) {
    private val contactsRepository = ContactsRepository()

    fun syncContacts(user: User) {
        val newContacts = user.contacts
        val prevContacts = contactsRepository.replace(user.id, newContacts) ?: emptySet()
        messageQueue.sendContactsSyncedMessage(user.id, prevContacts, newContacts)
    }

    fun getProspectiveUsers(userId: String): List<ProspectiveUser> {
        val currentUser = contactsRepository.getById(userId) ?: return emptyList()

        val allUsers = contactsRepository.getAll()
        val prospectiveUsers = mutableListOf<ProspectiveUser>()
        for (phoneNumber in currentUser.contacts) {
            if (!usersService.isZiinaUser(phoneNumber)) {
                val friendsOnZiina = allUsers.filter { it.id != currentUser.id && it.contacts.contains(phoneNumber) }
                prospectiveUsers.add(ProspectiveUser(phoneNumber, friendsOnZiina.size.toLong()))
            }
        }

        return prospectiveUsers
    }

    fun getProspectiveUsersV2(userId: String): List<ProspectiveUser> {
        val currentUser = contactsRepository.getById(userId) ?: return emptyList()

        val prospectiveUsers = mutableListOf<ProspectiveUser>()
        for (phoneNumber in currentUser.contacts) {
            if (!usersService.isZiinaUser(phoneNumber)) {
                val phoneZiinaUsersCount = phoneUsersRepository.getPhoneUsersCount(phoneNumber)
                val ziinaFriends = if (phoneZiinaUsersCount > 0) phoneZiinaUsersCount - 1 else 0
                prospectiveUsers.add(ProspectiveUser(phoneNumber, ziinaFriends))
            }
        }

        return prospectiveUsers
    }
}
