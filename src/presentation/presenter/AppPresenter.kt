package presentation.presenter

import app.repository.datasource.AuthDataStore
import app.localization.*
import data.repository.LanguageRepository
import data.repository.UserRepository
import domain.Language
import domain.Subscription
import domain.User
import kotlinext.js.jsObject
import presentation.view.AppView

class AppPresenter(view: AppView,
                   private val languageRepository: LanguageRepository,
                   private val userRepository: UserRepository,
                   private val authDataStore: AuthDataStore) : Presenter<AppView>(view) {
    enum class Message(val key: String) {
        USER_SUBSCRIBED("user-subscribed"),
        USER_UNSUBSCRIBED("user-unsubscribed")
    }

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
        authDataStore.getAuthToken()?.let {
            userRepository.findById(it)
        }?.let {
            it.authenticate()

            view.updateUser(it)
        } ?: view.updateUser(User("", "", "", ""))
    }

    fun signUp(user: User) {
        user.authenticate()

        userRepository.create(user)

        authDataStore.setAuthToken(user.id)

        view.updateUser(user)
    }

    fun signIn(user: User) {
        user.authenticate()

        userRepository.update(user)

        authDataStore.setAuthToken(user.id)

        view.updateUser(user)
    }

    fun signOut(user: User) {
        user.deauthenticate()

        userRepository.update(user)

        authDataStore.removeAuthToken()

        view.updateUser(user)
    }

    fun subscribe(user: User, subscription: Subscription) {
        user.subscribe(subscription)

        userRepository.update(user)

        view.updateUser(user)

        view.showMessage(Message.USER_SUBSCRIBED.key)
    }

    fun unsubscribe(user: User, subscription: Subscription) {
        user.unsubscribe(subscription)

        userRepository.update(user)

        view.updateUser(user)

        view.showMessage(Message.USER_UNSUBSCRIBED.key)
    }
}
