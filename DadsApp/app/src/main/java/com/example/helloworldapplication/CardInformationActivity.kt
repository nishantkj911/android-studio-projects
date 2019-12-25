package com.example.helloworldapplication

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
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
import kotlinx.android.synthetic.main.card_information_activity_2.*
import kotlinx.android.synthetic.main.editor_dialog.*
import kotlinx.android.synthetic.main.grid_edit_dialog.*
import kotlinx.android.synthetic.main.row_layout.view.*
import java.util.*

class CardInformationActivity : AppCompatActivity() {

    private var textViewUnderEditing: TextView? = null
    private var card: Card? = null
    private var didChangesTakePlace = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.card_information_activity_2)

        val cardInt: Int = intent.getIntExtra(MyRecyclerViewAdapter.getExtraText(), Int.MAX_VALUE)
        card = MainActivity.cards[cardInt]

        fillCardInformation()
        Runnable {
            addOnClickListenersToAllButtons()
        }.run()
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

        pinCopyButton.setOnClickListener {
            clipboardManager.setPrimaryClip(
                ClipData.newPlainText("PIN Text", pinTextView.text.toString())
            )
            Toast.makeText(this, "Copied!", Toast.LENGTH_SHORT).show()
        }

        addMoreGridInfo.setOnClickListener {
            addListenerToGridInfoAddButton(this, card!!.gridValues, gridTable)
        }

