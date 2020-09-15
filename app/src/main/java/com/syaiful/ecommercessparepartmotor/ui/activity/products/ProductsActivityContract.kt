package com.syaiful.ecommercessparepartmotor.ui.activity.products

import com.syaiful.ecommercessparepartmotor.base.BaseContract
import com.syaiful.ecommercessparepartmotor.model.RequestListModel
import com.syaiful.ecommercessparepartmotor.model.product.Product

class ProductsActivityContract {
    interface View: BaseContract.View {

        // add more for request
        fun onEmptyGetAllProduct()
        fun onGetAllProduct(data : ArrayList<Product>)
        fun showProgressGetAllProduct(show: Boolean)
        fun showErrorGetAllProduct(e: String)
    }

    interface Presenter: BaseContract.Presenter<View> {

        // add for request
        fun getAllProduct(req : RequestListModel, enableLoading :Boolean)
    }
}