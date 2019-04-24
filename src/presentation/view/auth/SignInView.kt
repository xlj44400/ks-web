package presentation.view.auth

import domain.model.User
import presentation.view.View

interface SignInView : View {
    fun updateUsername(value: String, isValid: Boolean, error: String? = null)
    fun updatePassword(value: String, isValid: Boolean, error: String? = null)
    fun showError(error: String)
    fun showLoading()
    fun hideLoading()
    fun onSignIn(user: User)
}
