package com.junting.drug_android_frontend;

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.junting.drug_android_frontend.databinding.ActivityResultBinding
import com.junting.drug_android_frontend.libs.BitmapUtils
import com.junting.drug_android_frontend.services.CloudVisionService
import kotlinx.coroutines.launch

class ResultActivity: AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding
    private val viewModel: CloudVisionViewModel = CloudVisionViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageBase64 = TakePhotoActivity.photoResultBase64
        binding.ivNormalize.setImageBitmap(BitmapUtils.base64ToBitmap(imageBase64))
        viewModel.recognize(imageBase64)

        this.viewModel.text.observe(this, Observer {
            // handle your text result there
            Toast.makeText(this, viewModel.text.value, Toast.LENGTH_SHORT).show()
        })
        binding.btnConfirm.setOnClickListener() {
            val intent = Intent(this, AutoRecognizeDrugbagInfoActivity::class.java)
            startActivity(intent)
        }
    }

    class CloudVisionViewModel: ViewModel() {

        var text = MutableLiveData<String>()

        fun recognize(imageBase64: String) {
            viewModelScope.launch {
                val service = CloudVisionService.getInstance()
                try {
                    val response = service.imageAnnotate(imageBase64).responses.get(0)
                    // recognize results is in response.textAnnotations list
                    // index 0 is the combined results of all identified texts
                    text.value = if (response.textAnnotations != null)
                        response.textAnnotations.get(0).description
                        else "no text"
                    Log.d("CloudVisionViewModel", text.value!!)
                } catch (e: Exception) {
                    Log.d("CloudVisionViewModel", "image recognize failed")
                    Log.e("CloudVisionViewModel", e.toString())
                    Log.e("CloudVisionViewModel", e.message!!)
                }
            }
        }
    }
}
