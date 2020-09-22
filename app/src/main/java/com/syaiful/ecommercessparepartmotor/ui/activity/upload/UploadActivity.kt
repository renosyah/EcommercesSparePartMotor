package com.syaiful.ecommercessparepartmotor.ui.activity.upload

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.syaiful.ecommercessparepartmotor.BuildConfig
import com.syaiful.ecommercessparepartmotor.R
import com.syaiful.ecommercessparepartmotor.di.component.DaggerActivityComponent
import com.syaiful.ecommercessparepartmotor.di.module.ActivityModule
import com.syaiful.ecommercessparepartmotor.model.transaction.Transaction
import com.syaiful.ecommercessparepartmotor.model.uploadResponse.UploadResponse
import com.syaiful.ecommercessparepartmotor.model.validateTransaction.ValidateTransaction
import com.syaiful.ecommercessparepartmotor.ui.util.ErrorLayout
import com.syaiful.ecommercessparepartmotor.ui.util.LoadingLayout
import com.syaiful.ecommercessparepartmotor.util.ImageRotation
import kotlinx.android.synthetic.main.activity_upload.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class UploadActivity : AppCompatActivity(),UploadActivityContract.View {

    @Inject
    lateinit var presenter: UploadActivityContract.Presenter

    lateinit var context: Context
    private val CAMERA_REQUEST = 102
    private val PICK_IMAGE = 103

    lateinit var transaction: Transaction
    private val validateTransaction = ValidateTransaction()

    lateinit var uploadFile : ByteArray

    lateinit var loading : LoadingLayout
    lateinit var error : ErrorLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)
        initWidget()
    }

    private fun initWidget(){
        this.context = this@UploadActivity

        injectDependency()
        presenter.attach(this)
        presenter.subscribe()

        if (intent.hasExtra("transaction")){
            this.transaction = intent.getSerializableExtra("transaction") as Transaction
            this.validateTransaction.transactionId = transaction.id
        }

        main_upload_layout.visibility = View.VISIBLE
        message_upload_layout.visibility = View.GONE

        loading = LoadingLayout(context,loading_layout)
        loading.setMessage(getString(R.string.loading_upload))
        loading.hide()

        error = ErrorLayout(context,error_layout) {
            upload_layout.visibility = View.VISIBLE
        }
        error.setMessage(getString(R.string.something_wrong))
        error.hide()

        change_image_button.setOnClickListener {

            val item = arrayOf<CharSequence>(getString(R.string.camera),getString(R.string.galery))
            AlertDialog.Builder(context)
                .setTitle(getString(R.string.title_dialog_uploadimage))
                .setItems(item, DialogInterface.OnClickListener { dialog, which ->
                    when (which) {
                        0 -> {
                            openCamera()
                            dialog.dismiss()
                        }
                        1 -> {
                            openGalery()
                            dialog.dismiss()
                        }
                    }
                })
                .create()
                .show()
        }
        send_image_button.setOnClickListener {
            if (this::uploadFile.isInitialized) {
                uploadImage(uploadFile)
            }
        }

        back_to_home_button.setOnClickListener {
            finish()
        }

        back_imageview.setOnClickListener {
            finish()
        }

        change_image_button.performClick()
    }

    private fun openCamera(){
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                val photoFile: File? = try { createImageFile() } catch (ignore : IOException) { null }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(context, "${BuildConfig.APPLICATION_ID}.provider", it)
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, CAMERA_REQUEST)
                }
            }
        }
    }

    lateinit var currentPhotoPath: String

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS",Locale.US).format(System.currentTimeMillis())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).also {
            currentPhotoPath = it.absolutePath
        }
    }

    private fun openGalery(){
        val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(pickPhoto, PICK_IMAGE)
    }

    private fun bmpToByteArray(bmp :Bitmap) : ByteArray {
        val stream = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        return stream.toByteArray()
    }

    private fun uploadImage(content : ByteArray){
        val name = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS",Locale.US).format(System.currentTimeMillis()) + ".jpg"
        val requestFile = RequestBody.create(MediaType.parse("image/jpeg"), content)
        val file = MultipartBody.Part.createFormData("file",name, requestFile)
        presenter.upload(file,true)
    }

    override fun onUploaded(uploadResponse: UploadResponse) {
        validateTransaction.imageId = "${BuildConfig.SERVER_URL}${uploadResponse.url}"
        presenter.addValidateTransaction(validateTransaction,true)
    }

    override fun showProgressUpload(show: Boolean) {
        upload_layout.visibility = if (show) View.GONE else View.VISIBLE
        loading.setMessage(getString(R.string.loading_upload))
        loading.setVisibility(show)
    }

    override fun showErrorUpload(e: String) {
        upload_layout.visibility = View.GONE
        error.setMessage(e)
        error.show()
    }

    override fun onValidated() {
        main_upload_layout.visibility = View.GONE
        message_upload_layout.visibility = View.VISIBLE
    }

    override fun showProgressValidate(show: Boolean) {
        upload_layout.visibility = if (show) View.GONE else View.VISIBLE
        loading.setMessage(getString(R.string.loading_upload))
        loading.setVisibility(show)
    }

    override fun showErrorValidate(e: String) {
        upload_layout.visibility = View.GONE
        error.setMessage(e)
        error.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){

            when (requestCode) {
                PICK_IMAGE -> {
                    if (data != null){
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && data.data != null) {
                            val source = ImageDecoder.createSource(this.contentResolver, data.data!!)
                            val bmp = ImageDecoder.decodeBitmap(source)
                            image_preview_imageview.setImageBitmap(bmp)
                            uploadFile = bmpToByteArray(bmp)

                        } else {
                            val bmp = MediaStore.Images.Media.getBitmap(this.contentResolver, data.data)
                            image_preview_imageview.setImageBitmap(bmp)
                            uploadFile = bmpToByteArray(bmp)

                        }

                        upload_layout.visibility = View.VISIBLE
                    }
                }
                CAMERA_REQUEST -> {

                    upload_layout.visibility = View.GONE
                    loading.setMessage(getString(R.string.process))
                    loading.show()

                    BitmapFactory.decodeFile(currentPhotoPath)?.also { bmp ->
                        val matrix = Matrix()
                        matrix.postRotate(90f)
                        val rotatedBitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.width,bmp.height, matrix, true)
                        image_preview_imageview.setImageBitmap(rotatedBitmap)
                        uploadFile = ImageRotation.getStreamByteFromImage(File(currentPhotoPath))

                        upload_layout.visibility = View.VISIBLE
                        loading.hide()
                    }
                }
            }
        }
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