package presentation.view

import app.localization.Locale
import domain.User

interface AppView : View {
    fun updateLocale(locale: Locale)
    fun updateAuth(user: User)
}
