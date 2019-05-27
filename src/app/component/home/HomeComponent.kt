package app.component.home

import app.component.drawevent.drawEvent
import domain.Subscription
import domain.User
import react.*
import react.dom.div

interface HomeProps : RProps {
    var user: User
    var onEventSubscribe: (Subscription) -> Unit
    var onEventUnsubscribe: (Subscription) -> Unit
}

class HomeComponent : RComponent<HomeProps, RState>() {
    override fun RBuilder.render() {
        div("home-container") {
            drawEvent {
                attrs {
                    user = props.user
                    onSubscribe = props.onEventSubscribe
                    onUnsubscribe = props.onEventUnsubscribe
                }
            }
        }
    }
}

fun RBuilder.home(handler: RHandler<HomeProps>) = child(HomeComponent::class, handler)
