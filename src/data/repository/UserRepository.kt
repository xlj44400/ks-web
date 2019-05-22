package data.repository

import data.repository.datasource.UserDataStore
import domain.User

class UserRepository(private val userDataStore: UserDataStore) {
    fun findById(id: String): User? = userDataStore.findById(id)

    fun findByUsername(username: String, hasTelegram: Boolean = false): User? = userDataStore.findByUsername(username, hasTelegram)

    fun findByEmail(email: String): User? = userDataStore.findByEmail(email)

    fun create(user: User) = userDataStore.create(user)

    fun update(user: User) = userDataStore.update(user)
}
