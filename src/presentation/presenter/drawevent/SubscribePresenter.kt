package presentation.presenter.drawevent

import data.repository.UserRepository
import presentation.presenter.Presenter
import presentation.view.drawevent.SubscribeView

class SubscribePresenter(view: SubscribeView, private val userRepository: UserRepository) : Presenter<SubscribeView>(view) {
    enum class Error(val key: String) {
        ADDRESS_EMPTY("address-empty")
    }

    fun validateAddress(value: String) {
        val isValid = value.isNotEmpty()

        view.updateAddress(value, isValid, Error.ADDRESS_EMPTY.key)
    }
}
