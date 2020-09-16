package com.syaiful.ecommercessparepartmotor.service

import android.util.Log
import com.syaiful.ecommercessparepartmotor.BuildConfig
import com.syaiful.ecommercessparepartmotor.model.RequestListModel
import com.syaiful.ecommercessparepartmotor.model.ResponseModel
import com.syaiful.ecommercessparepartmotor.model.cart.Cart
import com.syaiful.ecommercessparepartmotor.model.category.Category
import com.syaiful.ecommercessparepartmotor.model.product.Product
import io.reactivex.Observable
import okhttp3.Interceptor
import okhttp3.OkHttpClient
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
    @POST("api/category/list.php")
    fun allCategory(@Body req : RequestListModel): Observable<ResponseModel<ArrayList<Category>>>

    @POST("api/product/list.php")
    fun allProduct(@Body req : RequestListModel): Observable<ResponseModel<ArrayList<Product>>>

    @POST("api/cart/add.php")
    fun addCart(@Body cart : Cart): Observable<ResponseModel<String>>

    @POST("api/cart/list_detail.php")
    fun allCart(@Body req : RequestListModel): Observable<ResponseModel<ArrayList<Cart>>>

    @POST("api/cart/update.php")
    fun updateCart(@Body cart : Cart): Observable<ResponseModel<String>>

    @POST("api/cart/delete.php")
    fun deleteCart(@Body cart : Cart): Observable<ResponseModel<String>>

    @POST("api/cart/total.php")
    fun getTotal(@Body cart : Cart): Observable<ResponseModel<Int>>

    companion object {

        fun create() : RetrofitService {

            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BuildConfig.SERVER_URL)
                .build()

            return retrofit.create(RetrofitService::class.java)
        }
    }
}