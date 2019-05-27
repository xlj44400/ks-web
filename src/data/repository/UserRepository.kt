package data.repository

import data.entity.SubscriptionEntity
import data.entity.UserEntity
import data.repository.datasource.UserDataStore
import domain.Subscription
import domain.User

data class UserQuery(val username: String? = null, val email: String? = null, val isSubscribed: Boolean? = null)

class UserRepository(private val userDataStore: UserDataStore) {
    fun findById(id: String): User? = userDataStore.findById(id)?.let {
        val subscription = it.subscription?.let { s ->
            Subscription(s.id, s.address)
        }

        User(it.id, it.username, it.email, it.password, subscription, it.isAuthenticated)
    }

    fun findOne(query: UserQuery): User? = userDataStore.findOne(query)?.let {
        val subscription = it.subscription?.let { s ->
            Subscription(s.id, s.address)
        }

        User(it.id, it.username, it.email, it.password, subscription, it.isAuthenticated)
    }

    fun create(user: User) {
        val userEntity = UserEntity(
                user.id,
                user.username,
                user.email,
                user.password,
                user.subscription?.let {
                    SubscriptionEntity(it.id, it.address)
                },
                user.isAuthenticated)

        userDataStore.create(userEntity)
    }

    fun update(user: User) {
        val userEntity = UserEntity(
                user.id,
                user.username,
                user.email,
                user.password,
                user.subscription?.let {
                    SubscriptionEntity(it.id, it.address)
                },
                user.isAuthenticated)

        userDataStore.update(userEntity)
    }
}
