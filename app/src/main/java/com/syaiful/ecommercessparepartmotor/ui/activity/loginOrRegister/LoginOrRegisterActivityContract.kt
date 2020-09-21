package com.syaiful.ecommercessparepartmotor.ui.activity.loginOrRegister

import com.syaiful.ecommercessparepartmotor.base.BaseContract
import com.syaiful.ecommercessparepartmotor.model.customer.Customer

class LoginOrRegisterActivityContract {
    interface View: BaseContract.View {

        // add more for request
        fun onLogin(customer : Customer)
        fun showProgressLogin(show: Boolean)
        fun showErrorLogin(e: String)

        fun onRegister(customer : Customer)
        fun showProgressRegister(show: Boolean)
        fun showErrorRegister(e: String)
    }

    interface Presenter: BaseContract.Presenter<View> {

        // add for request
        fun login(customer : Customer, enableLoading :Boolean)
        fun register(customer : Customer, enableLoading :Boolean)
    }
}