package com.syaiful.ecommercessparepartmotor.ui.activity.transaction

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
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
import com.syaiful.ecommercessparepartmotor.ui.activity.upload.UploadActivity
import com.syaiful.ecommercessparepartmotor.ui.util.ErrorLayout
import com.syaiful.ecommercessparepartmotor.ui.util.LoadingLayout
import com.syaiful.ecommercessparepartmotor.util.Formatter.Companion.decimalFormat
import kotlinx.android.synthetic.main.activity_transaction.*
import kotlinx.android.synthetic.main.activity_transaction.error_layout
import kotlinx.android.synthetic.main.activity_transaction.loading_layout
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class TransactionActivity : AppCompatActivity(),TransactionActivityContract.View {

    @Inject
    lateinit var presenter: TransactionActivityContract.Presenter

    private lateinit var context: Context
    private var refId : String = ""
    lateinit var transaction: Transaction

    lateinit var timer : CountDownTimer

    lateinit var loading : LoadingLayout
    lateinit var error : ErrorLayout

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

        transaction_layout.visibility = View.VISIBLE
        transaction_message_layout.visibility = View.GONE

        loading = LoadingLayout(context,loading_layout)
        loading.setMessage(getString(R.string.loading_transaction))
        loading.hide()

        error = ErrorLayout(context,error_layout) {
            presenter.getOneTransactionByRef(Transaction(refId),true)
        }
        error.setMessage(getString(R.string.something_wrong))
        error.hide()

        back_imageview.setOnClickListener {
            finish()
        }

        back_to_home_button.setOnClickListener {
            finish()
        }

        upload_transaction_button.visibility = View.GONE
        upload_transaction_button.setOnClickListener {
            if (this::transaction.isInitialized){
                val i = Intent(context,UploadActivity::class.java)
                i.putExtra("transaction",transaction)
                startActivity(i)
                finish()
            }
        }

        presenter.getOneTransactionByRef(Transaction(refId),true)
    }

    private fun startCountDown(minute : Long){
         timer = object : CountDownTimer(TimeUnit.MINUTES.toMillis(minute), TimeUnit.SECONDS.toMillis(1L)) {
            override fun onTick(milis: Long) {
                val min = TimeUnit.MILLISECONDS.toMinutes(milis)
                val sec = TimeUnit.MILLISECONDS.toSeconds(milis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milis))
                remain_time_textview.text = "${min} menit ${sec} detik"
            }

            override fun onFinish() {
                transaction_layout.visibility = View.GONE
                transaction_message_layout.visibility = View.VISIBLE
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
        toast.setGravity(Gravity.BOTTOM, 0, 0)
        toast.duration = Toast.LENGTH_LONG
        toast.view = layout
        toast.show()
    }

    override fun onGetOneTransactionByRef(t: Transaction) {
        transaction = t
        total_payment_textview.text = "Rp ${decimalFormat(transaction.total)}"
        presenter.getOnePayment(Payment(transaction.paymentId),true)
        upload_transaction_button.visibility = View.VISIBLE
    }

    override fun showProgressGetOneTransactionByRef(show: Boolean) {
        loading.setVisibility(show)
        transaction_detail_layout.visibility = if (show) View.GONE else View.VISIBLE
    }

    override fun showErrorGetOneTransactionByRef(e: String) {
        transaction_detail_layout.visibility = View.GONE
        error.show()
    }

    override fun onGetOnePayment(payment: Payment) {
        payment_detail_textview.text = payment.detail
        copy_imageview.setOnClickListener {
            copyAccountNumber()
        }
        startCountDown(1)
    }

    override fun showProgressGetOnePayment(show: Boolean) {
        loading.setVisibility(show)
        transaction_detail_layout.visibility = if (show) View.GONE else View.VISIBLE
    }

    override fun showErrorGetOnePayment(e: String) {
        transaction_detail_layout.visibility = View.GONE
        error.show()
    }


    override fun onDestroy() {
        super.onDestroy()
        presenter.unsubscribe()
        if (this::timer.isInitialized){
            timer.onFinish()
        }
    }

    private fun injectDependency(){
        val listcomponent = DaggerActivityComponent.builder()
            .activityModule(ActivityModule(this))
            .build()

        listcomponent.inject(this)
    }

}