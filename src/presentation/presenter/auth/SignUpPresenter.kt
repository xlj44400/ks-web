package presentation.presenter.auth

import data.repository.UserQuery
import data.repository.UserRepository
import domain.User
import presentation.presenter.Presenter
import presentation.view.auth.SignUpView

class SignUpPresenter(view: SignUpView, private val userRepository: UserRepository) : Presenter<SignUpView>(view) {
    enum class Error(val key: String) {
        USERNAME_EMPTY("username-empty"),
        EMAIL_EMPTY("email-empty"),
        PASSWORD_EMPTY("password-empty"),
        REPEAT_PASSWORD_EMPTY("repeat-password-empty"),
        USER_ALREADY_REGISTERED("user-already-registered"),
        PASSWORD_NOT_MATCH("password-not-match")
    }

    fun validateUsername(value: String) {
        val isValid = value.isNotEmpty()

        view.updateUsername(value, isValid, Error.USERNAME_EMPTY.key)
    }

    fun validateEmail(value: String) {
        val isValid = value.isNotEmpty()

        view.updateEmail(value, isValid, Error.EMAIL_EMPTY.key)
    }

    fun validatePassword(value: String) {
        val isValid = value.isNotEmpty()

        view.updatePassword(value, isValid, Error.PASSWORD_EMPTY.key)
    }

    fun validateRepeatPassword(value: String) {
        val isValid = value.isNotEmpty()

        view.updateRepeatPassword(value,isValid, Error.REPEAT_PASSWORD_EMPTY.key)
    }

    fun signUp(user: User, password: String) {
        view.showLoading()

        userRepository.findOne(UserQuery(username = user.username!!, email = user.email!!))?.let {
            view.showError(Error.USER_ALREADY_REGISTERED.key)
        } ?: if (user.password != password) {
            view.showError(Error.PASSWORD_NOT_MATCH.key)
        } else {
            view.onSignUp(user)
        }
    }
}
