package com.example.helloworldapplication

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
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
        Runnable {
            setListenersForDateTextViews()
            setOnClickListenerForButtons()
        }.run()
    }

    private fun setOnClickListenerForButtons() {
        submitButton.setOnClickListener {
            val card = verifyIfFormatsAreCorrect()

            //A message saying 'Are you sure' and then proceed
            val alertDialog = AlertDialog.Builder(this)
                .setTitle("Are you sure you want to proceed?")
                .setMessage("Please make sure the information is correct before proceeding.")
                .setPositiveButton("YES") { dialog, _ ->
                    MainActivity.cards.add(card!!)
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
            addListenerToGridInfoAddButton(this, hashMap, gridTableAdd)
        }
    }

    private fun verifyIfFormatsAreCorrect(): Card? {
        var card: Card?
        try {
            Log.d(resources.getString(R.string.logtag), "Doing verification")
            card = Card(cardNameTextField.text.toString())
            card.cvv = (cvvTextField.text).toString().toInt()

            card.pin = if (pinTextField.text.isEmpty()) {
                0
            } else {
                pinTextField.text.toString().toInt()
            }

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
            showAlertDialog(
                this,
                "Incomplete Card Details",
                "Please make sure all the details are filled."
            )
            card = null
        } catch (e: NumberFormatException) {
            showAlertDialog(this, "Invalid CVV", "Please make the format for CVV is proper")
            card = null
        }

        if (card == null)
            return null

        card.gridValues = hashMap

        // Valid Text views being updated using OnDateSetListeners
        if (dateThru == null || dateFrom == null) {
            card = null
            showAlertDialog(this, "Invalid Dates", "Please Select some date to proceed")
        } else {
            card.validThru = dateThru
            card.validFrom = dateFrom
        }

        return card
    }

    @SuppressLint("SetTextI18n")
    private fun setListenersForDateTextViews() {

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
