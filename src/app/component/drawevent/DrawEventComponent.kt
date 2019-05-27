package app.component.drawevent

import antd.button.button
import antd.modal.modal
import antd.message.message as Message
import app.component.drawevent.countdown.countdown
import app.component.drawevent.subscribe.subscribe
import domain.Subscription
import domain.User
import kotlinext.js.jsObject
import presentation.view.drawevent.DrawEventView
import react.*
import react.dom.div
import reactintl.IntlShape
import reactintl.injectIntl
import reactintl.message.formattedHTMLMessage
import reactintl.message.formattedMessage
import kotlin.js.Date

interface DrawEventProps : RProps {
    var intl: IntlShape
    var user: User
    var onSubscribe: (Subscription) -> Unit
    var onUnsubscribe: (Subscription) -> Unit
}

interface DrawEventState : RState {
    var isSubscribeModalVisible: Boolean
    var isEventEnded: Boolean
}

class DrawEventComponent : RComponent<DrawEventProps, DrawEventState>(), DrawEventView {
    override fun showSubscribeModal() {
        setState {
            isSubscribeModalVisible = true
        }
    }

    override fun hideSubscribeModal() {
        setState {
            isSubscribeModalVisible = false
        }
    }

    override fun onEventEnded() {
        setState {
            isEventEnded = true
        }
    }

    override fun showMessage(messageKey: String) {
        Message.success(props.intl.formatHTMLMessage(jsObject {
            id = "draw-event.message-success.$messageKey"
        }, undefined))
    }

    override fun DrawEventState.init() {
        isSubscribeModalVisible = false
        isEventEnded = false
    }

    override fun RBuilder.render() {
        div("event-container") {
            div("event-header") {
                formattedHTMLMessage {
                    attrs.id = "draw-event.header.title"
                }
                formattedHTMLMessage {
                    attrs.id = "draw-event.header.description"
                }
            }
            div("event-content") {
                countdown {
                    attrs {
                        date = Date("2019-07-07T19:00:00")
                        onOver = {
                            onEventEnded()
                        }
                    }
                }
                if (!state.isEventEnded) {
                    if (!props.user.isAuthenticated) {
                        formattedMessage {
                            attrs.id = "draw-event.content.sign-up.text"
                        }
                    } else {
                        button {
                            attrs {
                                type = "primary"
                                size = "large"
                                onClick = {
                                    if (props.user.isSubscribed) {
                                        props.onUnsubscribe(props.user.subscription!!)
                                    } else {
                                        showSubscribeModal()
                                    }
                                }
                            }
                            formattedMessage {
                                if (props.user.isSubscribed) {
                                    attrs.id = "draw-event.content.unsubscribe-button.text"
                                } else {
                                    attrs.id = "draw-event.content.subscribe-button.text"
                                }
                            }
                        }
                    }
                } else {
                    formattedMessage {
                        attrs.id = "draw-event.content.event-ended"
                    }
                }
            }
            div("event-footer") {
                formattedHTMLMessage {
                    attrs.id = "draw-event.footer.info"
                }
            }
        }
        modal {
            attrs {
                centered = true
                visible = state.isSubscribeModalVisible
                footer = null
                onCancel = {
                    hideSubscribeModal()
                }
                destroyOnClose = true
            }

            subscribe {
                attrs {
                    onSubscribe = {
                        props.onSubscribe(it)

                        hideSubscribeModal()
                    }
                }
            }
        }
    }
}

private val injectedDrawEvent = injectIntl(DrawEventComponent::class.js.unsafeCast<JsClass<Component<DrawEventProps, DrawEventState>>>())

fun RBuilder.drawEvent(handler: RHandler<DrawEventProps>) = child(injectedDrawEvent, jsObject {}, handler)
