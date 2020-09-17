package com.syaiful.ecommercessparepartmotor.ui.activity.checkout

import com.syaiful.ecommercessparepartmotor.model.RequestListModel
import com.syaiful.ecommercessparepartmotor.model.ResponseModel
import com.syaiful.ecommercessparepartmotor.model.cart.Cart
import com.syaiful.ecommercessparepartmotor.model.checkout.Checkout
import com.syaiful.ecommercessparepartmotor.model.payment.Payment
import com.syaiful.ecommercessparepartmotor.service.RetrofitService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class CheckoutActivityPresenter : CheckoutActivityContract.Presenter {

    private val subscriptions = CompositeDisposable()
    private val api: RetrofitService = RetrofitService.create()
    private lateinit var view: CheckoutActivityContract.View

    override fun getAllCart(req: RequestListModel, enableLoading: Boolean) {
        if (enableLoading) {
            view.showProgressGetAllCart(true)
        }
        val subscribe = api.allCart(req.clone())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result: ResponseModel<ArrayList<Cart>>? ->
                if (enableLoading) {
                    view.showProgressGetAllCart(false)
                }
                if (result != null) {

                    if (result.Error != null){
                        view.showErrorGetAllCart(result.Error!!)
                    }
                    if (result.Data != null) {
                        view.onGetAllCart(result.Data!!)
                        if (result.Data!!.isEmpty()){
                            view.onEmptyGetAllCart()
                        }
                    }
                }

            }, { t: Throwable ->
                if (enableLoading) {
                    view.showProgressGetAllCart(false)
                }
                view.showErrorGetAllCart(t.message!!)
            })

        subscriptions.add(subscribe)
    }

    override fun getAllPayment(req: RequestListModel, enableLoading: Boolean) {
        if (enableLoading) {
            view.showProgressGetAllPayment(true)
        }
        val subscribe = api.allPayment(req.clone())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result: ResponseModel<ArrayList<Payment>>? ->
                if (enableLoading) {
                    view.showProgressGetAllPayment(false)
                }
                if (result != null) {

                    if (result.Error != null){
                        view.showErrorGetAllPayment(result.Error!!)
                    }
                    if (result.Data != null) {
                        view.onGetAllPayment(result.Data!!)
                        if (result.Data!!.isEmpty()){
                            view.onEmptyGetAllPayment()
                        }
                    }
                }

            }, { t: Throwable ->
                if (enableLoading) {
                    view.showProgressGetAllPayment(false)
                }
                view.showErrorGetAllPayment(t.message!!)
            })

        subscriptions.add(subscribe)
    }

    override fun getTotal(cart: Cart) {
        val subscribe = api.getTotal(cart.clone())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result: ResponseModel<Int>? ->

                if (result != null) {

                    if (result.Error != null){
                        view.showErrorGetTotal(result.Error!!)
                    }
                    if (result.Data != null) {
                        view.onGetTotal(result.Data!!)
                    }
                }

            }, { t: Throwable ->
                view.showErrorGetTotal(t.message!!)
            })

        subscriptions.add(subscribe)
    }

    override fun checkout(c: Checkout) {
        val subscribe = api.checkout(c.clone())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result: ResponseModel<String>? ->

                if (result != null) {

                    if (result.Error != null){
                        view.showErrorCheckout(result.Error!!)
                    }
                    if (result.Data != null) {
                        view.onCheckoutCompleted(result.Data!!)
                    }
                }

            }, { t: Throwable ->
                view.showErrorCheckout(t.message!!)
            })

        subscriptions.add(subscribe)
    }

    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: CheckoutActivityContract.View) {
        this.view = view
    }

}