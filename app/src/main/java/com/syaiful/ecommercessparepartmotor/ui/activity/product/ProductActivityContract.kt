package com.syaiful.ecommercessparepartmotor.ui.activity.product

import com.syaiful.ecommercessparepartmotor.base.BaseContract
import com.syaiful.ecommercessparepartmotor.model.cart.Cart

class ProductActivityContract {
    interface View: BaseContract.View {

        // add more for request
        fun onAddToCart()
        fun showProgressAddToCart(show: Boolean)
        fun showErrorAddToCart(e: String)
    }

    interface Presenter: BaseContract.Presenter<View> {

        // add for request
        fun getAddToCart(cart : Cart, enableLoading :Boolean)
    }
}