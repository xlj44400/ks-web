package presentation.view

import domain.model.User

interface AppView : View {
    fun updateAuth(user: User)
}
