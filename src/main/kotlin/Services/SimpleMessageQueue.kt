package Services

import ContactsSyncedMessage
import User
import java.util.concurrent.LinkedBlockingDeque

class SimpleMessageQueue {
    private val queue = LinkedBlockingDeque<ContactsSyncedMessage>()

    fun sendContactsSyncedMessage(userId: String, prevContacts: Set<String>, newContacts: Set<String>): Boolean {
        return queue.add(ContactsSyncedMessage(userId, prevContacts, newContacts))
    }

    fun getContactsSyncedMessage(): ContactsSyncedMessage {
        return queue.take()
    }
}