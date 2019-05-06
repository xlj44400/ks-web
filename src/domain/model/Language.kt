package domain.model

import kotlin.random.Random

class Language {
    companion object {
        fun create(locale: String, isActive: Boolean = false): Language {
            return Language().apply {
                this.id = "$locale$${Random.nextInt(0, 9999)}"
                this.locale = locale
                this.isActive = isActive
            }
        }
    }

    var id = ""
    var locale = ""
    var isActive = false
}
