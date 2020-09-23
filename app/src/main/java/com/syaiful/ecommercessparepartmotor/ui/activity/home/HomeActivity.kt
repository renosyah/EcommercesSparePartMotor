package com.syaiful.ecommercessparepartmotor.ui.activity.home

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.syaiful.ecommercessparepartmotor.R
import com.syaiful.ecommercessparepartmotor.di.component.DaggerActivityComponent
import com.syaiful.ecommercessparepartmotor.di.module.ActivityModule
import com.syaiful.ecommercessparepartmotor.model.RequestListModel
import com.syaiful.ecommercessparepartmotor.model.category.Category
import com.syaiful.ecommercessparepartmotor.model.customer.Customer
import com.syaiful.ecommercessparepartmotor.ui.activity.carts.CartsActivity
import com.syaiful.ecommercessparepartmotor.ui.activity.loginOrRegister.LoginOrRegisterActivity
import com.syaiful.ecommercessparepartmotor.ui.activity.products.ProductsActivity
import com.syaiful.ecommercessparepartmotor.ui.activity.profile.ProfileActivity
import com.syaiful.ecommercessparepartmotor.ui.adapter.AdapterBanner
import com.syaiful.ecommercessparepartmotor.ui.adapter.CategoryAdapter
import com.syaiful.ecommercessparepartmotor.ui.util.EmptyLayout
import com.syaiful.ecommercessparepartmotor.ui.util.ErrorLayout
import com.syaiful.ecommercessparepartmotor.ui.util.LoadingLayout
import com.syaiful.ecommercessparepartmotor.util.SerializableSave
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.content_home.*
import javax.inject.Inject

class HomeActivity : AppCompatActivity(), HomeActivityContract.View {

    @Inject
    lateinit var presenter: HomeActivityContract.Presenter

    private lateinit var context: Context

    private val banners : ArrayList<String> = ArrayList()
    private lateinit var bannerAdapter : AdapterBanner

    private val categories = ArrayList<Category>()
    private lateinit var categoryAdapter : CategoryAdapter
    private val reqCategories = RequestListModel()

    lateinit var emptyLayout: EmptyLayout
    lateinit var loading : LoadingLayout
    lateinit var error : ErrorLayout

    private val MY_PERMISSIONS_REQUEST = 121

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initWidget()
    }

    private fun initWidget(){
        this.context = this@HomeActivity

        injectDependency()
        presenter.attach(this)
        presenter.subscribe()

        if (SerializableSave(context, SerializableSave.userDataFileSessionName).load() != null){
            val customer = SerializableSave(context, SerializableSave.userDataFileSessionName).load() as Customer
            nav_name_textview.text = customer.name
            nav_email_textview.text = customer.email
        }

        banners.add("http://app-demo-api.000webhostapp.com/img/product/s6hkc7cBHC.png")
        banners.add("http://app-demo-api.000webhostapp.com/img/product/50aucZZ8LB.png")
        banners.add("http://app-demo-api.000webhostapp.com/img/product/e2oL9Sa55Q.png")

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

        side_menu_imageview.setOnClickListener {
            drawer_layout.openDrawer(nav_view)
        }

        nav_menu_profile_textview.setOnClickListener {
            startActivity(Intent(context,ProfileActivity::class.java))
        }

        nav_menu_transactions_textview.setOnClickListener {
            Toast.makeText(context,getText(R.string.comming_soon),Toast.LENGTH_SHORT).show()
        }

        nav_logout_button.setOnClickListener {
            if (SerializableSave(context, SerializableSave.userDataFileSessionName).delete()){
                startActivity(Intent(context,LoginOrRegisterActivity::class.java))
                finish()
            }
        }

        cart_menu_imageview.setOnClickListener {
            startActivity(Intent(context, CartsActivity::class.java))
        }

        nav_menu_layout.visibility = View.INVISIBLE

        requestPermission{}

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

        bannerAdapter = AdapterBanner(context,banners)
        banner_recycleview.adapter = bannerAdapter
        banner_recycleview.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
        }

        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(banner_recycleview)

        indicator.attachToRecyclerView(banner_recycleview,snapHelper)
        bannerAdapter.registerAdapterDataObserver(indicator.adapterDataObserver)
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


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_PERMISSIONS_REQUEST) {
            startActivity(Intent(context, HomeActivity::class.java))
            finish()
        }
    }

    private fun requestPermission(next: (Boolean)->Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((context as Activity),arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA), MY_PERMISSIONS_REQUEST)

        } else {
            next.invoke(true)
        }
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