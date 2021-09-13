package com.example.androidlearnproject.navigation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.androidlearnproject.databinding.ActivityLearnNavigationBinding

class LearnNavigationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLearnNavigationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLearnNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}