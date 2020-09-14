package com.syaiful.ecommercessparepartmotor.ui.activity.home

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.syaiful.ecommercessparepartmotor.R
import com.syaiful.ecommercessparepartmotor.di.component.DaggerActivityComponent
import com.syaiful.ecommercessparepartmotor.di.module.ActivityModule
import com.syaiful.ecommercessparepartmotor.model.RequestListModel
import com.syaiful.ecommercessparepartmotor.model.categories.Categories
import com.syaiful.ecommercessparepartmotor.ui.adapter.CategoriesAdapter
import kotlinx.android.synthetic.main.activity_home.*
import javax.inject.Inject

class HomeActivity : AppCompatActivity(), HomeActivityContract.View {

    @Inject
    lateinit var presenter: HomeActivityContract.Presenter

    lateinit var context: Context

    val categories = ArrayList<Categories>()
    lateinit var categoriesAdapter : CategoriesAdapter
    private val reqCategories = RequestListModel()

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

        cart_menu_imageview.setOnClickListener {

        }

        presenter.getAllCategories(reqCategories,true)
    }

    fun setQuery(){
        reqCategories.searchBy = "name"
        reqCategories.orderBy = "name"
        reqCategories.orderDir = "asc"
        reqCategories.offset = 0
        reqCategories.limit = 10
    }

    fun setAdapter(){
        categoriesAdapter = CategoriesAdapter(context,categories) { c, i -> }
        categories_recycleview.apply {
            layoutManager = GridLayoutManager(context, 2)
        }
        categories_recycleview.adapter = categoriesAdapter
    }


    override fun onEmptyCategories() {

    }

    override fun onGetAllCategories(datas: ArrayList<Categories>) {
        if (reqCategories.offset == 0){
            categories.clear()
        }
        categories.addAll(datas)
        categoriesAdapter.notifyDataSetChanged()
    }

    override fun showProgressGetAllCategories(show: Boolean) {

    }

    override fun showErrorGetAllCategories(error: String) {
        Toast.makeText(context,error,Toast.LENGTH_LONG).show()
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