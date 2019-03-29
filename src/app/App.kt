package app

import react.*
import react.dom.*

class App : RComponent<RProps, RState>() {
    override fun RBuilder.render() {
        div { +"Welcome to Kotlin ES Web!" }
    }
}

fun RBuilder.app() = child(App::class) {}
