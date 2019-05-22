package data.repository.datasource

import domain.User

interface UserDataStore {
    fun findById(id: String): User?
    fun findByUsername(username: String, hasTelegram: Boolean): User?
    fun findByEmail(email: String): User?
    fun add(user: User)
    fun update(user: User)
}
