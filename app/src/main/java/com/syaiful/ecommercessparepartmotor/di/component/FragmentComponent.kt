package com.syaiful.ecommercessparepartmotor.di.component

import com.syaiful.ecommercessparepartmotor.di.module.FragmentModule
import dagger.Component

@Component(modules = arrayOf(FragmentModule::class))
interface FragmentComponent {
    // add for each new fragment
}