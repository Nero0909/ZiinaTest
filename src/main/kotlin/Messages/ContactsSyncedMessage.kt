data class ContactsSyncedMessage(
    val userId: String,
    val prevContacts: Set<String>,
    val newContacts: Set<String>
)
