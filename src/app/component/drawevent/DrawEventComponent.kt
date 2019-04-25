package app.component.drawevent

import antd.button.button
import antd.message.message as Message
import antd.modal.modal
import app.component.Component
import app.component.drawevent.countdown.countdown
import app.component.drawevent.subscribe.subscribe
import app.datasource.UserDiskDataStore
import data.repository.UserRepository
import presentation.presenter.drawevent.DrawEventPresenter
import presentation.view.drawevent.DrawEventView
import react.*
import react.dom.div
import react.dom.h1
import react.dom.p
import react.dom.span
import kotlin.js.Date

interface DrawEventState : RState {
    var isSubscribeModalVisible: Boolean
    var isEventEnded: Boolean
}

class DrawEventComponent : Component<RProps, DrawEventState, DrawEventView>(), DrawEventView {
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

    override fun showMessage(message: String) {
        Message.success(message)
    }

    override fun DrawEventState.init() {
        isSubscribeModalVisible = false
        isEventEnded = false
    }

    override fun RBuilder.render() {
        div("event-container") {
            div("event-header") {
                h1 { +"Free Kotlin Stickers" }
                p {
                    +"We are drawing 30 Kotlin stickers for the first users of the platform, "
                    +"you can be one of the lucky ones, don't miss this opportunity."
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
                        +"Subscribe now"
                    }
                } else {
                    span { +"Event has ended" }
                }
            }
            div("event-footer") {
                p { +"*Only users from Spain can participate, we are sorry for everyone else." }
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

fun RBuilder.drawEvent() = child(DrawEventComponent::class) {}
