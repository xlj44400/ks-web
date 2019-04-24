package app

import app.component.app
import kotlinext.js.*
import react.dom.*
import kotlin.browser.*

fun main() {
    requireAll(require.context("src", true, js("/(\\.css|\\.less)$/")))

    render(document.getElementById("root")) {
        app()
    }
}
