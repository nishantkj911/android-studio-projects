package com.example.helloworldapplication

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

class Card : Serializable {
    var cardName: String? = null
    var pin: Int = 0
    var nameOnCard: String? = null
    var cardNumber: String? = null
    var validFrom: GregorianCalendar? = null
    var validThru: GregorianCalendar? = null
    var cvv: Int = 0
    var gridValues: HashMap<Char, Int>

    constructor(
        cardName: String,
        nameOnCard: String,
        cardNumber: String,
        validFrom: GregorianCalendar,
        validThru: GregorianCalendar,
        cvv: Int,
        pin: Int
    ) {
        this.cardName = cardName
        this.nameOnCard = nameOnCard
        this.cardNumber = cardNumber
        this.validFrom = validFrom
        this.validThru = validThru
        this.cvv = cvv
        gridValues = HashMap()
        this.pin = pin
    }

    constructor(cardName: String) {
        if (cardName == "")
            throw KotlinNullPointerException()

        this.cardName = cardName
        gridValues = HashMap()
    }
}

val SHARED_PREF_STRING: String = "sharedPrefString"
val SHARED_PREF_ARRAYLIST_STRING: String = "sharedPrefArrayListString"

fun loadData(context: Context) {
    val sp: SharedPreferences? = context.getSharedPreferences(
        SHARED_PREF_STRING,
        AppCompatActivity.MODE_PRIVATE
    )
    val jsonString: String? = sp!!.getString(SHARED_PREF_ARRAYLIST_STRING, "")
    if (jsonString.equals("")) {
        MainActivity.cards = ArrayList()
    } else
        MainActivity.cards =
            Gson().fromJson(jsonString, object : TypeToken<ArrayList<Card>>() {}.type)
}

fun saveData(context: Context) {
//         TODO("Find a safer way to store information")
//        TODO("Implement SQLite for storing data")
    val sp: SharedPreferences = context.getSharedPreferences(
        SHARED_PREF_STRING,
        AppCompatActivity.MODE_PRIVATE
    )
    val editor: SharedPreferences.Editor = sp.edit()
    val gson = Gson()
    val jsonString: String = gson.toJson(MainActivity.cards)

    editor.putString(SHARED_PREF_ARRAYLIST_STRING, jsonString)
    editor.apply()
}

fun deleteAllData(context: Context) {
    val sp = context.getSharedPreferences(SHARED_PREF_STRING, AppCompatActivity.MODE_PRIVATE)
    val editor = sp.edit()
    editor.putString(SHARED_PREF_ARRAYLIST_STRING, "")
    editor.apply()
}
