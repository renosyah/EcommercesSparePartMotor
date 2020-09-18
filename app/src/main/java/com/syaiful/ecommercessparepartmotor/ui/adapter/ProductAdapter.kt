package com.syaiful.ecommercessparepartmotor.ui.adapter

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.syaiful.ecommercessparepartmotor.R
import com.syaiful.ecommercessparepartmotor.model.product.Product
import com.syaiful.ecommercessparepartmotor.util.Formatter.Companion.decimalFormat

class ProductAdapter : RecyclerView.Adapter<ProductAdapter.Holder> {

    private lateinit var context: Context
    private lateinit var products : ArrayList<Product>
    private lateinit var onClick : (Product,Int) -> Unit

    constructor(context: Context, products: ArrayList<Product>, onClick: (Product, Int) -> Unit) : super() {
        this.context = context
        this.products = products
        this.onClick = onClick
    }

    private lateinit var onCartClick : (Product,Int) -> Unit

    fun setOnCartClick(onCartClick : (Product,Int) -> Unit){
        this.onCartClick = onCartClick
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder((context as Activity).layoutInflater.inflate(R.layout.adapter_product,parent,false))
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = products.get(position)

        Picasso.get()
            .load(item.imageUrl)
            .resize(300,300)
            .into(holder.image)

        holder.name.text = item.name
        holder.description.text = "Rp. ${decimalFormat(item.price)}\n${context.getString(R.string.stock)} : ${item.stock}"
        holder.layout.setOnClickListener {
            onClick.invoke(item,position)
        }
        holder.addToCart.setOnClickListener {
            onCartClick.invoke(item,position)
        }
    }


    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image : ImageView = itemView.findViewById(R.id.image_product_imageview)
        val name : TextView = itemView.findViewById(R.id.name_product_textview)
        val description : TextView = itemView.findViewById(R.id.description_product_textview)
        val addToCart : ImageView = itemView.findViewById(R.id.add_to_card_imageview)
        val layout : LinearLayout = itemView.findViewById(R.id.layout)
    }
}