package com.syaiful.ecommercessparepartmotor.ui.activity.loginOrRegister

import com.syaiful.ecommercessparepartmotor.model.ResponseModel
import com.syaiful.ecommercessparepartmotor.model.customer.Customer
import com.syaiful.ecommercessparepartmotor.service.RetrofitService
import com.syaiful.ecommercessparepartmotor.ui.activity.product.ProductActivityContract
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class LoginOrRegisterActivityPresenter : LoginOrRegisterActivityContract.Presenter {

    private val subscriptions = CompositeDisposable()
    private val api: RetrofitService = RetrofitService.create()
    private lateinit var view: LoginOrRegisterActivityContract.View

    override fun login(customer: Customer, enableLoading: Boolean) {
        if (enableLoading) {
            view.showProgressLogin(true)
        }
        val subscribe = api.login(customer.clone())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result: ResponseModel<Customer>? ->
                if (enableLoading) {
                    view.showProgressLogin(false)
                }
                if (result != null) {
                    if (result.Error != null){
                        view.showErrorLogin(result.Error!!)
                    }
                    if (result.Data != null) {
                        view.onLogin(result.Data!!)
                    }
                }

            }, { t: Throwable ->
                if (enableLoading) {
                    view.showProgressLogin(false)
                }
                view.showErrorLogin(t.message!!)
            })

        subscriptions.add(subscribe)
    }

    override fun register(customer: Customer, enableLoading: Boolean) {
        if (enableLoading) {
            view.showProgressRegister(true)
        }
        val subscribe = api.register(customer.clone())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result: ResponseModel<Customer>? ->
                if (enableLoading) {
                    view.showProgressRegister(false)
                }
                if (result != null) {
                    if (result.Error != null){
                        view.showErrorRegister(result.Error!!)
                    }
                    if (result.Data != null) {
                        view.onRegister(result.Data!!)
                    }
                }

            }, { t: Throwable ->
                if (enableLoading) {
                    view.showProgressRegister(false)
                }
                view.showErrorRegister(t.message!!)
            })

        subscriptions.add(subscribe)
    }

    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: LoginOrRegisterActivityContract.View) {
        this.view = view
    }

}