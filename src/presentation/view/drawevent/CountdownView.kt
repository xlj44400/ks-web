package presentation.view.drawevent

import presentation.presenter.drawevent.CountdownPresenter
import presentation.view.View

interface CountdownView : View {
    fun setTimer()
    fun updateTime(timeLeft: CountdownPresenter.TimeLeft)
    fun cancelTimer()
}
