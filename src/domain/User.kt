package domain

import kotlin.random.Random

class User {
    constructor(
            username: String? = null,
            email: String? = null,
            password: String? = null) {
        this.id = "$username${Random.nextInt(9999)}"
        this.username = username
        this.email = email
        this.password = password
    }

    constructor(
            id: String,
            username: String,
            email: String,
            password: String?) : this(username, email, password) {
        this.id = id
    }

    fun subscribe(isSubscribed: Boolean = true) = apply {
        this.isSubscribed = isSubscribed
    }

    fun authenticate(isAuthenticated: Boolean = true) = apply {
        this.isAuthenticated = isAuthenticated
    }

    var id: String = ""
        private set

    val username: String?
    val email: String?
    val password: String?

    var isSubscribed: Boolean = false
        private set

    var isAuthenticated: Boolean = false
        private set
}
