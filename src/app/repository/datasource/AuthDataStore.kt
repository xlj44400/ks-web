package app.repository.datasource

import kotlin.browser.localStorage

class AuthDataStore {
    fun getAuthToken(): String? = localStorage.getItem("token")

    fun setAuthToken(token: String) = localStorage.setItem("token", token)

    fun removeAuthToken() = localStorage.removeItem("token")
}
