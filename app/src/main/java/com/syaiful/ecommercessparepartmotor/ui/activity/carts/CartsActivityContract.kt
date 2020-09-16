package com.syaiful.ecommercessparepartmotor.ui.activity.carts

import com.syaiful.ecommercessparepartmotor.base.BaseContract
import com.syaiful.ecommercessparepartmotor.model.RequestListModel
import com.syaiful.ecommercessparepartmotor.model.cart.Cart

class CartsActivityContract {
    interface View: BaseContract.View {

        // add more for request
        fun onEmptyGetAllCart()
        fun onGetAllCart(data : ArrayList<Cart>)
        fun showProgressGetAllCart(show: Boolean)
        fun showErrorGetAllCart(e: String)

        fun onUpdateCart()
        fun showProgressUpdateCart(show: Boolean)
        fun showErrorUpdateCart(e: String)

        fun onDeleteCart()
        fun showProgressDeleteCart(show: Boolean)
        fun showErrorDeleteCart(e: String)

        fun onGetTotal(total : Int)
        fun showErrorGetTotal(e: String)
    }

    interface Presenter: BaseContract.Presenter<View> {

        // add for request
        fun getAllCart(req : RequestListModel, enableLoading :Boolean)
        fun getTotal(cart : Cart)
        fun updateCart(cart : Cart, enableLoading :Boolean)
        fun deleteCart(cart : Cart, enableLoading :Boolean)
    }
}