package com.syaiful.ecommercessparepartmotor.ui.activity.home

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.syaiful.ecommercessparepartmotor.R
import com.syaiful.ecommercessparepartmotor.di.component.DaggerActivityComponent
import com.syaiful.ecommercessparepartmotor.di.module.ActivityModule
import com.syaiful.ecommercessparepartmotor.model.RequestListModel
import com.syaiful.ecommercessparepartmotor.model.category.Category
import com.syaiful.ecommercessparepartmotor.ui.adapter.CategoriesAdapter
import com.syaiful.ecommercessparepartmotor.ui.util.ErrorLayout
import com.syaiful.ecommercessparepartmotor.ui.util.LoadingLayout
import kotlinx.android.synthetic.main.activity_home.*
import javax.inject.Inject

class HomeActivity : AppCompatActivity(), HomeActivityContract.View {

    @Inject
    lateinit var presenter: HomeActivityContract.Presenter

    private lateinit var context: Context

    private val categories = ArrayList<Category>()
    private lateinit var categoriesAdapter : CategoriesAdapter
    private val reqCategories = RequestListModel()

    lateinit var loading : LoadingLayout
    lateinit var error : ErrorLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initWidget()
    }

    private fun initWidget() {
        this.context = this@HomeActivity

        injectDependency()
        presenter.attach(this)
        presenter.subscribe()

        setQuery()
        setAdapter()

        loading = LoadingLayout(context,loading_layout)
        loading.setMessage(getString(R.string.loading_categories))
        loading.hide()

        error = ErrorLayout(context,error_layout) {
            presenter.getAllCategory(reqCategories,true)
        }
        error.setMessage(getString(R.string.something_wrong))
        error.hide()

        cart_menu_imageview.setOnClickListener {

        }

        presenter.getAllCategory(reqCategories,true)
    }

    fun setQuery(){
        reqCategories.searchBy = "name"
        reqCategories.orderBy = "name"
        reqCategories.orderDir = "asc"
        reqCategories.offset = 0
        reqCategories.limit = 10
    }

    fun setAdapter(){
        categoriesAdapter = CategoriesAdapter(context,categories) { c, i ->

        }
        categories_recycleview.adapter = categoriesAdapter
        categories_recycleview.apply {
            layoutManager = GridLayoutManager(context, 2)
        }
    }

    override fun onEmptyGetAllCategory() {
        if (reqCategories.offset == 0){
            home_scrollview.visibility = View.GONE
        }
    }

    override fun onGetAllCategory(data: ArrayList<Category>) {
        if (reqCategories.offset == 0){
            categories.clear()
        }
        categories.addAll(data)
        categoriesAdapter.notifyDataSetChanged()
        home_scrollview.visibility = View.VISIBLE
    }

    override fun showProgressGetAllCategory(show: Boolean) {
        loading.setVisibility(show)
    }

    override fun showErrorGetAllCategory(e: String) {
        home_scrollview.visibility = View.GONE
        error.show()
    }


    override fun onDestroy() {
        super.onDestroy()
        presenter.unsubscribe()
    }

    private fun injectDependency(){
        val listcomponent = DaggerActivityComponent.builder()
            .activityModule(ActivityModule(this))
            .build()

        listcomponent.inject(this)
    }
}