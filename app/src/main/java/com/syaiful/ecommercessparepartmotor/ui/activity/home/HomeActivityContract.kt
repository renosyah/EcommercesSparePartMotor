package com.syaiful.ecommercessparepartmotor.ui.activity.home

import com.syaiful.ecommercessparepartmotor.base.BaseContract
import com.syaiful.ecommercessparepartmotor.model.RequestListModel
import com.syaiful.ecommercessparepartmotor.model.category.Category

class HomeActivityContract {
    interface View: BaseContract.View {

        // add more for request
        fun onEmptyGetAllCategory()
        fun onGetAllCategory(data : ArrayList<Category>)
        fun showProgressGetAllCategory(show: Boolean)
        fun showErrorGetAllCategory(e: String)
    }

    interface Presenter: BaseContract.Presenter<View> {

        // add for request
        fun getAllCategory(req : RequestListModel, enableLoading :Boolean)
    }
}