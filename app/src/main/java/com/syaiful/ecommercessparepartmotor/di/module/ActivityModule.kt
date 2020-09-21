package com.syaiful.ecommercessparepartmotor.di.module
import dagger.Module
import dagger.Provides
import android.app.Activity
import com.syaiful.ecommercessparepartmotor.service.RetrofitService
import com.syaiful.ecommercessparepartmotor.ui.activity.carts.CartsActivityContract
import com.syaiful.ecommercessparepartmotor.ui.activity.carts.CartsActivityPresenter
import com.syaiful.ecommercessparepartmotor.ui.activity.checkout.CheckoutActivityContract
import com.syaiful.ecommercessparepartmotor.ui.activity.checkout.CheckoutActivityPresenter
import com.syaiful.ecommercessparepartmotor.ui.activity.home.HomeActivityContract
import com.syaiful.ecommercessparepartmotor.ui.activity.home.HomeActivityPresenter
import com.syaiful.ecommercessparepartmotor.ui.activity.loginOrRegister.LoginOrRegisterActivityContract
import com.syaiful.ecommercessparepartmotor.ui.activity.loginOrRegister.LoginOrRegisterActivityPresenter
import com.syaiful.ecommercessparepartmotor.ui.activity.product.ProductActivityContract
import com.syaiful.ecommercessparepartmotor.ui.activity.product.ProductActivityPresenter
import com.syaiful.ecommercessparepartmotor.ui.activity.products.ProductsActivityContract
import com.syaiful.ecommercessparepartmotor.ui.activity.products.ProductsActivityPresenter
import com.syaiful.ecommercessparepartmotor.ui.activity.search.SearchResultActivityContract
import com.syaiful.ecommercessparepartmotor.ui.activity.search.SearchResultActivityPresenter
import com.syaiful.ecommercessparepartmotor.ui.activity.transaction.TransactionActivity
import com.syaiful.ecommercessparepartmotor.ui.activity.transaction.TransactionActivityContract
import com.syaiful.ecommercessparepartmotor.ui.activity.transaction.TransactionActivityPresenter

@Module
class ActivityModule(private var activity : Activity) {
    @Provides
    fun provideActivity() : Activity {
        return activity
    }

    @Provides
    fun provideApiService(): RetrofitService {
        return RetrofitService.create()
    }

    // add for each new activity
    @Provides
    fun provideHomeActivityPresenter(): HomeActivityContract.Presenter {
        return HomeActivityPresenter()
    }

    @Provides
    fun provideProductsActivityPresenter(): ProductsActivityContract.Presenter {
        return ProductsActivityPresenter()
    }

    @Provides
    fun provideProductActivityPresenter(): ProductActivityContract.Presenter {
        return ProductActivityPresenter()
    }

    @Provides
    fun provideCartsActivityPresenter(): CartsActivityContract.Presenter {
        return CartsActivityPresenter()
    }

    @Provides
    fun provideSearchResultActivityPresenter(): SearchResultActivityContract.Presenter {
        return SearchResultActivityPresenter()
    }

    @Provides
    fun provideCheckoutActivityPresenter(): CheckoutActivityContract.Presenter {
        return CheckoutActivityPresenter()
    }

    @Provides
    fun provideTransactionActivityPresenter(): TransactionActivityContract.Presenter {
        return TransactionActivityPresenter()
    }

    @Provides
    fun provideLoginOrRegisterActivityPresenter(): LoginOrRegisterActivityContract.Presenter {
        return LoginOrRegisterActivityPresenter()
    }
}