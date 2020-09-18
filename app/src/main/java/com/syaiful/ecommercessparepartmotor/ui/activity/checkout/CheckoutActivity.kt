package com.syaiful.ecommercessparepartmotor.ui.activity.checkout

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.syaiful.ecommercessparepartmotor.R
import com.syaiful.ecommercessparepartmotor.di.component.DaggerActivityComponent
import com.syaiful.ecommercessparepartmotor.di.module.ActivityModule
import com.syaiful.ecommercessparepartmotor.model.RequestListModel
import com.syaiful.ecommercessparepartmotor.model.cart.Cart
import com.syaiful.ecommercessparepartmotor.model.checkout.Checkout
import com.syaiful.ecommercessparepartmotor.model.customer.Customer
import com.syaiful.ecommercessparepartmotor.model.payment.Payment
import com.syaiful.ecommercessparepartmotor.ui.activity.transaction.TransactionActivity
import com.syaiful.ecommercessparepartmotor.ui.adapter.CheckoutItemAdapter
import com.syaiful.ecommercessparepartmotor.ui.dialog.ChoosePaymentDialog
import com.syaiful.ecommercessparepartmotor.ui.util.ErrorLayout
import com.syaiful.ecommercessparepartmotor.ui.util.LoadingLayout
import com.syaiful.ecommercessparepartmotor.util.Formatter.Companion.decimalFormat
import com.syaiful.ecommercessparepartmotor.util.SerializableSave
import kotlinx.android.synthetic.main.activity_checkout.*
import javax.inject.Inject

class CheckoutActivity : AppCompatActivity(),CheckoutActivityContract.View {

    @Inject
    lateinit var presenter: CheckoutActivityContract.Presenter

    private lateinit var context: Context
    private var customer : Customer = Customer()

    private val carts = ArrayList<Cart>()
    private lateinit var checkoutItemAdapter: CheckoutItemAdapter
    private val reqCarts = RequestListModel()

    private lateinit var checkoutData : Checkout

    private val payments = ArrayList<Payment>()
    private val reqPayment = RequestListModel()

    lateinit var loading : LoadingLayout
    lateinit var error : ErrorLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)
        initWidget()
    }

    private fun initWidget(){
        this.context = this@CheckoutActivity

        injectDependency()
        presenter.attach(this)
        presenter.subscribe()


        if (intent.hasExtra("checkout_data")){
            checkoutData = intent.getSerializableExtra("checkout_data") as Checkout
            address_textview.text = checkoutData.address
        }

        if (SerializableSave(context, SerializableSave.userDataFileSessionName).load() != null){
            this.customer = SerializableSave(context, SerializableSave.userDataFileSessionName).load() as Customer
        }

        setQuery()
        setAdapter()

        loading = LoadingLayout(context,loading_layout)
        loading.setMessage(getString(R.string.loading_data))
        loading.hide()

        error = ErrorLayout(context,error_layout) {
            finish()
            startActivity(intent)
        }
        error.setMessage(getString(R.string.something_wrong))
        error.hide()

        choose_payment_method_button.isEnabled = false
        choose_payment_method_button.visibility = View.VISIBLE
        choose_payment_method_button.setOnClickListener {

            val dialog = ChoosePaymentDialog(payments) { p,i ->
                for (pi in payments){
                    pi.selected = false
                }
                payments[i].selected = true

                checkoutData.paymentId = p.id
                payment_method_choosed_button.text = "${getString(R.string.choose_payment)}\n${p.name}"
                payment_method_choosed_button.visibility = View.VISIBLE
                choose_payment_method_button.visibility = View.GONE
            }
            dialog.show(supportFragmentManager, "Bottom Sheet Dialog Fragment")

        }

        payment_method_choosed_button.visibility = View.GONE
        payment_method_choosed_button.setOnClickListener {
            choose_payment_method_button.performClick()
        }

        back_imageview.setOnClickListener {
            finish()
        }

        create_checkout_button.setOnClickListener {
            if (checkoutData.paymentId == 0){
                return@setOnClickListener
            }
            presenter.checkout(checkoutData)
        }

        presenter.getAllPayment(reqPayment,true)
        presenter.getAllCart(reqCarts,true)
    }

    fun setQuery(){
        reqCarts.customerId = customer.id
        reqCarts.searchBy = "product_id"
        reqCarts.orderBy = "product_id"
        reqCarts.orderDir = "asc"
        reqCarts.offset = 0
        reqCarts.limit = 10

        reqPayment.searchBy = "name"
        reqPayment.orderBy = "name"
        reqPayment.orderDir = "asc"
        reqPayment.offset = 0
        reqPayment.limit = 10
    }

    fun setAdapter(){
        checkoutItemAdapter = CheckoutItemAdapter(context,carts)
        checkout_item_recycleview.adapter = checkoutItemAdapter
        checkout_item_recycleview.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        }
    }

    override fun onEmptyGetAllCart() {

    }

    override fun onGetAllCart(data: ArrayList<Cart>) {
        if (reqCarts.offset == 0){
            carts.clear()
        }
        carts.addAll(data)
        checkoutItemAdapter.notifyDataSetChanged()
        presenter.getTotal(Cart(customer.id))
    }

    override fun showProgressGetAllCart(show: Boolean) {
        loading.setVisibility(show)
        checkout_main_layout.visibility = if (show) View.GONE else View.VISIBLE
    }

    override fun showErrorGetAllCart(e: String) {
        checkout_main_layout.visibility = View.GONE
        error.show()
    }

    override fun onEmptyGetAllPayment() {
        choose_payment_method_button.isEnabled = false
    }

    override fun onGetAllPayment(data: ArrayList<Payment>) {
        if (reqPayment.offset == 0){
            payments.clear()
        }
        payments.addAll(data)
        choose_payment_method_button.isEnabled = true
    }

    override fun showProgressGetAllPayment(show: Boolean) {
        loading.setVisibility(show)
        checkout_main_layout.visibility = if (show) View.GONE else View.VISIBLE
    }

    override fun showErrorGetAllPayment(e: String) {
        checkout_main_layout.visibility = View.GONE
        error.show()
    }

    override fun onGetTotal(total: Int) {
        total_textview.text = "Rp. ${decimalFormat(total)}"
        total_pay_textview.text = "Rp. ${decimalFormat(total)}"
    }

    override fun showErrorGetTotal(e: String) {
        checkout_main_layout.visibility = View.GONE
        error.show()
    }

    override fun onCheckoutCompleted(refId: String) {
        val i = Intent(context,TransactionActivity::class.java)
        i.putExtra("ref_id",refId)
        startActivity(i)
        finish()
    }

    override fun showProgressCheckout(show: Boolean) {
        loading.setVisibility(show)
        checkout_main_layout.visibility = if (show) View.GONE else View.VISIBLE
    }

    override fun showErrorCheckout(e: String) {
        checkout_main_layout.visibility = View.GONE
        error.setMessage(e)
        error.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unsubscribe()
    }

    private fun injectDependency(){
        val listcomponent = DaggerActivityComponent.builder()
            .activityModule(ActivityModule(this))
            .build()

        listcomponent.inject(this)
    }
}