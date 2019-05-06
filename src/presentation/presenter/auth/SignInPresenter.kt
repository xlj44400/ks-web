package presentation.presenter.auth

import data.repository.UserRepository
import presentation.presenter.Presenter
import presentation.view.auth.SignInView

class SignInPresenter(view: SignInView, private val userRepository: UserRepository) : Presenter<SignInView>(view) {
    enum class Error(val key: String) {
        USERNAME_EMPTY("username-empty"),
        PASSWORD_EMPTY("password-empty"),
        PASSWORD_NOT_MATCH("password-not-match"),
        USER_NOT_EXIST("user-not-exist")
    }

    fun validateUsername(value: String) {
        val isValid = value.isNotEmpty()

        view.updateUsername(value,isValid, Error.USERNAME_EMPTY.key)
    }

    fun validatePassword(value: String) {
        val isValid = value.isNotEmpty()

        view.updatePassword(value,isValid, Error.PASSWORD_EMPTY.key)
    }

    fun signIn(username: String, password: String) {
        view.showLoading()

        val user = userRepository.findByUsername(username)

        if (user != null) {
            if (user.password != password) {
                view.showError(Error.PASSWORD_NOT_MATCH.key)
            } else {
                view.onSignIn(user)
            }
        } else {
            view.showError(Error.USER_NOT_EXIST.key)
        }

        view.hideLoading()
    }
}
