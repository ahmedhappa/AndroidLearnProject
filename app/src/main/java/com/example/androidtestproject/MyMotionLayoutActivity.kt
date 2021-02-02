package com.example.androidtestproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.androidtestproject.databinding.ActivityMyMotionLayoutBinding

class MyMotionLayoutActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMyMotionLayoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyMotionLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}