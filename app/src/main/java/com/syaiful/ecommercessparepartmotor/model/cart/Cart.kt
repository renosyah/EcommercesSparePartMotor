package com.syaiful.ecommercessparepartmotor.model.cart

import com.google.gson.annotations.SerializedName
import com.syaiful.ecommercessparepartmotor.model.BaseModel

class Cart(
    @SerializedName("id")
    var id:Int = 0,

    @SerializedName("category_id")
    var categoryId : Int = 0,

    @SerializedName("product_id")
    var productId : Int = 0,

    @SerializedName("quantity")
    var quantity : Int = 0,

    @SerializedName("price")
    var price : Int = 0,

    @SerializedName("sub_total")
    var subTotal : Int = 0

) : BaseModel {
    fun clone() : Cart {
        return Cart(
            this.id,
            this.categoryId,
            this.productId,
            this.quantity,
            this.price,
            this.subTotal
        )
    }
}