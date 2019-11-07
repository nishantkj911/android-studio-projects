package com.example.helloworldapplication

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.helloworldapplication.MyRecyclerViewAdapter.ViewHolder

class MyRecyclerViewAdapter(context: Context, cards: ArrayList<Card>) :
    RecyclerView.Adapter<ViewHolder>() {

    var mContext: Context = context
    var mCards: ArrayList<Card> = cards

    companion object {
        var EXTRA_TEXT: String = "projectName.cardExtraText"
        fun getExtraText(): String {
            return EXTRA_TEXT
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.card_layout, parent, false)
        val vh: ViewHolder = ViewHolder(view)
        return vh
    }

    override fun getItemCount(): Int {
        return mCards.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d(R.string.app_name.toString(), "View Holder Bound")

        holder.cardNameLabel.text = mCards[position].cardName
        holder.parentLayout2.setOnClickListener {
            Log.d(mContext.packageName + "LogTag", "Clicked on " + holder.cardNameLabel.text)

            Toast.makeText(mContext, mCards[position].cardName, Toast.LENGTH_SHORT).show()
            val intent: Intent = Intent(mContext, CardInformation::class.java)
            intent.putExtra(MyRecyclerViewAdapter.EXTRA_TEXT, mCards[position])
            mContext.startActivity(intent)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cardNameLabel = itemView.findViewById<TextView>(R.id.cardNameTextView)
        var parentLayout2: ConstraintLayout =
            itemView.findViewById<ConstraintLayout>(R.id.parentLayout2)
    }

}