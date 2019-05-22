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
import antd.menu.subMenu
import antd.modal.modal
import app.component.Component
import app.component.auth.forgotpassword.forgotPassword
import app.component.auth.signin.signIn
import app.component.auth.signup.signUp
import app.repository.datasource.LanguageDiskDataStore
import app.navigator.Navigator
import data.repository.LanguageRepository
import domain.Language
import domain.User
import kotlinext.js.jsObject
import presentation.presenter.navbar.NavbarPresenter
import presentation.view.NavbarView
import react.*
import react.dom.*
import react.router.dom.routeLink
import reactintl.IntlShape
import reactintl.injectIntl
import reactintl.message.formattedMessage
import reactresponsive.mediaQuery

interface NavbarProps : RProps {
    var intl: IntlShape
    var navigator: Navigator
    var user: User
    var onSignIn: (User) -> Unit
    var onSignUp: (User) -> Unit
    var onSignOut: (User) -> Unit
    var onChangeLanguage: (String) -> Unit
}

interface NavbarState : RState {
    var languages: Array<Language>
    var isModalVisible: Boolean
    var isSignIn: Boolean
    var isSignUp: Boolean
    var isForgotPassword: Boolean
    var isSearchVisible: Boolean
    var isMenuGroupVisible: Boolean
}

class NavbarComponent : Component<NavbarProps, NavbarState, NavbarView>(), NavbarView {
    override val presenter = NavbarPresenter(this, LanguageRepository(LanguageDiskDataStore()))

    override fun updateLanguages(languages: Array<Language>) {
        setState {
            this.languages = languages
        }
    }

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
        languages = emptyArray()
        isModalVisible = false
        isSignIn = false
        isSignUp = false
        isForgotPassword = false
        isSearchVisible = false
        isMenuGroupVisible = true
    }

    override fun componentDidMount() {
        presenter.loadLanguages()
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
                                        placeholder = props.intl.formatMessage(jsObject {
                                            id = "navbar.search-box.placeholder"
                                        }, undefined)
                                        onBlur = {
                                            if (state.isSearchVisible) {
                                                hideSearchInput()
                                            }
                                        }
                                    }
                                }
                                if (matches && state.isSearchVisible) {
                                    hideSearchInput()
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
                                                formattedMessage {
                                                    attrs.id = "navbar.menu.sign-up"
                                                }
                                            }
                                            button {
                                                attrs {
                                                    type = "primary"
                                                    onClick = {
                                                        showModal(NavbarView.ModalType.SIGN_IN)
                                                    }
                                                }
                                                formattedMessage {
                                                    attrs.id = "navbar.menu.sign-in"
                                                }
                                            }
                                        }
                                    } else if (matches && props.user.isAuthenticated) {
                                        div("navbar-user-link") {
                                            +props.intl.formatMessage(jsObject {
                                                id = "navbar.welcome"
                                            }, undefined)
                                            +" "
                                            routeLink("/user/${props.user.id}/account") { +props.user.username }
                                        }
                                    } else {
                                        buttonGroup {
                                            a {
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
                                                        val key = param.keyPath[0]
                                                        val subKey = param.keyPath[1]

                                                        when (key) {
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

                                                        if (subKey == NavbarView.MenuItem.LANGUAGE.key) {
                                                            props.onChangeLanguage.invoke(key)
                                                        }
                                                    }
                                                    if (!matches && !props.user.isAuthenticated) {
                                                        menuItem {
                                                            attrs.key = NavbarView.MenuItem.GET_STARTED.key
                                                            icon {
                                                                attrs.type = "login"
                                                            }
                                                            formattedMessage {
                                                                attrs.id = "navbar.menu.get-started"
                                                            }
                                                        }
                                                        menuDivider {}
                                                    }
                                                    if (props.user.isAuthenticated) {
                                                        menuItem {
                                                            attrs.key = NavbarView.MenuItem.ACCOUNT.key
                                                            icon {
                                                                attrs.type = "profile"
                                                            }
                                                            formattedMessage {
                                                                attrs.id = "navbar.menu.account"
                                                            }
                                                        }
                                                        menuItem {
                                                            attrs.key = NavbarView.MenuItem.SIGN_OUT.key
                                                            icon {
                                                                attrs.type = "logout"
                                                            }
                                                            formattedMessage {
                                                                attrs.id = "navbar.menu.sign-out"
                                                            }
                                                        }
                                                        menuDivider {}
                                                    }
                                                    subMenu {
                                                        attrs {
                                                            key = NavbarView.MenuItem.LANGUAGE.key
                                                            title = buildElement {
                                                                span {
                                                                    languageIcon()
                                                                    formattedMessage {
                                                                        attrs.id = "navbar.menu.language"
                                                                    }
                                                                }
                                                            }
                                                        }
                                                        state.languages.map { language ->
                                                            menuItem {
                                                                attrs.key = language.locale
                                                                formattedMessage {
                                                                    attrs.id = "navbar.menu.language.${language.locale}"
                                                                }
                                                            }
                                                        }.toTypedArray()
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
                                        a("", "ant-dropdown-link") {
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

private val injectedNavbar = injectIntl(NavbarComponent::class.js.unsafeCast<JsClass<react.Component<NavbarProps, NavbarState>>>())

fun RBuilder.navbar(handler: RHandler<NavbarProps>) = child(injectedNavbar, jsObject {}, handler)
