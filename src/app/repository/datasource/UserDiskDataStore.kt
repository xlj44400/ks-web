package app.repository.datasource

import data.repository.datasource.UserDataStore
import domain.User
import kotlin.browser.localStorage

class UserDiskDataStore : UserDataStore {
    override fun findById(id: String): User? = localStorage.getItem("users")?.let {
        JSON.parse<Array<User>>(it).filter { u -> !u.hasTelegram }.find { u -> u.id == id }
    }

    override fun findByUsername(username: String, hasTelegram: Boolean): User? = localStorage.getItem("users")?.let {
        JSON.parse<Array<User>>(it).filter { u -> u.hasTelegram == hasTelegram }.find { u -> u.username == username }
    }

    override fun findByEmail(email: String): User? = localStorage.getItem("users")?.let {
        JSON.parse<Array<User>>(it).filter { u -> !u.hasTelegram }.find { u -> u.email == email }
    }

    override fun create(user: User) = localStorage.getItem("users")?.let {
        val users = JSON.parse<Array<User>>(it).toMutableList()

        users.add(user)

        users.toTypedArray()

        localStorage.setItem("users", JSON.stringify(users))
    } ?: let {
        val users = mutableListOf(user).toTypedArray()

        localStorage.setItem("users", JSON.stringify(users))
    }

    override fun update(user: User) = localStorage.getItem("users")?.let {
        val users = mutableListOf<User>()

        JSON.parse<Array<User>>(it).forEach { u ->
            if (u.id == user.id && !u.hasTelegram) {
                u.isAuthenticated = user.isAuthenticated
            }

            users.add(u)
        }

        users.toTypedArray()

        localStorage.setItem("users", JSON.stringify(users))
    }!!
}
