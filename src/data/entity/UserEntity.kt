package data.entity

data class UserEntity(
        val id: String,
        val username: String,
        val email: String,
        val password: String?,
        var isSubscribed: Boolean = false,
        var isAuthenticated: Boolean = false)
