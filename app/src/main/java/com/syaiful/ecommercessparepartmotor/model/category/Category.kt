package com.syaiful.ecommercessparepartmotor.model.category

import com.google.gson.annotations.SerializedName
import com.syaiful.ecommercessparepartmotor.model.BaseModel

class Category(
    @SerializedName("id")
    var id: Int = 0,

    @SerializedName("name")
    var name : String = "",

    @SerializedName("image_url")
    var imageUrl : String = ""

) : BaseModel {
    fun clone() : Category {
        return Category(
            this.id,
            this.name,
            this.imageUrl
        )
    }
}