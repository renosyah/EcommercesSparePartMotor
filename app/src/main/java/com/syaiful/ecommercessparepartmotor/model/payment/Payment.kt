package com.syaiful.ecommercessparepartmotor.model.payment

import com.google.gson.annotations.SerializedName
import com.syaiful.ecommercessparepartmotor.model.BaseModel

class Payment(
    @SerializedName("id")
     var id:Int = 0,

     @SerializedName("name")
     var name : String = "",

     @SerializedName("detail")
     var detail : String = "",

    var selected : Boolean = false

) : BaseModel {

    fun clone() : Payment {
        return Payment(
            this.id,
            this.name,
            this.detail
        )
    }
}