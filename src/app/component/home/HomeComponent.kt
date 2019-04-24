package app.component.home

import app.component.drawevent.drawEvent
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.div

class HomeComponent : RComponent<RProps, RState>() {
    override fun RBuilder.render() {
        div("home-container") {
            drawEvent()
        }
    }
}

fun RBuilder.home() = child(HomeComponent::class) {}
