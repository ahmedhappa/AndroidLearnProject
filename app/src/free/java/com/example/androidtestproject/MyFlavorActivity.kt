package com.example.androidtestproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.androidtestproject.databinding.ActivityMyFalvourBinding

class MyFlavorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyFalvourBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyFalvourBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imgPls.setOnClickListener {
            Toast.makeText(this, "Toast From Free Package", Toast.LENGTH_LONG).show()
        }
    }
}