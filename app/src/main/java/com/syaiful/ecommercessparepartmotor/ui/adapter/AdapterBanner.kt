package com.syaiful.ecommercessparepartmotor.ui.adapter

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.syaiful.ecommercessparepartmotor.R

class AdapterBanner : RecyclerView.Adapter<AdapterBanner.Holder> {

    lateinit var context: Context
    lateinit var list : ArrayList<String>

    constructor(context: Context, list: ArrayList<String>) : super() {
        this.context = context
        this.list = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder((context as Activity).layoutInflater.inflate(R.layout.adapter_banner,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = list.get(position)

        if (item != "") {
            Picasso.get()
                .load(item)
                .into(holder.image)
        }
    }

    class Holder : RecyclerView.ViewHolder {
        lateinit var image : ImageView
        constructor(itemView: View) : super(itemView) {
            this.image = itemView.findViewById(R.id.image_banner)
        }
    }
}