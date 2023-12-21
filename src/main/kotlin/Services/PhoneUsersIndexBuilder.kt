import Repositories.PhoneUsersRepository
import Services.SimpleMessageQueue

class PhoneUsersIndexBuilder(
    private val simpleMessageQueue: SimpleMessageQueue,
    private val repository: PhoneUsersRepository
) {
    fun run() {
        while (!Thread.currentThread().isInterrupted) {
            val message = simpleMessageQueue.getContactsSyncedMessage()
            val userId = message.userId
            val addedPhones = message.newContacts - message.prevContacts
            val deletedPhones = message.prevContacts - message.newContacts

            for (addedPhone in addedPhones) {
                repository.addUserToPhone(addedPhone, userId)
            }

            for (deletedPhone in deletedPhones) {
                repository.deleteUserFromPhone(deletedPhone, userId)
            }
        }
    }
}