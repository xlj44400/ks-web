package presentation.presenter

import app.datasource.AuthDataStore
import data.repository.UserRepository
import domain.model.User
import presentation.view.AppView

class AppPresenter(view: AppView,
                   private val userRepository: UserRepository,
                   private val authDataStore: AuthDataStore) : Presenter<AppView>(view) {
    fun checkUserSigned() {
        val user = authDataStore.getAuthToken()?.let {
            userRepository.findById(it)
        }

        if (user != null) {
            user.isAuthenticated = true

            view.updateAuth(user)
        } else {
            view.updateAuth(User())
        }
    }

    fun signUp(user: User) {
        val newUser = User.create(user.username, user.email, user.password)
        newUser.isAuthenticated = true

        userRepository.add(newUser)

        authDataStore.setAuthToken(newUser.id)

        view.updateAuth(newUser)
    }

    fun signIn(user: User) {
        user.isAuthenticated = true

        userRepository.update(user)

        authDataStore.setAuthToken(user.id)

        view.updateAuth(user)
    }

    fun signOut(user: User) {
        user.isAuthenticated = false

        userRepository.update(user)

        authDataStore.removeAuthToken()

        view.updateAuth(user)
    }
}
