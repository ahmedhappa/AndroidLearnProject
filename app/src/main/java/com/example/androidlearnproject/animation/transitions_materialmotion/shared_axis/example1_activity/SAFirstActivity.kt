package com.example.androidlearnproject.animation.transitions_materialmotion.shared_axis.example1_activity

import android.content.Intent
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import com.example.androidlearnproject.R
import com.example.androidlearnproject.databinding.ActivitySaFirstBinding
import com.google.android.material.transition.platform.MaterialSharedAxis


class SAFirstActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySaFirstBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        // Enable Activity Transitions.
        /* Optionally enable Activity transitions in your theme for all activities with
           <item name=”android:windowActivityTransitions”>true</item>. */
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        /* Since there is no persistent container involved in this navigation change,
        we can use a Shared Z-Axis transition to reinforce the spatial relationship between the two screens and
        indicate moving one level upward in the app's hierarchy. */
        //set exit transition to be shared axis Z (we can also use X and Y)
        window.exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true).apply {
            excludeTarget(R.id.action_bar_container, true)
            excludeTarget(android.R.id.statusBarBackground, true)
            excludeTarget(android.R.id.navigationBarBackground, true)
            duration = 300
        }

        window.reenterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false).apply {
            excludeTarget(R.id.action_bar_container, true)
            excludeTarget(android.R.id.statusBarBackground, true)
            excludeTarget(android.R.id.navigationBarBackground, true)
            duration = 300
        }

        super.onCreate(savedInstanceState)
        binding = ActivitySaFirstBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = "First Activity"

        binding.btnGotToSecondActivity.setOnClickListener {
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this)
            Intent(this, SASecondActivity::class.java).also {
                startActivity(it, options.toBundle())
            }
        }

    }
}