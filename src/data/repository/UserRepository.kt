package data.repository

import data.datasource.UserDataStore
import domain.model.User

class UserRepository(private val userDataStore: UserDataStore) {
    fun findById(id: String): User? {
        return userDataStore.findById(id)
    }

    fun findByUsername(username: String, hasTelegram: Boolean = false): User? {
        return userDataStore.findByUsername(username, hasTelegram)
    }

    fun findByEmail(email: String): User? {
        return userDataStore.findByEmail(email)
    }

    fun add(user: User) {
        userDataStore.add(user)
    }

    fun update(user: User) {
        userDataStore.update(user)
    }
}
