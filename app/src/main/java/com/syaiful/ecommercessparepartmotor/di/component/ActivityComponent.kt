package com.syaiful.ecommercessparepartmotor.di.component

import com.syaiful.ecommercessparepartmotor.di.module.ActivityModule
import com.syaiful.ecommercessparepartmotor.ui.activity.carts.CartsActivity
import com.syaiful.ecommercessparepartmotor.ui.activity.checkout.CheckoutActivity
import com.syaiful.ecommercessparepartmotor.ui.activity.checkout.CheckoutActivityContract
import com.syaiful.ecommercessparepartmotor.ui.activity.home.HomeActivity
import com.syaiful.ecommercessparepartmotor.ui.activity.product.ProductActivity
import com.syaiful.ecommercessparepartmotor.ui.activity.product.ProductActivityContract
import com.syaiful.ecommercessparepartmotor.ui.activity.products.ProductsActivity
import com.syaiful.ecommercessparepartmotor.ui.activity.search.SearchResultActivity
import com.syaiful.ecommercessparepartmotor.ui.activity.search.SearchResultActivityPresenter
import dagger.Component

@Component(modules = arrayOf(ActivityModule::class))
interface ActivityComponent {
    // add for each new activity
    fun inject(homeActivity: HomeActivity)
    fun inject(productsActivity: ProductsActivity)
    fun inject(productActivity: ProductActivity)
    fun inject(cartsActivity: CartsActivity)
    fun inject(searchResultActivity: SearchResultActivity)
    fun inject(checkoutActivity: CheckoutActivity)
}