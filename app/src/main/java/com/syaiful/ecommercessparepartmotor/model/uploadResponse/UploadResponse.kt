package com.syaiful.ecommercessparepartmotor.model.uploadResponse

import com.google.gson.annotations.SerializedName
import com.syaiful.ecommercessparepartmotor.model.BaseModel

class UploadResponse(
    @SerializedName("file_name")
    var fileName : String = "",

    @SerializedName("url")
    var url  : String = ""

) : BaseModel {
    fun clone() : UploadResponse {
        return UploadResponse(
            this.fileName,
            this.url
        )
    }
}