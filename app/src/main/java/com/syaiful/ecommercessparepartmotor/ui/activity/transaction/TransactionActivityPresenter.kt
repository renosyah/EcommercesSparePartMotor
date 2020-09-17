package com.syaiful.ecommercessparepartmotor.ui.activity.transaction

import com.syaiful.ecommercessparepartmotor.model.ResponseModel
import com.syaiful.ecommercessparepartmotor.model.cart.Cart
import com.syaiful.ecommercessparepartmotor.model.payment.Payment
import com.syaiful.ecommercessparepartmotor.model.transaction.Transaction
import com.syaiful.ecommercessparepartmotor.service.RetrofitService
import com.syaiful.ecommercessparepartmotor.ui.activity.carts.CartsActivityContract
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class TransactionActivityPresenter : TransactionActivityContract.Presenter {

    private val subscriptions = CompositeDisposable()
    private val api: RetrofitService = RetrofitService.create()
    private lateinit var view: TransactionActivityContract.View

    override fun getOneTransactionByRef(transaction: Transaction, enableLoading: Boolean) {
        if (enableLoading) {
            view.showProgressGetOneTransactionByRef(true)
        }
        val subscribe = api.oneTransactionByRef(transaction.clone())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result: ResponseModel<Transaction>? ->
                if (enableLoading) {
                    view.showProgressGetOneTransactionByRef(false)
                }
                if (result != null) {

                    if (result.Error != null){
                        view.showErrorGetOneTransactionByRef(result.Error!!)
                    }
                    if (result.Data != null) {
                        view.onGetOneTransactionByRef(result.Data!!)
                    }
                }

            }, { t: Throwable ->
                if (enableLoading) {
                    view.showProgressGetOneTransactionByRef(false)
                }
                view.showErrorGetOneTransactionByRef(t.message!!)
            })

        subscriptions.add(subscribe)
    }

    override fun getOnePayment(payment: Payment, enableLoading: Boolean) {
        if (enableLoading) {
            view.showProgressGetOnePayment(true)
        }
        val subscribe = api.onePayment(payment.clone())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result: ResponseModel<Payment>? ->
                if (enableLoading) {
                    view.showProgressGetOnePayment(false)
                }
                if (result != null) {

                    if (result.Error != null){
                        view.showErrorGetOnePayment(result.Error!!)
                    }
                    if (result.Data != null) {
                        view.onGetOnePayment(result.Data!!)
                    }
                }

            }, { t: Throwable ->
                if (enableLoading) {
                    view.showProgressGetOnePayment(false)
                }
                view.showErrorGetOnePayment(t.message!!)
            })

        subscriptions.add(subscribe)
    }

    override fun subscribe() {
    }

    override fun unsubscribe() {
       subscriptions.clear()
    }

    override fun attach(view: TransactionActivityContract.View) {
        this.view = view
    }

}