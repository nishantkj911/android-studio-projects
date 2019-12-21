package com.example.helloworldapplication

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_card_information.cardNameTextView
import kotlinx.android.synthetic.main.activity_card_information.cardNoCopyButton
import kotlinx.android.synthetic.main.activity_card_information.cardNumberTextView
import kotlinx.android.synthetic.main.activity_card_information.cvvCopyButton
import kotlinx.android.synthetic.main.activity_card_information.cvvTextView
import kotlinx.android.synthetic.main.activity_card_information.deleteButton
import kotlinx.android.synthetic.main.activity_card_information.gridTable
import kotlinx.android.synthetic.main.activity_card_information.nameCopyButton
import kotlinx.android.synthetic.main.activity_card_information.nameOnCardTextView
import kotlinx.android.synthetic.main.activity_card_information.validFromTextView
import kotlinx.android.synthetic.main.activity_card_information.validThruTextView
import kotlinx.android.synthetic.main.card_information_activity_2.*
import kotlinx.android.synthetic.main.editor_dialog.*
import java.util.*

class CardInformationActivity : AppCompatActivity() {

    private var textViewUnderEditing: TextView? = null
    private var card: Card? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.card_information_activity_2)

//         TODO ("Get index instead of card directly so that we can directly update the card data
        val cardInt: Int = intent.getIntExtra(MyRecyclerViewAdapter.getExtraText(), Int.MAX_VALUE)
        card = MainActivity.cards!![cardInt]

        fillCardInformation()
        addOnClickListenersToAllButtons()
        enableContextMenusForTextViews()
    }

    private fun addOnClickListenersToAllButtons() {

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
            saveData(this)
            finish()
        }

        saveButton.setOnClickListener {
            card!!.cardNumber = cardNumberTextView.text.toString()
            card!!.cardName = cardNameTextView.text.toString()
            card!!.nameOnCard = nameOnCardTextView.text.toString()
            card!!.cvv = cvvTextView.text.toString().toInt()

            saveData(this)
            Toast.makeText(this, "Saved!!", Toast.LENGTH_SHORT).show()
        }

//        TODO("Give an option to edit the card information")
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        textViewUnderEditing = v as TextView
        menuInflater.inflate(R.menu.long_press_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {

        val editingDialog = Dialog(this)
        when (item.itemId) {
            R.id.menuEdit -> {
                editingDialog.setContentView(R.layout.editor_dialog)

                // Changing the reference to the TextView which is actually being edited
                editingDialog.editText1.setText(textViewUnderEditing!!.text)

                // What happens when the OK button is clicked
                editingDialog.button.setOnClickListener {
                    textViewUnderEditing!!.text = editingDialog.editText1.text
                    Log.d(resources.getString(R.string.logtag), "Editing taking place")
                    editingDialog.dismiss()
                }

                // Editing appropriate TextViews' input type and Text to appear
                when (textViewUnderEditing!!) {
                    cardNumberTextView -> {
                        editingDialog.textView10.text = "Enter new Card Number"
                        editingDialog.editText1.inputType = InputType.TYPE_CLASS_NUMBER
                    }
                    cvvTextView -> {
                        editingDialog.textView10.text = "Enter new CVV"
                        editingDialog.editText1.inputType = InputType.TYPE_CLASS_NUMBER
                    }
                    validThruTextView -> {
                        editingDialog.textView10.text = "Select new Valid Thru"
//                        TODO()
                    }
                    validFromTextView -> {
                        editingDialog.textView10.text = "Select new Valid From"
//                        TODO()
                    }
                    nameOnCardTextView -> {
                        editingDialog.textView10.text = "Enter new Name"
                    }
                    else ->
                        editingDialog.textView10.text = "Update details"
                }
                editingDialog.show()
            }
            else ->
                return super.onContextItemSelected(item)
        }

        return super.onContextItemSelected(item)
    }

    @SuppressLint("SetTextI18n")
    private fun fillCardInformation() {

        if (card == null) {
            Log.e(resources.getString(R.string.logtag), "Card object is null")
        } else {

            // Card Name
            val cardName: String? = card!!.cardName
            cardNameTextView.text = cardName

            // Card Number
            val cardNum: String? = card!!.cardNumber
            cardNumberTextView.text = cardNum

            // Name on Card
            nameOnCardTextView.text = card!!.nameOnCard

            // ValidFrom
            validFromTextView.text =
                (card!!.validFrom?.get(Calendar.MONTH)!! + 1).toString() + "/" + card!!.validFrom?.get(
                    Calendar.YEAR
                ).toString()

            // ValidThru
            validThruTextView.text =
                (card!!.validThru?.get(Calendar.MONTH)!! + 1).toString() + "/" + card!!.validThru?.get(
                    Calendar.YEAR
                ).toString()

            //CVV
            cvvTextView.text = card!!.cvv.toString()

            // Grid Values
            Log.d(packageName + "LogTag", card!!.gridValues.toString())
            inflateTableLayoutWithGridInformation()


//        TODO("Give spacing between every 4 letters of card number")
        }
    }

    @SuppressLint("SetTextI18n")
    private fun inflateTableLayoutWithGridInformation() {
        var i = 0
        card!!.gridValues!!.toList().sortedBy { (key, _) -> key }.toMap().forEach { (key, value) ->
            //            TODO("find a better way to get the reference to the inserted TableRow")
            LayoutInflater.from(gridTable.context).inflate(R.layout.row_layout, gridTable, true)
            val row = gridTable.getChildAt(i)
            row.findViewById<TextView>(R.id.letterTextView).text = key.toString()
            row.findViewById<TextView>(R.id.valueTextView).text = value.toString()
            i++
        }
    }

    private fun enableContextMenusForTextViews() {
        registerForContextMenu(cardNameTextView)
        registerForContextMenu(cardNumberTextView)
        registerForContextMenu(nameOnCardTextView)
        registerForContextMenu(cvvTextView)
        registerForContextMenu(validFromTextView)
        registerForContextMenu(validThruTextView)
    }

}
