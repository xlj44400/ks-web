package domain

import kotlin.random.Random

class User {
    constructor(
            username: String,
            email: String,
            password: String) {
        this.id = "$username${Random.nextInt(9999)}"
        this.username = username
        this.email = email
        this.password = password
    }

    constructor(
            id: String,
            username: String,
            email: String,
            password: String,
            subscription: Subscription? = null,
            isAuthenticated: Boolean = false) : this(username, email, password) {
        this.id = id
        this.subscription = subscription
        this.isAuthenticated = isAuthenticated
    }

    var id: String = ""
        private set

    val username: String
    val email: String
    val password: String

    var subscription: Subscription? = null
        private set

    var isSubscribed: Boolean = false
        get() = subscription != null
        private set

    var isAuthenticated: Boolean = false
        private set

    fun authenticate() {
        this.isAuthenticated = true
    }

    fun deauthenticate() {
        this.isAuthenticated = false
    }

    fun subscribe(subscription: Subscription) {
        this.subscription = subscription
    }

    fun unsubscribe(subscription: Subscription) {
        this.subscription = this.subscription?.let {
            if (it.id == subscription.id) null else it
        }
    }
}
