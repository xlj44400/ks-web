package app.component.drawevent.subscribe

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
import kotlinext.js.jsObject
import presentation.presenter.drawevent.SubscribePresenter
import presentation.view.drawevent.SubscribeView
import react.*
import react.dom.div
import reactintl.IntlShape
import reactintl.injectIntl
import reactintl.message.formattedMessage

interface SubscribeProps : RProps {
    var intl: IntlShape
    var onSubscribe: (User) -> Unit
}

interface SubscribeState : RState {
    var username: String
    var email: String
    var isValidUsername: Boolean
    var isValidEmail: Boolean
    var usernameError: String?
    var emailError: String?
    var isLoading: Boolean
}

class SubscribeComponent : Component<SubscribeProps, SubscribeState, SubscribeView>(), SubscribeView {
    override val presenter = SubscribePresenter(this, UserRepository(UserDiskDataStore()))

    override fun updateUsername(value: String, isValid: Boolean, errorKey: String) {
        setState {
            username = value
            isValidUsername = isValid
            usernameError = props.intl.formatMessage(jsObject {
                id = "subscribe.input-error.$errorKey"
            }, undefined)
        }
    }

    override fun updateEmail(value: String, isValid: Boolean, errorKey: String) {
        setState {
            email = value
            isValidEmail = isValid
            emailError = props.intl.formatMessage(jsObject {
                id = "subscribe.input-error.$errorKey"
            }, undefined)
        }
    }

    override fun showError(errorKey: String) {
        Message.error(props.intl.formatMessage(jsObject {
            id = "subscribe.message-error.$errorKey"
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

    override fun onSubscribe(user: User) {
        props.onSubscribe(user)
    }

    override fun SubscribeState.init() {
        username = ""
        email = ""
        isValidUsername = false
        isValidEmail = false
        isLoading = false
    }

    override fun RBuilder.render() {
        div("subscribe-modal-header") {
            formattedMessage {
                attrs.id = "subscribe.header.title"
            }
        }
        div("subscribe-modal-content") {
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
                                id = "subscribe.content.input-username.placeholder"
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
                                id = "subscribe.content.input-email.placeholder"
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
        div("subscribe-modal-footer") {
            button {
                attrs {
                    type = "primary"
                    onClick = {
                        if (state.isValidUsername && state.isValidEmail) {
                            val user = User().apply {
                                username = state.username
                                email = state.email
                            }

                            presenter.subscribe(user)
                        } else {
                            presenter.validateUsername(state.username)
                            presenter.validateEmail(state.email)
                        }
                    }
                }
                formattedMessage {
                    attrs.id = "subscribe.footer.button.text"
                }
            }
        }
    }
}

private val injectedSubscribe = injectIntl(SubscribeComponent::class.js.unsafeCast<JsClass<react.Component<SubscribeProps, SubscribeState>>>())

fun RBuilder.subscribe(handler: RHandler<SubscribeProps>) = child(injectedSubscribe, jsObject {}, handler)
