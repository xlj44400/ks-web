package presentation.presenter

import app.repository.datasource.AuthDataStore
import app.localization.*
import data.repository.LanguageRepository
import data.repository.UserRepository
import domain.Language
import domain.User
import kotlinext.js.jsObject
import presentation.view.AppView

class AppPresenter(view: AppView,
                   private val languageRepository: LanguageRepository,
                   private val userRepository: UserRepository,
                   private val authDataStore: AuthDataStore) : Presenter<AppView>(view) {
    fun checkCurrentLocale(defaultLocale: String) {
        val language = languageRepository.findAll().find { l -> l.isActive }

        if (language != null) {
            changeLocale(language.locale)
        } else {
            changeLocale(defaultLocale)
        }
    }

    fun changeLocale(locale: String) {
        val newLocale: Locale = when (locale) {
            ES_ES -> jsObject {
                id = locale
                localeData = esLocale
                messages = esMessages
            }
            else -> jsObject {
                id = locale
                localeData = enLocale
                messages = enMessages
            }
        }

        languageRepository.update(Language.create(locale, true))

        view.updateLocale(newLocale)
    }

    fun checkUserSigned() {
        val user = authDataStore.getAuthToken()?.let {
            userRepository.findById(it)
        }

        if (user != null) {
            user.isAuthenticated = true

            view.updateAuth(user)
        } else {
            view.updateAuth(User())
        }
    }

    fun signUp(user: User) {
        val newUser = User.create(user.username, user.email, user.password)
        newUser.isAuthenticated = true

        userRepository.add(newUser)

        authDataStore.setAuthToken(newUser.id)

        view.updateAuth(newUser)
    }

    fun signIn(user: User) {
        user.isAuthenticated = true

        userRepository.update(user)

        authDataStore.setAuthToken(user.id)

        view.updateAuth(user)
    }

    fun signOut(user: User) {
        user.isAuthenticated = false

        userRepository.update(user)

        authDataStore.removeAuthToken()

        view.updateAuth(user)
    }
}
