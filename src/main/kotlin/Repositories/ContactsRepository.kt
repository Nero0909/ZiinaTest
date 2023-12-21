package Repositories

import User

class ContactsRepository {
    private val userContacts = mutableMapOf<String, Set<String>>()

    fun replace(id: String, phoneNumbers: Set<String>): Set<String>? {
        val prevContacts = userContacts[id]
        userContacts[id] = phoneNumbers
        return prevContacts
    }

    fun getById(id: String): User? {
        val contacts = userContacts[id] ?: return null
        return User(id, contacts)
    }

    fun getAll(): List<User> {
        return userContacts.map { User(it.key, it.value) }
    }
}

