package domain

import kotlin.random.Random

class Subscription {
    constructor(address: String) {
        this.id = Random.nextInt(9999)
        this.address = address
    }

    constructor(id: Number, address: String) : this(address) {
        this.id = id
    }

    var id: Number
        private set

    val address: String

    var isEventEnded: Boolean = false
}
