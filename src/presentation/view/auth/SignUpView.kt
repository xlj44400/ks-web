package presentation.view.auth

import domain.model.User
import presentation.view.View

interface SignUpView : View {
    fun updateUsername(value: String, isValid: Boolean, error: String? = null)
    fun updateEmail(value: String, isValid: Boolean, error: String? = null)
    fun updatePassword(value: String, isValid: Boolean, error: String? = null)
    fun updateRepeatPassword(value: String, isValid: Boolean, error: String? = null)
    fun showError(error: String)
    fun showLoading()
    fun hideLoading()
    fun onSignUp(user: User)
}
