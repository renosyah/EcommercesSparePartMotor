package com.syaiful.ecommercessparepartmotor.ui.activity.home

import com.syaiful.ecommercessparepartmotor.model.RequestListModel
import com.syaiful.ecommercessparepartmotor.model.ResponseModel
import com.syaiful.ecommercessparepartmotor.model.categories.Categories
import com.syaiful.ecommercessparepartmotor.service.RetrofitService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class HomeActivityPresenter : HomeActivityContract.Presenter {

    private val subscriptions = CompositeDisposable()
    private val api: RetrofitService = RetrofitService.create()
    private lateinit var view: HomeActivityContract.View

    override fun getAllCategories(req : RequestListModel, enableLoading :Boolean) {
        if (enableLoading) {
            view.showProgressGetAllCategories(true)
        }
        val subscribe = api.allCategories(req.clone())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result: ResponseModel<ArrayList<Categories>>? ->
                if (enableLoading) {
                    view.showProgressGetAllCategories(false)
                }
                if (result != null) {

                    if (result.Error != null){
                        view.showErrorGetAllCategories(result.Error!!)
                    }
                    if (result.Data != null) {
                        view.onGetAllCategories(result.Data!!)
                        if (result.Data!!.isEmpty()){
                            view.onEmptyCategories()
                        }
                    }
                }

            }, { t: Throwable ->
                if (enableLoading) {
                    view.showProgressGetAllCategories(false)
                }
                view.showErrorGetAllCategories(t.message!!)
            })

        subscriptions.add(subscribe)
    }

    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: HomeActivityContract.View) {
        this.view = view
    }
}