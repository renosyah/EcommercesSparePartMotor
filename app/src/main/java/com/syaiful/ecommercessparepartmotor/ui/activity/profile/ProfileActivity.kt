package com.syaiful.ecommercessparepartmotor.ui.activity.profile

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.syaiful.ecommercessparepartmotor.R
import com.syaiful.ecommercessparepartmotor.model.customer.Customer
import com.syaiful.ecommercessparepartmotor.util.SerializableSave
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {

    private lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        initWidget()
    }

    private fun initWidget() {
        this.context = this@ProfileActivity

        if (SerializableSave(context, SerializableSave.userDataFileSessionName).load() != null){
            val customer = SerializableSave(context, SerializableSave.userDataFileSessionName).load() as Customer
            name_textview.text = customer.name
            email_textview.text = customer.email
        }

        back_imageview.setOnClickListener {
            finish()
        }
    }
}