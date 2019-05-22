package presentation.view.auth

import domain.User
import presentation.view.View

interface SignUpView : View {
    fun updateUsername(value: String, isValid: Boolean, errorKey: String)
    fun updateEmail(value: String, isValid: Boolean, errorKey: String)
    fun updatePassword(value: String, isValid: Boolean, errorKey: String)
    fun updateRepeatPassword(value: String, isValid: Boolean, errorKey: String)
    fun showError(errorKey: String)
    fun showLoading()
    fun hideLoading()
    fun onSignUp(user: User)
}
