package presentation.view.drawevent

import domain.User
import presentation.view.View

interface SubscribeView : View {
    fun updateAddress(value: String, isValid: Boolean, errorKey: String)
    fun showError(errorKey: String)
    fun showLoading()
    fun hideLoading()
}
