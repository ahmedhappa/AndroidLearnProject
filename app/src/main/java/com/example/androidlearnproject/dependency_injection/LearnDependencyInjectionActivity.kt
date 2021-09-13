package com.example.androidlearnproject.dependency_injection

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.example.androidlearnproject.databinding.ActivityLearnDependencyInjectionBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

/* this annotation is used in Activity, Fragment, View, Service and BroadcastReceiver. to make them use Hilt
   because these classes needs a special treatment */
//Annotating Android classes with @AndroidEntryPoint creates a dependencies container that follows the Android class lifecycle.
// ## if a fragment is annotated with this annotation it's activity must be annotated with this annotation too or the app will crash
@AndroidEntryPoint
class LearnDependencyInjectionActivity : AppCompatActivity() {

    //To perform field injection, use the @Inject annotation on Android class fields you want to be injected by Hilt.
    // ## Warning: Fields injected by Hilt cannot be private.
    @Inject
    lateinit var myInjectableThemeClass: MyInjectableThemeClass

    @Inject
    lateinit var myInjectableInterface: MyInjectableInterface

    //because there is 2 diff calender object provider we need to decide which one to inject using qualifiers.
    @CustomDateCalendar
    @Inject
    lateinit var calendar: Calendar

    private val viewModel: LearnDiViewModel by viewModels()

    private lateinit var binding: ActivityLearnDependencyInjectionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLearnDependencyInjectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnStartSecondActivity.setOnClickListener {
                startActivity(Intent(this@LearnDependencyInjectionActivity, DISecondActivity::class.java))
            }

            btnShowInjectedThemeClassData.setOnClickListener {
                myInjectableThemeClass.backgroundColorName = "Blaccccck"
                Toast.makeText(
                    this@LearnDependencyInjectionActivity,
                    "injected theme Background color name : ${myInjectableThemeClass.backgroundColorName}\n"
                        .plus("Date : ${myInjectableThemeClass.date.time}"),
                    Toast.LENGTH_LONG
                ).show()
            }

            btnShowInjectedClassInheritInterfaceWithBindAnnotation.setOnClickListener {
                Toast.makeText(
                    this@LearnDependencyInjectionActivity,
                    "Data : ${myInjectableInterface.showTestString()}",
                    Toast.LENGTH_LONG
                ).show()
            }

            btnShowWhichCalnderInjectedData.setOnClickListener {
                Toast.makeText(
                    this@LearnDependencyInjectionActivity,
                    "Calendar : ${calendar.get(Calendar.YEAR)} ${calendar.get(Calendar.MONTH)} ${calendar.get(Calendar.DAY_OF_MONTH)}",
                    Toast.LENGTH_LONG
                ).show()
            }

        }

        viewModel.calendarLiveData.observe(this, {
            Toast.makeText(
                this@LearnDependencyInjectionActivity,
                "Calendar : ${it.get(Calendar.YEAR)} ${it.get(Calendar.MONTH)} ${it.get(Calendar.DAY_OF_MONTH)}",
                Toast.LENGTH_LONG
            ).show()
        })
    }
}