package com.syaiful.ecommercessparepartmotor.di.module
import android.app.Application
import com.syaiful.ecommercessparepartmotor.BaseApp
import com.syaiful.ecommercessparepartmotor.di.scope.PerApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class ApplicationModule(private val baseApp: BaseApp) {

    @Provides
    @Singleton
    @PerApplication
    fun provideApplication(): Application {
        return baseApp
    }
}