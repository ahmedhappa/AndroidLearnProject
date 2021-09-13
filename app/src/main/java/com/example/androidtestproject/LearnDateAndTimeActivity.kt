package com.example.androidtestproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.androidtestproject.databinding.ActivityLearnDateAndTimeBinding
import java.time.*
import java.time.format.DateTimeFormatter

class LearnDateAndTimeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLearnDateAndTimeBinding
    private val TAG = "DateTime"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLearnDateAndTimeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnCreateDateAtSpecificPoint.setOnClickListener {
                //different ways to create a custom date
                val someDate = LocalDate.of(2005, 10, 5)
                Log.e(TAG, "Some Specific Date : $someDate")
//                val someDateTime = LocalDateTime.of(2005, 10, 5,10,30)
                val someDateTime = LocalDateTime.ofEpochSecond(1595363833, 545343, ZoneOffset.UTC)
                Log.e(TAG, "Some Specific Date Time : $someDateTime")
                val someInstant = Instant.parse("2008-12-18T10:15:30.00Z")
                Log.e(TAG, "Some Specific Date Time Instant : ${someInstant.toEpochMilli()}")
            }

            btnGetCurrentDateTime.setOnClickListener {
                val currentDate = LocalDate.now()
                Log.e(TAG, "Current Date : $currentDate")
                val currentDateAtStartOfDay = currentDate.atStartOfDay()
                Log.e(TAG, "Current Date With Start Of The Dat : $currentDateAtStartOfDay")
                val currentDateTime = LocalDateTime.now()
                Log.e(TAG, "Current Date Time : $currentDateTime")
            }

            btnManipulateDate.setOnClickListener {
                val lastWeekFromNow = LocalDate.now().minusWeeks(1)
                Log.e(TAG, "Last week from now : $lastWeekFromNow")
                val formattedDate = DateTimeFormatter.ofPattern("dd/MMMM/yyyy hh:mm").format(LocalDateTime.of(2010,5,5,7,11))
                Log.e(TAG, "Formatted Date : $formattedDate")
            }
        }
    }
}