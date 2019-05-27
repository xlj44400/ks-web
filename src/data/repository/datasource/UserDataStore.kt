package data.repository.datasource

import data.entity.UserEntity
import data.repository.UserQuery

interface UserDataStore {
    fun findById(id: String): UserEntity?
    fun findOne(query: UserQuery): UserEntity?
    fun create(user: UserEntity)
    fun update(user: UserEntity)
}
