package com.syaiful.ecommercessparepartmotor.model.cart

import com.google.gson.annotations.SerializedName
import com.syaiful.ecommercessparepartmotor.model.BaseModel
import com.syaiful.ecommercessparepartmotor.model.product.Product

class Cart(
    @SerializedName("id")
    var id:Int = 0,

    @SerializedName("customer_id")
    var customerId : Int = 0,

    @SerializedName("product_id")
    var productId : Int = 0,

    @SerializedName("quantity")
    var quantity : Int = 0,

    @SerializedName("price")
    var price : Int = 0,

    @SerializedName("sub_total")
    var subTotal : Int = 0,

    @SerializedName("product")
    var product : Product = Product()

) : BaseModel {

    constructor(customerId : Int) : this() {
        this.customerId = customerId
    }

    fun clone() : Cart {
        return Cart(
            this.id,
            this.customerId,
            this.productId,
            this.quantity,
            this.price,
            this.subTotal,
            Product()
        )
    }
}