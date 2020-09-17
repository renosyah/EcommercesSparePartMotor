package com.syaiful.ecommercessparepartmotor.ui.activity.transaction

import com.syaiful.ecommercessparepartmotor.base.BaseContract
import com.syaiful.ecommercessparepartmotor.model.payment.Payment
import com.syaiful.ecommercessparepartmotor.model.transaction.Transaction

class TransactionActivityContract {
    interface View: BaseContract.View {

        // add more for request
        fun onGetOneTransactionByRef(transaction: Transaction)
        fun showProgressGetOneTransactionByRef(show: Boolean)
        fun showErrorGetOneTransactionByRef(e: String)

        fun onGetOnePayment(payment: Payment)
        fun showProgressGetOnePayment(show: Boolean)
        fun showErrorGetOnePayment(e: String)
    }

    interface Presenter: BaseContract.Presenter<View> {

        // add for request
        fun getOneTransactionByRef(transaction: Transaction, enableLoading :Boolean)
        fun getOnePayment(payment: Payment, enableLoading :Boolean)
    }
}