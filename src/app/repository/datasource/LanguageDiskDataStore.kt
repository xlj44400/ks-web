package app.repository.datasource

import data.repository.datasource.LanguageDataStore
import domain.Language
import kotlin.browser.localStorage

class LanguageDiskDataStore : LanguageDataStore {
    override fun findAll(): Array<Language> = localStorage.getItem("languages")?.let {
        JSON.parse<Array<Language>>(it)
    } ?: let {
        val languages = arrayOf(Language.create("en-US", true), Language.create("es-ES"))

        localStorage.setItem("languages", JSON.stringify(languages))

        languages
    }

    override fun findById(id: String): Language? = localStorage.getItem("languages")?.let {
        JSON.parse<Array<Language>>(it).find { l -> l.id == id }
    }

    override fun findByLocale(locale: String): Language? = localStorage.getItem("languages")?.let {
        JSON.parse<Array<Language>>(it).find { l -> l.locale == locale }
    }

    override fun update(language: Language) = localStorage.getItem("languages")?.let {
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

        localStorage.setItem("languages", JSON.stringify(languages))
    }!!
}
