package com.syaiful.ecommercessparepartmotor.model.carts

import com.google.gson.annotations.SerializedName
import com.syaiful.ecommercessparepartmotor.model.BaseModel

class Carts(
    @SerializedName("id")
    var id:Int = 0,

    @SerializedName("categories_id")
    var categoriesId : Int = 0,

    @SerializedName("products_id")
    var productsId : Int = 0,

    @SerializedName("quantity")
    var quantity : Int = 0,

    @SerializedName("price")
    var price : Int = 0,

    @SerializedName("sub_total")
    var subTotal : Int = 0

) : BaseModel