package domain

import kotlin.random.Random

class User {
    companion object {
        fun create(username: String, email: String, password: String): User {
            return User().apply {
                this.id = "$username$${Random.nextInt(0, 9999)}"
                this.username = username
                this.email = email
                this.password = password
            }
        }
    }

    var id = ""
    var username = ""
    var email = ""
    var password = ""

    var isAuthenticated = false
    var hasTelegram = false
}
