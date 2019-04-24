package presentation.presenter.auth

import data.repository.UserRepository
import presentation.presenter.Presenter
import presentation.view.auth.ForgotPasswordView

class ForgotPasswordPresenter(view: ForgotPasswordView, private val userRepository: UserRepository) : Presenter<ForgotPasswordView>(view) {
    object Error {
        const val EMAIL_EMPTY = "Email can not be empty"
        const val USER_NOT_FOUND= "There is not user account with that email"
    }

    object Message {
        const val EMAIL_SENT = "Email was sent successfully"
    }

    fun validateEmail(value: String) {
        val isValid = value.isNotEmpty()

        if (isValid) {
            view.updateEmail(value, true)
        } else {
            view.updateEmail(value,false, Error.EMAIL_EMPTY)
        }
    }

    fun sendEmail(email: String) {
        view.showLoading()

        val user = userRepository.findByEmail(email)

        if (user != null) {
            view.onSendEmail()

            view.showMessage(Message.EMAIL_SENT)
        } else {
            view.showError(Error.USER_NOT_FOUND)
        }

        view.hideLoading()
    }
}
