package com.example.androidlearnproject.animation.transitions_materialmotion.container_transform.example1_activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import androidx.core.app.ActivityOptionsCompat
import com.example.androidlearnproject.R
import com.example.androidlearnproject.databinding.ActivityCtFirstBinding
import com.google.android.material.transition.platform.MaterialElevationScale


class CTFirstActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCtFirstBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        // Enable Activity Transitions.
        /* Optionally enable Activity transitions in your theme for all activities with
           <item name=”android:windowActivityTransitions”>true</item>. */
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        // doesn't work !! Keep system bars (status bar, navigation bar) persistent throughout the transition.
//        window.sharedElementsUseOverlay = false

        super.onCreate(savedInstanceState)
        binding = ActivityCtFirstBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = "First Activity"

        binding.ivFirstScreen.setOnClickListener {
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                binding.ivFirstScreen, // shared element view in this activity
                "second_screen_image"  // The transition name to be matched in Activity B shared element view.
            )
            Intent(this, CTSecondActivity::class.java).also {
                startActivity(it, options.toBundle())
            }
        }

    }
}