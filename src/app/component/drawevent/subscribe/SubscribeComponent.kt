package app.component.drawevent.subscribe

import antd.button.button
import antd.form.form
import antd.form.formItem
import antd.icon.icon
import antd.input.input
import antd.modal.ModalComponent
import app.component.Component
import app.datasource.UserDiskDataStore
import data.repository.UserRepository
import domain.model.User
import kotlinext.js.jsObject
import presentation.presenter.drawevent.SubscribePresenter
import presentation.view.drawevent.SubscribeView
import react.*
import react.dom.div
import react.dom.span

interface SubscribeProps : RProps {
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

    override fun showError(error: String) {
        ModalComponent.error(jsObject {
            content = error
        })
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
            span { +"Subscribe to event" }
        }
        div("subscribe-modal-content") {
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
                +"Subscribe"
            }
        }
    }
}

fun RBuilder.subscribe(handler: RHandler<SubscribeProps>) = child(SubscribeComponent::class, handler)
