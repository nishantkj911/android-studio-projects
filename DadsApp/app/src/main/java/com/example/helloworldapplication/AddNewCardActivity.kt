package com.example.helloworldapplication

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_add_new_card.*
import java.util.*
import kotlin.collections.HashMap

class AddNewCardActivity : AppCompatActivity() {

    private var dateSetListenerValidFrom: DatePickerDialog.OnDateSetListener? = null
    private var dateSetListenerValidThru: DatePickerDialog.OnDateSetListener? = null
    private var dateFrom: GregorianCalendar? = null
    private var dateThru: GregorianCalendar? = null
    private var hashMap: HashMap<Char, Int> = HashMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_card)
        setOnClickListenersForValidTextBoxes()
        setOnClickListenerForButtons()


//        TODO("Make a undo message and mechanism")
//        TODO("Show the already entered grid information while entering")
    }

    private fun setOnClickListenerForButtons() {
        submitButton.setOnClickListener {
            val card = verifyIfFormatsAreCorrect()

            //A message saying 'Are you sure' and then proceed
            val alertDialog = AlertDialog.Builder(this)
                .setTitle("Are you sure you want to proceed?")
                .setMessage("Please make sure the information is correct before proceeding.")
                .setPositiveButton("YES") { dialog, _ ->
                    MainActivity.cards!!.add(card!!)
                    saveData(this)
                    Toast.makeText(this, "New Card Successfully Added", Toast.LENGTH_LONG).show()
                    dialog.dismiss()
                    finish()
                }
                .setNegativeButton("NO") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()

            if (card == null)
                return@setOnClickListener
            else
                alertDialog.show()
        }

        addGridInfoButton.setOnClickListener {
            showGridInfoAddDialog()
        }
    }

    private fun verifyIfFormatsAreCorrect(): Card? {
        var card: Card?
        try {
            Log.d(resources.getString(R.string.logtag), "Doing verification")
            card = Card(cardNameTextField.text.toString())
            card.cvv = (cvvTextField.text).toString().toInt()

            card.cardNumber = if (cardNumberTextField.text.isEmpty()) {
                Log.d(resources.getString(R.string.logtag), "throwing Exception")
                throw KotlinNullPointerException()
            } else
                cardNumberTextField.text.toString()

            card.nameOnCard = if (nameOnCardTextField.text.isEmpty()) {
                Log.d(resources.getString(R.string.logtag), "throwing Exception")
                throw KotlinNullPointerException()
            } else
                nameOnCardTextField.text.toString()
        } catch (e: KotlinNullPointerException) {
            AlertDialog.Builder(this)
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                }
                .setTitle("Incomplete Card Details")
                .setMessage("Please make sure all the details are filled.")
                .create()
                .show()

            card = null
        } catch (e: NumberFormatException) {
            AlertDialog.Builder(this)
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                }
                .setTitle("Invalid CVV")
                .setMessage("Please make the format for CVV is proper")
                .create()
                .show()
            card = null
        }

        if (card == null)
            return null

        card.gridValues = hashMap

        // Valid Text views being updated using OnDateSetListeners
        if (dateThru == null || dateFrom == null) {
            card = null
            AlertDialog.Builder(this)
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                }
                .setTitle("Invalid Dates")
                .setMessage("Please Select some date to proceed")
                .create()
                .show()
        } else {
            card.validThru = dateThru
            card.validFrom = dateFrom
        }

        return card
    }

    private fun showGridInfoAddDialog() {
        val d = Dialog(this)
        d.setContentView(R.layout.grid_layout_dialog)

        val valueAB: Button = d.findViewById(R.id.addValueButton)
        val letterTB: EditText = d.findViewById(R.id.letterTextBox)
        val valueTB: EditText = d.findViewById(R.id.valueTextBox)

        valueAB.setOnClickListener {
            Log.d(resources.getString(R.string.logtag), "Value add button Clicked")
            hashMap[letterTB.text[0].toUpperCase()] = valueTB.text.toString().toInt()

            valueTB.setText("")
            letterTB.setText("")

            Toast.makeText(this, "Added!", Toast.LENGTH_SHORT).show()
        }
        d.show()

//        TODO("Show the grid information once added and option to edit it.")
    }

    @SuppressLint("SetTextI18n")
    private fun setOnClickListenersForValidTextBoxes() {

//        TODO("Modify this to just see month and year")
        dateSetListenerValidFrom =
            DatePickerDialog.OnDateSetListener { _, year, month, _ ->
                validFromTextView?.text = """${(month + 1)}/$year"""
                dateFrom = GregorianCalendar(year, month, 1)
            }
        dateSetListenerValidThru =
            DatePickerDialog.OnDateSetListener { _, year, month, _ ->
                validThruTextView?.text = (month + 1).toString() + "/" + year.toString()
                dateThru = GregorianCalendar(year, month, 1)
            }

        validFromTextView.setOnClickListener {
            val dp = DatePickerDialog(
                this,
                dateSetListenerValidFrom,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            )

            dp.show()

        }

        validThruTextView.setOnClickListener {
            val dp = DatePickerDialog(
                this,
                dateSetListenerValidThru,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            )

            dp.show()
        }
    }
}
