package com.example.androidlearnproject.sensors

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.androidlearnproject.databinding.ActivitySensorsBinding

class SensorsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySensorsBinding
    private lateinit var lightSensor: MeasurableSensor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySensorsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lightSensor = LightSensor(this).apply {
            startListening()
            setOnSensorValuesChangeListener {
                val isDark = it[0] <= 60f
                if (isDark) {
                    binding.rootView.setBackgroundColor(Color.BLACK)
                    binding.tvLightSensorResult.text = "it is dark outside"
                    binding.tvLightSensorResult.setTextColor(Color.WHITE)
                } else {
                    binding.rootView.setBackgroundColor(Color.WHITE)
                    binding.tvLightSensorResult.text = "it is light outside"
                    binding.tvLightSensorResult.setTextColor(Color.BLACK)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        lightSensor.stopListening()
    }
}