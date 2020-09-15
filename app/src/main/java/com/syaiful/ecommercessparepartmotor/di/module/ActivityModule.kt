package com.syaiful.ecommercessparepartmotor.di.module
import dagger.Module
import dagger.Provides
import android.app.Activity
import com.syaiful.ecommercessparepartmotor.service.RetrofitService
import com.syaiful.ecommercessparepartmotor.ui.activity.home.HomeActivityContract
import com.syaiful.ecommercessparepartmotor.ui.activity.home.HomeActivityPresenter
import com.syaiful.ecommercessparepartmotor.ui.activity.product.ProductActivityContract
import com.syaiful.ecommercessparepartmotor.ui.activity.product.ProductActivityPresenter
import com.syaiful.ecommercessparepartmotor.ui.activity.products.ProductsActivityContract
import com.syaiful.ecommercessparepartmotor.ui.activity.products.ProductsActivityPresenter

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
}