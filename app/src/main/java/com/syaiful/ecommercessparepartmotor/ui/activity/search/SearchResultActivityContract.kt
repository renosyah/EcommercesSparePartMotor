package com.syaiful.ecommercessparepartmotor.ui.activity.search

import com.syaiful.ecommercessparepartmotor.base.BaseContract
import com.syaiful.ecommercessparepartmotor.model.RequestListModel
import com.syaiful.ecommercessparepartmotor.model.cart.Cart
import com.syaiful.ecommercessparepartmotor.model.product.Product

class SearchResultActivityContract {
    interface View: BaseContract.View {

        // add more for request
        fun onEmptyGetAllProduct()
        fun onGetAllProduct(data : ArrayList<Product>)
        fun showProgressGetAllProduct(show: Boolean)
        fun showErrorGetAllProduct(e: String)

        fun onAddToCart()
        fun showProgressAddToCart(show: Boolean)
        fun showErrorAddToCart(e: String)
    }

    interface Presenter: BaseContract.Presenter<View> {

        // add for request
        fun getAllProduct(req : RequestListModel, enableLoading :Boolean)
        fun addToCart(cart : Cart, enableLoading :Boolean)
    }
}