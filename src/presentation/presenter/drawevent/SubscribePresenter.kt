package presentation.presenter.drawevent

import data.repository.UserQuery
import data.repository.UserRepository
import domain.User
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

        userRepository.findOne(UserQuery(username = user.username!!, isSubscribed = true))?.let {
            view.showError(Error.USER_ALREADY_SUBSCRIBED.key)
        } ?: view.onSubscribe(user)

        view.hideLoading()
    }
}
