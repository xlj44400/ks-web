package app.component

import antd.layout.layout
import antd.layout.header
import antd.layout.content
import antd.layout.footer
import app.component.home.HomeComponent
import app.component.navbar.navbar
import app.component.user.account.AccountRouteProps
import app.component.user.account.account
import app.datasource.AuthDataStore
import app.datasource.UserDiskDataStore
import app.navigator.Navigator
import data.repository.UserRepository
import domain.model.User
import presentation.presenter.AppPresenter
import presentation.view.AppView
import react.*
import react.router.dom.browserRouter
import react.router.dom.redirect
import react.router.dom.route
import react.router.dom.switch

interface AppState : RState {
    var user: User
}

class AppComponent : Component<RProps, AppState, AppView>(), AppView {
    override val presenter = AppPresenter(this, UserRepository(UserDiskDataStore()), AuthDataStore())

    override fun updateAuth(user: User) {
        setState {
            this.user = user
        }
    }

    override fun AppState.init() {
        user = User()
    }

    override fun componentDidMount() {
        presenter.checkUserSigned()
    }

    override fun RBuilder.render() {
        browserRouter {
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
                footer { +"Copyright (C) 2019 Kotlin ES" }
            }
        }
    }
}

fun RBuilder.app() = child(AppComponent::class) {}
