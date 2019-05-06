package app

import app.component.app
import app.localization.addLocaleData
import kotlinext.js.*
import react.dom.*
import kotlin.browser.*

fun main() {
    requireAll(require.context("src", true, js("/(\\.css|\\.less)$/")))

    addLocaleData()

    render(document.getElementById("root")) {
        app()
    }
}
