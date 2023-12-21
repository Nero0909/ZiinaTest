class UsersService(private val ziinaUserPhones: List<String>) {
    fun isZiinaUser(phoneNumber: String): Boolean {
        return ziinaUserPhones.contains(phoneNumber)
    }
}