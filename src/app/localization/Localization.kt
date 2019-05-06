package app.localization

import kotlinext.js.Object
import reactintl.addLocaleData

@JsModule("antd/lib/locale-provider/en_US")
external val enLocale: antd.localeprovider.Locale

@JsModule("antd/lib/locale-provider/es_ES")
external val esLocale: antd.localeprovider.Locale

@JsModule("react-intl/locale-data/en")
external val enLocaleData: reactintl.Locale

@JsModule("react-intl/locale-data/es")
external val esLocaleData: reactintl.Locale

@JsModule("src/app/localization/messages/messages.en-US.json")
external val enMessages: Object

@JsModule("src/app/localization/messages/messages.es-ES.json")
external val esMessages: Object

const val EN_US = "en-US"
const val ES_ES = "es-ES"

interface Locale {
    var id: String
    var localeData: antd.localeprovider.Locale
    var messages: Object
}

fun addLocaleData() {
    addLocaleData(enLocaleData)
    addLocaleData(esLocaleData)
}
