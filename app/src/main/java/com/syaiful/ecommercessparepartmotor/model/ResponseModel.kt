package com.syaiful.ecommercessparepartmotor.model

import com.google.gson.annotations.SerializedName


class ResponseModel<T>(
    @SerializedName("data") var Data: T?,
    @SerializedName("error") var Error: String?
)