package com.syaiful.ecommercessparepartmotor.ui.activity.carts

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.syaiful.ecommercessparepartmotor.R
import com.syaiful.ecommercessparepartmotor.di.component.DaggerActivityComponent
import com.syaiful.ecommercessparepartmotor.di.module.ActivityModule
import com.syaiful.ecommercessparepartmotor.model.RequestListModel
import com.syaiful.ecommercessparepartmotor.model.cart.Cart
import com.syaiful.ecommercessparepartmotor.model.checkout.Checkout
import com.syaiful.ecommercessparepartmotor.model.customer.Customer
import com.syaiful.ecommercessparepartmotor.ui.activity.setAddress.SetAddressActivity
import com.syaiful.ecommercessparepartmotor.ui.adapter.CartAdapter
import com.syaiful.ecommercessparepartmotor.ui.util.EmptyLayout
import com.syaiful.ecommercessparepartmotor.ui.util.ErrorLayout
import com.syaiful.ecommercessparepartmotor.ui.util.LoadingLayout
import com.syaiful.ecommercessparepartmotor.util.Formatter.Companion.decimalFormat
import com.syaiful.ecommercessparepartmotor.util.SerializableSave
import kotlinx.android.synthetic.main.activity_carts.*
import kotlinx.android.synthetic.main.activity_carts.back_imageview
import kotlinx.android.synthetic.main.activity_carts.empty_layout
import kotlinx.android.synthetic.main.activity_carts.error_layout
import kotlinx.android.synthetic.main.activity_carts.loading_layout
import javax.inject.Inject

class CartsActivity : AppCompatActivity(),CartsActivityContract.View {

    @Inject
    lateinit var presenter: CartsActivityContract.Presenter

    private lateinit var context: Context
    private var customer : Customer = Customer()

    private val carts = ArrayList<Cart>()
    private lateinit var cartAdapter: CartAdapter
    private val reqCarts = RequestListModel()

    private val checkoutData = Checkout()

    lateinit var emptyLayout: EmptyLayout
    lateinit var loading : LoadingLayout
    lateinit var error : ErrorLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carts)
        initWidget()
    }

    private fun initWidget() {
        this.context = this@CartsActivity

        injectDependency()
        presenter.attach(this)
        presenter.subscribe()

        if (SerializableSave(context,SerializableSave.userDataFileSessionName).load() != null){
            this.customer = SerializableSave(context,SerializableSave.userDataFileSessionName).load() as Customer
        }

        prepareCheckoutData()
        setQuery()
        setAdapter()

        emptyLayout = EmptyLayout(context,empty_layout)
        emptyLayout.setMessageAndIcon(getString(R.string.empty_cart),getString(R.string.empty_cart_message),R.drawable.empty_cart)
        emptyLayout.hide()

        loading = LoadingLayout(context,loading_layout)
        loading.setMessage(getString(R.string.loading_products))
        loading.hide()

        error = ErrorLayout(context,error_layout) {
            finish()
            startActivity(intent)
        }
        error.setMessage(getString(R.string.something_wrong))
        error.hide()

        back_imageview.setOnClickListener { 
            finish()
        }

        checkout_button.setOnClickListener {
            val i = Intent(context,SetAddressActivity::class.java)
            i.putExtra("checkout_data",checkoutData)
            startActivity(i)
            finish()
        }

        go_home.visibility = View.GONE
        go_home.setOnClickListener {
            finish()
        }

        presenter.getAllCart(reqCarts,true)
    }

    fun prepareCheckoutData(){
        checkoutData.customerId = customer.id
        checkoutData.address = ""
        checkoutData.paymentId = 0
        checkoutData.total = 0
    }

    fun setQuery(){
        reqCarts.customerId = customer.id
        reqCarts.searchBy = "product_id"
        reqCarts.orderBy = "product_id"
        reqCarts.orderDir = "asc"
        reqCarts.offset = 0
        reqCarts.limit = 10
    }

    fun setAdapter(){
        cartAdapter = CartAdapter(context,carts,{ c,i ->
            c.quantity++
            c.subTotal = c.quantity * c.price
            presenter.updateCart(c,false)
        },{ c,i ->

            if (c.quantity <= 0){
                presenter.deleteCart(c,true)
                return@CartAdapter
            }

            c.quantity--
            c.subTotal = c.quantity * c.price
            presenter.updateCart(c,false)
        })
        carts_recycleview.adapter = cartAdapter
        carts_recycleview.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        }
    }

    override fun onEmptyGetAllCart() {
        if (reqCarts.offset == 0){
            emptyLayout.show()
            cart_scrollview.visibility = View.GONE
            go_home.visibility = View.VISIBLE
            checkout_layout.visibility = View.GONE
        }
    }

    override fun onGetAllCart(data: ArrayList<Cart>) {
        if (reqCarts.offset == 0){
            carts.clear()
        }
        carts.addAll(data)
        cartAdapter.notifyDataSetChanged()
        cart_scrollview.visibility = View.VISIBLE
        checkout_layout.visibility = View.VISIBLE
        emptyLayout.hide()

        presenter.getTotal(Cart(customer.id))
    }

    override fun showProgressGetAllCart(show: Boolean) {
        loading.setVisibility(show)
        cart_scrollview.visibility = if (show) View.GONE else View.VISIBLE
        checkout_layout.visibility = if (show) View.GONE else View.VISIBLE
    }

    override fun showErrorGetAllCart(e: String) {
        cart_scrollview.visibility = View.GONE
        checkout_layout.visibility = View.GONE
        error.show()
    }

    override fun onUpdateCart() {
       presenter.getAllCart(reqCarts,false)
    }

    override fun showProgressUpdateCart(show: Boolean) {
        loading.setVisibility(show)
        cart_scrollview.visibility = if (show) View.GONE else View.VISIBLE
        checkout_layout.visibility = if (show) View.GONE else View.VISIBLE
    }

    override fun showErrorUpdateCart(e: String) {
        cart_scrollview.visibility = View.GONE
        checkout_layout.visibility = View.GONE
        error.show()
    }

    override fun onDeleteCart() {
        presenter.getAllCart(reqCarts,false)
    }

    override fun showProgressDeleteCart(show: Boolean) {
        loading.setVisibility(show)
        cart_scrollview.visibility = if (show) View.GONE else View.VISIBLE
        checkout_layout.visibility = if (show) View.GONE else View.VISIBLE
    }

    override fun showErrorDeleteCart(e: String) {
        cart_scrollview.visibility = View.GONE
        checkout_layout.visibility = View.GONE
        error.show()
    }

    override fun onGetTotal(total: Int) {
        total_checkout_text_view.text = "Rp. ${decimalFormat(total)}"
        checkoutData.total = total
    }

    override fun showErrorGetTotal(e: String) {
        carts_recycleview.visibility = View.GONE
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