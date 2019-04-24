package app.navigator

import react.RProps
import react.router.dom.RouteResultProps
import kotlin.browser.window

class Navigator(props: RouteResultProps<RProps>) {
    object Route {
        const val HOME = "/"
        const val ACCOUNT = "/user/:id/account"
    }

    private val history = props.history

    val match = props.match
    val location = props.location

    fun navigateTo(path: String) {
        history.push(path)
    }

    fun openWindow(url: String) {
        window.open(url, "_blank")
    }
}
