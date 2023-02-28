package com.junting.drug_android_frontend

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.junting.drug_android_frontend.databinding.ActivityTakePhotoBinding


class TakePhoto : AppCompatActivity() {

    private lateinit var binding: ActivityTakePhotoBinding
    val CAMERA_PERM_CODE = 101
    val CAMERA_REQUEST_CODE = 102
    var selectedImage: ImageView? = null
    var btn_camera: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_take_photo)
//        使用ViewBinding
        binding = ActivityTakePhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        selectedImage = binding.ivDisplay
        btn_camera = binding.btnCamera

        //開啟相機
        btn_camera!!.setOnClickListener(View.OnClickListener { askCameraPermissions() })


    }
    private fun askCameraPermissions() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.CAMERA),
                CAMERA_PERM_CODE
            )
        } else {
            openCamera()
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERM_CODE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            } else {
                Toast.makeText(
                    this,
                    "Camera Permission is Required to Use camera.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    private fun openCamera() {
        val camera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(camera, CAMERA_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST_CODE) {
            val image = data?.extras!!["data"] as Bitmap?
            selectedImage!!.setImageBitmap(image)
        }
    }
}