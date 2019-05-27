package app

import antd.localeprovider.localeProvider
import app.component.app
import app.localization.*
import kotlinext.js.*
import react.*
import react.dom.*
import reactintl.provider.intlProvider
import kotlin.browser.*

interface RootState : RState {
    var locale: Locale
}

class RootComponent : RComponent<RProps, RootState>() {
    override fun RootState.init() {
        locale = jsObject {
            id = EN_US
            localeData = enLocale
            messages = enMessages
        }
    }

    override fun RBuilder.render() {
        localeProvider {
            attrs.locale = state.locale.localeData
            intlProvider {
                attrs {
                    locale = state.locale.id
                    messages = state.locale.messages
                }
                app {
                    attrs.onLocaleChange = {
                        moment.locale(it.id)

                        setState {
                            locale = it
                        }
                    }
                }
            }
        }
    }
}

fun RBuilder.root() = child(RootComponent::class) {}

fun main() {
    requireAll(require.context("src", true, js("/(\\.css|\\.less)$/")))

    addLocaleData()

    render(document.getElementById("root")) {
        root()
    }
}
