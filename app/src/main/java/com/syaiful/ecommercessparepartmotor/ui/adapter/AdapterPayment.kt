package com.syaiful.ecommercessparepartmotor.ui.adapter

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.syaiful.ecommercessparepartmotor.R
import com.syaiful.ecommercessparepartmotor.model.payment.Payment

class AdapterPayment : RecyclerView.Adapter<AdapterPayment.Holder> {

    lateinit var context: Context
    lateinit var payments : ArrayList<Payment>
    lateinit var onClick : (Payment,Int) ->Unit

    constructor(context: Context, payments: ArrayList<Payment>, onClick: (Payment, Int) -> Unit) : super() {
        this.context = context
        this.payments = payments
        this.onClick = onClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder((context as Activity).layoutInflater.inflate(R.layout.adapter_payment,parent,false))
    }

    override fun getItemCount(): Int {
        return payments.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = payments.get(position)
        holder.name.text = item.name
        holder.selected.isChecked = item.selected
        holder.layout.setOnClickListener {
            notifyDataSetChanged()
            onClick.invoke(item,position)
        }
    }

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name : TextView = itemView.findViewById(R.id.payment_name_textview)
        val selected : RadioButton = itemView.findViewById(R.id.is_selected_radiobutton)
        val layout : LinearLayout = itemView.findViewById(R.id.layout)
    }


}