package app.component.drawevent.countdown

import app.component.Component
import kotlinext.js.js
import presentation.presenter.drawevent.CountdownPresenter
import presentation.view.drawevent.CountdownView
import react.*
import react.dom.div
import react.dom.span
import react.dom.strong
import reactintl.message.formattedMessage
import kotlin.browser.window
import kotlin.js.Date

interface CountdownProps : RProps {
    var date: Date
    var onOver: () -> Unit
}

interface CountdownState : RState {
    var days: Int
    var hours: Int
    var minutes: Int
    var seconds: Int
}

class CountdownComponent : Component<CountdownProps, CountdownState, CountdownView>(), CountdownView {
    private var intervalId: Int? = null

    private fun formatTime(time: Int) = time.toString().let {
        if (it.length < 2) "0$it" else it
    }

    override val presenter = CountdownPresenter(this)

    override fun setTimer() {
        intervalId = window.setInterval({
            presenter.calculateTime(props.date)
        }, 1000)
    }

    override fun updateTime(timeLeft: CountdownPresenter.TimeLeft) {
        if (!timeLeft.isOver) {
            setState {
                days = timeLeft.days
                hours = timeLeft.hours
                minutes = timeLeft.minutes
                seconds = timeLeft.seconds
            }
        } else {
            props.onOver.invoke()
        }
    }

    override fun cancelTimer() {
        window.clearInterval(intervalId!!)
    }

    override fun CountdownState.init() {
        days = 0
        hours = 0
        minutes = 0
        seconds = 0
    }

    override fun componentDidMount() {
        presenter.startCounter()
    }

    override fun componentWillUnmount() {
        presenter.stopCounter()
    }

    override fun RBuilder.render() {
        div("countdown") {
            div("countdown-col") {
                span("countdown-col-element") {
                    strong {
                        +formatTime(state.days)
                    }
                    formattedMessage {
                        attrs {
                            id = "countdown.time.days"
                            values = js { days = state.days }.unsafeCast<Any>()
                        }
                    }
                }
                span("countdown-col-element") {
                    strong {
                        +formatTime(state.hours)
                    }
                    formattedMessage {
                        attrs {
                            id = "countdown.time.hours"
                            values = js { hours = state.hours }.unsafeCast<Any>()
                        }
                    }
                }
            }
            div("countdown-col") {
                span("countdown-col-element") {
                    strong {
                        +formatTime(state.minutes)
                    }
                    formattedMessage {
                        attrs.id = "countdown.time.min"
                    }
                }
                span("countdown-col-element") {
                    strong {
                        +formatTime(state.seconds)
                    }
                    formattedMessage {
                        attrs.id = "countdown.time.sec"
                    }
                }
            }
        }
    }
}

fun RBuilder.countdown(handler: RHandler<CountdownProps>) = child(CountdownComponent::class, handler)
