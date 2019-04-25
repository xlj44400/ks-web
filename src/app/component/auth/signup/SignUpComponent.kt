package app.component.auth.signup

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
import presentation.presenter.auth.SignUpPresenter
import presentation.view.auth.SignUpView
import react.*
import react.dom.a
import react.dom.div
import react.dom.span

interface SignUpProps : RProps {
    var onSignUp: (User) -> Unit
    var onSignInLinkClick: () -> Unit
}

interface SignUpState : RState {
    var username: String
    var email: String
    var password: String
    var repeatPassword: String
    var isValidUsername: Boolean
    var isValidEmail: Boolean
    var isValidPassword: Boolean
    var isValidRepeatPassword: Boolean
    var usernameError: String?
    var emailError: String?
    var passwordError: String?
    var repeatPasswordError: String?
    var isLoading: Boolean
}

class SignUpComponent : Component<SignUpProps, SignUpState, SignUpView>(), SignUpView {
    override val presenter = SignUpPresenter(this, UserRepository(UserDiskDataStore()))

    override fun updateUsername(value: String, isValid: Boolean, error: String?) {
        setState {
            username = value
            isValidUsername = isValid
            usernameError = error
        }
    }

    override fun updateEmail(value: String, isValid: Boolean, error: String?) {
        setState {
            email = value
            isValidEmail = isValid
            emailError = error
        }
    }

    override fun updatePassword(value: String, isValid: Boolean, error: String?) {
        setState {
            password = value
            isValidPassword = isValid
            passwordError = error
        }
    }

    override fun updateRepeatPassword(value: String, isValid: Boolean, error: String?) {
        setState {
            repeatPassword = value
            isValidRepeatPassword = isValid
            repeatPasswordError = error
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

    override fun onSignUp(user: User) {
        props.onSignUp(user)
    }

    override fun SignUpState.init() {
        username = ""
        email = ""
        password = ""
        repeatPassword = ""
        isValidUsername = false
        isValidEmail = false
        isValidPassword = false
        isValidRepeatPassword = false
        isLoading = false
    }

    override fun RBuilder.render() {
        div("sign-up-modal-header") {
            span { +"Welcome to Kotlin ES" }
        }
        div("sign-up-modal-content") {
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
                        validateStatus = if (state.isValidEmail) "success" else "error"
                        help = state.emailError
                    }
                    input {
                        attrs {
                            prefix = buildElement {
                                icon {
                                    attrs.type = "mail"
                                }
                            }
                            placeholder = "Email"
                            onChange = { event ->
                                event.persist()

                                val inputValue = event.target.asDynamic().value as String

                                presenter.validateEmail(inputValue)
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
                    attrs {
                        validateStatus = if (state.isValidRepeatPassword) "success" else "error"
                        help = state.repeatPasswordError
                    }
                    input {
                        attrs {
                            prefix = buildElement {
                                icon {
                                    attrs.type = "lock"
                                }
                            }
                            type = "password"
                            placeholder = "Repeat password"
                            onChange = { event ->
                                event.persist()

                                val inputValue = event.target.asDynamic().value as String

                                presenter.validateRepeatPassword(inputValue)
                            }
                        }
                    }
                }
            }
        }
        div("sign-up-modal-footer") {
            button {
                attrs {
                    type = "primary"
                    onClick = {
                        if (state.isValidUsername && state.isValidEmail && state.isValidPassword && state.isValidRepeatPassword) {
                            val user = User().apply {
                                username = state.username
                                email = state.email
                                password = state.password
                            }

                            presenter.signUp(user, state.repeatPassword)
                        } else {
                            presenter.validateUsername(state.username)
                            presenter.validateEmail(state.email)
                            presenter.validatePassword(state.password)
                            presenter.validateRepeatPassword(state.repeatPassword)
                        }
                    }
                }
                +"Sign up"
            }
            span {
                +"Already have an account?"
                a {
                    attrs.onClickFunction = {
                        props.onSignInLinkClick.invoke()
                    }
                    +"Sign in"
                }
            }
        }
    }
}

fun RBuilder.signUp(handler: RHandler<SignUpProps>) = child(SignUpComponent::class, handler)
