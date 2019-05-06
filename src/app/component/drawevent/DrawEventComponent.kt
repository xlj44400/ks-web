package app.component.drawevent

import antd.button.button
import antd.message.message as Message
import antd.modal.modal
import app.component.Component
import app.component.drawevent.countdown.countdown
import app.component.drawevent.subscribe.subscribe
import app.datasource.UserDiskDataStore
import data.repository.UserRepository
import kotlinext.js.jsObject
import presentation.presenter.drawevent.DrawEventPresenter
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
}

interface DrawEventState : RState {
    var isSubscribeModalVisible: Boolean
    var isEventEnded: Boolean
}

class DrawEventComponent : Component<DrawEventProps, DrawEventState, DrawEventView>(), DrawEventView {
    override val presenter = DrawEventPresenter(this, UserRepository(UserDiskDataStore()))

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

    override fun hideSubscribeButton() {
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
                            hideSubscribeButton()
                        }
                    }
                }
                if (!state.isEventEnded) {
                    button {
                        attrs {
                            type = "primary"
                            size = "large"
                            onClick = {
                                showSubscribeModal()
                            }
                        }
                        formattedMessage {
                            attrs.id = "draw-event.content.button.text"
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
                    onSubscribe = { user ->
                        presenter.subscribe(user)

                        hideSubscribeModal()
                    }
                }
            }
        }
    }
}

private val injectedDrawEvent = injectIntl(DrawEventComponent::class.js.unsafeCast<JsClass<react.Component<DrawEventProps, DrawEventState>>>())

fun RBuilder.drawEvent(handler: RHandler<DrawEventProps>) = child(injectedDrawEvent, jsObject {}, handler)
