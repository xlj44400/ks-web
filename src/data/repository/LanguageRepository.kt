package data.repository

import data.datasource.LanguageDataStore
import domain.model.Language

class LanguageRepository(private val languageDataStore: LanguageDataStore) {
    fun findAll(): Array<Language> {
        return languageDataStore.findAll()
    }

    fun findById(id: String): Language? {
        return languageDataStore.findById(id)
    }

    fun findByLocale(locale: String): Language? {
        return languageDataStore.findByLocale(locale)
    }

    fun update(language: Language) {
        languageDataStore.update(language)
    }
}
