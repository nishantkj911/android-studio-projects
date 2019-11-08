package com.example.helloworldapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_card_information.*
import java.util.*

class CardInformationActivity : AppCompatActivity() {

    val SHARED_PREF_STRING: String = "sharedPrefString"
    val SHARED_PREF_ARRAYLIST_STRING: String = "sharedPrefArrayListString"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_information)

        val intent: Intent = intent
        val card: Card? = intent.getSerializableExtra(MyRecyclerViewAdapter.getExtraText()) as? Card

        fillCardInformation(card)
        addOnClickListenersToCopyButtons(card)
    }

    private fun addOnClickListenersToCopyButtons(card: Card?) {
//        TODO("Add OnClickListeners to all copy buttons and copy it to clipboard")

        deleteButton.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                MainActivity.cards?.removeIf { t: Card ->
                    t.cardName.equals(card?.cardName)
                }
            }
            Toast.makeText(this, "Card Deleted", Toast.LENGTH_SHORT).show()
            MainActivity().saveData(this)
            startActivity(Intent(this, MainActivity::class.java))
        }

//        TODO("Make a mechanism to give a message saying 'Are you sure' and then proceed")
//        TODO("Make a undo message and mechanism")
    }

    /*private fun saveData() {
        var sp: SharedPreferences = getSharedPreferences(SHARED_PREF_STRING, MODE_PRIVATE)
        var editor: SharedPreferences.Editor = sp.edit()
        var gson: Gson = Gson()
        var jsonString: String = gson.toJson(MainActivity.cards)

        editor.putString(SHARED_PREF_ARRAYLIST_STRING, jsonString)
        editor.apply()
        Toast.makeText(this, "Data Saved", Toast.LENGTH_SHORT).show()
    }*/

    @SuppressLint("SetTextI18n")
    private fun fillCardInformation(card: Card?) {

        if (card == null) {
            Log.e(packageName + "LogTag", "Card object is null")
        } else {

            // Card Number
            val cardNum: String? = card.cardNumber
            cardNumberTextView.text = cardNum

            // Name on Card
            nameOnCardTextView.text = card.nameOnCard

            // ValidFrom
            textView10.text =
                (card.validFrom?.get(Calendar.MONTH)!! + 1).toString() + "/" + card.validFrom?.get(
                    Calendar.YEAR
                ).toString()

            // ValidThru
            textView12.text =
                (card.validThru?.get(Calendar.MONTH)!! + 1).toString() + "/" + card.validThru?.get(
                    Calendar.YEAR
                ).toString()

            //CVV
            cvvTextView.text = card.cvv.toString()

            // Grid Values
            Log.d(packageName + "LogTag", card.gridValues.toString())
            TODO("Add a table view to see grid information")

//        TODO("Give spacing between every 4 letters of card number")
        }
    }


}
