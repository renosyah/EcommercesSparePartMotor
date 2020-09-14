package com.syaiful.ecommercessparepartmotor.di.component

import com.syaiful.ecommercessparepartmotor.di.module.ActivityModule
import com.syaiful.ecommercessparepartmotor.ui.activity.home.HomeActivity
import dagger.Component

@Component(modules = arrayOf(ActivityModule::class))
interface ActivityComponent {
    // add for each new activity
    fun inject(homeActivity: HomeActivity)
}