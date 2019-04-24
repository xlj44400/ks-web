package presentation.view.auth

import presentation.view.View

interface ForgotPasswordView : View {
    fun updateEmail(value: String, isValid: Boolean, error: String? = null)
    fun showError(error: String)
    fun showMessage(message: String)
    fun showLoading()
    fun hideLoading()
    fun onSendEmail()
}
