package com.syaiful.ecommercessparepartmotor.model.validateTransaction

import com.google.gson.annotations.SerializedName
import com.syaiful.ecommercessparepartmotor.model.BaseModel

class ValidateTransaction(
    @SerializedName("id")
    var id : Int = 0,

    @SerializedName("transaction_id")
    var transactionId : Int = 0,

    @SerializedName("image_url")
    var imageId : String = ""

) :BaseModel {
    fun clone() : ValidateTransaction {
        return ValidateTransaction(
            this.id,
            this.transactionId,
            this.imageId
        )
    }
}