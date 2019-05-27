package app.component

import antd.layout.layout
import antd.layout.header
import antd.layout.content
import antd.layout.footer
import antd.message.message
import app.component.home.home
import app.component.navbar.*
import app.component.user.account.AccountRouteProps
import app.component.user.account.account
import app.repository.datasource.AuthDataStore
import app.repository.datasource.LanguageDiskDataStore
import app.localization.EN_US
import app.localization.Locale
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
import reactintl.IntlShape
import reactintl.injectIntl

interface AppProps : RProps {
    var intl: IntlShape
    var onLocaleChange: (Locale) -> Unit
}

interface AppState : RState {
    var user: User
}

class AppComponent : Component<AppProps, AppState, AppView>(), AppView {
    override val presenter = AppPresenter(this,
            LanguageRepository(LanguageDiskDataStore()), UserRepository(UserDiskDataStore()),
            AuthDataStore())
    override fun updateLocale(locale: Locale) {
        props.onLocaleChange(locale)
    }

    override fun updateUser(user: User) {
        setState {
            this.user = user
        }
    }

    override fun showMessage(messageKey: String) {
        message.success(props.intl.formatHTMLMessage(jsObject {
            id = "app.message-success.$messageKey"
        }, undefined))
    }

    override fun AppState.init() {
        user = User("", "", "", "")
    }

    override fun componentDidMount() {
        presenter.checkCurrentLocale(EN_US)
        presenter.checkUserSigned()
    }

    override fun RBuilder.render() {
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
                            route<RProps>(Navigator.Route.HOME, exact = true) {
                                home {
                                    attrs{
                                        user = state.user
                                        onEventSubscribe = {
                                            presenter.subscribe(state.user, it)
                                        }
                                        onEventUnsubscribe = {
                                            presenter.unsubscribe(state.user, it)
                                        }
                                    }
                                }
                            }
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

private val injectedApp = injectIntl(AppComponent::class.js.unsafeCast<JsClass<react.Component<AppProps, AppState>>>())

fun RBuilder.app(handler: RHandler<AppProps>) = child(injectedApp, jsObject {}, handler)
