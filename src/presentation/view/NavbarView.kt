package presentation.view

import domain.model.Language

interface NavbarView : View {
    enum class MenuItem(val key: String) {
        GET_STARTED("get-started"),
        ACCOUNT("account"),
        SIGN_OUT("sign-out"),
        LANGUAGE("language"),
        TELEGRAM("telegram")
    }

    object ModalType {
        const val SIGN_IN = "sign-in"
        const val SIGN_UP = "sign-up"
        const val FORGOT_PASSWORD = "forgot-password"
    }

    fun updateLanguages(languages: Array<Language>)
    fun showModal(type: String)
    fun hideModal()
    fun showSearchInput()
    fun hideSearchInput()
    fun focusSearchInput(element: dynamic)
}
