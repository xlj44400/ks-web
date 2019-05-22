package presentation.view.user

import domain.User
import presentation.view.View

interface AccountView : View {
    fun updateAccountData(user: User)
}
