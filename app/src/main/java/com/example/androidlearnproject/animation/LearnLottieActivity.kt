package com.example.androidlearnproject.animation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.androidlearnproject.databinding.ActivityLearnLottieBinding

class LearnLottieActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLearnLottieBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLearnLottieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.lavLoadingSquare.apply {
            setMinAndMaxFrame(0,120)
        }
    }
}