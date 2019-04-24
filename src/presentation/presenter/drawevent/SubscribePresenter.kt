package presentation.presenter.drawevent

import data.repository.UserRepository
import domain.model.User
import presentation.presenter.Presenter
import presentation.view.drawevent.SubscribeView

class SubscribePresenter(view: SubscribeView, private val userRepository: UserRepository) : Presenter<SubscribeView>(view) {
    object Error {
        const val USERNAME_EMPTY = "Username can not be empty"
        const val EMAIL_EMPTY = "Email can not be empty"
        const val USER_ALREADY_SUBSCRIBED = "User with that username already subscribed"
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

    fun subscribe(user: User) {
        view.showLoading()

        val foundUser = userRepository.findByUsername(user.username, true)

        if (foundUser != null) {
            view.showError(Error.USER_ALREADY_SUBSCRIBED)
        } else {
            view.onSubscribe(user)
        }

        view.hideLoading()
    }
}
