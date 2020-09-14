package com.syaiful.ecommercessparepartmotor.model.categories

import com.google.gson.annotations.SerializedName
import com.syaiful.ecommercessparepartmotor.model.BaseModel

class Categories(
    @SerializedName("id")
    var id: Int = 0,

    @SerializedName("name")
    var name : String = "",

    @SerializedName("image_url")
    var imageUrl : String = ""

) : BaseModel