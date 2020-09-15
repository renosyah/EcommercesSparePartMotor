package com.syaiful.ecommercessparepartmotor.ui.activity.products

import com.syaiful.ecommercessparepartmotor.model.RequestListModel
import com.syaiful.ecommercessparepartmotor.model.ResponseModel
import com.syaiful.ecommercessparepartmotor.model.category.Category
import com.syaiful.ecommercessparepartmotor.model.product.Product
import com.syaiful.ecommercessparepartmotor.service.RetrofitService
import com.syaiful.ecommercessparepartmotor.ui.activity.home.HomeActivityContract
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ProductsActivityPresenter : ProductsActivityContract.Presenter {

    private val subscriptions = CompositeDisposable()
    private val api: RetrofitService = RetrofitService.create()
    private lateinit var view: ProductsActivityContract.View

    override fun getAllProduct(req: RequestListModel, enableLoading: Boolean) {
        if (enableLoading) {
            view.showProgressGetAllProduct(true)
        }
        val subscribe = api.allProduct(req.clone())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result: ResponseModel<ArrayList<Product>>? ->
                if (enableLoading) {
                    view.showProgressGetAllProduct(false)
                }
                if (result != null) {

                    if (result.Error != null){
                        view.showErrorGetAllProduct(result.Error!!)
                    }
                    if (result.Data != null) {
                        view.onGetAllProduct(result.Data!!)
                        if (result.Data!!.isEmpty()){
                            view.onEmptyGetAllProduct()
                        }
                    }
                }

            }, { t: Throwable ->
                if (enableLoading) {
                    view.showProgressGetAllProduct(false)
                }
                view.showErrorGetAllProduct(t.message!!)
            })

        subscriptions.add(subscribe)
    }

    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: ProductsActivityContract.View) {
       this.view = view
    }

}