package data.repository

import data.entity.UserEntity
import data.repository.datasource.UserDataStore
import domain.User

data class UserQuery(val username: String? = null, val email: String? = null, val isSubscribed: Boolean? = null)

class UserRepository(private val userDataStore: UserDataStore) {
    fun findById(id: String): User? = userDataStore.findById(id)?.let {
        val user = User(it.id, it.username, it.email, it.password)

        user.subscribe(it.isSubscribed)
        user.authenticate(it.isAuthenticated)

        user
    }

    fun findOne(query: UserQuery): User? = userDataStore.findOne(query)?.let {
        val user = User(it.id, it.username, it.email, it.password)

        user.subscribe(it.isSubscribed)
        user.authenticate(it.isAuthenticated)

        user
    }

    fun create(user: User) {
        val userEntity = UserEntity(
                user.id,
                user.username!!,
                user.email!!,
                user.password,
                user.isSubscribed,
                user.isAuthenticated)

        userDataStore.create(userEntity)
    }

    fun update(user: User) {
        val userEntity = UserEntity(
                user.id,
                user.username!!,
                user.email!!,
                user.password,
                user.isSubscribed,
                user.isAuthenticated)

        userDataStore.update(userEntity)
    }
}
