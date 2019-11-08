package com.example.helloworldapplication

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_add_new_card.*
import java.util.*

class AddNewCardActivity : AppCompatActivity() {

    private var dateSetListenerValidFrom: DatePickerDialog.OnDateSetListener? = null
    private var dateSetListenerValidThru: DatePickerDialog.OnDateSetListener? = null
    private var dateFrom: GregorianCalendar? = null
    private var dateThru: GregorianCalendar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_card)
        setOnClickListenersForValidTextBoxes()
        setOnClickListenerForButtons()
    }

    private fun setOnClickListenerForButtons() {
        submitButton.setOnClickListener {
            val card: Card = Card(cardNameTextField.text.toString())
            card.cardNumber = cardNumberTextField.text?.toString()
            card.nameOnCard = nameOnCardTextField.text?.toString()
            card.cvv = (cvvTextField.text)?.toString()!!.toInt()

            // Valid Text views being updated using OnDateSetListeners
            card.validFrom = dateFrom
            card.validThru = dateThru

            MainActivity.cards!!.add(card)
            MainActivity().saveData()
//            Toast.makeText(this, "New Card Successfully Added", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, MainActivity::class.java))
        }

        addGridInfoButton.setOnClickListener {
            showDialog()
        }
    }

    private fun showDialog() {
        val d: Dialog = Dialog(this)
        d.setContentView(R.layout.grid_view_dialog)
        d.show()

    }

/*    private fun saveData() {
        var sp: SharedPreferences = getSharedPreferences(SHARED_PREF_STRING, MODE_PRIVATE)
        var editor: SharedPreferences.Editor = sp.edit()
        var gson: Gson = Gson()
        var jsonString: String = gson.toJson(MainActivity.cards)

        editor.putString(SHARED_PREF_ARRAYLIST_STRING, jsonString)
        editor.apply()
        Toast.makeText(this, "Data Saved", Toast.LENGTH_SHORT).show()
    }*/

    @SuppressLint("SetTextI18n")
    private fun setOnClickListenersForValidTextBoxes() {

//        TODO("Modify this to just see month and year")
        dateSetListenerValidFrom =
            DatePickerDialog.OnDateSetListener { _, year, month, _ ->
                textView14?.text = """${(month + 1)}/$year"""
                dateFrom = GregorianCalendar(year, month, 1)
            }
        dateSetListenerValidThru =
            DatePickerDialog.OnDateSetListener { datePicker, year, month, dayOfMonth ->
                textView16?.text = (month + 1).toString() + "/" + year.toString()
                dateThru = GregorianCalendar(year, month, 1)
            }

        textView14.setOnClickListener { v ->
            Log.d(packageName + "LogTag", "Valid from focus has been changed")
            var dp: DatePickerDialog = DatePickerDialog(
                this,
                dateSetListenerValidFrom,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            )

            dp.show()
        }

        textView16.setOnClickListener { v ->
            Log.d(packageName + "LogTag", "Valid thru focus has been changed")
            var dp: DatePickerDialog = DatePickerDialog(
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
