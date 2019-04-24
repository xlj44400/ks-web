package presentation.view.drawevent

import domain.model.User
import presentation.view.View

interface SubscribeView : View {
    fun updateUsername(value: String, isValid: Boolean, error: String? = null)
    fun updateEmail(value: String, isValid: Boolean, error: String? = null)
    fun showError(error: String)
    fun showLoading()
    fun hideLoading()
    fun onSubscribe(user: User)
}
