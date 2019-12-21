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
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.helloworldapplication.MyRecyclerViewAdapter.ViewHolder

class MyRecyclerViewAdapter(context: Context, cards: ArrayList<Card>) :
    RecyclerView.Adapter<ViewHolder>() {

    private var mContext: Context = context
    private var mCards: ArrayList<Card> = cards

    companion object {
        var EXTRA_TEXT: String = "projectName.cardExtraText"
        fun getExtraText(): String {
            return EXTRA_TEXT
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.card_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mCards.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d(R.string.app_name.toString(), "View Holder Bound")

        holder.cardNameLabel.text = mCards[position].cardName
        holder.parentLayout2.setOnClickListener {
            Log.d(
                mContext.resources.getString(R.string.logtag),
                "Clicked on " + holder.cardNameLabel.text
            )

            Toast.makeText(mContext, mCards[position].cardName, Toast.LENGTH_SHORT).show()
            val intent = Intent(mContext, CardInformationActivity::class.java)
//            intent.putExtra(EXTRA_TEXT, mCards[position])
            intent.putExtra(EXTRA_TEXT, position) // using position to refer to the card.
            mContext.startActivity(intent)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cardNameLabel = itemView.findViewById(R.id.cardNameTextView) as TextView
        var parentLayout2: CardView =
            itemView.findViewById(R.id.parent_layout)
    }

}