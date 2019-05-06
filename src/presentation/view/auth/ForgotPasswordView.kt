package presentation.view.auth

import presentation.view.View

interface ForgotPasswordView : View {
    fun updateEmail(value: String, isValid: Boolean, errorKey: String)
    fun showError(errorKey: String)
    fun showMessage(messageKey: String)
    fun showLoading()
    fun hideLoading()
    fun onSendEmail()
}
