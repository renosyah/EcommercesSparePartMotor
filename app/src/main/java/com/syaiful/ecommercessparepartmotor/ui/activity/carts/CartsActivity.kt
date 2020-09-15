package com.syaiful.ecommercessparepartmotor.ui.activity.carts

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.syaiful.ecommercessparepartmotor.R
import com.syaiful.ecommercessparepartmotor.model.RequestListModel
import com.syaiful.ecommercessparepartmotor.model.cart.Cart
import com.syaiful.ecommercessparepartmotor.ui.util.EmptyLayout
import com.syaiful.ecommercessparepartmotor.ui.util.ErrorLayout
import com.syaiful.ecommercessparepartmotor.ui.util.LoadingLayout
import kotlinx.android.synthetic.main.activity_carts.*
import kotlinx.android.synthetic.main.activity_carts.back_imageview
import kotlinx.android.synthetic.main.activity_carts.empty_layout
import kotlinx.android.synthetic.main.activity_carts.error_layout
import kotlinx.android.synthetic.main.activity_carts.loading_layout
import kotlinx.android.synthetic.main.activity_products.*

class CartsActivity : AppCompatActivity() {

    private lateinit var context: Context

    private val carts = ArrayList<Cart>()
    private val reqCarts = RequestListModel()

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

        setQuery()
        setAdapter()

        emptyLayout = EmptyLayout(context,empty_layout)
        emptyLayout.setMessageAndIcon(getString(R.string.empty_cart),getString(R.string.empty_cart_message),R.drawable.no_found)
        emptyLayout.hide()

        loading = LoadingLayout(context,loading_layout)
        loading.setMessage(getString(R.string.loading_products))
        loading.hide()

        error = ErrorLayout(context,error_layout) {

        }
        error.setMessage(getString(R.string.something_wrong))
        error.hide()

        back_imageview.setOnClickListener { 
            finish()
        }
    }

    fun setQuery(){
        reqCarts.searchBy = "name"
        reqCarts.orderBy = "name"
        reqCarts.orderDir = "asc"
        reqCarts.offset = 0
        reqCarts.limit = 10
    }

    fun setAdapter(){
        carts_recycleview.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        }
    }
}