package com.syaiful.ecommercessparepartmotor.ui.adapter

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.syaiful.ecommercessparepartmotor.R
import com.syaiful.ecommercessparepartmotor.model.cart.Cart

class CheckoutItemAdapter : RecyclerView.Adapter<CheckoutItemAdapter.Holder> {

    lateinit var context: Context
    lateinit var carts : ArrayList<Cart>

    constructor(context: Context, carts: ArrayList<Cart>) : super() {
        this.context = context
        this.carts = carts
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            (context as Activity).layoutInflater.inflate(R.layout.adapter_checkout_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return carts.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = carts.get(position)

        Picasso.get()
            .load(item.product.imageUrl)
            .resize(100,100)
            .into(holder.image)

        holder.name.text = item.product.name
        holder.quantity.text = item.quantity.toString()
    }

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.image_product_imageview)
        val name: TextView = itemView.findViewById(R.id.name_product_textview)
        val quantity : TextView = itemView.findViewById(R.id.quantity_buy_textview)
    }

}