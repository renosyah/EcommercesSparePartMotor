package com.syaiful.ecommercessparepartmotor.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.syaiful.ecommercessparepartmotor.R
import com.syaiful.ecommercessparepartmotor.model.payment.Payment
import com.syaiful.ecommercessparepartmotor.ui.adapter.AdapterPayment

class ChoosePaymentDialog : BottomSheetDialogFragment {

    var payments : ArrayList<Payment>
    var onItemClick : (Payment,Int) -> Unit

    constructor(payments: ArrayList<Payment>,onItemClick : (Payment,Int) -> Unit) {
        this.payments = payments
        this.onItemClick = onItemClick
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL,R.style.CustomBottomSheetDialogTheme)
    }

    override fun setupDialog(dialog: Dialog, style: Int) {
        val v = activity!!.layoutInflater.inflate(R.layout.dialog_payment,null)

        val paymentRecycleView : RecyclerView = v.findViewById(R.id.payment_recycleview)
        paymentRecycleView.adapter = AdapterPayment(activity as Context,payments) { p, i ->
            onItemClick.invoke(p,i)
            dialog.dismiss()
        }
        paymentRecycleView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        }
        dialog.setContentView(v)
    }
}