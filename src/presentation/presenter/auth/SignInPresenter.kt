package presentation.presenter.auth

import data.repository.UserQuery
import data.repository.UserRepository
import presentation.presenter.Presenter
import presentation.view.auth.SignInView

class SignInPresenter(view: SignInView, private val userRepository: UserRepository) : Presenter<SignInView>(view) {
    enum class Error(val key: String) {
        USERNAME_EMPTY("username-empty"),
        PASSWORD_EMPTY("password-empty"),
        PASSWORD_NOT_MATCH("password-not-match"),
        USER_NOT_REGISTERED("user-not-registered")
    }

    fun validateUsername(value: String) {
        val isValid = value.isNotEmpty()

        view.updateUsername(value, isValid, Error.USERNAME_EMPTY.key)
    }

    fun validatePassword(value: String) {
        val isValid = value.isNotEmpty()

        view.updatePassword(value, isValid, Error.PASSWORD_EMPTY.key)
    }

    fun signIn(username: String, password: String) {
        view.showLoading()

        userRepository.findOne(UserQuery(username = username))?.let {
            if (it.password != password) {
                view.showError(Error.PASSWORD_NOT_MATCH.key)
            } else {
                view.onSignIn(it)
            }
        } ?: view.showError(Error.USER_NOT_REGISTERED.key)

        view.hideLoading()
    }
}
