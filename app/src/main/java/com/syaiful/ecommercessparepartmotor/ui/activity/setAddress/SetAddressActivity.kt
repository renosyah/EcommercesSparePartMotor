package com.syaiful.ecommercessparepartmotor.ui.activity.setAddress

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.syaiful.ecommercessparepartmotor.R
import com.syaiful.ecommercessparepartmotor.ui.activity.checkout.CheckoutActivity
import kotlinx.android.synthetic.main.activity_set_address.*

class SetAddressActivity : AppCompatActivity() {

    private lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_address)
        initWidget()
    }

    private fun initWidget(){
        this.context = this@SetAddressActivity

        address_edittext.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                checkout_button.visibility = if (address_edittext.text.toString().trim().isEmpty()) View.GONE else View.VISIBLE
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })
        checkout_button.visibility = View.GONE
        checkout_button.setOnClickListener {
            val i = Intent(context,CheckoutActivity::class.java)
            i.putExtra("address", address_edittext.text.toString())
            startActivity(i)
            finish()
        }

        back_imageview.setOnClickListener {
            finish()
        }
    }
}