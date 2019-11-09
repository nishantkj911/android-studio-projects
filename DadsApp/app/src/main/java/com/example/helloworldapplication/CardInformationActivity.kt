package com.example.helloworldapplication

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_card_information.*
import java.util.*

class CardInformationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.card_information_activity_2)

        val intent: Intent = intent
        val card: Card? = intent.getSerializableExtra(MyRecyclerViewAdapter.getExtraText()) as? Card

        fillCardInformation(card)
        addOnClickListenersToCopyButtons(card)
    }

    private fun addOnClickListenersToCopyButtons(card: Card?) {
        val clipboardManager: ClipboardManager =
            getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        var clip = ClipData.newPlainText("simple Text", "Hello world")
        clipboardManager.setPrimaryClip(clip)

        cardNoCopyButton.setOnClickListener {
            clip = ClipData.newPlainText("Card No Text", cardNumberTextView.text.toString())
            clipboardManager.setPrimaryClip(clip)
            Toast.makeText(this, "Copied!", Toast.LENGTH_SHORT).show()
        }
        nameCopyButton.setOnClickListener {
            clip = ClipData.newPlainText("Name on card Text", nameOnCardTextView.text.toString())
            clipboardManager.setPrimaryClip(clip)
            Toast.makeText(this, "Copied!", Toast.LENGTH_SHORT).show()
        }
        cvvCopyButton.setOnClickListener {
            clip = ClipData.newPlainText("CVV Text", cvvTextView.text.toString())
            clipboardManager.setPrimaryClip(clip)
            Toast.makeText(this, "Copied!", Toast.LENGTH_SHORT).show()
        }

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

    @SuppressLint("SetTextI18n")
    private fun fillCardInformation(card: Card?) {

        if (card == null) {
            Log.e(packageName + "LogTag", "Card object is null")
        } else {

            // Card Name
            val cardName: String? = card.cardName
            cardNameTextView.text = cardName

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
            inflateTableLayoutWithGridInformation(card)


//        TODO("Give spacing between every 4 letters of card number")
        }
    }

    @SuppressLint("SetTextI18n")
    private fun inflateTableLayoutWithGridInformation(card: Card) {
        var i = 0
        card.gridValues!!.toList().sortedBy { (key, _) -> key }.toMap().forEach { (key, value) ->
            //            TODO("find a better way to get the reference to the inserted TableRow")
            LayoutInflater.from(gridTable.context).inflate(R.layout.row_layout, gridTable, true)
            val row = gridTable.getChildAt(i)
            row.findViewById<TextView>(R.id.letterTextView).text = key.toString()
            row.findViewById<TextView>(R.id.valueTextView).text = value.toString()
            i++
        }
    }


}
