package com.syaiful.ecommercessparepartmotor.ui.activity.products

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.syaiful.ecommercessparepartmotor.model.category.Category
import com.syaiful.ecommercessparepartmotor.model.customer.Customer
import com.syaiful.ecommercessparepartmotor.model.product.Product
import com.syaiful.ecommercessparepartmotor.ui.activity.carts.CartsActivity
import com.syaiful.ecommercessparepartmotor.ui.activity.product.ProductActivity
import com.syaiful.ecommercessparepartmotor.ui.activity.search.SearchResultActivity
import com.syaiful.ecommercessparepartmotor.ui.adapter.ProductAdapter
import com.syaiful.ecommercessparepartmotor.ui.util.EmptyLayout
import com.syaiful.ecommercessparepartmotor.ui.util.ErrorLayout
import com.syaiful.ecommercessparepartmotor.ui.util.LoadingLayout
import com.syaiful.ecommercessparepartmotor.util.SerializableSave
import kotlinx.android.synthetic.main.activity_products.*
import kotlinx.android.synthetic.main.activity_products.error_layout
import kotlinx.android.synthetic.main.activity_products.loading_layout
import javax.inject.Inject


class ProductsActivity : AppCompatActivity(),ProductsActivityContract.View {

    @Inject
    lateinit var presenter: ProductsActivityContract.Presenter

    private lateinit var context: Context
    private var customer : Customer = Customer()

    private val products = ArrayList<Product>()
    private lateinit var productAdapter : ProductAdapter
    private val reqProducts = RequestListModel()
    private lateinit var category : Category

    lateinit var emptyLayout: EmptyLayout
    lateinit var loading : LoadingLayout
    lateinit var error : ErrorLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_products)
        initWidget()
    }

    private fun initWidget() {
        this.context = this@ProductsActivity

        injectDependency()
        presenter.attach(this)
        presenter.subscribe()

        if (intent.hasExtra("category")){
            category = intent.getSerializableExtra("category") as Category
            title_category_textview.text = category.name
        }

        if (SerializableSave(context, SerializableSave.userDataFileSessionName).load() != null){
            this.customer = SerializableSave(context, SerializableSave.userDataFileSessionName).load() as Customer
        }

        setQuery()
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

        search_button_imageview.setOnClickListener {
            reqProducts.searchValue = search_text_edittext.text.toString()

            val i = Intent(context,SearchResultActivity::class.java)
            i.putExtra("request_products",reqProducts)
            startActivity(i)
        }

        cart_menu_imageview.setOnClickListener {
            startActivity(Intent(context, CartsActivity::class.java))
        }
        back_imageview.setOnClickListener {
            finish()
        }

        presenter.getAllProduct(reqProducts,true)
    }

    fun setQuery(){
        reqProducts.categoryId = category.id
        reqProducts.searchBy = "name"
        reqProducts.orderBy = "name"
        reqProducts.orderDir = "asc"
        reqProducts.offset = 0
        reqProducts.limit = 10
    }

    fun setAdapter(){
        productAdapter = ProductAdapter(context,products) { product, pos ->
            val i = Intent(context, ProductActivity::class.java)
            i.putExtra("product",product)
            startActivity(i)
        }
        productAdapter.setOnCartClick { product, i ->
            val cart = Cart(
                0, customer.id, product.id,
                1, product.price, product.price
            )
            presenter.addToCart(cart,true)
        }
        products_recycleview.adapter = productAdapter
        products_recycleview.apply {
            layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
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
        products_scrollview.visibility = View.VISIBLE
        products_recycleview.visibility = View.VISIBLE
        emptyLayout.hide()
    }

    override fun showProgressGetAllProduct(show: Boolean) {
        loading.setVisibility(show)
        products_scrollview.visibility = if (show) View.GONE else View.VISIBLE
    }

    override fun showErrorGetAllProduct(e: String) {
        products_scrollview.visibility = View.GONE
        error.show()
    }

    override fun onAddToCart() {
        Toast.makeText(context,getString(R.string.add_to_cart), Toast.LENGTH_SHORT).show()
    }

    override fun showProgressAddToCart(show: Boolean) {
        loading.setVisibility(show)
        products_scrollview.visibility = if (show) View.GONE else View.VISIBLE
    }

    override fun showErrorAddToCart(e: String) {
        products_scrollview.visibility = View.GONE
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