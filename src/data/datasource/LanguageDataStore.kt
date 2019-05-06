package data.datasource

import domain.model.Language

interface LanguageDataStore {
    fun findAll(): Array<Language>
    fun findById(id: String): Language?
    fun findByLocale(locale: String): Language?
    fun update(language: Language)
}
