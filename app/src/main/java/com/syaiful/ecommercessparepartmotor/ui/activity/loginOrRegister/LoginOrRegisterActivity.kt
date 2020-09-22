package com.syaiful.ecommercessparepartmotor.ui.activity.loginOrRegister

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.syaiful.ecommercessparepartmotor.R
import com.syaiful.ecommercessparepartmotor.di.component.DaggerActivityComponent
import com.syaiful.ecommercessparepartmotor.di.module.ActivityModule
import com.syaiful.ecommercessparepartmotor.model.customer.Customer
import com.syaiful.ecommercessparepartmotor.ui.activity.home.HomeActivity
import com.syaiful.ecommercessparepartmotor.ui.util.ErrorLayout
import com.syaiful.ecommercessparepartmotor.ui.util.LoadingLayout
import com.syaiful.ecommercessparepartmotor.util.SerializableSave
import kotlinx.android.synthetic.main.activity_login_or_register.*
import kotlinx.android.synthetic.main.activity_login_or_register.error_layout
import kotlinx.android.synthetic.main.activity_login_or_register.loading_layout
import javax.inject.Inject

class LoginOrRegisterActivity : AppCompatActivity(),LoginOrRegisterActivityContract.View {

    @Inject
    lateinit var presenter: LoginOrRegisterActivityContract.Presenter

    private lateinit var context: Context
    private val customer = Customer()

    lateinit var loading : LoadingLayout
    lateinit var error : ErrorLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_or_register)
        initWidget()
    }

    private fun initWidget(){
        this.context = this@LoginOrRegisterActivity

        injectDependency()
        presenter.attach(this)
        presenter.subscribe()

        if (SerializableSave(context,SerializableSave.userDataFileSessionName).load() != null){
            startActivity(Intent(context,HomeActivity::class.java))
            finish()
        }

        layout_login.visibility = View.VISIBLE
        layout_register.visibility = View.GONE

        loading = LoadingLayout(context,loading_layout)
        loading.hide()

        error = ErrorLayout(context,error_layout) {

        }
        error.setButtonColor(ContextCompat.getColor(context,R.color.textColorWhite))
        error.setButtonMessageColor(ContextCompat.getColor(context,R.color.colorPrimaryDark))
        error.hide()

        login_button.setOnClickListener {

            if (login_email_edittext.text.toString().trim().isEmpty() || login_password_edittext.text.toString().trim().isEmpty()){
                Toast.makeText(context,getString(R.string.login_form_empty),Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            customer.id = 0
            customer.username = ""
            customer.email = login_email_edittext.text.toString()
            customer.password = login_password_edittext.text.toString()

            presenter.login(customer, true)
        }
        register_button.setOnClickListener {

            if (register_name_edittext.text.toString().trim().isEmpty() || register_email_edittext.text.toString().trim().isEmpty() || register_password_edittext.text.toString().trim().isEmpty()){
                Toast.makeText(context,getString(R.string.register_form_empty),Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (register_password_edittext.text.toString() != confirm_password_edittext.text.toString()){
                Toast.makeText(context,getString(R.string.register_confirm_password),Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            customer.id = 0
            customer.username = ""
            customer.name = register_name_edittext.text.toString()
            customer.email = register_email_edittext.text.toString()
            customer.password = register_password_edittext.text.toString()

            presenter.register(customer,true)
        }

        to_login_textview.setOnClickListener {
            layout_login.visibility = View.VISIBLE
            layout_register.visibility = View.GONE
        }

        to_register_textview.setOnClickListener {
            layout_login.visibility = View.GONE
            layout_register.visibility = View.VISIBLE
        }
    }

    override fun onLogin(customer: Customer) {
        if (SerializableSave(context, SerializableSave.userDataFileSessionName).save(customer)) {
            startActivity(Intent(context, HomeActivity::class.java))
            finish()
        }
    }

    override fun showProgressLogin(show: Boolean) {
        loading.setMessage(getString(R.string.loading_login))
        loading.setVisibility(show)
        layout_login.visibility = if (show) View.GONE else View.VISIBLE
    }

    override fun showErrorLogin(e: String) {
        layout_login.visibility = View.GONE
        error.setMessage(getString(R.string.login_failed))
        error.setOnclick {
            to_login_textview.performClick()
        }
        error.show()
    }

    override fun onRegister(customer: Customer) {
        if (SerializableSave(context, SerializableSave.userDataFileSessionName).save(customer)) {
            startActivity(Intent(context,HomeActivity::class.java))
            finish()
        }
    }

    override fun showProgressRegister(show: Boolean) {
        loading.setMessage(getString(R.string.loading_register))
        loading.setVisibility(show)
        layout_register.visibility = if (show) View.GONE else View.VISIBLE
    }

    override fun showErrorRegister(e: String) {
        layout_register.visibility = View.GONE
        error.setMessage(getString(R.string.register_failed))
        error.setOnclick {
            to_register_textview.performClick()
        }
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