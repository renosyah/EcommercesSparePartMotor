package com.syaiful.ecommercessparepartmotor.ui.activity.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.syaiful.ecommercessparepartmotor.BuildConfig
import com.syaiful.ecommercessparepartmotor.R
import com.syaiful.ecommercessparepartmotor.di.component.DaggerActivityComponent
import com.syaiful.ecommercessparepartmotor.di.module.ActivityModule
import com.syaiful.ecommercessparepartmotor.model.RequestListModel
import com.syaiful.ecommercessparepartmotor.model.cart.Cart
import com.syaiful.ecommercessparepartmotor.model.product.Product
import com.syaiful.ecommercessparepartmotor.ui.activity.carts.CartsActivity
import com.syaiful.ecommercessparepartmotor.ui.activity.product.ProductActivity
import com.syaiful.ecommercessparepartmotor.ui.adapter.ProductAdapter
import com.syaiful.ecommercessparepartmotor.ui.util.EmptyLayout
import com.syaiful.ecommercessparepartmotor.ui.util.ErrorLayout
import com.syaiful.ecommercessparepartmotor.ui.util.LoadingLayout
import kotlinx.android.synthetic.main.activity_search_result.*
import javax.inject.Inject

class SearchResultActivity : AppCompatActivity(), SearchResultActivityContract.View {

    @Inject
    lateinit var presenter: SearchResultActivityContract.Presenter

    private lateinit var context: Context

    private val products = ArrayList<Product>()
    private lateinit var productAdapter : ProductAdapter
    lateinit var reqProducts : RequestListModel

    lateinit var emptyLayout: EmptyLayout
    lateinit var loading : LoadingLayout
    lateinit var error : ErrorLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result)
        initWidget()
    }

    private fun initWidget() {
        this.context = this@SearchResultActivity

        injectDependency()
        presenter.attach(this)
        presenter.subscribe()

        if (intent.hasExtra("request_products")) {
            reqProducts = intent.getSerializableExtra("request_products") as RequestListModel
        }

        setAdapter()

        emptyLayout = EmptyLayout(context,empty_layout)
        emptyLayout.setMessageAndIcon(getString(R.string.empty_product),getString(R.string.empty_product_message),R.drawable.no_found)
        emptyLayout.hide()

        loading = LoadingLayout(context,loading_layout)
        loading.setMessage(getString(R.string.loading_products))
        loading.hide()

        error = ErrorLayout(context,error_layout) {
            presenter.getAllProduct(reqProducts,true)
        }
        error.setMessage(getString(R.string.something_wrong))
        error.hide()

        back_imageview.setOnClickListener {
            finish()
        }
        cart_menu_imageview.setOnClickListener {
            startActivity(Intent(context, CartsActivity::class.java))
        }

        presenter.getAllProduct(reqProducts,true)
    }

    fun setAdapter(){
        productAdapter = ProductAdapter(context,products) { product, pos ->
            val i = Intent(context, ProductActivity::class.java)
            i.putExtra("product",product)
            startActivity(i)
        }
        productAdapter.setOnCartClick { product, i ->
            val cart = Cart(
                0, BuildConfig.DEFAULT_CUSTOMER_ID, product.id,
                1, product.price, product.price
            )
            presenter.addToCart(cart,true)
        }
        products_recycleview.adapter = productAdapter
        products_recycleview.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        }
    }

    override fun onEmptyGetAllProduct() {
        if (reqProducts.offset == 0){
            products_recycleview.visibility = View.GONE
            emptyLayout.show()
        }
    }

    override fun onGetAllProduct(data: ArrayList<Product>) {
        if (reqProducts.offset == 0){
            products.clear()
        }
        products.addAll(data)
        productAdapter.notifyDataSetChanged()
        products_recycleview.visibility = View.VISIBLE
        emptyLayout.hide()
    }

    override fun showProgressGetAllProduct(show: Boolean) {
        loading.setVisibility(show)
        products_recycleview.visibility = if (show) View.GONE else View.VISIBLE
    }

    override fun showErrorGetAllProduct(e: String) {
        products_recycleview.visibility = View.GONE
        error.show()
    }

    override fun onAddToCart() {
        Toast.makeText(context,getString(R.string.add_to_cart), Toast.LENGTH_SHORT).show()
    }

    override fun showProgressAddToCart(show: Boolean) {
        loading.setVisibility(show)
        products_recycleview.visibility = if (show) View.GONE else View.VISIBLE
    }

    override fun showErrorAddToCart(e: String) {
        products_recycleview.visibility = View.GONE
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