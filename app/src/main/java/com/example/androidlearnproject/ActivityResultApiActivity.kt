package com.example.androidlearnproject

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import com.example.androidlearnproject.databinding.ActivityResultApiBinding

class ActivityResultApiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultApiBinding

    // you can create the object of  ActivityResultLauncher here but it must be launched after the activity is created
    /*ActivityResultContracts class contains a lot of other options to do ex (pick contact , launch other activity for result
     , pick contact , ask for permission,.......)*/
    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) {
        //result of call
        Log.e("Data Uri", "${it?.path}")
    }

    private val launchActivityForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                Log.e("data", (it.data != null).toString())
            }
        }

    private val requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()){
        if (it){
            Log.e("Permission", "Granted")
        }else{
            Log.e("Permission", "Not Granted")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultApiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnPickImage.setOnClickListener {
            // Pass in the mime type you'd like to allow the user to select
            // as the input
            getContent.launch("image/*")
        }

        binding.btnStartOtherActivityForResult.setOnClickListener {
            launchActivityForResult.launch(Intent(this, ChartActivity::class.java))
        }

        binding.btnPermission.setOnClickListener {
            requestPermission.launch(Manifest.permission.CAMERA)
        }
    }
}