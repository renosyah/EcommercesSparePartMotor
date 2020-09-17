package com.syaiful.ecommercessparepartmotor.ui.activity.checkout

import com.syaiful.ecommercessparepartmotor.base.BaseContract
import com.syaiful.ecommercessparepartmotor.model.RequestListModel
import com.syaiful.ecommercessparepartmotor.model.cart.Cart
import com.syaiful.ecommercessparepartmotor.model.checkout.Checkout
import com.syaiful.ecommercessparepartmotor.model.payment.Payment

class CheckoutActivityContract {
    interface View: BaseContract.View {

        // add more for request
        fun onEmptyGetAllCart()
        fun onGetAllCart(data : ArrayList<Cart>)
        fun showProgressGetAllCart(show: Boolean)
        fun showErrorGetAllCart(e: String)

        fun onEmptyGetAllPayment()
        fun onGetAllPayment(data : ArrayList<Payment>)
        fun showProgressGetAllPayment(show: Boolean)
        fun showErrorGetAllPayment(e: String)

        fun onGetTotal(total : Int)
        fun showErrorGetTotal(e: String)

        fun onCheckoutCompleted(refId : String)
        fun showProgressCheckout(show: Boolean)
        fun showErrorCheckout(e: String)
    }

    interface Presenter: BaseContract.Presenter<View> {

        // add for request
        fun getAllCart(req : RequestListModel, enableLoading :Boolean)
        fun getAllPayment(req : RequestListModel, enableLoading :Boolean)
        fun getTotal(cart : Cart)
        fun checkout(c :Checkout)
    }
}