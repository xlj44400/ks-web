package data.repository

import data.repository.datasource.LanguageDataStore
import domain.Language

class LanguageRepository(private val languageDataStore: LanguageDataStore) {
    fun findAll(): Array<Language> = languageDataStore.findAll()

    fun findById(id: String): Language? = languageDataStore.findById(id)

    fun findByLocale(locale: String): Language? = languageDataStore.findByLocale(locale)

    fun update(language: Language) = languageDataStore.update(language)
}
