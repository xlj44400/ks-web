package app.component.navbar

import antd.button.button
import antd.button.buttonGroup
import antd.dropdown.dropdown
import antd.grid.col
import antd.grid.row
import antd.icon.icon
import antd.input.input
import antd.menu.menu
import antd.menu.menuDivider
import antd.menu.menuItem
import antd.modal.modal
import app.component.auth.forgotpassword.forgotPassword
import app.component.auth.signin.signIn
import app.component.auth.signup.signUp
import app.navigator.Navigator
import domain.model.User
import presentation.view.NavbarView
import react.*
import react.dom.*
import react.router.dom.routeLink
import reactresponsive.mediaQuery

interface NavbarProps : RProps {
    var navigator: Navigator
    var user: User
    var onSignIn: (User) -> Unit
    var onSignUp: (User) -> Unit
    var onSignOut: (User) -> Unit
}

interface NavbarState : RState {
    var isModalVisible: Boolean
    var isSignIn: Boolean
    var isSignUp: Boolean
    var isForgotPassword: Boolean
    var isSearchVisible: Boolean
}

class NavbarComponent : RComponent<NavbarProps, NavbarState>(), NavbarView {
    override fun showModal(type: String) {
        when (type) {
            NavbarView.ModalType.SIGN_IN -> {
                setState {
                    isModalVisible = true
                    isSignIn = true
                    isSignUp = false
                    isForgotPassword = false
                }
            }
            NavbarView.ModalType.SIGN_UP -> {
                setState {
                    isModalVisible = true
                    isSignIn = false
                    isSignUp = true
                    isForgotPassword = false
                }
            }
            NavbarView.ModalType.FORGOT_PASSWORD -> {
                setState {
                    isModalVisible = true
                    isSignIn = false
                    isSignUp = false
                    isForgotPassword = true
                }
            }
        }
    }

    override fun hideModal() {
        setState {
            isModalVisible = false

            if (state.isSignIn) {
                isSignIn = false
            }

            if (state.isSignUp) {
                isSignUp = false
            }

            if (state.isForgotPassword) {
                isForgotPassword = false
            }
        }
    }

    override fun showSearchInput() {
        setState {
            isSearchVisible = true
        }
    }

    override fun hideSearchInput() {
        setState {
            isSearchVisible = false
        }
    }

    override fun focusSearchInput(element: dynamic) {
        element?.focus()
    }

    override fun NavbarState.init() {
        isModalVisible = false
        isSignIn = false
        isSignUp = false
        isForgotPassword = false
        isSearchVisible = false
    }

