package com.syaiful.ecommercessparepartmotor.ui.activity.product

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.annotations.SerializedName
import com.squareup.picasso.Picasso
import com.syaiful.ecommercessparepartmotor.BuildConfig
import com.syaiful.ecommercessparepartmotor.R
import com.syaiful.ecommercessparepartmotor.di.component.DaggerActivityComponent
import com.syaiful.ecommercessparepartmotor.di.module.ActivityModule
import com.syaiful.ecommercessparepartmotor.model.cart.Cart
import com.syaiful.ecommercessparepartmotor.model.product.Product
import com.syaiful.ecommercessparepartmotor.ui.activity.carts.CartsActivity
import com.syaiful.ecommercessparepartmotor.ui.util.ErrorLayout
import com.syaiful.ecommercessparepartmotor.ui.util.LoadingLayout
import kotlinx.android.synthetic.main.activity_product.*
import kotlinx.android.synthetic.main.activity_product.back_imageview
import kotlinx.android.synthetic.main.activity_product.cart_menu_imageview
import kotlinx.android.synthetic.main.activity_product.error_layout
import kotlinx.android.synthetic.main.activity_product.loading_layout
import javax.inject.Inject

class ProductActivity : AppCompatActivity(),ProductActivityContract.View {

    @Inject
    lateinit var presenter: ProductActivityContract.Presenter

    private lateinit var context: Context
    private lateinit var product : Product

    lateinit var loading : LoadingLayout
    lateinit var error : ErrorLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)
        initWidget()
    }

    private fun initWidget() {
        this.context = this@ProductActivity

        injectDependency()
        presenter.attach(this)
        presenter.subscribe()

        if (intent.hasExtra("product")){
            product = intent.getSerializableExtra("product") as Product

            name_text_view.text = product.name
            detail_text_view.text = " ${product.detail}"
            price_text_view.text = " Rp. ${product.price}"
            stock_text_view.text = " ${product.stock}"
            rating_text_view.text = " ${product.rating}"

            Picasso.get()
                .load(product.imageUrl)
                .resize(200,250)
                .into(product_image_view)
        }

        loading = LoadingLayout(context,loading_layout)
        loading.setMessage(getString(R.string.loading_products))
        loading.hide()

        error = ErrorLayout(context,error_layout) {
            product_layout.visibility = View.VISIBLE
        }
        error.setMessage(getString(R.string.something_wrong))
        error.hide()

        cart_menu_imageview.setOnClickListener {
            startActivity(Intent(context, CartsActivity::class.java))
        }

        add_product_to_cart_button.setOnClickListener {
            val cart = Cart(
                0, BuildConfig.DEFAULT_CUSTOMER_ID, product.id,
                1, product.price, product.price
            )
            presenter.getAddToCart(cart,true)
        }

        back_imageview.setOnClickListener {
            finish()
        }
    }

    override fun onAddToCart() {
        Toast.makeText(context,"${product.name} ${getString(R.string.add_to_cart)}", Toast.LENGTH_SHORT).show()
    }

    override fun showProgressAddToCart(show: Boolean) {
        loading.setVisibility(show)
        product_layout.visibility = if (show) View.GONE else View.VISIBLE
    }

    override fun showErrorAddToCart(e: String) {
        product_layout.visibility = View.GONE
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