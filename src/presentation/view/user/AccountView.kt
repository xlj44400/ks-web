package presentation.view.user

import domain.model.User
import presentation.view.View

interface AccountView : View {
    fun updateAccountData(user: User)
}
