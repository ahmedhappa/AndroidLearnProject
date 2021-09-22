package com.example.androidlearnproject.animation.motionlayout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.androidlearnproject.databinding.ActivityMyMotionLayoutBinding

class MyMotionLayoutActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMyMotionLayoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyMotionLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}