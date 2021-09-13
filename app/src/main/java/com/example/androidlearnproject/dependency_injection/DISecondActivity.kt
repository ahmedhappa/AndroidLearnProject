package com.example.androidlearnproject.dependency_injection

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.androidlearnproject.databinding.ActivityDiSecondBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DISecondActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDiSecondBinding

    @Inject
    lateinit var myInjectableThemeClass: MyInjectableThemeClass

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiSecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {

            btnShowInjectedThemeClassData.setOnClickListener {
                Toast.makeText(
                    this@DISecondActivity,
                    "injected theme Background color name : ${myInjectableThemeClass.backgroundColorName}\n"
                        .plus("Date : ${myInjectableThemeClass.date.time}"),
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }
}