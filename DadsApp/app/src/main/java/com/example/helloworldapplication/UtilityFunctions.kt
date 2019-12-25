package com.example.helloworldapplication

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.TableLayout
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.grid_edit_dialog.*
import kotlinx.android.synthetic.main.grid_layout_dialog.*
import kotlinx.android.synthetic.main.row_layout.view.*

fun showAlertDialog(context: Context, title: String, message: String) {
    AlertDialog.Builder(context)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        .create()
        .show()
}

fun addListenerToGridInfoAddButton(
    context: Context,
    hashMap: HashMap<Char, Int>,
    tableLayout: TableLayout
) {
    val d = Dialog(context)
    d.setContentView(R.layout.grid_layout_dialog)
    d.addValueButton.setOnClickListener {
        //        Log.d(resources.getString(R.string.logtag), "Value add button Clicked")
        val key = if (d.letterTextBox.text.isEmpty()) {
//                TODO("Find a better way to verify all these formats wherever these dialogs are used")
            showAlertDialog(
                context,
                "Invalid Value Format",
                "Please do not leave grid letter field empty."
            )
            return@setOnClickListener
        } else
            d.letterTextBox.text[0].toUpperCase()

        val value = try {
            d.valueTextBox.text.toString().toInt()
        } catch (e: NumberFormatException) {
            showAlertDialog(
                context,
                "Invalid Value Format",
                "Please make sure the format of value given is correct."
            )
            return@setOnClickListener
        }

        if (hashMap[key] != null) {
            showAlertDialog(
                context,
                "Grid Letter already exists!!",
                "Please enter a new grid letter. To edit, press the edit button on row of your choice."
            )
            return@setOnClickListener
        }
        hashMap[key] = value

        d.valueTextBox.setText("")
        d.letterTextBox.setText("")

        // Adding each row to the grid table after each addition
        LayoutInflater.from(context).inflate(R.layout.row_layout, tableLayout, true)
        val row = tableLayout.getChildAt(hashMap.size - 1)
        row.letterTextView.text = key.toString()
        row.valueTextView.text = value.toString()

        Runnable {
            // Onclick listeners to edit and delete button in each row
            row.editGridValueButton.setOnClickListener {
                //                Log.d(resources.getString(R.string.logtag), "Edit value of grid")
                val dialog = Dialog(context)
                dialog.setContentView(R.layout.grid_edit_dialog)
                dialog.editGridValueDialogButton.setOnClickListener {
                    try {
                        hashMap[key] = dialog.gridValueEditTB.text.toString().toInt()
                        row.findViewById<TextView>(R.id.valueTextView).text =
                            dialog.gridValueEditTB.text
                        dialog.dismiss()
                    } catch (e: java.lang.NumberFormatException) {
                        showAlertDialog(
                            context,
                            "Invalid Value Format",
                            "Please make sure the format of value given is correct."
                        )
                    }
                }
                dialog.show()
            }
            row.deleteGridValueButton.setOnClickListener {
                tableLayout.removeView(row)
                hashMap.remove(key)
//                Log.d(resources.getString(R.string.logtag), "Delete value of grid")
                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show()
            }
        }.run()

        Toast.makeText(context, "Added!", Toast.LENGTH_SHORT).show()
    }
    d.show()
}