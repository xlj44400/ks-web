package app.component.drawevent.subscribe

import antd.button.button
import antd.form.form
import antd.form.formItem
import antd.icon.icon
import antd.input.input
import antd.message.message as Message
import app.component.Component
import app.repository.datasource.UserDiskDataStore
import data.repository.UserRepository
import domain.Subscription
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
    var onSubscribe: (Subscription) -> Unit
}

interface SubscribeState : RState {
    var address: String
    var isValidAddress: Boolean
    var addressError: String?
    var isLoading: Boolean
}

class SubscribeComponent : Component<SubscribeProps, SubscribeState, SubscribeView>(), SubscribeView {
    override val presenter = SubscribePresenter(this, UserRepository(UserDiskDataStore()))

    override fun updateAddress(value: String, isValid: Boolean, errorKey: String) {
        setState {
            address = value
            isValidAddress= isValid
            addressError = props.intl.formatMessage(jsObject {
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

    override fun SubscribeState.init() {
        address = ""
        isValidAddress = false
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
                        validateStatus = if (state.isValidAddress) "success" else "error"
                        help = if (state.isValidAddress) null else state.addressError
                    }
                    input {
                        attrs {
                            prefix = buildElement {
                                icon {
                                    attrs.type = "global"
                                }
                            }
                            placeholder = props.intl.formatMessage(jsObject {
                                id = "subscribe.content.input-address.placeholder"
                            }, undefined)
                            onChange = { event ->
                                event.persist()

                                val inputValue = event.target.asDynamic().value as String

                                presenter.validateAddress(inputValue)
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
                        if (state.isValidAddress) {
                            val subscription = Subscription(state.address)

                            props.onSubscribe(subscription)
                        } else {
                            presenter.validateAddress(state.address)
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
