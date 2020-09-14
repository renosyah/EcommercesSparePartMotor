package com.syaiful.ecommercessparepartmotor.di.component

import com.syaiful.ecommercessparepartmotor.BaseApp
import com.syaiful.ecommercessparepartmotor.di.module.ApplicationModule
import dagger.Component

@Component(modules = arrayOf(ApplicationModule::class))
interface ApplicationComponent {
    fun inject(application: BaseApp)
}