package presentation.presenter.drawevent

import data.repository.UserRepository
import domain.User
import presentation.presenter.Presenter
import presentation.view.drawevent.DrawEventView

class DrawEventPresenter(view: DrawEventView, private val userRepository: UserRepository) : Presenter<DrawEventView>(view) {
    enum class Message(val key: String) {
        SUBSCRIBED("subscribed")
    }

    fun subscribe(user: User) {
        val subscribeUser = User(user.username, user.email)

        subscribeUser.subscribe(true)

        userRepository.create(subscribeUser)

        view.showMessage(Message.SUBSCRIBED.key)
    }
}
