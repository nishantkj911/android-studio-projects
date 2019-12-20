package com.example.helloworldapplication

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.Serializable
import java.util.*

class Card : Serializable {
    var id: Int = Int.MAX_VALUE
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

        try {
            this.id = MainActivity.cards!!.size
        } catch (exc: KotlinNullPointerException) {
            println("Cards reference not found")
        }

    }

    constructor(cardName: String) {
        this.cardName = cardName
        try {
            this.id = MainActivity.cards!!.size
        } catch (exc: KotlinNullPointerException) {
            println("Cards reference not found")
        }
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

    MainActivity.cards = Gson().fromJson(jsonString, object : TypeToken<ArrayList<Card>>() {}.type)
    if (MainActivity.cards == null)
        MainActivity.cards = ArrayList()
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
    Toast.makeText(context, "Data Saved", Toast.LENGTH_SHORT).show()
}
