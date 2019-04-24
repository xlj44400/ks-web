package app.component

import presentation.presenter.Presenter
import presentation.view.View
import react.RComponent
import react.RProps
import react.RState

abstract class Component<P : RProps, S : RState, out V : View> : RComponent<P, S>(), View {
    abstract val presenter: Presenter<V>
}
