package com.syaiful.ecommercessparepartmotor.model.product

import com.google.gson.annotations.SerializedName
import com.syaiful.ecommercessparepartmotor.model.BaseModel

class Product(
    @SerializedName("id")
    var id:Int = 0,

    @SerializedName("category_id")
    var categoryId : Int = 0,

    @SerializedName("name")
    var name : String = "",

    @SerializedName("price")
    var price : Int = 0,

    @SerializedName("stock")
    var stock : Int = 0,

    @SerializedName("rating")
    var rating : Int = 0,

    @SerializedName("image_url")
    var imageUrl : String = "",

    @SerializedName("detail")
    var detail : String = ""

) : BaseModel {
    fun clone() : Product {
        return Product(
            this.id,
            this.categoryId,
            this.name,
            this.price,
            this.stock,
            this.rating,
            this.imageUrl,
            this.detail
        )
    }
}