package app.component.auth.forgotpassword

import antd.button.button
import antd.form.form
import antd.form.formItem
import antd.icon.icon
import antd.input.input
import antd.message.message as Message
import app.component.Component
import app.datasource.UserDiskDataStore
import data.repository.UserRepository
import kotlinext.js.jsObject
import kotlinx.html.js.onClickFunction
import presentation.presenter.auth.ForgotPasswordPresenter
import presentation.view.auth.ForgotPasswordView
import react.*
import react.dom.a
import react.dom.div
import react.dom.span
import reactintl.IntlShape
import reactintl.injectIntl
import reactintl.message.formattedMessage

interface ForgotPasswordProps : RProps {
    var intl: IntlShape
    var onSendEmail: () -> Unit
    var onSigninLinkClick: () -> Unit
    var onSignupLinkClick: () -> Unit
}

interface ForgotPasswordState : RState {
    var email: String
    var isValidEmail: Boolean
    var emailError: String?
    var isLoading: Boolean
}

class ForgotPasswordComponent : Component<ForgotPasswordProps, ForgotPasswordState, ForgotPasswordView>(), ForgotPasswordView {
    override val presenter = ForgotPasswordPresenter(this, UserRepository(UserDiskDataStore()))

    override fun updateEmail(value: String, isValid: Boolean, errorKey: String) {
        setState {
            email = value
            isValidEmail = isValid
            emailError = props.intl.formatMessage(jsObject {
                id = "forgot-password.input-error.$errorKey"
            }, undefined)
        }
    }

    override fun showError(errorKey: String) {
        Message.error(props.intl.formatMessage(jsObject {
            id = "forgot-password.message-error.$errorKey"
        }, undefined))
    }

    override fun showMessage(messageKey: String) {
        Message.success(props.intl.formatMessage(jsObject {
            id = "forgot-password.message-success.$messageKey"
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

    override fun onSendEmail() {
        props.onSendEmail.invoke()
    }

    override fun ForgotPasswordState.init() {
        email = ""
        isValidEmail = false
        emailError = ""
        isLoading = false
    }

    override fun RBuilder.render() {
        div("forgot-password-modal-header") {
            formattedMessage {
                attrs {
                    id = "forgot-password.header.title"
                    defaultMessage = "Reset password"
                }
            }
        }
        div("forgot-password-modal-content") {
            form {
                attrs.layout = "vertical"
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
                                id =  "forgot-password.content.input-email.placeholder"
                                defaultMessage = "Email"
                            }, undefined)
                            onChange = { event ->
                                event.persist()

                                val inputValue = event.target.asDynamic().value as String

                                presenter.validateEmail(inputValue)
                            }
                        }
                    }
                }
            }
        }
        div("forgot-password-modal-footer") {
            button {
                attrs {
                    type = "primary"
                    loading = state.isLoading
                    onClick = {
                        if (state.isValidEmail) {
                            presenter.sendEmail(state.email)
                        } else {
                            presenter.validateEmail(state.email)
                        }
                    }
                }
                formattedMessage {
                    attrs {
                        id = "forgot-password.footer.button.text"
                        defaultMessage = "Send email"
                    }
                }
            }
            span {
                a {
                    attrs.onClickFunction = {
                        props.onSigninLinkClick.invoke()
                    }
                    formattedMessage {
                        attrs {
                            id = "forgot-password.footer.link-sign-in"
                            defaultMessage = "Sign in"
                        }
                    }
                }
                +props.intl.formatMessage(jsObject {
                    id = "forgot-password.footer.or.text"
                    defaultMessage = "or"
                }, undefined)
                a {
                    attrs.onClickFunction = {
                        props.onSignupLinkClick.invoke()
                    }
                    formattedMessage {
                        attrs {
                            id = "forgot-password.footer.link-sign-up"
                            defaultMessage = "Sign up"
                        }
                    }
                }
            }
        }
    }
}

private val injectedForgotPassword = injectIntl(ForgotPasswordComponent::class.js.unsafeCast<JsClass<react.Component<ForgotPasswordProps, ForgotPasswordState>>>())

fun RBuilder.forgotPassword(handler: RHandler<ForgotPasswordProps>) = child(injectedForgotPassword, jsObject {}, handler)
