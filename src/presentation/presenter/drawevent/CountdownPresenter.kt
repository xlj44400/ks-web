package presentation.presenter.drawevent

import presentation.presenter.Presenter
import presentation.view.drawevent.CountdownView
import kotlin.js.Date
import kotlin.math.floor

class CountdownPresenter(view: CountdownView) : Presenter<CountdownView>(view) {
    fun startCounter() {
        view.setTimer()
    }

    fun calculateTime(date: Date) {
        val timeLeft = TimeLeft.calculate(Date(), date)

        view.updateTime(timeLeft)
    }

    fun stopCounter() {
        view.cancelTimer()
    }

    object TimeLeft {
        private object Limit {
            const val YEAR = 365.25 * 24 * 60 * 60
            const val DAY = 24 * 60 * 60
            const val HOUR = 60 * 60
            const val MINUTE = 60
            const val SECOND = 1
        }

        var years = 0
            private set
        var days = 0
            private set
        var hours = 0
            private set
        var minutes = 0
            private set
        var seconds = 0
            private set

        var isOver = false
            private set

        fun calculate(starDate: Date, endDate: Date) = if (!isOver) {
            var timeDiff = (endDate.getTime() - starDate.getTime()) / 1000

            if (timeDiff <= 0) {
                isOver = true
            } else {
                if (timeDiff >= Limit.YEAR) {
                    years = floor(timeDiff / Limit.YEAR).toInt()
                    timeDiff -= years * Limit.YEAR
                }

                if (timeDiff >= Limit.DAY) {
                    days = floor(timeDiff / Limit.DAY).toInt()
                    timeDiff -= days * Limit.DAY
                }

                if (timeDiff >= Limit.HOUR) { // 60 * 60
                    hours = floor(timeDiff / Limit.HOUR).toInt()
                    timeDiff -= hours * Limit.HOUR
                }

                if (timeDiff >= Limit.MINUTE) {
                    minutes = floor(timeDiff / Limit.MINUTE).toInt()
                    timeDiff -= minutes * Limit.MINUTE
                }

                seconds = timeDiff.toInt() * Limit.SECOND
            }

            this
        } else this
    }
}
