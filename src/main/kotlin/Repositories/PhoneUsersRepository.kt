package Repositories

class PhoneUsersRepository {
    private val phoneUsers = mutableMapOf<String, HashSet<String>>()

    fun addUserToPhone(phone: String, userId: String) {
        if(phoneUsers.containsKey(phone)) {
            phoneUsers[phone]?.add(userId)
        } else {
            phoneUsers[phone] = hashSetOf(userId)
        }
    }

    fun deleteUserFromPhone(phone: String, userId: String){
        if(phoneUsers.containsKey(phone)) {
            phoneUsers[phone]?.remove(userId)
        }
    }

    fun getPhoneUsersCount(phone: String): Long {
        val users = phoneUsers[phone] ?: emptySet()
        return users.size.toLong()
    }
}