package com.syaiful.ecommercessparepartmotor.service

import android.util.Log
import com.syaiful.ecommercessparepartmotor.BuildConfig
import com.syaiful.ecommercessparepartmotor.model.RequestListModel
import com.syaiful.ecommercessparepartmotor.model.ResponseModel
import com.syaiful.ecommercessparepartmotor.model.categories.Categories
import io.reactivex.Observable
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.Buffer
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.io.IOException
import java.util.*

interface RetrofitService {

    // add more end point to access
    @POST("api/categories/list.php")
    fun allCategories(@Body req : RequestListModel): Observable<ResponseModel<ArrayList<Categories>>>

    companion object {

        fun create() : RetrofitService {

            val client = OkHttpClient.Builder()
                .addInterceptor(object : Interceptor{
                    override fun intercept(chain: Interceptor.Chain): Response {
                        try {
                            val buffer = Buffer()
                            chain.request().body()!!.writeTo(buffer)
                            Log.e("body", buffer.readUtf8())

                        } catch (ignore: IOException) { }

                        return chain.proceed(chain.request())
                    }

                })
                .build()

            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BuildConfig.SERVER_URL)
                .client(client)
                .build()

            return retrofit.create(RetrofitService::class.java)
        }
    }
}