    override fun RBuilder.render() {
        div("navbar-container") {
            mediaQuery {
                attrs.minWidth = 768
                childList.add(fun(matches: Boolean): ReactElement {
                    return row {
                        attrs {
                            type = "flex"
                            justify = "center"
                            gutter = 16
                        }
                        col {
                            attrs.span = 6
                            routeLink("/") {
                                attrs.asDynamic().className = "navbar-logo"
                                img {
                                    attrs.src = "/assets/img/kotlin-spain-logo.png"
                                }
                                if (matches) {
                                    span { +"Kotlin ES" }
                                }
                            }
                        }
                        if (matches || state.isSearchVisible) {
                            col {
                                attrs.span = if (matches) 8 else 18
                                input {
                                    attrs {
                                        ref<dynamic> { node -> focusSearchInput(node) }
                                        prefix = buildElement {
                                            icon {
                                                attrs.type = "search"
                                            }
                                        }
                                        placeholder = "Search Kotlin ES"
                                        onBlur = {
                                            hideSearchInput()
                                        }
                                    }
                                }
                            }
                        }
                        if (!state.isSearchVisible) {
                            col {
                                attrs.span = if (matches) 10 else 18
                                div("navbar-menu-group") {
                                    if (matches && !props.user.isAuthenticated) {
                                        buttonGroup {
                                            button {
                                                attrs.onClick = {
                                                    showModal(NavbarView.ModalType.SIGN_UP)
                                                }
                                                +"Sign up"
                                            }
                                            button {
                                                attrs {
                                                    type = "primary"
                                                    onClick = {
                                                        showModal(NavbarView.ModalType.SIGN_IN)
                                                    }
                                                }
                                                +"Sign in"
                                            }
                                        }
                                    } else if (matches && props.user.isAuthenticated) {
                                        div("navbar-user-link") {
                                            span { +"Welcome " }
                                            routeLink("/user/${props.user.id}/account") { +props.user.username }
                                        }
                                    } else {
                                        buttonGroup {
                                            a("#") {
                                                icon {
                                                    attrs {
                                                        type = "search"
                                                        onClick = {
                                                            showSearchInput()
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    dropdown {
                                        attrs {
                                            overlay = buildElement {
                                                menu {
                                                    attrs.onClick = { param ->
                                                        when (param.key) {
                                                            NavbarView.MenuItem.GET_STARTED.key -> {
                                                                showModal(NavbarView.ModalType.SIGN_UP)
                                                            }
                                                            NavbarView.MenuItem.ACCOUNT.key -> {
                                                                props.navigator.navigateTo("/user/${props.user.id}/account")
                                                            }
                                                            NavbarView.MenuItem.SIGN_OUT.key -> {
                                                                props.onSignOut.invoke(props.user)
                                                            }
                                                            NavbarView.MenuItem.TELEGRAM.key -> {
                                                                props.navigator.openWindow("https://t.me/kotlinES")
                                                            }
                                                        }
                                                    }
                                                    if (!matches && !props.user.isAuthenticated) {
                                                        menuItem {
                                                            attrs.key = NavbarView.MenuItem.GET_STARTED.key
                                                            icon {
                                                                attrs.type = "login"
                                                            }
                                                            +"Get started"
                                                        }
                                                        menuDivider {}
                                                    }
                                                    if (props.user.isAuthenticated) {
                                                        menuItem {
                                                            attrs.key = NavbarView.MenuItem.ACCOUNT.key
                                                            icon {
                                                                attrs.type = "profile"
                                                            }
                                                            +"My account"
                                                        }
                                                        menuItem {
                                                            attrs.key = NavbarView.MenuItem.SIGN_OUT.key
                                                            icon {
                                                                attrs.type = "logout"
                                                            }
                                                            +"Sign out"
                                                        }
                                                        menuDivider {}
                                                    }
                                                    menuItem {
                                                        attrs.key = NavbarView.MenuItem.TELEGRAM.key
                                                        telegramIcon()
                                                        +"Telegram"
                                                    }
                                                }
                                            }!!
                                            trigger = arrayOf("click")
                                        }
                                        a("#", "ant-dropdown-link") {
                                            icon {
                                                attrs.type = "user"
                                            }
                                            icon {
                                                attrs.type = "down"
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                })
            }
            modal {
                attrs {
                    centered = true
                    visible = state.isModalVisible
                    footer = null
                    onCancel = {
                        hideModal()
                    }
                    destroyOnClose = true
                }

                if (state.isSignIn) {
                    signIn {
                        attrs {
                            onSignIn = { user ->
                                hideModal()

                                props.onSignIn.invoke(user)
                            }
                            onSignUpLinkClick = {
                                showModal(NavbarView.ModalType.SIGN_UP)
                            }
                            onForgotPasswordLinkClick = {
                                showModal(NavbarView.ModalType.FORGOT_PASSWORD)
                            }
                        }
                    }
                }

                if (state.isSignUp) {
                    signUp {
                        attrs {
                            onSignUp = { user ->
                                hideModal()

                                props.onSignUp.invoke(user)
                            }
                            onSignInLinkClick = {
                                showModal(NavbarView.ModalType.SIGN_IN)
                            }
                        }
                    }
                }

                if (state.isForgotPassword) {
                    forgotPassword {
                        attrs {
                            onSendEmail = {
                                hideModal()
                            }
                            onSigninLinkClick = {
                                showModal(NavbarView.ModalType.SIGN_IN)
                            }
                            onSignupLinkClick = {
                                showModal(NavbarView.ModalType.SIGN_UP)
                            }
                        }
                    }
                }
            }
        }
    }
}

fun RBuilder.navbar(handler: RHandler<NavbarProps>) = child(NavbarComponent::class, handler)
