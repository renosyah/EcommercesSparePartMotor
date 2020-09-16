package com.syaiful.ecommercessparepartmotor.ui.adapter

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.syaiful.ecommercessparepartmotor.R
import com.syaiful.ecommercessparepartmotor.model.cart.Cart
import com.syaiful.ecommercessparepartmotor.model.product.Product

class CartAdapter : RecyclerView.Adapter<CartAdapter.Holder> {

    lateinit var context: Context
    lateinit var carts : ArrayList<Cart>
    lateinit var onIncrement : (Cart,Int) -> Unit
    lateinit var onDecrement : (Cart,Int) -> Unit

    constructor(context: Context, carts: ArrayList<Cart>, onIncrement: (Cart, Int) -> Unit, onDecrement: (Cart, Int) -> Unit) : super() {
        this.context = context
        this.carts = carts
        this.onIncrement = onIncrement
        this.onDecrement = onDecrement
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder((context as Activity).layoutInflater.inflate(R.layout.adapter_cart,parent,false))
    }

    override fun getItemCount(): Int {
        return carts.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = carts.get(position)

        Picasso.get()
            .load(item.product.imageUrl)
            .resize(300,300)
            .into(holder.image)

        holder.name.text = item.product.name
        holder.description.text = "Rp. ${item.price}"
        holder.quantity.text = item.quantity.toString()

        holder.inc.setOnClickListener {
            onIncrement.invoke(item,position)
        }

        holder.dec.setOnClickListener {
            onDecrement.invoke(item,position)
        }
    }


    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.image_product_imageview)
        val name: TextView = itemView.findViewById(R.id.name_product_textview)
        val description: TextView = itemView.findViewById(R.id.description_product_textview)
        val inc : Button = itemView.findViewById(R.id.inc_button)
        val dec : Button = itemView.findViewById(R.id.dec_button)
        val quantity : TextView = itemView.findViewById(R.id.quantity)
    }


}