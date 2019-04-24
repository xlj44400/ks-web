package presentation.presenter.user

import data.repository.UserRepository
import presentation.presenter.Presenter
import presentation.view.user.AccountView

class AccountPresenter(view: AccountView, private val userRepository: UserRepository) : Presenter<AccountView>(view) {
    fun getUser(id: String) {
        val user = userRepository.findById(id)

        user?.let {
            view.updateAccountData(it)
        }
    }
}
