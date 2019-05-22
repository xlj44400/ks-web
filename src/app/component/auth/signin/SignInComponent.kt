package app.component.auth.signin

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
import presentation.presenter.auth.SignInPresenter
import presentation.view.auth.SignInView
import react.*
import react.dom.a
import react.dom.div
import react.dom.span
import reactintl.IntlShape
import reactintl.injectIntl
import reactintl.message.formattedMessage

interface SignInProps : RProps {
    var intl: IntlShape
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

    override fun updateUsername(value: String, isValid: Boolean, errorKey: String) {
        setState {
            username = value
            isValidUsername = isValid
            usernameError = props.intl.formatMessage(jsObject {
                id = "sign-in.input-error.$errorKey"
            }, undefined)
        }
    }

    override fun updatePassword(value: String, isValid: Boolean, errorKey: String) {
        setState {
            password = value
            isValidPassword = isValid
            passwordError = props.intl.formatMessage(jsObject {
                id = "sign-in.input-error.$errorKey"
            }, undefined)
        }
    }

    override fun showError(errorKey: String) {
        Message.error(props.intl.formatMessage(jsObject {
            id = "sign-in.message-error.$errorKey"
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
            formattedMessage {
                attrs.id = "sign-in.header.title"
            }
        }
        div("sign-in-modal-content") {
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
                                id =  "sign-in.content.input-username.placeholder"
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
                                id =  "sign-in.content.input-password.placeholder"
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
                    a {
                        attrs.onClickFunction = {
                            props.onForgotPasswordLinkClick.invoke()
                        }
                        formattedMessage {
                            attrs.id = "sign-in.content.link-forgot-password"
                        }
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
                formattedMessage {
                    attrs.id = "sign-in.footer.button.text"
                }
            }
            span {
                +props.intl.formatMessage(jsObject {
                    id =  "sign-in.footer.sign-up.text"
                }, undefined)
                a {
                    attrs.onClickFunction = {
                        props.onSignUpLinkClick.invoke()
                    }
                    formattedMessage {
                        attrs.id = "sign-in.footer.link-sign-up"
                    }
                }
            }
        }
    }
}

private val injectedSignIn = injectIntl(SignInComponent::class.js.unsafeCast<JsClass<react.Component<SignInProps, SignInState>>>())

fun RBuilder.signIn(handler: RHandler<SignInProps>) = child(injectedSignIn, jsObject {}, handler)
