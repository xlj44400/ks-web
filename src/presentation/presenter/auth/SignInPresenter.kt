package presentation.presenter.auth

import data.repository.UserRepository
import presentation.presenter.Presenter
import presentation.view.auth.SignInView

class SignInPresenter(view: SignInView, private val userRepository: UserRepository) : Presenter<SignInView>(view) {
    object Error {
        const val USERNAME_EMPTY = "Username can not be empty"
        const val PASSWORD_EMPTY = "Password can not be empty"
        const val PASSWORD_NOT_MATCH = "Password does not match"
        const val USER_NOT_EXIST = "User with that username does not exists"
    }

    fun validateUsername(value: String) {
        val isValid = value.isNotEmpty()

        if (isValid) {
            view.updateUsername(value, true)
        } else {
            view.updateUsername(value,false, Error.USERNAME_EMPTY)
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

    fun signIn(username: String, password: String) {
        view.showLoading()

        val user = userRepository.findByUsername(username)

        if (user != null) {
            if (user.password != password) {
                view.showError(Error.PASSWORD_NOT_MATCH)
            } else {
                view.onSignIn(user)
            }
        } else {
            view.showError(Error.USER_NOT_EXIST)
        }

        view.hideLoading()
    }
}
