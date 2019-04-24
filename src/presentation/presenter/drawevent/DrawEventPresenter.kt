package presentation.presenter.drawevent

import data.repository.UserRepository
import domain.model.User
import presentation.presenter.Presenter
import presentation.view.drawevent.DrawEventView

class DrawEventPresenter(view: DrawEventView, private val userRepository: UserRepository) : Presenter<DrawEventView>(view) {
    object Message {
        const val SUBSCRIBED = "You have been subscribed successfully"
    }

    fun subscribe(user: User) {
        val subscribeUser = User.create(user.username, user.email, "")
        subscribeUser.hasTelegram = true

        userRepository.add(subscribeUser)

        view.showMessage(Message.SUBSCRIBED)
    }
}