package app.component.navbar

import antd.icon.IconProps
import antd.icon.icon
import kotlinx.html.HTMLTag
import react.*
import react.dom.RDOMBuilder
import react.dom.svg
import react.dom.tag

private fun RBuilder.customTag(tagName: String, block: RDOMBuilder<HTMLTag>.() -> Unit): ReactElement = tag(block) {
    HTMLTag(tagName, it, mapOf(), null, true, emptyTag = false)
}

class TelegramSvgComponent : RComponent<RProps, RState>() {
    override fun RBuilder.render() {
        svg {
            attrs["width"] = "1em"
            attrs["height"] = "1em"
            attrs["viewBox"] = "0 0 48 48"
            attrs["fill"] = "currentColor"
            customTag("circle") {
                attrs["fill"] = "#29b6f6"
                attrs["cx"] = "24"
                attrs["cy"] = "24"
                attrs["r"] = "20"
            }
            customTag("path") {
                attrs["fill"] = "#ffffff"
                attrs["d"] = "M33.95,15l-3.746,19.126c0,0-0.161,0.874-1.245,0.874c-0.576,0-0.873-0.274-0.873-0.274l-8.114-6.733  l-3.97-2.001l-5.095-1.355c0,0-0.907-0.262-0.907-1.012c0-0.625,0.933-0.923,0.933-0.923l21.316-8.468  c-0.001-0.001,0.651-0.235,1.126-0.234C33.667,14,34,14.125,34,14.5C34,14.75,33.95,15,33.95,15z"
            }
            customTag("path") {
                attrs["fill"] = "#b0bec5"
                attrs["d"] = "M23,30.505l-3.426,3.374c0,0-0.149,0.115-0.348,0.12c-0.069,0.002-0.143-0.009-0.219-0.043  l0.964-5.965L23,30.505z"
            }
            customTag("path") {
                attrs["fill"] = "#cfd8dc"
                attrs["d"] = "M29.897,18.196c-0.169-0.22-0.481-0.26-0.701-0.093L16,26c0,0,2.106,5.892,2.427,6.912  c0.322,1.021,0.58,1.045,0.58,1.045l0.964-5.965l9.832-9.096C30.023,18.729,30.064,18.416,29.897,18.196z"
            }
        }
    }
}

private fun RBuilder.telegramSvg() = child(TelegramSvgComponent::class) {}

class TelegramIconComponent : RComponent<IconProps, RState>() {
    override fun RBuilder.render() {
        icon {
            attrs.component = telegramSvg().asDynamic().type.unsafeCast<Any>()
        }
    }
}

fun RBuilder.telegramIcon() = child(TelegramIconComponent::class) {}