package com.syaiful.ecommercessparepartmotor.ui.activity.home

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.syaiful.ecommercessparepartmotor.R
import com.syaiful.ecommercessparepartmotor.di.component.DaggerActivityComponent
import com.syaiful.ecommercessparepartmotor.di.module.ActivityModule
import com.syaiful.ecommercessparepartmotor.model.RequestListModel
import com.syaiful.ecommercessparepartmotor.model.cart.Cart
import com.syaiful.ecommercessparepartmotor.model.category.Category
import com.syaiful.ecommercessparepartmotor.ui.activity.carts.CartsActivity
import com.syaiful.ecommercessparepartmotor.ui.activity.products.ProductsActivity
import com.syaiful.ecommercessparepartmotor.ui.adapter.CategoryAdapter
import com.syaiful.ecommercessparepartmotor.ui.util.EmptyLayout
import com.syaiful.ecommercessparepartmotor.ui.util.ErrorLayout
import com.syaiful.ecommercessparepartmotor.ui.util.LoadingLayout
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_home.cart_menu_imageview
import kotlinx.android.synthetic.main.activity_home.error_layout
import kotlinx.android.synthetic.main.activity_home.loading_layout
import kotlinx.android.synthetic.main.activity_home.empty_layout
import javax.inject.Inject

class HomeActivity : AppCompatActivity(), HomeActivityContract.View {

    @Inject
    lateinit var presenter: HomeActivityContract.Presenter

    private lateinit var context: Context

    private val categories = ArrayList<Category>()
    private lateinit var categoryAdapter : CategoryAdapter
    private val reqCategories = RequestListModel()

    lateinit var emptyLayout: EmptyLayout
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

        emptyLayout = EmptyLayout(context,empty_layout)
        emptyLayout.setMessageAndIcon(getString(R.string.empty_category),getString(R.string.empty_category_message),R.drawable.no_found)
        emptyLayout.hide()

        loading = LoadingLayout(context,loading_layout)
        loading.setMessage(getString(R.string.loading_categories))
        loading.hide()

        error = ErrorLayout(context,error_layout) {
            presenter.getAllCategory(reqCategories,true)
        }
        error.setMessage(getString(R.string.something_wrong))
        error.hide()

        cart_menu_imageview.setOnClickListener {
            startActivity(Intent(context, CartsActivity::class.java))
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
        categoryAdapter = CategoryAdapter(context,categories) { c,pos ->
            val i = Intent(context, ProductsActivity::class.java)
            i.putExtra("category",c)
            startActivity(i)
        }
        categories_recycleview.adapter = categoryAdapter
        categories_recycleview.apply {
            layoutManager = GridLayoutManager(context, 2)
        }
    }

    override fun onEmptyGetAllCategory() {
        if (reqCategories.offset == 0){
            categories_recycleview.visibility = View.GONE
            emptyLayout.show()
        }
    }

    override fun onGetAllCategory(data: ArrayList<Category>) {
        if (reqCategories.offset == 0){
            categories.clear()
        }
        categories.addAll(data)
        categoryAdapter.notifyDataSetChanged()
        home_scrollview.visibility = View.VISIBLE
        categories_recycleview.visibility = View.VISIBLE
        emptyLayout.hide()
    }

    override fun showProgressGetAllCategory(show: Boolean) {
        loading.setVisibility(show)
        home_scrollview.visibility = if (show) View.GONE else View.VISIBLE
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