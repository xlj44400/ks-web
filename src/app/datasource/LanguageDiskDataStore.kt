package app.datasource

import data.datasource.LanguageDataStore
import domain.model.Language
import kotlin.browser.localStorage

class LanguageDiskDataStore : LanguageDataStore {
    override fun findAll(): Array<Language> {
        val languagesJson = localStorage.getItem("languages")

        return if (languagesJson != null) {
            JSON.parse(languagesJson)
        } else {
            val languages = arrayOf(Language.create("en-US", true), Language.create("es-ES"))

            localStorage.setItem("languages", JSON.stringify(languages))

            languages
        }
    }

    override fun findById(id: String): Language? {
        return localStorage.getItem("languages")?.let {
            JSON.parse<Array<Language>>(it).find { l -> l.id == id }
        }
    }

    override fun findByLocale(locale: String): Language? {
        return localStorage.getItem("languages")?.let {
            JSON.parse<Array<Language>>(it).find { l -> l.locale == locale }
        }
    }

    override fun update(language: Language) {
        localStorage.getItem("languages")?.let {
            val languages = mutableListOf<Language>()

            JSON.parse<Array<Language>>(it).forEach { l ->
                if (l.locale == language.locale) {
                    l.isActive = language.isActive
                } else {
                    l.isActive = false
                }

                languages.add(l)
            }

            languages.toTypedArray()
        }?.let {
            localStorage.setItem("languages", JSON.stringify(it))
        }
    }
}
