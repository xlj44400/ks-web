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
import kotlinx.html.js.onClickFunction
import presentation.presenter.auth.ForgotPasswordPresenter
import presentation.view.auth.ForgotPasswordView
import react.*
import react.dom.a
import react.dom.div
import react.dom.span

interface ForgotPasswordProps : RProps {
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

    override fun updateEmail(value: String, isValid: Boolean, error: String?) {
        setState {
            email = value
            isValidEmail = isValid
            emailError = error
        }
    }

    override fun showError(error: String) {
        Message.error(error)
    }

    override fun showMessage(message: String) {
        Message.success(message)
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
            span { +"Reset password" }
        }
        div("forgot-password-modal-content") {
            form {
                attrs.layout = "vertical"
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
                +"Send email"
            }
            span {
                a {
                    attrs.onClickFunction = {
                        props.onSigninLinkClick.invoke()
                    }
                    +"Sign in"
                }
                +"or"
                a {
                    attrs.onClickFunction = {
                        props.onSignupLinkClick.invoke()
                    }
                    +"Sign up"
                }
            }
        }
    }
}

fun RBuilder.forgotPassword(handler: RHandler<ForgotPasswordProps>) = child(ForgotPasswordComponent::class, handler)
