package com.syaiful.ecommercessparepartmotor.service

import com.syaiful.ecommercessparepartmotor.BuildConfig
import com.syaiful.ecommercessparepartmotor.model.RequestListModel
import com.syaiful.ecommercessparepartmotor.model.ResponseModel
import com.syaiful.ecommercessparepartmotor.model.cart.Cart
import com.syaiful.ecommercessparepartmotor.model.category.Category
import com.syaiful.ecommercessparepartmotor.model.checkout.Checkout
import com.syaiful.ecommercessparepartmotor.model.customer.Customer
import com.syaiful.ecommercessparepartmotor.model.payment.Payment
import com.syaiful.ecommercessparepartmotor.model.product.Product
import com.syaiful.ecommercessparepartmotor.model.transaction.Transaction
import com.syaiful.ecommercessparepartmotor.model.uploadResponse.UploadResponse
import com.syaiful.ecommercessparepartmotor.model.validateTransaction.ValidateTransaction
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.util.*

interface RetrofitService {

    // add more end point to access
    @POST("api/customer/login.php")
    fun login(@Body customer: Customer): Observable<ResponseModel<Customer>>

    @POST("api/customer/add.php")
    fun register(@Body customer: Customer): Observable<ResponseModel<Customer>>

    @POST("api/category/list.php")
    fun allCategory(@Body req : RequestListModel): Observable<ResponseModel<ArrayList<Category>>>

    @POST("api/product/list.php")
    fun allProduct(@Body req : RequestListModel): Observable<ResponseModel<ArrayList<Product>>>

    @POST("api/payment/list.php")
    fun allPayment(@Body req : RequestListModel): Observable<ResponseModel<ArrayList<Payment>>>

    @POST("api/payment/one.php")
    fun onePayment(@Body payment: Payment): Observable<ResponseModel<Payment>>

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

    @POST("api/transaction/one_by_ref.php")
    fun oneTransactionByRef(@Body transaction: Transaction): Observable<ResponseModel<Transaction>>

    @POST("api/checkout/add.php")
    fun checkout(@Body checkout: Checkout): Observable<ResponseModel<String>>

    @POST("api/validate_transaction/add.php")
    fun addValidateTransaction(@Body validateTransaction: ValidateTransaction): Observable<ResponseModel<String>>


    @Multipart
    @POST("/api/upload_file.php")
    fun upload(@Part file: MultipartBody.Part): Observable<ResponseModel<UploadResponse>>

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