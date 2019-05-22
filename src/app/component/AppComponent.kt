package app.component

import antd.layout.layout
import antd.layout.header
import antd.layout.content
import antd.layout.footer
import antd.localeprovider.localeProvider
import app.component.home.HomeComponent
import app.component.navbar.*
import app.component.user.account.AccountRouteProps
import app.component.user.account.account
import app.repository.datasource.AuthDataStore
import app.repository.datasource.LanguageDiskDataStore
import app.localization.EN_US
import app.localization.Locale
import app.localization.enLocale
import app.localization.enMessages
import app.navigator.Navigator
import app.repository.datasource.UserDiskDataStore
import data.repository.LanguageRepository
import data.repository.UserRepository
import domain.User
import kotlinext.js.jsObject
import presentation.presenter.AppPresenter
import presentation.view.AppView
import react.*
import react.dom.div
import react.dom.span
import react.router.dom.browserRouter
import react.router.dom.redirect
import react.router.dom.route
import react.router.dom.switch
import reactintl.provider.intlProvider

interface AppState : RState {
    var locale: Locale
    var user: User
}

class AppComponent : Component<RProps, AppState, AppView>(), AppView {
    override val presenter = AppPresenter(this,
            LanguageRepository(LanguageDiskDataStore()), UserRepository(UserDiskDataStore()),
            AuthDataStore())

    override fun updateLocale(locale: Locale) {
        moment.locale(locale.id)

        setState {
            this.locale = locale
        }
    }

    override fun updateAuth(user: User) {
        setState {
            this.user = user
        }
    }

    override fun AppState.init() {
        locale = jsObject {
            id = EN_US
            localeData = enLocale
            messages = enMessages
        }
        user = User()
    }

    override fun componentDidMount() {
        presenter.checkCurrentLocale(EN_US)
        presenter.checkUserSigned()
    }

    override fun RBuilder.render() {
        localeProvider {
            attrs.locale = state.locale.localeData
            intlProvider {
                attrs {
                    locale = state.locale.id
                    messages = state.locale.messages
                }
                browserRouter {
                    div("app-container") {
                        layout {
                            header {
                                route<RProps>(Navigator.Route.HOME) { props ->
                                    navbar {
                                        attrs {
                                            navigator = Navigator(props)
                                            user = state.user
                                            onSignIn = { user ->
                                                presenter.signIn(user)
                                            }
                                            onSignUp = { user ->
                                                presenter.signUp(user)
                                            }
                                            onSignOut = { user ->
                                                presenter.signOut(user)
                                            }
                                            onChangeLanguage = { locale ->
                                                presenter.changeLocale(locale)
                                            }
                                        }
                                    }
                                }
                            }
                            content {
                                switch {
                                    route(Navigator.Route.HOME, HomeComponent::class, exact = true)
                                    route<AccountRouteProps>(Navigator.Route.ACCOUNT, exact = true) { props ->
                                        val userId = props.match.params.id

                                        if (state.user.isAuthenticated && state.user.id == userId) {
                                            account {
                                                attrs.userId = userId
                                            }
                                        } else {
                                            redirect(props.location.pathname, Navigator.Route.HOME)
                                        }
                                    }
                                }
                            }
                            footer {
                                span { +"Copyright (C) 2019 Kotlin ES" }
                            }
                        }
                    }
                }
            }
        }
    }
}

fun RBuilder.app() = child(AppComponent::class) {}
