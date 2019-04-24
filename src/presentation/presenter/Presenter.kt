package presentation.presenter

import presentation.view.View

abstract class Presenter<out V : View>(val view: V)
