package presentation.presenter.auth

import data.repository.UserRepository
import domain.model.User
import presentation.presenter.Presenter
import presentation.view.auth.SignUpView

class SignUpPresenter(view: SignUpView, private val userRepository: UserRepository) : Presenter<SignUpView>(view) {
    object Error {
        const val USERNAME_EMPTY = "Username can not be empty"
        const val EMAIL_EMPTY = "Email can not be empty"
        const val PASSWORD_EMPTY = "Password can not be empty"
        const val REPEAT_PASSWORD_EMPTY = "Repeat password can not be empty"
        const val USER_ALREADY_EXIST = "User with that username already exists"
        const val PASSWORD_NOT_MATCH = "Password does not match"
    }

    fun validateUsername(value: String) {
        val isValid = value.isNotEmpty()

        if (isValid) {
            view.updateUsername(value, true)
        } else {
            view.updateUsername(value,false, Error.USERNAME_EMPTY)
        }
    }

    fun validateEmail(value: String) {
        val isValid = value.isNotEmpty()

        if (isValid) {
            view.updateEmail(value, true)
        } else {
            view.updateEmail(value,false, Error.EMAIL_EMPTY)
        }
    }

    fun validatePassword(value: String) {
        val isValid = value.isNotEmpty()

        if (isValid) {
            view.updatePassword(value, true)
        } else {
            view.updatePassword(value,false, Error.PASSWORD_EMPTY)
        }
    }

    fun validateRepeatPassword(value: String) {
        val isValid = value.isNotEmpty()

        if (isValid) {
            view.updateRepeatPassword(value, true)
        } else {
            view.updateRepeatPassword(value,false, Error.REPEAT_PASSWORD_EMPTY)
        }
    }

    fun signUp(user: User, password: String) {
        view.showLoading()

        val foundUser = userRepository.findByUsername(user.username)

        if (foundUser != null) {
            view.showError(Error.USER_ALREADY_EXIST)
        } else {
            if (user.password != password) {
                view.showError(Error.PASSWORD_NOT_MATCH)
            } else {
                view.onSignUp(user)
            }
        }

        view.hideLoading()
    }
}