//        TODO("when in password mode, make the length of the hidden text unknown")
        cvvViewButton.setOnClickListener {
            if (cvvTextView.inputType and InputType.TYPE_NUMBER_VARIATION_PASSWORD > 0) {
                cvvTextView.inputType =
                    cvvTextView.inputType xor InputType.TYPE_NUMBER_VARIATION_PASSWORD
                cvvViewButton.setImageResource(R.drawable.ic_visibility_off_grey_900_24dp)
            } else {
                cvvTextView.inputType =
                    cvvTextView.inputType xor InputType.TYPE_NUMBER_VARIATION_PASSWORD
                cvvViewButton.setImageResource(R.drawable.ic_visibility_grey_900_24dp)
            }
        }

        pinViewButton.setOnClickListener {
            if (pinTextView.inputType and InputType.TYPE_NUMBER_VARIATION_PASSWORD > 0) {
                pinTextView.inputType =
                    pinTextView.inputType xor InputType.TYPE_NUMBER_VARIATION_PASSWORD
                pinViewButton.setImageResource(R.drawable.ic_visibility_off_grey_900_24dp)
            } else {
                pinTextView.inputType =
                    pinTextView.inputType xor InputType.TYPE_NUMBER_VARIATION_PASSWORD
                pinViewButton.setImageResource(R.drawable.ic_visibility_grey_900_24dp)
            }
        }

        deleteButton.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                MainActivity.cards.removeIf { t: Card ->
                    t.cardName.equals(card?.cardName)
                }
            }
            Toast.makeText(this, "Card Deleted", Toast.LENGTH_SHORT).show()
            saveData(this)
            // TODO("Add a way to undo this")
            finish()
        }

        saveButton.setOnClickListener {
            card!!.cardNumber = cardNumberTextView.text.toString()
            card!!.cardName = cardNameTextView.text.toString()
            card!!.nameOnCard = nameOnCardTextView.text.toString()
            card!!.cvv = cvvTextView.text.toString().toInt()
            card!!.pin = pinTextView.text.toString().toInt()

            saveData(this)
            didChangesTakePlace = false
            Toast.makeText(this, "Saved!!", Toast.LENGTH_SHORT).show()
        }
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

    override fun onBackPressed() {
        if (didChangesTakePlace) {
            AlertDialog.Builder(this)
                .setTitle("Are you sure you want to discard changes?")
                .setMessage("There are some unsaved changes detected in the card. Do you want to proceed further without saving?")
                .setPositiveButton("YES") { dialog, _ ->
                    dialog.dismiss()
                    super.onBackPressed()
                }
                .setNegativeButton("NO") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
        } else {
            super.onBackPressed()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onContextItemSelected(item: MenuItem): Boolean {

        var editingDialog: Dialog? = Dialog(this)
        when (item.itemId) {
            R.id.menuEdit -> {
                if (!(textViewUnderEditing == validFromTextView || textViewUnderEditing == validThruTextView)) {
                    // dialog for cardNo, Card name, name on card, cvv
                    editingDialog!!.setContentView(R.layout.editor_dialog)

                    // Changing the reference to the TextView which is actually being edited
                    editingDialog.editText1.setText(textViewUnderEditing!!.text)

                    // What happens when the OK button is clicked
                    editingDialog.button.setOnClickListener {
                        textViewUnderEditing!!.text = editingDialog!!.editText1.text
                        didChangesTakePlace = true
                        Log.d(resources.getString(R.string.logtag), "Editing taking place")
                        editingDialog!!.dismiss()
                    }
                } else {
                    editingDialog = null
                    DatePickerDialog(
                        this, DatePickerDialog.OnDateSetListener { _, year, month, day ->
                            textViewUnderEditing?.text = """${(month + 1)}/$year"""
                            if (textViewUnderEditing == validFromTextView) {
                                card!!.validFrom = GregorianCalendar(year, month, day)
                            } else {
                                card!!.validThru = GregorianCalendar(year, month, day)
                            }

                            didChangesTakePlace = true
                        },
                        Calendar.getInstance().get(Calendar.YEAR),
                        Calendar.getInstance().get(Calendar.MONTH),
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                    )
                        .show()
                    Log.d(resources.getString(R.string.logtag), "Edited the date")
                }

                if (editingDialog == null)
                    return super.onContextItemSelected(item)

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
                    nameOnCardTextView -> {
                        editingDialog.textView10.text = "Enter new Name"
                    }
                    pinTextView -> {
                        editingDialog.textView10.text = "Enter new PIN"
                        editingDialog.editText1.inputType = InputType.TYPE_CLASS_NUMBER
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
//            TODO("Add password field for CVV and a view button to view it")

            // Grid Values
            Log.d(packageName + "LogTag", card!!.gridValues.toString())
            Runnable {
                Log.d(
                    resources.getString(R.string.logtag),
                    "running grid value thing in a separate thread"
                )
                inflateTableLayoutWithGridInformation()
            }.run()

            // PIN
            pinTextView.text = card!!.pin.toString()
            // TODO("if it is 0000, then it'd show 0. So, change that")


//        TODO("Give spacing between every 4 letters of card number")
        }
    }

    @SuppressLint("SetTextI18n")
    private fun inflateTableLayoutWithGridInformation() {
        var i = 0
        card!!.gridValues.toList().sortedBy { (key, _) -> key }.toMap().forEach { (key, value) ->
            //            TODO("find a better way to get the reference to the inserted TableRow")
            LayoutInflater.from(gridTable.context).inflate(R.layout.row_layout, gridTable, true)
            val row = gridTable.getChildAt(i)

            // key and values to be displayed in each row
            row.findViewById<TextView>(R.id.letterTextView).text = key.toString()
            row.findViewById<TextView>(R.id.valueTextView).text = value.toString()

            // OnClickListeners of edit button and delete button in each row
            row.editGridValueButton.setOnClickListener {
                Log.d(resources.getString(R.string.logtag), "Edit value of grid")
                val dialog = Dialog(this)
                dialog.setContentView(R.layout.grid_edit_dialog)
                dialog.editGridValueDialogButton.setOnClickListener {
                    try {
                        card!!.gridValues[key] = dialog.gridValueEditTB.text.toString().toInt()
                        row.valueTextView.text = dialog.gridValueEditTB.text
                        didChangesTakePlace = true
                        dialog.dismiss()
                    } catch (e: NumberFormatException) {
                        showAlertDialog(
                            this,
                            "Invalid Value Format",
                            "Please make sure the format of value given is correct."
                        )
                    }
                }
                dialog.show()
            }
            row.deleteGridValueButton.setOnClickListener {
                gridTable.removeView(row)
//                TODO("Add a menu to undo this feature")
                didChangesTakePlace = true
                card!!.gridValues.remove(key)
                Log.d(resources.getString(R.string.logtag), "Delete value of grid")
                Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show()
            }
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
        registerForContextMenu(pinTextView)
    }

}
