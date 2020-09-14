package com.syaiful.ecommercessparepartmotor.di.module
import androidx.fragment.app.Fragment
import com.syaiful.ecommercessparepartmotor.service.RetrofitService
import dagger.Module
import dagger.Provides

@Module
class FragmentModule(private var fragment: Fragment) {

    @Provides
    fun provideFragment() : Fragment {
        return fragment
    }

    @Provides
    fun provideApiService(): RetrofitService {
        return RetrofitService.create()
    }

    // add for each new fragment
}