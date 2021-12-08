package com.example.androidlearnproject.animation.transitions_materialmotion.container_transform.example2_act_fra_list_fab

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.androidlearnproject.databinding.ActivityCtMainBinding
import com.google.android.material.transition.MaterialElevationScale

class CtMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCtMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCtMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction().add(binding.frameFragmentContainer.id, CtFirstListFragment()).commit()

        val thirdFragment = CtThirdFabFragment()
        binding.fabCtMainActivity.setOnClickListener {
            supportFragmentManager.fragments.first()?.apply {
                exitTransition = MaterialElevationScale(false).apply {
                    duration = 500
                }
                reenterTransition = MaterialElevationScale(true).apply {
                    duration = 500
                }
            }
            supportFragmentManager.beginTransaction().replace(binding.frameFragmentContainer.id, thirdFragment)
                .addToBackStack("thirdFragment").commit()
            binding.fabCtMainActivity.hide()
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.fragments[0] is CtThirdFabFragment)
            binding.fabCtMainActivity.show()
        super.onBackPressed()
    }
}