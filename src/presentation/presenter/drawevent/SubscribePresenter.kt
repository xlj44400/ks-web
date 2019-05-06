package presentation.presenter.drawevent

import data.repository.UserRepository
import domain.model.User
import presentation.presenter.Presenter
import presentation.view.drawevent.SubscribeView

class SubscribePresenter(view: SubscribeView, private val userRepository: UserRepository) : Presenter<SubscribeView>(view) {
    enum class Error(val key: String) {
        USERNAME_EMPTY("username-empty"),
        EMAIL_EMPTY("email-empty"),
        USER_ALREADY_SUBSCRIBED("user-already-subscribed")
    }

    fun validateUsername(value: String) {
        val isValid = value.isNotEmpty()

        view.updateUsername(value, isValid, Error.USERNAME_EMPTY.key)
    }

    fun validateEmail(value: String) {
        val isValid = value.isNotEmpty()

        view.updateEmail(value,isValid, Error.EMAIL_EMPTY.key)
    }

    fun subscribe(user: User) {
        view.showLoading()

        val foundUser = userRepository.findByUsername(user.username, true)

        if (foundUser != null) {
            view.showError(Error.USER_ALREADY_SUBSCRIBED.key)
        } else {
            view.onSubscribe(user)
        }

        view.hideLoading()
    }
}
