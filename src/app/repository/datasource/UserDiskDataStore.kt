package app.repository.datasource

import data.entity.UserEntity
import data.repository.UserQuery
import data.repository.datasource.UserDataStore
import kotlin.browser.localStorage

class UserDiskDataStore : UserDataStore {
    override fun findById(id: String): UserEntity? = localStorage.getItem("users")?.let {
        JSON.parse<Array<UserEntity>>(it).find { u -> !u.isSubscribed && u.id == id }
    }

    override fun findOne(query: UserQuery): UserEntity? = localStorage.getItem("users")?.let {
        JSON.parse<Array<UserEntity>>(it).singleOrNull { u ->
            var condition = query.isSubscribed?.let { isSubscribed ->
                (isSubscribed == u.isSubscribed)
            } ?: !u.isSubscribed

            condition = if (!query.username.isNullOrEmpty() && !query.email.isNullOrEmpty()) {
                condition && (query.username == u.username || query.email == u.email)
            } else {
                condition = query.username?.let { username ->
                    condition && username == u.username
                } ?: condition

                condition = query.email?.let { email ->
                    condition && email == u.email
                } ?: condition

                condition
            }

            condition
        }
    }

    override fun create(user: UserEntity) = localStorage.getItem("users")?.let {
        val users = JSON.parse<Array<UserEntity>>(it).toMutableList()

        users.add(user)

        users.toTypedArray()

        localStorage.setItem("users", JSON.stringify(users))
    } ?: let {
        val users = mutableListOf(user).toTypedArray()

        localStorage.setItem("users", JSON.stringify(users))
    }

    override fun update(user: UserEntity) = localStorage.getItem("users")?.let {
        val users = mutableListOf<UserEntity>()

        JSON.parse<Array<UserEntity>>(it).forEach { u ->
            if (u.id == user.id && !u.isSubscribed) {
                u.isAuthenticated = user.isAuthenticated
            }

            users.add(u)
        }

        users.toTypedArray()

        localStorage.setItem("users", JSON.stringify(users))
    }!!
}
