package com.example.helloworldapplication

import java.io.Serializable
import java.util.*

class Card : Serializable {
    var cardName: String? = null
    var nameOnCard: String? = null
    var cardNumber: String? = null
    var validFrom: GregorianCalendar? = null
    var validThru: GregorianCalendar? = null
    var cvv: Int = 0
    var gridValues: HashMap<Char, Int>? = null

    constructor(
        cardName: String,
        nameOnCard: String,
        cardNumber: String,
        validFrom: GregorianCalendar,
        validThru: GregorianCalendar,
        cvv: Int
    ) {
        this.cardName = cardName
        this.nameOnCard = nameOnCard
        this.cardNumber = cardNumber
        this.validFrom = validFrom
        this.validThru = validThru
        this.cvv = cvv
        gridValues = HashMap()
    }

    constructor(cardName: String) {
        this.cardName = cardName
        gridValues = HashMap()
    }
}
