package com.syaiful.ecommercessparepartmotor.ui.activity.home

import com.syaiful.ecommercessparepartmotor.model.RequestListModel
import com.syaiful.ecommercessparepartmotor.model.ResponseModel
import com.syaiful.ecommercessparepartmotor.model.category.Category
import com.syaiful.ecommercessparepartmotor.service.RetrofitService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class HomeActivityPresenter : HomeActivityContract.Presenter {

    private val subscriptions = CompositeDisposable()
    private val api: RetrofitService = RetrofitService.create()
    private lateinit var view: HomeActivityContract.View

    override fun getAllCategory(req : RequestListModel, enableLoading :Boolean) {
        if (enableLoading) {
            view.showProgressGetAllCategory(true)
        }
        val subscribe = api.allCategory(req.clone())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result: ResponseModel<ArrayList<Category>>? ->
                if (enableLoading) {
                    view.showProgressGetAllCategory(false)
                }
                if (result != null) {

                    if (result.Error != null){
                        view.showErrorGetAllCategory(result.Error!!)
                    }
                    if (result.Data != null) {
                        view.onGetAllCategory(result.Data!!)
                        if (result.Data!!.isEmpty()){
                            view.onEmptyGetAllCategory()
                        }
                    }
                }

            }, { t: Throwable ->
                if (enableLoading) {
                    view.showProgressGetAllCategory(false)
                }
                view.showErrorGetAllCategory(t.message!!)
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