package app.component.user.account

import antd.avatar.avatar
import antd.form.form
import antd.form.formItem
import antd.grid.col
import antd.grid.row
import antd.icon.icon
import antd.input.input
import app.component.Component
import app.datasource.UserDiskDataStore
import data.repository.UserRepository
import domain.model.User
import presentation.presenter.user.AccountPresenter
import presentation.view.user.AccountView
import react.*
import react.dom.div
import react.dom.span
import reactresponsive.mediaQuery

interface AccountRouteProps : RProps {
    var id: String
}

interface AccountProps : RProps {
    var userId: String
}

interface AccountState : RState {
    var username: String
    var email: String
    var password: String
}

class AccountComponent : Component<AccountProps, AccountState, AccountView>(), AccountView {
    override val presenter = AccountPresenter(this, UserRepository(UserDiskDataStore()))

    override fun updateAccountData(user: User) {
        setState {
            username = user.username
            email = user.email
            password = user.password
        }
    }

    override fun AccountState.init() {
        username = ""
        email = ""
        password = ""
    }

    override fun componentDidMount() {
        presenter.getUser(props.userId)
    }

    override fun RBuilder.render() {
        div("account-container") {
            mediaQuery {
                attrs.minWidth = 768
                childList.add(fun (matches: Boolean): ReactElement {
                    return row {
                        attrs {
                            type = "flex"
                            justify = "center"
                            if (!matches) {
                                prefixCls = "ant-column"
                            }
                            gutter = 32
                        }
                        col {
                            attrs.span = 8
                            div("account-master") {
                                avatar {
                                    attrs {
                                        icon = "user"
                                        size = "large"
                                    }
                                }
                                span { +state.username}
                            }
                        }
                        col {
                            attrs.span = 16
                            div("account-detail") {
                                form {
                                    attrs.layout = "vertical"
                                    formItem {
                                        attrs.label = "Email:"
                                        input {
                                            attrs {
                                                prefix = buildElement {
                                                    icon {
                                                        attrs.type = "mail"
                                                    }
                                                }
                                                value = state.email
                                                disabled = true
                                            }
                                        }
                                    }
                                    formItem {
                                        attrs.label = "Password:"
                                        input {
                                            attrs {
                                                prefix = buildElement {
                                                    icon {
                                                        attrs.type = "lock"
                                                    }
                                                }
                                                type = "password"
                                                value = state.password
                                                disabled = true
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                })
            }
        }
    }
}

fun RBuilder.account(handler: RHandler<AccountProps>) = child(AccountComponent::class, handler)
