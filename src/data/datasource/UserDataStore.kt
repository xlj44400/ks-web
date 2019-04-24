package data.datasource

import domain.model.User

interface UserDataStore {
    fun findById(id: String): User?
    fun findByUsername(username: String, hasTelegram: Boolean): User?
    fun findByEmail(email: String): User?
    fun add(user: User)
    fun update(user: User)
}
