package com.syaiful.ecommercessparepartmotor.model.products

import com.google.gson.annotations.SerializedName
import com.syaiful.ecommercessparepartmotor.model.BaseModel

class Products(
    @SerializedName("id")
    var id:Int = 0,

    @SerializedName("categories_id")
    var categoriesId : Int = 0,

    @SerializedName("name")
    var name : String = "",

    @SerializedName("price")
    var price : Int = 0,

    @SerializedName("stock")
    var stock : Int = 0,

    @SerializedName("rating")
    var rating : Int = 0,

    @SerializedName("image_url")
    var imageUrl : String = ""

) : BaseModel