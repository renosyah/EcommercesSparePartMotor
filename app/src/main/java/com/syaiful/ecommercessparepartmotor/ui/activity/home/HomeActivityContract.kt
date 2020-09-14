package com.syaiful.ecommercessparepartmotor.ui.activity.home

import com.syaiful.ecommercessparepartmotor.base.BaseContract
import com.syaiful.ecommercessparepartmotor.model.RequestListModel
import com.syaiful.ecommercessparepartmotor.model.categories.Categories

class HomeActivityContract {
    interface View: BaseContract.View {

        // add more for request
        fun onEmptyCategories()
        fun onGetAllCategories(datas : ArrayList<Categories>)
        fun showProgressGetAllCategories(show: Boolean)
        fun showErrorGetAllCategories(error: String)
    }

    interface Presenter: BaseContract.Presenter<View> {

        // add for request
        fun getAllCategories(req : RequestListModel, enableLoading :Boolean)
    }
}