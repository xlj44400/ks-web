package app.datasource

import data.datasource.UserDataStore
import domain.model.User
import kotlin.browser.localStorage

class UserDiskDataStore : UserDataStore {
    override fun findById(id: String): User? {
        return localStorage.getItem("users")?.let {
            JSON.parse<Array<User>>(it)
                    .filter { u -> !u.hasTelegram }
                    .find { u -> u.id == id }
        }
    }

    override fun findByUsername(username: String, hasTelegram: Boolean): User? {
        return localStorage.getItem("users")?.let {
            JSON.parse<Array<User>>(it)
                    .filter { u -> u.hasTelegram == hasTelegram }
                    .find { u -> u.username == username }
        }
    }

    override fun findByEmail(email: String): User? {
        return localStorage.getItem("users")?.let {
            JSON.parse<Array<User>>(it)
                    .filter { u -> !u.hasTelegram }
                    .find { u -> u.email == email }
        }
    }

    override fun add(user: User) {
        var users = localStorage.getItem("users")?.let {
            JSON.parse<Array<User>>(it)
        }

        users = if (users != null) {
            val userList = users.toMutableList()

            userList.add(user)

            userList.toTypedArray()
        } else {
            mutableListOf(user).toTypedArray()
        }

        localStorage.setItem("users", JSON.stringify(users))
    }

    override fun update(user: User) {
        localStorage.getItem("users")?.let {
            val users = mutableListOf<User>()

            JSON.parse<Array<User>>(it).forEach { u ->
                if (u.id == user.id && !u.hasTelegram) {
                    u.isAuthenticated = user.isAuthenticated
                }

                users.add(u)
            }

            users.toTypedArray()
        }?.let {
            localStorage.setItem("users", JSON.stringify(it))
        }
    }
}
