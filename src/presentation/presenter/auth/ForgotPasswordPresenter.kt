package presentation.presenter.auth

import data.repository.UserQuery
import data.repository.UserRepository
import presentation.presenter.Presenter
import presentation.view.auth.ForgotPasswordView

class ForgotPasswordPresenter(view: ForgotPasswordView, private val userRepository: UserRepository) : Presenter<ForgotPasswordView>(view) {
    enum class Error(val key: String) {
        EMAIL_EMPTY("email-empty"),
        USER_NOT_FOUND("user-not-found")
    }

    enum class Message(val key: String) {
        EMAIL_SENT("email-sent")
    }

    fun validateEmail(value: String) {
        val isValid = value.isNotEmpty()

        view.updateEmail(value, isValid, Error.EMAIL_EMPTY.key)
    }

    fun sendEmail(email: String) {
        view.showLoading()

        userRepository.findOne(UserQuery(email = email))?.let {
            view.onSendEmail()

            view.showMessage(Message.EMAIL_SENT.key)
        } ?: view.showError(Error.USER_NOT_FOUND.key)

        view.hideLoading()
    }
}
