package com.syaiful.ecommercessparepartmotor.ui.activity.transaction

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.syaiful.ecommercessparepartmotor.R
import com.syaiful.ecommercessparepartmotor.di.component.DaggerActivityComponent
import com.syaiful.ecommercessparepartmotor.di.module.ActivityModule
import com.syaiful.ecommercessparepartmotor.model.payment.Payment
import com.syaiful.ecommercessparepartmotor.model.transaction.Transaction
import kotlinx.android.synthetic.main.activity_transaction.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class TransactionActivity : AppCompatActivity(),TransactionActivityContract.View {

    @Inject
    lateinit var presenter: TransactionActivityContract.Presenter

    private lateinit var context: Context
    private var refId : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction)
        initWidget()
    }

    private fun initWidget(){
        this.context = this@TransactionActivity

        injectDependency()
        presenter.attach(this)
        presenter.subscribe()

        if (intent.hasExtra("ref_id")){
            refId = intent.getStringExtra("ref_id")!!
        }

        back_imageview.setOnClickListener {
            finish()
        }

        presenter.getOneTransactionByRef(Transaction(refId),true)
    }

    private fun startCountDown(minute : Long){
        val timer = object : CountDownTimer(TimeUnit.MINUTES.toMillis(minute), TimeUnit.SECONDS.toMillis(1L)) {
            override fun onTick(milis: Long) {
                val min = TimeUnit.MILLISECONDS.toMinutes(milis)
                val sec = TimeUnit.MILLISECONDS.toSeconds(milis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milis))
                remain_time_textview.text = "${min} menit ${sec} detik"
            }

            override fun onFinish() {

            }

        }
        timer.start()
    }

    private fun copyAccountNumber(){
        val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("rekening", payment_detail_textview.text)
        clipboardManager.setPrimaryClip(clipData)
        notifTextIsCopied()
    }

    private fun notifTextIsCopied(){
        val inflater = layoutInflater
        val layout: View = inflater.inflate(R.layout.toast_copied, null)

        val toast = Toast(context)
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
        toast.duration = Toast.LENGTH_LONG
        toast.view = layout
        toast.show()
    }

    override fun onGetOneTransactionByRef(transaction: Transaction) {
        total_payment_textview.text = "Rp ${transaction.total + transaction.shipmentFee}"
        presenter.getOnePayment(Payment(transaction.paymentId),true)
    }

    override fun showProgressGetOneTransactionByRef(show: Boolean) {

    }

    override fun showErrorGetOneTransactionByRef(e: String) {
        Toast.makeText(context,e,Toast.LENGTH_SHORT).show()
    }

    override fun onGetOnePayment(payment: Payment) {
        payment_detail_textview.text = payment.detail
        copy_imageview.setOnClickListener {
            copyAccountNumber()
        }
        startCountDown(60)
    }

    override fun showProgressGetOnePayment(show: Boolean) {

    }

    override fun showErrorGetOnePayment(e: String) {
        Toast.makeText(context,e,Toast.LENGTH_SHORT).show()
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