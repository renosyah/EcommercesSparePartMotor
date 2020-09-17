package com.syaiful.ecommercessparepartmotor.model.checkout

import com.google.gson.annotations.SerializedName
import com.syaiful.ecommercessparepartmotor.model.BaseModel

class Checkout (
    @SerializedName("customer_id")
    var customerId : Int = 0,

    @SerializedName("payment_id")
    var paymentId : Int = 0,

    @SerializedName("address")
    var address : String = "",

    @SerializedName("total")
    var total : Int = 0

) : BaseModel {

    fun clone() : Checkout {
        return Checkout(
            this.customerId,
            this.paymentId,
            this.address,
            this.total
        )
    }
}