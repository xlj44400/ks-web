package presentation.view.drawevent

import presentation.view.View

interface DrawEventView : View {
    fun showSubscribeModal()
    fun hideSubscribeModal()
    fun onEventEnded()
    fun showMessage(messageKey: String)
}
