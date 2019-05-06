package presentation.presenter.navbar

import data.repository.LanguageRepository
import presentation.presenter.Presenter
import presentation.view.NavbarView

class NavbarPresenter(view: NavbarView, private val languageRepository: LanguageRepository) : Presenter<NavbarView>(view) {
    fun loadLanguages() {
        val languages = languageRepository.findAll()

        view.updateLanguages(languages)
    }
}
