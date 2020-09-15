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
import com.syaiful.ecommercessparepartmotor.model.category.Category

class CategoriesAdapter : RecyclerView.Adapter<CategoriesAdapter.Holder> {

    lateinit var context: Context
    lateinit var categories: ArrayList<Category>
    lateinit var onItemClick : (Category, Int) -> Unit

    constructor(context: Context, categories: ArrayList<Category>, onItemClick: (Category, Int) -> Unit) : super() {
        this.context = context
        this.categories = categories
        this.onItemClick = onItemClick
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder((context as Activity).layoutInflater.inflate(R.layout.adapter_categories,parent,false))
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = categories.get(position)

        Picasso.get()
            .load(item.imageUrl)
            .resize(150,150)
            .into(holder.image)

        holder.name.text = item.name

        holder.image.setOnClickListener {
            onItemClick.invoke(item,position)
        }
    }


    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image : ImageView = itemView.findViewById(R.id.image_categories)
        val name : TextView = itemView.findViewById(R.id.name_categories)
    }
}