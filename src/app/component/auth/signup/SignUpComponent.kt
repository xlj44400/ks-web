package app.component.auth.signup

import antd.button.button
import antd.form.form
import antd.form.formItem
import antd.icon.icon
import antd.input.input
import antd.message.message as Message
import app.component.Component
import app.repository.datasource.UserDiskDataStore
import data.repository.UserRepository
import domain.User
import kotlinext.js.jsObject
import kotlinx.html.js.onClickFunction
import presentation.presenter.auth.SignUpPresenter
import presentation.view.auth.SignUpView
import react.*
import react.dom.a
import react.dom.div
import react.dom.span
import reactintl.IntlShape
import reactintl.injectIntl
import reactintl.message.formattedMessage

interface SignUpProps : RProps {
    var intl: IntlShape
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

    override fun updateUsername(value: String, isValid: Boolean, errorKey: String) {
        setState {
            username = value
            isValidUsername = isValid
            usernameError = props.intl.formatMessage(jsObject {
                id = "sign-up.input-error.$errorKey"
            }, undefined)
        }
    }

    override fun updateEmail(value: String, isValid: Boolean, errorKey: String) {
        setState {
            email = value
            isValidEmail = isValid
            emailError = props.intl.formatMessage(jsObject {
                id = "sign-up.input-error.$errorKey"
            }, undefined)
        }
    }

    override fun updatePassword(value: String, isValid: Boolean, errorKey: String) {
        setState {
            password = value
            isValidPassword = isValid
            passwordError = props.intl.formatMessage(jsObject {
                id = "sign-up.input-error.$errorKey"
            }, undefined)
        }
    }

    override fun updateRepeatPassword(value: String, isValid: Boolean, errorKey: String) {
        setState {
            repeatPassword = value
            isValidRepeatPassword = isValid
            repeatPasswordError = props.intl.formatMessage(jsObject {
                id = "sign-up.input-error.$errorKey"
            }, undefined)
        }
    }

    override fun showError(errorKey: String) {
        Message.error(props.intl.formatMessage(jsObject {
            id = "sign-up.message-error.$errorKey"
        }, undefined))
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
            formattedMessage {
                attrs.id = "sign-up.header.title"
            }
        }
        div("sign-up-modal-content") {
            form {
                attrs.layout = "vertical"
                formItem {
                    attrs {
                        validateStatus = if (state.isValidUsername) "success" else "error"
                        help = if (state.isValidUsername) null else state.usernameError
                    }
                    input {
                        attrs {
                            prefix = buildElement {
                                icon {
                                    attrs.type = "user"
                                }
                            }
                            placeholder = props.intl.formatMessage(jsObject {
                                id =  "sign-up.content.input-username.placeholder"
                            }, undefined)
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
                        help = if (state.isValidEmail) null else state.emailError
                    }
                    input {
                        attrs {
                            prefix = buildElement {
                                icon {
                                    attrs.type = "mail"
                                }
                            }
                            placeholder = props.intl.formatMessage(jsObject {
                                id =  "sign-up.content.input-email.placeholder"
                            }, undefined)
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
                        help = if (state.isValidPassword) null else state.passwordError
                    }
                    input {
                        attrs {
                            prefix = buildElement {
                                icon {
                                    attrs.type = "lock"
                                }
                            }
                            type = "password"
                            placeholder = props.intl.formatMessage(jsObject {
                                id =  "sign-up.content.input-password.placeholder"
                            }, undefined)
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
                        help = if (state.isValidRepeatPassword) null else state.repeatPasswordError
                    }
                    input {
                        attrs {
                            prefix = buildElement {
                                icon {
                                    attrs.type = "lock"
                                }
                            }
                            type = "password"
                            placeholder = props.intl.formatMessage(jsObject {
                                id =  "sign-up.content.input-repeat-password.placeholder"
                            }, undefined)
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
                            val user = User(state.username, state.email, state.password)

                            presenter.signUp(user, state.repeatPassword)
                        } else {
                            presenter.validateUsername(state.username)
                            presenter.validateEmail(state.email)
                            presenter.validatePassword(state.password)
                            presenter.validateRepeatPassword(state.repeatPassword)
                        }
                    }
                }
                formattedMessage {
                    attrs.id = "sign-up.footer.button.text"
                }
            }
            span {
                +props.intl.formatMessage(jsObject {
                    id =  "sign-up.footer.sign-in.text"
                }, undefined)
                a {
                    attrs.onClickFunction = {
                        props.onSignInLinkClick.invoke()
                    }
                    formattedMessage {
                        attrs.id = "sign-up.footer.link-sign-in"
                    }
                }
            }
        }
    }
}

private val injectedSignUp = injectIntl(SignUpComponent::class.js.unsafeCast<JsClass<react.Component<SignUpProps, SignUpState>>>())

fun RBuilder.signUp(handler: RHandler<SignUpProps>) = child(injectedSignUp, jsObject {}, handler)
