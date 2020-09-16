package com.syaiful.ecommercessparepartmotor.ui.activity.carts

import com.syaiful.ecommercessparepartmotor.model.RequestListModel
import com.syaiful.ecommercessparepartmotor.model.ResponseModel
import com.syaiful.ecommercessparepartmotor.model.cart.Cart
import com.syaiful.ecommercessparepartmotor.model.product.Product
import com.syaiful.ecommercessparepartmotor.service.RetrofitService
import com.syaiful.ecommercessparepartmotor.ui.activity.products.ProductsActivityContract
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class CartsActivityPresenter : CartsActivityContract.Presenter {
    private val subscriptions = CompositeDisposable()
    private val api: RetrofitService = RetrofitService.create()
    private lateinit var view: CartsActivityContract.View

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

    override fun updateCart(cart: Cart, enableLoading: Boolean) {
        if (enableLoading) {
            view.showProgressUpdateCart(true)
        }
        val subscribe = api.updateCart(cart.clone())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result: ResponseModel<String>? ->
                if (enableLoading) {
                    view.showProgressUpdateCart(false)
                }
                if (result != null) {

                    if (result.Error != null){
                        view.showErrorUpdateCart(result.Error!!)
                    }
                    if (result.Data != "") {
                        view.onUpdateCart()
                    }
                }

            }, { t: Throwable ->
                if (enableLoading) {
                    view.showProgressUpdateCart(false)
                }
                view.showErrorUpdateCart(t.message!!)
            })

        subscriptions.add(subscribe)
    }

    override fun deleteCart(cart: Cart, enableLoading: Boolean) {
        if (enableLoading) {
            view.showProgressDeleteCart(true)
        }
        val subscribe = api.deleteCart(cart.clone())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result: ResponseModel<String>? ->
                if (enableLoading) {
                    view.showProgressDeleteCart(false)
                }
                if (result != null) {

                    if (result.Error != null){
                        view.showErrorDeleteCart(result.Error!!)
                    }
                    if (result.Data != "") {
                        view.onDeleteCart()
                    }
                }

            }, { t: Throwable ->
                if (enableLoading) {
                    view.showProgressDeleteCart(false)
                }
                view.showErrorDeleteCart(t.message!!)
            })

        subscriptions.add(subscribe)
    }

    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: CartsActivityContract.View) {
        this.view = view
    }

}