package app.component.auth.signin

import antd.button.button
import antd.form.form
import antd.form.formItem
import antd.icon.icon
import antd.input.input
import antd.message.message as Message
import app.component.Component
import app.datasource.UserDiskDataStore
import data.repository.UserRepository
import domain.model.User
import kotlinx.html.js.onClickFunction
import presentation.presenter.auth.SignInPresenter
import presentation.view.auth.SignInView
import react.*
import react.dom.a
import react.dom.div
import react.dom.span

interface SignInProps : RProps {
    var onSignIn: (User) -> Unit
    var onSignUpLinkClick: () -> Unit
    var onForgotPasswordLinkClick: () -> Unit
}

interface SignInState : RState {
    var username: String
    var password: String
    var isValidUsername: Boolean
    var isValidPassword: Boolean
    var usernameError: String?
    var passwordError: String?
    var isLoading: Boolean
}

class SignInComponent : Component<SignInProps, SignInState, SignInView>(), SignInView {
    override val presenter = SignInPresenter(this, UserRepository(UserDiskDataStore()))

    override fun updateUsername(value: String, isValid: Boolean, error: String?) {
        setState {
            username = value
            isValidUsername = isValid
            usernameError = error
        }
    }

    override fun updatePassword(value: String, isValid: Boolean, error: String?) {
        setState {
            password = value
            isValidPassword = isValid
            passwordError = error
        }
    }

    override fun showError(error: String) {
        Message.error(error)
    }

    override fun showLoading() {
        setState {
            isLoading = true
        }
    }

    override fun hideLoading() {
        setState {
            isLoading = false
        }
    }

    override fun onSignIn(user: User) {
        props.onSignIn(user)
    }

    override fun SignInState.init() {
        username = ""
        password = ""
        isValidUsername = false
        isValidPassword = false
        isLoading = false
    }

    override fun RBuilder.render() {
        div("sign-in-modal-header") {
            span { +"Welcome back to Kotlin ES" }
        }
        div("sign-in-modal-content") {
            form {
                attrs.layout = "vertical"
                formItem {
                    attrs {
                        validateStatus = if (state.isValidUsername) "success" else "error"
                        help = state.usernameError
                    }
                    input {
                        attrs {
                            prefix = buildElement {
                                icon {
                                    attrs.type = "user"
                                }
                            }
                            placeholder = "Username"
                            onChange = { event ->
                                event.persist()

                                val inputValue = event.target.asDynamic().value as String

                                presenter.validateUsername(inputValue)
                            }
                        }
                    }
                }
                formItem {
                    attrs {
                        validateStatus = if (state.isValidPassword) "success" else "error"
                        help = state.passwordError
                    }
                    input {
                        attrs {
                            prefix = buildElement {
                                icon {
                                    attrs.type = "lock"
                                }
                            }
                            type = "password"
                            placeholder = "Password"
                            onChange = { event ->
                                event.persist()

                                val inputValue = event.target.asDynamic().value as String

                                presenter.validatePassword(inputValue)
                            }
                        }
                    }
                }
                formItem {
                    a {
                        attrs.onClickFunction = {
                            props.onForgotPasswordLinkClick.invoke()
                        }
                        +"Forgot password?"
                    }
                }
            }
        }
        div("sign-in-modal-footer") {
            button {
                attrs {
                    type = "primary"
                    onClick = {
                        if (state.isValidUsername && state.isValidPassword) {
                            presenter.signIn(state.username, state.password)
                        } else {
                            presenter.validateUsername(state.username)
                            presenter.validatePassword(state.password)
                        }
                    }
                }
                +"Sign in"
            }
            span {
                +"New in Kotlin ES?"
                a {
                    attrs.onClickFunction = {
                        props.onSignUpLinkClick.invoke()
                    }
                    +"Sign up"
                }
            }
        }
    }
}

fun RBuilder.signIn(handler: RHandler<SignInProps>) = child(SignInComponent::class, handler)
