package presentation.view.auth

import domain.User
import presentation.view.View

interface SignInView : View {
    fun updateUsername(value: String, isValid: Boolean, errorKey: String)
    fun updatePassword(value: String, isValid: Boolean, errorKey: String)
    fun showError(errorKey: String)
    fun showLoading()
    fun hideLoading()
    fun onSignIn(user: User)
}
