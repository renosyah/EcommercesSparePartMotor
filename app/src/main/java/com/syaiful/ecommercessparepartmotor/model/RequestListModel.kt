package com.syaiful.ecommercessparepartmotor.model

import com.google.gson.annotations.SerializedName

class RequestListModel(
    @SerializedName("transaction_id")
    var transactionId: String = "",

    @SerializedName("customer_id")
    var customerId: String = "",

    @SerializedName("category_id")
    var categoryId: String = "",

    @SerializedName("search_by")
    var searchBy: String = "",

    @SerializedName("search_value")
    var searchValue: String = "",

    @SerializedName("order_by")
    var orderBy: String = "",

    @SerializedName("order_dir")
    var orderDir: String = "",

    @SerializedName("offset")
    var offset: Int = 0,

    @SerializedName("limit")
    var limit: Int = 0

) : BaseModel {
    fun clone() : RequestListModel {
        return RequestListModel(
            this.transactionId,
            this.customerId,
            this.categoryId,
            this.searchBy,
            this.searchValue,
            this.orderBy,
            this.orderDir,
            this.offset,
            this.limit
        )
    }
}

