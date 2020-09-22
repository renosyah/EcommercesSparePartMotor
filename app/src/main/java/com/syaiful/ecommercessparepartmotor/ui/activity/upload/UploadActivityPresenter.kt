package com.syaiful.ecommercessparepartmotor.ui.activity.upload

import com.syaiful.ecommercessparepartmotor.model.ResponseModel
import com.syaiful.ecommercessparepartmotor.model.transaction.Transaction
import com.syaiful.ecommercessparepartmotor.model.uploadResponse.UploadResponse
import com.syaiful.ecommercessparepartmotor.model.validateTransaction.ValidateTransaction
import com.syaiful.ecommercessparepartmotor.service.RetrofitService
import com.syaiful.ecommercessparepartmotor.ui.activity.transaction.TransactionActivityContract
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody

class UploadActivityPresenter : UploadActivityContract.Presenter {

    private val subscriptions = CompositeDisposable()
    private val api: RetrofitService = RetrofitService.create()
    private lateinit var view: UploadActivityContract.View

    override fun upload(file: MultipartBody.Part, enableLoading: Boolean) {
        if (enableLoading) {
            view.showProgressUpload(true)
        }
        val subscribe = api.upload(file)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result: ResponseModel<UploadResponse>? ->
                if (enableLoading) {
                    view.showProgressUpload(false)
                }
                if (result != null) {

                    if (result.Error != null){
                        view.showErrorUpload(result.Error!!)
                    }
                    if (result.Data != null) {
                        view.onUploaded(result.Data!!)
                    }
                }

            }, { t: Throwable ->
                if (enableLoading) {
                    view.showProgressUpload(false)
                }
                view.showErrorUpload(t.message!!)
            })

        subscriptions.add(subscribe)
    }

    override fun addValidateTransaction(validateTransaction: ValidateTransaction, enableLoading: Boolean) {
        if (enableLoading) {
            view.showProgressValidate(true)
        }
        val subscribe = api.addValidateTransaction(validateTransaction.clone())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result: ResponseModel<String>? ->
                if (enableLoading) {
                    view.showProgressValidate(false)
                }
                if (result != null) {

                    if (result.Error != null){
                        view.showErrorValidate(result.Error!!)
                    }
                    if (result.Data != "") {
                        view.onValidated()
                    }
                }

            }, { t: Throwable ->
                if (enableLoading) {
                    view.showProgressValidate(false)
                }
                view.showErrorValidate(t.message!!)
            })

        subscriptions.add(subscribe)
    }

    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: UploadActivityContract.View) {
        this.view = view
    }

}