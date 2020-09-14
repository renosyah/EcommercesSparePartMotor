package com.syaiful.ecommercessparepartmotor
import android.app.Application
import com.squareup.picasso.BuildConfig
import com.syaiful.ecommercessparepartmotor.di.component.ApplicationComponent
import com.syaiful.ecommercessparepartmotor.di.component.DaggerApplicationComponent
import com.syaiful.ecommercessparepartmotor.di.module.ApplicationModule

class BaseApp : Application() {

    lateinit var component: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        instance = this
        setup()

        if (BuildConfig.DEBUG) {
        }
    }

    fun setup() {
        component = DaggerApplicationComponent.builder()
            .applicationModule(ApplicationModule(this)).build()
        component.inject(this)
    }

    companion object {
        lateinit var instance: BaseApp private set
    }
}